package com.bvn13.covid19.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "covid_statistics")
public class CovidStat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_info_id")
    private CovidUpdateInfo updateInfo;

    private long sick;
    private long healed;
    private long died;

    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
    }

}
