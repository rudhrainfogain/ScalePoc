package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo3Application implements CommandLineRunner{

    @Autowired
    private ScaleServiceImpl scaleService;
    
	public static void main(String[] args) {
		SpringApplication.run(Demo3Application.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        scaleService.openConnection();
    }

}
