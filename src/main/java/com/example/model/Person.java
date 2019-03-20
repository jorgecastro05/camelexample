package com.example.model;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Person {

    private String name;
    private Integer age;
    private String city;
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate  getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}