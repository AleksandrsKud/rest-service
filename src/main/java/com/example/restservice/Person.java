package com.example.restservice;

import java.util.List;

public class Person {
    private String personalCode;
    private Integer modifier;

    public Person(String personalCode, Integer modifier){
        this.personalCode = personalCode;
        this.modifier = modifier;
    }

    public String getPersonalCode(){
        return personalCode;
    }

    public Integer getModifier(){
        return modifier;
    }
}
