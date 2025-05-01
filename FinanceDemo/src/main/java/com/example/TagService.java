package com.example;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TagService {
    private EntityManagerFactory emf;

    public TagService() {
        emf = Persistence.createEntityManagerFactory("finance-demo");
    }

    public Set<Tag> processTags(EntityManager em, String tagsInput) {
        return Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .map(tag -> findOrCreateTag(em, tag))
                .collect(Collectors.toSet());
    }

    private Tag findOrCreateTag(EntityManager em, String name) {
        return (Tag) em.createQuery(
                        "SELECT t FROM Tag t WHERE t.name = :name", Tag.class)
                .setParameter("name", name.toLowerCase())
            .getResultStream()
                .findFirst()
                .orElseGet(() -> {
                    Tag newTag = new Tag();
                    em.persist(newTag);
                    return newTag;
                });
    }
}