package com.tts.transitapp.controller;

import java.util.List;

import com.tts.transitapp.model.Bus;
import com.tts.transitapp.model.BusRequest;
import com.tts.transitapp.service.TransitService;
import com.tts.transitapp.model.Location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransitController {
    
    @Autowired
    TransitService transitService;

    @GetMapping("/")
    public String getBusesPage(Model model) {

        model.addAttribute("request", new BusRequest());
        return "index";
    }

    @PostMapping("/")
    public String getNearbyBuses(BusRequest request, Model model) {

        //person location
        System.out.println(request);

        List<Bus> buses = transitService.getNearbyBuses(request);

        model.addAttribute("buses", buses);
        model.addAttribute("request", request);

        
        Location userLocation = transitService.getPersonLocation(request);
        model.addAttribute("userlocation", userLocation);

        return "index";


    }




}