package com.bvn13.covid19.api.dtos;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CovidUpdateInfoDto {
    LocalDateTime createdOn;
}
