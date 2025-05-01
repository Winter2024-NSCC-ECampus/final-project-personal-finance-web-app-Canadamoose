package com.example;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class TransactionService {
    private static final String PERSISTENCE_UNIT_NAME = "finance-demo";
    private EntityManagerFactory emf;

    public BigDecimal getCategoryTotal(Category category, YearMonth period) {
        EntityManager em = PersistenceManager.getEntityManager();
        try {
            LocalDate start = period.atDay(1);
            LocalDate end = period.atEndOfMonth();

            String typeFilter = category.getType() == CategoryType.INCOME ?
                    "AND t.type = 'INCOME'" : "AND t.type = 'EXPENSE'";

            BigDecimal result = em.createQuery(
                            "SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
                                    "WHERE t.category = :category " +
                                    "AND t.date BETWEEN :start AND :end " +
                                    typeFilter, BigDecimal.class)
                    .setParameter("category", category)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getSingleResult();

            return result != null ? result : BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }



    public TransactionService() {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create EntityManagerFactory", e);
        }
    }



    public void saveTransaction(Transaction t) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(t);
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    public List<Transaction> getAllTransactions() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.tags",
                    Transaction.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}