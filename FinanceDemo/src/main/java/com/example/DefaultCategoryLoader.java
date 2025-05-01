package com.example;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.List;

@WebListener
public class DefaultCategoryLoader implements ServletContextListener {

    private static final List<DefaultCategory> DEFAULT_CATEGORIES = List.of(
            new DefaultCategory("Rent", CategoryType.EXPENSE),
            new DefaultCategory("Groceries", CategoryType.EXPENSE),
            new DefaultCategory("Utilities", CategoryType.EXPENSE),
            new DefaultCategory("Transportation", CategoryType.EXPENSE),
            new DefaultCategory("Salary", CategoryType.INCOME),
            new DefaultCategory("Investments", CategoryType.INCOME)
    );

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== INITIALIZING DEFAULT CATEGORIES ===");

        EntityManager em = PersistenceManager.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            for (DefaultCategory dc : DEFAULT_CATEGORIES) {
                Category existing = em.createQuery(
                                "SELECT c FROM Category c WHERE c.name = :name AND c.type = :type",
                                Category.class)
                        .setParameter("name", dc.name())
                        .setParameter("type", dc.type())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);

                if (existing == null) {
                    Category newCat = new Category(dc.name(), dc.type());
                    em.persist(newCat);
                    System.out.println("Created category: " + newCat);
                }
            }

            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    private record DefaultCategory(String name, CategoryType type) {}
}