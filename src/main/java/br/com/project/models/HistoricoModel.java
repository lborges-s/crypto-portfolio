package br.com.project.models;


import br.com.project.models.portfolio.AporteModel;
import br.com.project.models.portfolio.Transacao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoricoModel {
	private Transacao transacao;
	private AporteModel aporte;
	
	public HistoricoModel(Transacao transacao) {
		this.transacao = transacao;
	}

	public HistoricoModel(AporteModel aporte) {
		this.aporte = aporte;
	}
	
	
	public boolean isTransacao() {
		return transacao != null;
	}

}
