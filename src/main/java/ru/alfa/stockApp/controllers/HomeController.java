package ru.alfa.stockApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alfa.stockApp.services.StockService;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    StockService stockService;

    @GetMapping
    public String showGifAccordingToWealth(
            Model model) {
                String gifUrl = stockService.getGifUrl();
                model.addAttribute("gifUrl", gifUrl);
                return "home";
            }
}
