package br.com.project.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortifolioModel {
	private String id;
	private String nome;
	private double vlrInicialAporte;
	
	private List <Aporte> aportes;
}