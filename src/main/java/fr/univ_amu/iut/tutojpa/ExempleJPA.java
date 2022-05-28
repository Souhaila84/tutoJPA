package fr.univ_amu.iut.tutojpa;

import jakarta.persistence.*;

public class ExempleJPA {
    public static void main(String[] args) {
        // Initializes the Entity manager
        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("employePU")) {
            try (EntityManager em = emf.createEntityManager()) {
                EntityTransaction tx = em.getTransaction();

                Department informatique = new Department();
                informatique.setName("Informatique");

                Employee dupont = new Employee();
                dupont.setName("Dupont");
                dupont.setDepartment(informatique);
                dupont.setSalary(5000);

                tx.begin();
                em.persist(dupont);
                tx.commit();
            }
        }
    }
}
