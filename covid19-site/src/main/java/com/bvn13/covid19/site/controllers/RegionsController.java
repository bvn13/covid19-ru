package com.bvn13.covid19.site.controllers;

import com.bvn13.covid19.site.service.CovidStatsMaker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/regions")
public class RegionsController {

    private final CovidStatsMaker covidStatsMaker;

    @GetMapping
    public List<String> getAllRegions() {
        return covidStatsMaker.findAllRegionsNames();
    }

}
