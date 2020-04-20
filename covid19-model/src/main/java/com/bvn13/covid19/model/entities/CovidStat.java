/*
Copyright [2020] [bvn13]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.bvn13.covid19.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "covid", name = "cvd_stats")
public class CovidStat implements StatsProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "cvd_stats_seq")
    @SequenceGenerator(schema = "covid", name = "cvd_stats_seq", sequenceName = "cvd_stats_seq", allocationSize = 1)
    private long id;

    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_id")
    private CovidUpdate updateInfo;

    private long sick;
    private long healed;
    private long died;

    @PrePersist
    public void prePersist() {
        this.createdOn = LocalDateTime.now();
    }

}
