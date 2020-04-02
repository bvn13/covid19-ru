package com.bvn13.covid19.scheduler.updater.stopcoronovirusrf.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RowData {

    String region;
    String sick;
    String healed;
    String died;

}
