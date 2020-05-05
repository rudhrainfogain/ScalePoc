package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Copyright (c) 2019 FedEx. All Rights Reserved.<br>
 * <br>
 * Theme - Core Retail Peripheral Services<br>
 * Feature - Peripheral Services - Design and Architecture<br>
 * Description - This class
 * 
 * @author Abhishek Singhal [3692173]
 * @version 1.0.0
 * @since Mar 17, 2020
 */
@RestController
public class TestController {
    @Autowired
    private ScaleServiceImpl scaleService;

    @GetMapping(path = "/scale/open")
    public void open() {
        scaleService.openConnection();
    }

    @GetMapping(path = "/scale/close")
    public void close() {
        scaleService.disconnect();
    }

    @GetMapping(path = "/scale/weight")
    public String getWeight() {
        return scaleService.getWeight();
    }
}
