package com.moonz.springPractice01.javaPrac;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JavaPrac {

    public static void main(String[] args) {
        String title = "웹개발의 봄, Spring";
        String tutor = "Moonz";
        int days = 35;
        Course course = new Course(title, tutor, days);
        course.setTitle(title);
        course.setTutor(tutor);
        course.setDays(days);
    }
}
