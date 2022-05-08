package fr.univ_amu.iut.tutojpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Departement {
    @Id
    @GeneratedValue
    private long id;
    private String nom;
    private String telephone;

    public Departement() {
    }

    public Departement(long id, String nom, String telephone) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getTelephone() {
        return telephone;
    }
}