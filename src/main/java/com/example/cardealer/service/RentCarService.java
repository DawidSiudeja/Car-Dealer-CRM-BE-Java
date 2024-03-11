package com.example.cardealer.service;

import com.example.cardealer.entity.auth.Role;
import com.example.cardealer.entity.car.CarCalendar;
import com.example.cardealer.entity.car.CarCalendarDTO;
import com.example.cardealer.entity.car.CarForRent;
import com.example.cardealer.entity.car.CarForRentDTO;
import com.example.cardealer.repository.CarCalendarRepository;
import com.example.cardealer.repository.CarForRentRepository;
import com.example.cardealer.repository.UserRepository;
import com.example.cardealer.translator.CarForRentDtoToCarForRent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentCarService {

    private final CarForRentRepository carForRentRepository;
    private final CarForRentDtoToCarForRent carForRentDtoToCarForRent;
    private final UserRepository userRepository;
    private final CarCalendarRepository carCalendarRepository;

    public ResponseEntity<?> createCarForRent(CarForRentDTO carForRentDTO) {
        return userRepository.findUserById(carForRentDTO.getSeller())
                .map(user -> {
                    if (user.getRole() == Role.DEALER) {
                        CarForRent carForRent = carForRentDtoToCarForRent.toCarForRent(carForRentDTO);
                        carForRent.setUuid(UUID.randomUUID().toString());
                        carForRent.setCreateAt(LocalDate.now());
                        carForRent.setSeller(carForRentDTO.getSeller());
                        try {
                            carForRentRepository.save(carForRent);
                            return ResponseEntity.ok(carForRent);
                        } catch (DataAccessException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Użytkownik nie jest dealerem");
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brak informacji o dealerze"));
    }

    public ResponseEntity<?> getCarsForRent(String seller) {
        List<CarForRent> carForRentList = new ArrayList<>();
        carForRentRepository.findBySeller(Integer.parseInt(seller)).forEach( value -> {
            carForRentList.add(value);
        });
        return ResponseEntity.ok(carForRentList);
    }

    public ResponseEntity<?> editCarForRent(CarForRent carForRent) {
        Optional<CarForRent> existingCarOptional = carForRentRepository.findByUuid(carForRent.getUuid());

        if (existingCarOptional.isPresent()) {
            CarForRent existingCar = existingCarOptional.get();
            carForRent.setCreateAt(existingCar.getCreateAt());
            BeanUtils.copyProperties(carForRent, existingCar);
            carForRentRepository.save(existingCar);
            return ResponseEntity.ok(existingCar);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znalezionego samochodu o takim id.");
        }
    }

    public ResponseEntity<?> rentCar(CarCalendarDTO carCalendarDTO) {
        Optional<CarForRent> carForRentOptional = carForRentRepository.findById(carCalendarDTO.getCarId());

        if (carForRentOptional.isPresent()) {
            CarForRent carForRent = carForRentOptional.get();

            List<CarCalendar> carCalendarList = carCalendarRepository.findByCarForRent(carForRent);

            for (CarCalendar calendarEntry : carCalendarList) {
                if (isOverlap(calendarEntry, carCalendarDTO)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Samochód jest już zarezerwowany w podanym okresie.");
                }
            }

            CarCalendar newCalendarEntry = new CarCalendar();
            newCalendarEntry.setStartRent(carCalendarDTO.getStartRent());
            newCalendarEntry.setEndRent(carCalendarDTO.getEndRent());
            newCalendarEntry.setUuid(UUID.randomUUID().toString());
            newCalendarEntry.setCarForRent(carForRent);
            carCalendarRepository.save(newCalendarEntry);

            return ResponseEntity.ok("Rezerwacja samochodu została pomyślnie utworzona.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isOverlap(CarCalendar existingCalendarEntry, CarCalendarDTO newCalendarEntry) {
        LocalDate startRentNew = newCalendarEntry.getStartRent();
        LocalDate endRentNew = newCalendarEntry.getEndRent();
        LocalDate startRentExisting = existingCalendarEntry.getStartRent();
        LocalDate endRentExisting = existingCalendarEntry.getEndRent();

        if (startRentNew.isBefore(endRentExisting) && endRentNew.isAfter(startRentExisting)) {
            return true;
        }

        if (startRentExisting.isBefore(endRentNew) && endRentExisting.isAfter(startRentNew)) {
            return true;
        }

        return false;
    }
}
