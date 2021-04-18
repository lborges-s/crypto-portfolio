package br.com.project.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinChangeModel {

	private String nomeMoeda;
	private String imgMoeda;
	private double minima;
	private double maxima;

}
