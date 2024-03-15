package com.example.cardealer.service;

import com.example.cardealer.entity.auth.Dealer;
import com.example.cardealer.entity.car.CarForSale;
import com.example.cardealer.entity.car.CarForSaleDTO;
import com.example.cardealer.entity.car.CarImageDTO;
import com.example.cardealer.entity.car.CarSoldDTO;
import com.example.cardealer.translator.FloatPriceToWordPrice;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.example.cardealer.entity.contract.SaleContractDTO;
import com.example.cardealer.repository.CarForSaleRepository;
import com.example.cardealer.repository.DealerRepository;
import com.example.cardealer.translator.CarForSaleDtoToCarForSale;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.jsoup.nodes.Document;
import java.io.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import static com.example.cardealer.translator.FloatPriceToWordPrice.convert;


@Service
@RequiredArgsConstructor
@Slf4j
public class SaleCarService {

    private final CarForSaleDtoToCarForSale carForSaleDtoToCarForSale;
    private final CarForSaleRepository carForSaleRepository;
    private final ImageService imageService;


    @Value("classpath:static/sale-contract.html")
    private Resource saleContractTemplate;
    private final DealerRepository dealerRepository;

    public ResponseEntity<List<CarForSale>> getCarsForeSale(HttpServletRequest request, HttpServletResponse response) {
        String dealer = null;
        if (request.getCookies() != null) {
            for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
                if (value.getName().equals("Dealer")) {
                    dealer = value.getValue();
                }
            }
            if (dealer != null) {
                Optional<Dealer> dealerOptional = dealerRepository.findDealerByUuid(dealer);
                if (dealerOptional.isPresent()) {
                    Dealer seller = dealerOptional.get();
                    long sellerId = seller.getId();
                    List<CarForSale> carForSaleList = carForSaleRepository.findBySeller(sellerId);
                    return ResponseEntity.ok(carForSaleList);
                } else {
                    log.warn("Dealer with UUID {} not found", dealer);
                    return ResponseEntity.notFound().build();
                }
            } else {
                log.info("Dealer cookie not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            log.info("No cookies found in the request");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    public ResponseEntity<?> createCarForSale(CarForSaleDTO carForSaleDTO) {
        return dealerRepository.findDealerById(carForSaleDTO.getSeller())
                .map(dealer -> {
                    CarForSale carForSale = carForSaleDtoToCarForSale.toCarForSale(carForSaleDTO);
                    carForSale.setUuid(UUID.randomUUID().toString());
                    carForSale.setCreateAt(LocalDate.now());
                    carForSale.setSeller(carForSaleDTO.getSeller());
                    carForSale.setSold(false);
                    try {
                        Long nextId =  carForSaleRepository.getNextId() + 1;
                        List<String> fileList = new ArrayList<>();
                        for(MultipartFile file : carForSaleDTO.getImageFile()) {
                            String pathFile = imageService.saveImage(file, dealer, String.valueOf(nextId));
                            fileList.add(pathFile);
                        }
                        carForSale.setImageFile(fileList);
                        carForSaleRepository.save(carForSale);
                        return ResponseEntity.ok(carForSale);
                    } catch (DataAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Brak informacji o dealerze"));
    }

    public ResponseEntity<?> editCarForSale(CarForSale carForSale) {
        Optional<CarForSale> existingCarOptional = carForSaleRepository.findByUuid(carForSale.getUuid());

        if (existingCarOptional.isPresent()) {
            CarForSale existingCar = existingCarOptional.get();
            carForSale.setCreateAt(existingCar.getCreateAt());
            BeanUtils.copyProperties(carForSale, existingCar);
            carForSaleRepository.save(existingCar);
            return ResponseEntity.ok(existingCar);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znalezionego samochodu o takim id.");
        }
    }

    public ResponseEntity<?> generateSaleContract(SaleContractDTO saleContractDto) throws Exception {
        try {
            String htmlContent = StreamUtils.copyToString(saleContractTemplate.getInputStream(), StandardCharsets.UTF_8);
            String outputPath = "generated.pdf";

            AtomicReference<CarForSale> carForSale = new AtomicReference<>(new CarForSale());
            AtomicReference<Dealer> dealer = new AtomicReference<>(new Dealer());

            dealerRepository.findDealerByUuid(saleContractDto.getDealerUuid()).ifPresent( value -> {
                dealer.set(value);
            });

            carForSaleRepository.findByUuid(saleContractDto.getCarUuid()).ifPresent( value -> {
                carForSale.set(value);
            });

            htmlContent = htmlContent.replace("%CAR_BRAND%", carForSale.get().getBrand());
            htmlContent = htmlContent.replace("%CAR_MODEL%", carForSale.get().getModel());
            htmlContent = htmlContent.replace("%CAR_YEAR%", carForSale.get().getCar_year().toString());
            htmlContent = htmlContent.replace("%CAR_MILEAGE%", carForSale.get().getMileage().toString());
            htmlContent = htmlContent.replace("%CAR_PRICE%", String.valueOf(carForSale.get().getPrice()));
            htmlContent = htmlContent.replace("%CAR_PRICE_WORDS%", convert(carForSale.get().getPrice()));
            htmlContent = htmlContent.replace("%DEALER_NAME%", dealer.get().getCompanyName());
            htmlContent = htmlContent.replace("%DEALER_NIP%", dealer.get().getNip());
            htmlContent = htmlContent.replace("%DEALER_ADDRESS%", dealer.get().getAddress());
            convertHTMLtoPDF(htmlContent, outputPath);

            byte[] pdfBytes = readPDF(outputPath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "generated.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Metoda do konwersji HTML na PDF
    private void convertHTMLtoPDF(String htmlContent, String outputPath) throws Exception {
        // Parsowanie HTML za pomocą JSoup
        Document doc = Jsoup.parse(htmlContent);

        // Konwertowanie do formatu PDF
        try (FileOutputStream os = new FileOutputStream(outputPath)) {
            ConverterProperties properties = new ConverterProperties();
            HtmlConverter.convertToPdf(doc.html(), os, properties);
        }
    }

    // Metoda do odczytu pliku PDF jako tablicy bajtów
    private byte[] readPDF(String filePath) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
        }
        return baos.toByteArray();
    }

    public ResponseEntity<?> setCarSold(CarSoldDTO carSoldDTO) {
        Optional<CarForSale> optionalCarForSale = carForSaleRepository.findByUuid(carSoldDTO.getCarUuid());
        log.info(String.valueOf(optionalCarForSale.get().getId()));
        if (optionalCarForSale.isPresent()) {
            CarForSale carForSale = optionalCarForSale.get();

            CarImageDTO carImageDTO = new CarImageDTO();
            carImageDTO.setCarUuid(carSoldDTO.getCarUuid());
            carImageDTO.setDealerUuid(carSoldDTO.getDealerUuid());

            for (int i = carForSale.getImageFile().size() - 1; i >= 0; i--) {
                carImageDTO.setPhotoNumber(i);
                imageService.deleteImageByPhotoNumber(carImageDTO);
            }

            carForSale.setSold(true);
            carForSaleRepository.save(carForSale);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        }
    }
}

