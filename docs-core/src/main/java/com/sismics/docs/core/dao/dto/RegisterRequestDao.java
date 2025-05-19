package com.sismics.docs.core.dao.dto;

import com.sismics.docs.core.event.model.jpa.RegisterRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RegisterRequestDao {

    private EntityManager getEntityManager() {
        return Persistence.createEntityManagerFactory("your-persistence-unit-name").createEntityManager();
    }

    public void create(RegisterRequest request) {
        request.setId(UUID.randomUUID().toString());
        request.setStatus("pending");
        request.setCreateDate(new Date());

        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(request);
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    public RegisterRequest findById(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RegisterRequest.class, id);
        } finally {
            em.close();
        }
    }

    public List<RegisterRequest> findPending() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<RegisterRequest> query = em.createQuery(
                    "FROM RegisterRequest r WHERE r.status = :status", RegisterRequest.class);
            query.setParameter("status", "pending");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void update(RegisterRequest request) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(request);
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }
}
