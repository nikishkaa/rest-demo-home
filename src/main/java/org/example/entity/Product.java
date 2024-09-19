package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "Product")
@Table(name = "Products_v2")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true, length = 11)
    private int id;

    @Column(name = "NAME", nullable = true, length = 20)
    private String name;

    @Column(name = "descr", nullable = true, length = 20)
    private String descr;

    @Column(name = "insert_time", nullable = true)
    private Date insertTime;

}
