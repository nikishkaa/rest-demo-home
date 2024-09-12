package org.example.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
public class Person {
    private int id;
    private String name;
}
