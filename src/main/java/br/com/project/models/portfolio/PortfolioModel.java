package br.com.project.models.portfolio;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.project.models.MongoID;
import lombok.Getter;
import lombok.Setter;

public class PortfolioModel {

	@JsonIgnore
	private MongoID id;

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private List<Aporte> aportes = new ArrayList<>();

	@Getter
	@Setter
	private List<Transacao> transacoes = new ArrayList<>();

	public double calcVlrTotalAportes() {
		if (aportes.isEmpty())
			return 0;
		double vlrTotal = 0;
		for (Aporte aporte : aportes) {
			vlrTotal = aporte.getValor();
		}
		return vlrTotal;
	}

	public void addAporte(Aporte aporte) {
		aportes.add(aporte);
	}

	public void addTransacao(Transacao transacao) {
		transacoes.add(transacao);
	}

	@JsonIgnore
	public String getId() {
		return id.get$oid();
	}

	@JsonProperty("_id")
	public void setId(MongoID id) {
		this.id = id;
	}

}