package com.moonz.springPractice01.javaPrac;

public class Tutor {
    private String name;
    private int bio;

    //기본 생성자
    public Tutor() {
        System.out.println("기본 생성자!");
    }
    public Tutor(String name, int bio) {
        this.name = name;
        this.bio = bio;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setBio(int bio) {
        this.bio = bio;
    }

    public String getName() {
        return this.name;
    }
    public int getBio() {
        return this.bio;
    }
}
