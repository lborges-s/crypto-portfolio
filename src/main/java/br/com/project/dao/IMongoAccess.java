package br.com.project.dao;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.project.models.portfolio.AporteModel;

interface IMongoAccess<T>  {
	void add(T entity);
	
	void addMany(List<T> entities);
	
	T getById(String id) throws JsonMappingException, JsonProcessingException;
	
	List<T> getAll() throws JsonMappingException, JsonProcessingException;

	boolean remove(String id);
	
	void addAporte(String id, AporteModel aporte);
	
	void close();
}