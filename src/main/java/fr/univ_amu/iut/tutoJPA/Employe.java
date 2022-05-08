package fr.univ_amu.iut.tutoJPA;

import javax.persistence.*;

@Entity //1
public class Employe {
    @Id //2
    @GeneratedValue//3
    private int id;
    @Column(length = 50) //4
    private String nom;
    private long salaire;
    @Embedded //5
    private Adresse adresse;
    @ManyToOne //6
    private Departement departement;

    public Employe() {
    }

    public Employe(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // private void setId(int id) { this.id = id; }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public long getSalaire() {
        return salaire;
    }

    public void setSalaire(long salaire) {
        this.salaire = salaire;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        adresse = adresse;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}