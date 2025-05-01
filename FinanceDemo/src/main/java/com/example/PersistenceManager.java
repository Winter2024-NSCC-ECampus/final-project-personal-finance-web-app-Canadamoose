package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("finance-demo");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}