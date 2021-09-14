package com.moonz.springPractice01.controller;

import com.moonz.springPractice01.javaPrac.Person;
import org.springframework.web.bind.annotation.GetMapping;

public class PersonController {
    @GetMapping("/myinfo")
    public Person getPersons() {
        Person person = new Person();
        person.setName("moon");
        person.setAge(23);
        person.setAddress("hwaseongsi");
        person.setJob("Student");
        return person;
    }
}
