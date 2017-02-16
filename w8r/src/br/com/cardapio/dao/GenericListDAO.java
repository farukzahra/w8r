package br.com.cardapio.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;

import br.com.cardapio.exception.BancoDadosException;

public class GenericListDAO<E> {
	
	public static final String FILTRO_GENERICO_QUERY = "FILTRO_GENERICO_QUERY";

	protected final String persistenceUnitName = "Cardapio";

	protected final EntityManager entityManager;

	public GenericListDAO() {
		entityManager = Connection.getInstance().getEntityManager(persistenceUnitName);
	}

	public E find(Class<E> classEntity, Object primaryKey) throws BancoDadosException {
		E entity = null;
		if (classEntity != null && primaryKey != null) {
			try {
				entity = entityManager.find(classEntity, primaryKey);
			}
			catch (Exception e) {
				throw new BancoDadosException(e);
			}
		}
		return entity;
	}
	
	public E findByField(Class<E> classEntity, String nomeCampo, Object valorCampo) throws BancoDadosException {
		Query query = createQuery("select o from "+classEntity.getSimpleName()+" o where o."+nomeCampo+" = :"+nomeCampo.replace('.', '_'));
		query.setParameter(nomeCampo.replace('.', '_'), valorCampo);
		return (E)getSingleResult(query);
	}
	
	public E findByFields(Class<E> classEntity, Map<String, Object> filtros) {
		Query query = montarQueryDinamica(classEntity, filtros);		
		return (E)getSingleResult(query);
	}
	
	public List<E> listByFields(Class<E> classEntity, Map<String, Object> filtros, String[] ordenacao) {
		Query query = montarQueryDinamica(classEntity, filtros, ordenacao);			
		return (List<E>)list(query);
	}
	
	public List<E> listByFields(Class<E> classEntity, Map<String, Object> filtros) {
		return listByFields(classEntity, filtros);
	}

	private Query montarQueryDinamica(Class<E> classEntity,
			Map<String, Object> filtros, String[] ordenacoes) {
		String sql = "select o from "+classEntity.getSimpleName()+" o where ";
		for(String nomeFiltro : filtros.keySet()){
			if(FILTRO_GENERICO_QUERY.equals(filtros.get(nomeFiltro)))
				sql += " and "+nomeFiltro;
			else
				sql += " and o."+nomeFiltro+" = :"+nomeFiltro.replace('.', '_');
				
		}
		sql = sql.replaceFirst(" and ", " ");
		if(ordenacoes != null){
			sql += " order by ";
			for(String ordenacao : ordenacoes){
				sql += " ,o." + ordenacao;
			}
			sql = sql.replaceFirst(" ,", " ");
		}
		Query query = createQuery(sql);
		for(String nomeCampo : filtros.keySet()){
			if(!FILTRO_GENERICO_QUERY.equals(filtros.get(nomeCampo)))
				query.setParameter(nomeCampo.replace('.', '_'), filtros.get(nomeCampo));
		}
		
		return query;
	}
	
	private Query montarQueryDinamica(Class<E> classEntity,
			Map<String, Object> filtros) {
		return montarQueryDinamica(classEntity, filtros, null);
	}

	@SuppressWarnings("unchecked")
	public E find(E entity) throws BancoDadosException {
		if (entity != null) {
			for (Field field : entity.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
					boolean oldAccessible = field.isAccessible();
					field.setAccessible(true);
					try{
						Object key = field.get(entity);
						field.setAccessible(oldAccessible);
						return find((Class<E>) entity.getClass(), key);
					}catch (Exception e) {
						throw new BancoDadosException(e);
					}
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<E> list(Query query) {
		List<E> list =  new ArrayList<E>();
		if (query != null) {
			list = query.getResultList();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public void list(Query query, List<?> list){
		if (query != null) {
			list = query.getResultList();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object getSingleResult(Query query) {
		Object entity = null;
		if (query != null && query.getResultList().size() > 0) {
			if(query.getResultList().size() > 1)
				entity = query.getResultList().get(0);
			else if(query.getResultList().size() == 1)
				entity = query.getSingleResult();
		}
		return entity;
	}

	public List<E> list(String jpql){
		return list(createQuery(jpql));
	}
	
	public void list(String jpql, List<?> list){
		list(createQuery(jpql), list);
	}
	
	public Object getSingleResult(String jpql) throws Exception {
		return getSingleResult(createQuery(jpql));
	}
	
	public Object getSingleResultNativeQuery(String sql) throws Exception {
		return getSingleResult(createNativeQuery(sql));
	}
	
	public List<E> listNativeQuery(String sql, Class<E> classEntity) throws Exception {
		return list(createNativeQuery(sql, classEntity));
	}
	
	public List<E> listAll(Class<E> classEntity) {
		return list(createQuery("select o from "+classEntity.getSimpleName()+" o"));
	}
	
	public void executeQuery(String jpql, Map<String, Object> parametros) throws BancoDadosException {
		Query query = createQuery(jpql);
		for(String nomeParametro : parametros.keySet()){
			Object valorParametro = parametros.get(nomeParametro);
			query.setParameter(nomeParametro, valorParametro);
		}
		try {
			entityManager.getTransaction().begin();
			query.executeUpdate();
			entityManager.getTransaction().commit();
		}catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			throw new BancoDadosException(e);
		}		
	}

	private Query createQuery(String jpql) {
		Query query = null;
		if (jpql != null && !jpql.isEmpty()) {
			query = entityManager.createQuery(jpql);
		}
		return query;
	}
	
	private Query createNativeQuery(String sql, Class<E> classEntity) {
		Query query = null;
		if (sql != null && !sql.isEmpty()) {
			query = entityManager.createNativeQuery(sql, classEntity);
		}
		return query;
	}
	
	private Query createNativeQuery(String sql) {
		Query query = null;
		if (sql != null && !sql.isEmpty()) {
			query = entityManager.createNativeQuery(sql);
		}
		return query;
	}
}
