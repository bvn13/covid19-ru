package com.bvn13.covid19.api.model;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Builder
@Value
public class CovidStatsInfo {

    String region;
    long sick;
    long healed;
    long died;

    @Builder
    @Value
    public static class Delta {
        long sick;
        long healed;
        long died;
    }

}
