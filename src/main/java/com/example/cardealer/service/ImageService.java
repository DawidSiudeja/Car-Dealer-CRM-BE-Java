package com.example.cardealer.service;


import com.example.cardealer.entity.auth.Dealer;
import com.example.cardealer.entity.car.CarForSale;
import com.example.cardealer.entity.car.CarImageDTO;
import com.example.cardealer.exceptions.file.FtpConnectionException;
import com.example.cardealer.repository.CarForSaleRepository;
import com.example.cardealer.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

   private final FtpService ftpService;
   private final CarForSaleRepository carForSaleRepository;
   private final DealerRepository dealerRepository;

    public String saveImage(MultipartFile file, Dealer dealer, String nextId) {
        try {
            if (file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1).equals("png")) {
                ftpService.uploadFileToFtp(file, dealer, nextId);
                String imagePath = dealer.getId() + "/" + nextId + "/" + file.getOriginalFilename();
                return imagePath;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public ResponseEntity<?> deleteImage(CarImageDTO carImageDTO) {
        return carForSaleRepository.findByUuid(carImageDTO.getCarUuid()).map(carForSale -> {
            deleteImageByPhotoNumber(carImageDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Succes");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found car by uuid"));
    }

    public void deleteImageByPhotoNumber(CarImageDTO carImageDTO) {
        log.info("delete" + carImageDTO.getPhotoNumber());
        CarForSale carForSale = carForSaleRepository.findByUuid(carImageDTO.getCarUuid()).get();
        List<String> existingImageList = carForSale.getImageFile();
        int photoNumber = carImageDTO.getPhotoNumber();

        if (photoNumber >= 0 && photoNumber < existingImageList.size()) {
            try {
                ftpService.deleteFile(existingImageList.get(photoNumber));
            } catch (IOException e) {
                throw new FtpConnectionException("Cannot delete file from ftp");
            }
            existingImageList.remove(photoNumber);
            carForSale.setImageFile(existingImageList);
            carForSaleRepository.save(carForSale);
        }
    }
}
