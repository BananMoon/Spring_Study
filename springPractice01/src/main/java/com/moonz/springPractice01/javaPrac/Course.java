package com.moonz.springPractice01.javaPrac;
// 빵틀
public class Course {
    //멤버 변수
    private String title;
    private String tutor;
    private int days;

    //기본 생성자 (자동으로 생성해줌)
    public Course() {
        System.out.println("기본 생성자!");
    }

    //매개변수 있는 생성자 (기본 생성자 생성해줘야함)
    public Course(String title, String tutor, int days) {
        this.title = title;
        this.tutor = tutor;
        this.days = days;
    }

    // setter : private인 멤버변수를 변경!
    public void setTitle(String title) {
        this.title = title;
    }
    public void setTutor(String tutor) {
        this.tutor = tutor;
    }
    public void setDays(int days) {
        this.days = days;
    }

    //getter
    public String getTitle() {
        return this.title;
    }
    public String getTutor() {
        return this.tutor;
    }
    public int getDays() {
        return this.days;
    }
}
