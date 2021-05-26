package br.com.project.models.portfolio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.project.models.MongoID;
import lombok.Getter;
import lombok.Setter;

public class AporteModel {

	public AporteModel() {}

	public AporteModel(double valor, String data) {
		this.valor = valor;
		this.data = data;
	}

	@JsonIgnore
	private MongoID id;
	@Getter
	@Setter
	private double valor;
	@Getter
	@Setter
	private String data;

	@JsonIgnore
	public String getId() {
		return id.get$oid();
	}

	@JsonProperty("_id")
	public void setId(MongoID id) {
		this.id = id;
	}
}