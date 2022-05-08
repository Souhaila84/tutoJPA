package fr.univ_amu.iut;

import fr.univ_amu.iut.tutojpa.Employe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TestJPA {
    static EntityManager em;
    static EntityManagerFactory emf;

    @BeforeAll
    static void beforeAll() {
        emf = Persistence.createEntityManagerFactory("employePU");
        em = emf.createEntityManager();
    }

    @AfterAll
    static void afterAll() {
        em.close();
        emf.close();
    }

    @Test
    public void test_should_never_fail() {
        assertThat(true).isTrue();
    }
}