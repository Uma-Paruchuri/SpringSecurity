package com.uv.springsecurity1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @GetMapping("/myCards")
    public String getCardsDetails(){
        return "myCards";
    }
    @GetMapping("/myLoans")
    public String getLoanDetails(){
        return "myLoans";

    }
    @GetMapping("/myAccounts")
    public String getAccountsDetails(){
        return "myAccounts";

    }
    @GetMapping("/notices")
    public String getNoticeDetails(){
        return "notices";
    }
}
