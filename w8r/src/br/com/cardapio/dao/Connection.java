package br.com.cardapio.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Connection implements Serializable {
	
    private static final long serialVersionUID = 1L;

    private static Connection instance;
    	
	private Connection() {}

	public static Connection getInstance() {		
		if (instance == null) {
			instance = new Connection();
		}		
		return instance;
	}

	private EntityManager createEntityManager(String persistenceUnitName) {
		try {			
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);			
			return emf.createEntityManager();			
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
	}

	public EntityManager getEntityManager(String persistenceUnitName) {
	    return createEntityManager(persistenceUnitName);
	}
}
