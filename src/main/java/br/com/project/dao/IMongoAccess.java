package br.com.project.dao;

import java.util.List;

import org.bson.conversions.Bson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

interface IMongoAccess<T> {
	void add(T entity);
	
	void addMany(List<T> entities);
	
	T getById(String id);
	
	List<T> getAll() throws JsonMappingException, JsonProcessingException;

	void remove(Bson filterQuery);
	
	void removeMany(Bson filterQuery);
	
	void close();
}
