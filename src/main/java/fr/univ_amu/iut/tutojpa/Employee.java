package fr.univ_amu.iut.tutojpa;

import jakarta.persistence.*;

@Entity
public class Employee {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private long salary;

    @Embedded
    private Adress adress;

    @ManyToOne(cascade = CascadeType.ALL)
    private Department department;

    public Adress getAdresse() {
        return adress;
    }

    public void setAdresse(Adress adress) {
        this.adress = adress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String toString() {
        return "Employee id: " + getId() + " name: " + getName() + " with " + getDepartment();
    }
}