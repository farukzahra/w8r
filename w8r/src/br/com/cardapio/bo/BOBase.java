package br.com.cardapio.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import br.com.cardapio.dao.GenericDAO;
import br.com.cardapio.exception.BancoDadosException;
import br.com.cardapio.exception.IntegridadeReferencialException;
import br.com.cardapio.exception.RegistroExistenteException;

// TODO : Trocar as Exceptions por BancoDadosException e lancar elas
public class BOBase<E extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient GenericDAO<E> dao;

	private Class<E> clazz;

	public GenericDAO<E> getDao() {
		if (this.dao == null) {
			this.dao = new GenericDAO<E>();
		}
		return this.dao;
	}

	public E find(E entity) throws BancoDadosException {
		return getDao().find(entity);
	}

	public E find(Object primaryKey) throws BancoDadosException {
		return getDao().find(clazz, primaryKey);
	}

	public E findByField(String fieldName, Object fieldValue) throws BancoDadosException {
		return getDao().findByField(clazz, fieldName, fieldValue);
	}

	public E findByFields(Map<String, Object> filtros) throws BancoDadosException {
		return getDao().findByFields(clazz, filtros);
	}

	public List<E> listByFields(Map<String, Object> filtros) throws BancoDadosException {
		return listByFields(filtros, null);
	}
	
	public List<E> listByFields(Map<String, Object> filtros, String[] ordenacao) throws BancoDadosException {
		return getDao().listByFields(clazz, filtros, ordenacao);
	}

	public E find(Class<E> classEntity, Object primaryKey) throws BancoDadosException {
		return getDao().find(classEntity, primaryKey);
	}

	public boolean refresh(E entity) throws BancoDadosException {
		return getDao().refresh(entity);
	}

	public boolean persist(E entity) throws BancoDadosException, IntegridadeReferencialException, RegistroExistenteException, BancoDadosException {
		if (getDao().find(entity) != null) {
			return getDao().merge(entity);
		} else {
			return getDao().persist(entity);
		}
	}
	
	public boolean merge(E entity) throws BancoDadosException, IntegridadeReferencialException {
		return getDao().merge(entity);
	}
	
	public void persistBatch(List<E> entities) throws BancoDadosException, RegistroExistenteException {
		getDao().persistBatch(entities);
	}
	
	public void mergeBatch(List<E> entities) throws BancoDadosException, IntegridadeReferencialException {
		getDao().mergeBatch(entities);
	}
	
	public void executeQuery(String jpql, Map<String, Object> parametros) throws BancoDadosException {
		getDao().executeQuery(jpql, parametros);
	}

	public boolean remove(E entity) throws BancoDadosException, IntegridadeReferencialException {
		return getDao().remove(entity);
	}

	public List<E> list(final String jpql) throws BancoDadosException {
		List<E> list = null;
		list = getDao().list(jpql);
		return list;
	}

	public List<E> list(final Query jpql) throws BancoDadosException {
		return getDao().list(jpql);
	}

	public List<E> listAll() throws BancoDadosException {
		return getDao().listAll(getClazz());
	}

	public Class<E> getClazz() {
		return clazz;
	}

	public void setClazz(Class<E> clazz) {
		this.clazz = clazz;
	}
}
