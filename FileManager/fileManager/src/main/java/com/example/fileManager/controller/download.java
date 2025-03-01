package com.example.fileManager.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class download {

    @PostMapping("/download")
    public void downloadFile(){

    }
}
