package com.example.easybank.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class BalanceController {

    @GetMapping("/my-balance")
    public String getBalanceDetails() {
        return "Here are the balance details from the DB";
    }
}
