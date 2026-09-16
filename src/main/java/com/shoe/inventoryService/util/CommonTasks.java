package com.shoe.inventoryService.util;


import com.shoe.inventoryService.entity.Status;
import com.shoe.inventoryService.repository.StatusRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonTasks {

    private final StatusRepo statusRepo;


    public Status getStatus(int id) {
        return statusRepo.findById(id).orElse(null);
    }



}
