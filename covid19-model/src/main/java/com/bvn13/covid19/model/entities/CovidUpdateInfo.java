package com.bvn13.covid19.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "update_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"createdOn"})
})
public class CovidUpdateInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private LocalDateTime createdOn;
    private ZonedDateTime datetime;

}
