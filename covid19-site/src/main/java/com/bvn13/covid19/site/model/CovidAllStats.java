package com.bvn13.covid19.site.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class CovidAllStats {

    List<String> regions;
    List<CovidDayStats> progress;

}
