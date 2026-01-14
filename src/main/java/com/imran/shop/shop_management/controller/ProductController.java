package com.imran.shop.shop_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/")
public class ProductController {
    @GetMapping("product")
    public String product(){
        return "this is the product";
    }
}
