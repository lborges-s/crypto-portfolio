package br.com.project.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import br.com.project.models.portfolio.AporteModel;
import br.com.project.models.portfolio.PortfolioModel;

public class MongoConcretePortfolio implements IMongoAccess<PortfolioModel> {
	private static final MongoClient client = MongoClients.create("mongodb://localhost:27017");
	private static MongoDatabase database = client.getDatabase("criptofolio");
	private MongoCollection<Document> collection;
	private ObjectMapper objectMapper = new ObjectMapper();

//	private Class<T> entityClass;

	public MongoConcretePortfolio() {
		collection = database.getCollection("portfolios");
//		this.entityClass =entityClass;
	}

	@Override
	public void add(PortfolioModel portfolio) {

		try {
			String json = objectMapper.writeValueAsString(portfolio);
			System.out.println("Documento para adicionar > " + Document.parse(json));
			collection.insertOne(Document.parse(json));

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addMany(List<PortfolioModel> entities) {

	}

	@Override
	public PortfolioModel getById(String id) throws JsonMappingException, JsonProcessingException {
		ObjectId objId = new ObjectId(id);
		Document doc = collection.find(new BasicDBObject("_id", objId)).first();
		return objectMapper.readValue(doc.toJson(), PortfolioModel.class);
	}

	@Override
	public List<PortfolioModel> getAll() throws JsonMappingException, JsonProcessingException {
		List<PortfolioModel> list = new ArrayList<>();
		List<Document> iterDoc = collection.find().into(new ArrayList<>());

		for (Document doc : iterDoc) {
			System.out.println(doc.toJson());
			final PortfolioModel object = objectMapper.readValue(doc.toJson(), PortfolioModel.class);
			list.add(object);
		}
		return list;
	}

	@Override
	public boolean remove(String id) {
		ObjectId objId = new ObjectId(id);
		Bson filterQuery = new BasicDBObject("_id", objId);
		Document doc = collection.findOneAndDelete(filterQuery);
		return doc != null;
	}

	@Override
	public void addAporte(String id, AporteModel aporte) {
		try {
			String json = objectMapper.writeValueAsString(aporte);
			Document newAporte = Document.parse(json);
			ObjectId objId = new ObjectId(id);

			collection.updateOne(Filters.eq("_id", objId), Updates.addToSet("aportes", newAporte));
			
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public void updatePortfolioName(String id, String newName) {
		ObjectId objId = new ObjectId(id);
//		BasicDBObject newDocument = new BasicDBObject();
//		newDocument.append("$set", new BasicDBObject().append("nome", newName));

		UpdateResult result = collection.updateOne(Filters.eq("_id", objId), Updates.set("nome", newName));
	}

	@Override
	public void close() {
		client.close();
	}
}
