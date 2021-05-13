package br.com.project.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import br.com.project.models.portfolio.AporteModel;

public class MongoConcrete<T> implements IMongoAccess<T> {
	private static final MongoClient client = MongoClients.create("mongodb://localhost:27017");
	private static MongoDatabase database = client.getDatabase("criptofolio");
	private MongoCollection<Document> collection;
	private ObjectMapper objectMapper = new ObjectMapper();

	private Class<T> entityClass;
	
	public MongoConcrete(Class<T> entityClass) {
		collection = database.getCollection("portfolios");
		this.entityClass =entityClass;
	}

	@Override
	public void add(T entity) {

		try {
			String json = objectMapper.writeValueAsString(entity);
			System.out.println("Documento para adicionar > " + Document.parse(json));
			collection.insertOne(Document.parse(json));
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addMany(List<T> entities) {

	}

	@Override
	public T getById(String id) {
		
		return null;
	}

	@Override
	public List<T> getAll() throws JsonMappingException, JsonProcessingException {
		List<T> list = new ArrayList<>();
		List<Document> iterDoc = collection.find().into(new ArrayList<>());

		for (Document doc : iterDoc) {
			System.out.println(doc.toJson());
			final T object = objectMapper.readValue(doc.toJson(), entityClass);
			list.add(object);
		}
		return list;
	}

	@Override
	public void remove(Bson filterQuery) {
		collection.findOneAndDelete(filterQuery);
	}

	@Override
	public void removeMany(Bson filterQuery) {

	}

	@Override
	public void close() {
		client.close();
	}

	@Override
	public void addAporte(String id, AporteModel aporte) {
		// TODO Auto-generated method stub
		try {
			String json = objectMapper.writeValueAsString(aporte);
			
			collection.updateOne(
			    Filters.eq("_id", id),
			    new Document().append(
		            "$push",
		            new Document("aportes", Document.parse(json)))
			);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
