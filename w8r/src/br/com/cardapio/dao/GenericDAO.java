package br.com.cardapio.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.postgresql.util.PSQLException;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;

public class GenericDAO<E> extends GenericListDAO<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    public GenericDAO() {
    }

    public boolean persist(E entity) throws BancoDadosException, RegistroExistenteException {
        boolean persist = false;
        if (entity != null) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(entity);
                entityManager.getTransaction().commit();
                persist = true;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                if (e.getCause() instanceof DatabaseException) {
                    DatabaseException de = (DatabaseException) e.getCause();
                    if (de.getInternalException() instanceof PSQLException) {
                        PSQLException pe = (PSQLException) de.getInternalException();
                        if ("23505".equals(pe.getSQLState())) {
                            throw new RegistroExistenteException(e.getCause());
                        }
                    }else if(de.getInternalException() instanceof MySQLIntegrityConstraintViolationException ){
                        throw new RegistroExistenteException(e.getCause());                    
                    }
                }
                throw new BancoDadosException(e);
            }
        }
        return persist;
    }

    public boolean merge(E entity) throws BancoDadosException, IntegridadeReferencialException {
        boolean merge = false;
        if (entity != null) {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(entity);
                entityManager.getTransaction().commit();
                merge = true;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                if (e.getCause() instanceof DatabaseException) {
                    DatabaseException de = (DatabaseException) e.getCause();
                    if (de.getInternalException() instanceof PSQLException) {
                        PSQLException pe = (PSQLException) de.getInternalException();
                        if ("23503".equals(pe.getSQLState())) {
                            throw new IntegridadeReferencialException(e.getCause());
                        }
                    }else if(de.getInternalException() instanceof MySQLIntegrityConstraintViolationException ){
                        throw new IntegridadeReferencialException(e.getCause());                    
                    }
                }
                throw new BancoDadosException(e);
            }
        }
        return merge;
    }

    public void persistBatch(List<E> entities) throws BancoDadosException {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        int i = 0;
        for (E entity : entities) {
            try {
                entityManager.persist(entity);
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw new BancoDadosException(e);
            }
            if (i++ % 20 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
        tx.commit();
    }

    public void mergeBatch(List<E> entities) throws BancoDadosException {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        int i = 0;
        for (E entity : entities) {
            try {
                entityManager.merge(entity);
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw new BancoDadosException(e);
            }
            if (i++ % 20 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
        tx.commit();
    }

    public boolean remove(E entity) throws BancoDadosException, IntegridadeReferencialException {
        boolean remove = false;
        try {
            if (entity != null) {
                entityManager.getTransaction().begin();
                entity = entityManager.merge(entity);
                entityManager.remove(entity);
                entityManager.getTransaction().commit();
                remove = true;
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            if (e.getCause() instanceof DatabaseException) {
                DatabaseException de = (DatabaseException) e.getCause();
                if (de.getInternalException() instanceof PSQLException) {
                    PSQLException pe = (PSQLException) de.getInternalException();
                    if ("23503".equals(pe.getSQLState())) {
                        throw new IntegridadeReferencialException(e.getCause());
                    }
                }else if(de.getInternalException() instanceof MySQLIntegrityConstraintViolationException ){
                    throw new IntegridadeReferencialException(e.getCause());                    
                }
            }
            throw new BancoDadosException(e);
        }
        return remove;
    }

    public boolean refresh(E entity) throws BancoDadosException {
        boolean refresh = false;
        if (entity != null) {
            entityManager.refresh(entity);
            refresh = true;
        }
        return refresh;
    }

    public Query createQuery(String jpql) throws BancoDadosException {
        Query query = null;
        if (jpql != null && !jpql.isEmpty()) {
            query = entityManager.createQuery(jpql);
        }
        return query;
    }

    public Query createNamedQuery(String nameQuery) throws BancoDadosException {
        Query query = null;
        if (nameQuery != null && !nameQuery.isEmpty()) {
            query = entityManager.createNamedQuery(nameQuery);
        }
        return query;
    }

    public void executeQuery(Query query) throws BancoDadosException {
        if (query != null) {
            entityManager.getTransaction().begin();
            query.executeUpdate();
            entityManager.flush();
            entityManager.getTransaction().commit();
        }
    }

    public Query createNativeQuery(String sql, Class clazz) throws BancoDadosException {
        Query query = null;
        if (sql != null && !sql.isEmpty()) {
            query = entityManager.createNativeQuery(sql, clazz);
        }
        return query;
    }
}
