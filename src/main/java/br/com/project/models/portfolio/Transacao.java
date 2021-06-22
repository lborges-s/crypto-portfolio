package br.com.project.models.portfolio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private int counter = 1;
	/**
	 * C = COMPRA |
	 * V = VENDA
	*/
	private char tpTransacao;
	private String simboloMoeda;
	private double qtde;
	private double precoTransacao;
	private String dtTransacao;
	private int ano;

	@JsonIgnore
	public String getId() {
		return id.get$oid();
	}

	@JsonProperty("_id")
	public void setId(MongoID id) {
		this.id = id;
	}

	@JsonIgnore
	public boolean isVenda() {
		return tpTransacao == 'V';
	}
	
	@JsonIgnore
	public boolean isCompra() {
		return tpTransacao == 'C';
	}
	
	@JsonIgnore
	public Date getDate() throws ParseException {
		return new SimpleDateFormat("dd/MM/yyyy").parse(dtTransacao);
	}

	public double custoTransacao() {
		return qtde * precoTransacao;
	}
}
