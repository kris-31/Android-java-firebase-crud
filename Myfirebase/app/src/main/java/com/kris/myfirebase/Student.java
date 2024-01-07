package com.kris.myfirebase;

public class Student {

    private String name;
    private String regno;
    private int marks;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Student(String name, String regno, int marks) {
        this.name = name;
        this.regno = regno;
        this.marks = marks;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for regno
    public String getRegno() {
        return regno;
    }

    // Setter for regno
    public void setRegno(String regno) {
        this.regno = regno;
    }

    // Getter for marks
    public int getMarks() {
        return marks;
    }

    // Setter for marks
    public void setMarks(int marks) {
        this.marks = marks;
    }
}
