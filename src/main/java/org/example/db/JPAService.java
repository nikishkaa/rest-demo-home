package org.example.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import org.example.exception.JpaException;
import org.example.util.HibernateAnnotationUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.String.format;
import static org.hibernate.cfg.JdbcSettings.*;


/**
 * Root JPA Service to interact with the database using persistence units.
 * <p><b>
 * Note: Before using {@link #getInstance getInstance}, make sure that it's {@link #initialize initialized}
 * </b></p>
 */
public class JPAService implements AutoCloseable {

    private static volatile JPAService instance;
    private EntityManagerFactory entityManagerFactory;

    private static final String FROM_FORMAT = "FROM %s";
    private static final String FROM_WHERE_FORMAT = "FROM %s WHERE %s";

    private JPAService() {
        entityManagerFactory = (EntityManagerFactory) HibernateAnnotationUtil.getSessionFactory();
    }

    public static JPAService initialize() {
        if (instance != null)
            throw new JpaException("JPAService already initialized.");
        synchronized (JPAService.class) {
            if (instance == null)
                instance = new JPAService();
        }
        return instance;
    }


    public static JPAService getInstance() {
        synchronized (JPAService.class) {
            if (instance == null)
                instance = new JPAService();
        }

//        if (instance == null)
//            throw new JpaException("Initialize JPAService first.");

        return instance;
    }

    public synchronized void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            instance = null;
        }
    }

    public <T> T findById(Class<T> entityClass, Object pk) {
        return run(entityManager -> {
            return entityManager.find(entityClass, pk);
        });
    }

    public <T> void saveOrUpdate(T entity) {
        runInTransaction(entityManager -> {
            entityManager.unwrap(Session.class).saveOrUpdate(entity);
        });
    }

    public <T> void create(T entity) {
        runInTransaction(entityManager -> {
            entityManager.persist(entity);
        });
    }

    public <T> T update(T entity) {
        return runInTransaction(entityManager -> {
            return entityManager.merge(entity);
        });
    }


    public <T> void deleteById(Class<T> entityClass, Object pk) {
        runInTransaction(entityManager -> {
            final T entity = entityManager.find(entityClass, pk);
            if (entity == null)
                // also the case EntityManager.lock with pessimistic locking
                throw new EntityNotFoundException("Entity does not exist");
            entityManager.remove(entity);
        });
    }

    public <T> void delete(T entity) {
        runInTransaction(entityManager -> {
            entityManager.remove(entity);
        });
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        return run(entityManager -> {
            return entityManager.createQuery(format(FROM_FORMAT, entityClass.getSimpleName()), entityClass).getResultList();
        });

    }

    public <T> List<T> findAll(Class<T> entityClass, String condition) {
        return run(entityManager -> {
            return entityManager.createQuery(format(FROM_WHERE_FORMAT, entityClass.getSimpleName(), condition), entityClass).getResultList();
        });
    }

    public <T> T run(Function<EntityManager, T> function) {
        final EntityManager em = getEntityManager();
        try {
            return function.apply(em);
        } finally {
            em.close();
        }
    }


    public <T> T runInTransaction(Function<EntityManager, T> function) {
        final EntityManager em = getEntityManager();
        EntityTransaction tx = null;
        T entity = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            entity = function.apply(em);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
        return entity;
    }

    private void runInTransaction(Consumer<EntityManager> function) {
        runInTransaction(entityManager -> {
            function.accept(entityManager);
            return null;
        });
    }

    protected EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

}
