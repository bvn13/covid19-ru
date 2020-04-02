package com.bvn13.covid19.model.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Table(name = "regions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Region {

    @Id
    private String name;

}
