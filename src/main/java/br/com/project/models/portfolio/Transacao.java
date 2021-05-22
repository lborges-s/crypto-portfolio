package br.com.project.models.portfolio;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.project.models.MongoID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transacao {
	public Transacao() {
		
	}

	@JsonIgnore
	private MongoID id;
	private String tpTransacao;
	private String simboloMoeda;
	private double qtde;
	private double precoTransaco;
	private Date dtTransacao;
	
	
	@JsonIgnore
	public String getId() {
		return id.get$oid();
	}

	@JsonProperty("_id")
	public void setId(MongoID id) {
		this.id = id;
	}
}