package com.example;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class BudgetService {
    public List<Budget> getAllBudgets() {
        EntityManager em = PersistenceManager.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT b FROM Budget b LEFT JOIN FETCH b.category",
                    Budget.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}