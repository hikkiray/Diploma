package com.omgtu.medhelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileViewController {
    
    @GetMapping("/profile")
    public String profilePage() {
        return "forward:/profile.html";
    }
} 