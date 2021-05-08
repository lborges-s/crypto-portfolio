package br.com.project.models;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aporte {
	private Date data;
	private double valor;
}