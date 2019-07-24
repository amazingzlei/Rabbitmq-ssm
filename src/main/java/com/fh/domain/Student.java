package com.fh.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements Serializable{
    private String name;
    private String age;
    private String address;

    public Student(String name, String age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public Student(){}
}
