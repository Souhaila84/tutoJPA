package fr.univ_amu.iut.tutoJPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class TestJPA {
    public static void main(String[] args) {
        // Initializes the Entity manager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("employePU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // Creates a new object and persists it
        Employe employe = new Employe();
        employe.setNom("Dupont");
        employe.setSalaire(5000);
        tx.begin();
        em.persist(employe);
        tx.commit();

        em.close();
        emf.close();
    }
}
