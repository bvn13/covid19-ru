package com.bvn13.covid19.api.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Value
public class CovidStatsResponse {

    ZonedDateTime updatedOn;
    ZonedDateTime datetime;

    @Singular(value = "stats")
    List<CovidStatsInfo> stats;
}
