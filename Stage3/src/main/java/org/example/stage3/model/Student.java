package org.example.stage3.model;


/*
    * This class is a model class that represents a student entity.
    * It has four fields: id, firstName, lastName, and age.
    * It has a constructor that initializes all the fields.
    *
    * in future stages, we will use lombok to generate getters, setters, and toString methods.
    * it is a good practice to use lombok to avoid boilerplate code.
 */
public class Student {
    Long id;
    String firstName;
    String lastName;
    double age;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, double age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                '}';
    }
}
