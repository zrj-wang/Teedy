package com.sismics.docs.core.service;


import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.event.model.jpa.RegisterRequest;
import com.sismics.docs.core.event.model.jpa.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Date;
import java.util.List;

public class RegisterRequestService {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("transactions-optional");

    /**
     * 用户提交注册请求
     */
    public void submitRequest(String username, String email, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            RegisterRequest request = new RegisterRequest();
            request.setUsername(username);
            request.setEmail(email);
            request.setPassword(password);
            request.setStatus("pending");
            request.setCreateDate(new Date());

            em.persist(request);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * 管理员查看所有待处理的注册请求
     */
    public List<RegisterRequest> getPendingRequests() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "FROM RegisterRequest WHERE status = 'pending'", RegisterRequest.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * 管理员批准请求
     */
    public void approveRequest(String requestId, String adminId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // 查找注册请求
            RegisterRequest request = em.find(RegisterRequest.class, requestId);
            if (request != null && "pending".equals(request.getStatus())) {
                // 创建新用户实例
                User user = new User();
                user.setUsername(request.getUsername());
                user.setPassword(request.getPassword()); // 明文（UserDao.create 内部会加密）
                user.setEmail(request.getEmail());

                // 设置默认值
                user.setCreateDate(new Date());
                user.setRoleId("user"); // 确保 T_ROLE 中有这个 ID
                user.setStorageQuota(100L * 1024 * 1024); // 默认 100MB
                user.setOnboarding(true);

                // 使用 UserDao 创建用户（会自动设置缺失字段，比如 locale、theme、privateKey）
                UserDao userDao = new UserDao();
                userDao.create(user, adminId);

                // 更新请求状态为已批准
                request.setStatus("approved");
                em.merge(request);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }



    /**
     * 管理员拒绝请求
     */
    public void rejectRequest(String requestId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            RegisterRequest request = em.find(RegisterRequest.class, requestId);
            if (request != null && "pending".equals(request.getStatus())) {
                request.setStatus("rejected");
                em.merge(request);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
