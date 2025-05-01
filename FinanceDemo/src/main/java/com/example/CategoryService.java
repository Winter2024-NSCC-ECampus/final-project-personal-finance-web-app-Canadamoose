package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import java.util.List;

public class CategoryService {
    private EntityManagerFactory emf;

    public CategoryService() {
        emf = Persistence.createEntityManagerFactory("finance-demo");
    }

    public List<Category> getCategoriesByType(CategoryType type) {
        EntityManager em = PersistenceManager.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT c FROM Category c WHERE c.type = :type",
                            Category.class)
                    .setParameter("type", type)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Category findOrCreateCategory(EntityManager em, String name, CategoryType type) {
        try {
            return em.createQuery(
                            "SELECT c FROM Category c WHERE c.name = :name AND c.type = :type",
                            Category.class)
                    .setParameter("name", name)
                    .setParameter("type", type)
                    .getSingleResult();
        } catch (NoResultException e) {
            Category newCategory = new Category();
            newCategory.setName(name);
            newCategory.setType(type);
            em.persist(newCategory);
            return newCategory;
        }
    }
    public List<Category> getAllCategories(EntityManager em) {
        return em.createQuery("SELECT c FROM Category c ORDER BY c.type, c.name", Category.class)
                .getResultList();
    }
}