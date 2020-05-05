package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

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
@Service
public class ScaleServiceImpl implements SerialPortEventListener {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    OutputStream outputStream;
    InputStream inputStream;
    SerialPort serialPort;

    /**
     * 
     * It takes 11 seconds for the device to be turned on
     * 
     * @since Mar 18, 2020
     */
    public void openConnection() {
        
        CommPortIdentifier port = null;
        Enumeration comm = CommPortIdentifier.getPortIdentifiers();
        while (comm.hasMoreElements()) {
            port = (CommPortIdentifier) comm.nextElement();
            if (port.getName().equals("COM3")) {
                break;
            }
        }
        try {
            if (port != null) {
                serialPort = port.open("ScaleServiceImpl", 2000);
            }
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            System.out.println("Model: " + sendCommand("m").substring(1));
            System.out.println("Version: " + sendCommand("V").substring(1));
            System.out.println("Serial Number: " + sendCommand("S").substring(1));
            serialPort.notifyOnDataAvailable(true);
            serialPort.addEventListener(this);
        } catch (PortInUseException | TooManyListenersException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;

            case SerialPortEvent.DATA_AVAILABLE:
                String str = sendCommand("U");
                String output = str.split("\r")[1].substring(9, 17);
                System.out.println(output);
                simpMessagingTemplate.convertAndSend("/queue/scale/output", output);
                break;

        }
    }

    public String sendCommand(String command) {
        String str = "";
        try {
            outputStream.write(command.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            byte[] readBuffer = new byte[50];
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int numBytes = inputStream.read(readBuffer);
            str = new String(readBuffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 
     * When device is disconnected or powered off
     * 
     * @since Mar 18, 2020
     */
    public void disconnect() {
        serialPort.removeEventListener();
        serialPort.close();

    }

    public String getWeight() {
        String str = sendCommand("U");
        return str.split("\r")[1].substring(9, 17);
    }
}
