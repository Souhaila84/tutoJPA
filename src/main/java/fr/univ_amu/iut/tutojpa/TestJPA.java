package fr.univ_amu.iut.tutojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class TestJPA {
    public static void main(String[] args) {
        // Initializes the Entity manager
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("employePU")) {
            try (EntityManager em = emf.createEntityManager()) {
                // Creates a new employee and persists it
                Employe employe = new Employe();
                employe.setNom("Dupont");
                employe.setSalaire(5000);
                
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                em.persist(employe);
                tx.commit();
            }
        }
    }
}
