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
import java.time.ZonedDateTime;

@Data
@Entity
@Table(schema = "covid", name = "cvd_updates", uniqueConstraints = {
        @UniqueConstraint(name = "cvd_upd_created_uniq", columnNames = {"createdOn"})
})
public class CovidUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "cvd_updates_seq")
    @SequenceGenerator(schema = "covid", name = "cvd_updates_seq", sequenceName = "cvd_updates_seq", allocationSize = 1)
    private long id;

    private LocalDateTime createdOn;
    private ZonedDateTime datetime;

}
