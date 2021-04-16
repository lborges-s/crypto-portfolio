package br.com.project.models;

public class CoinChangeModel {

	private String nomeMoeda;
	private String imgMoeda;
	private double minima;
	private double maxima;

	public String getNomeMoeda() {
		return nomeMoeda;
	}

	public void setNomeMoeda(String nomeMoeda) {
		this.nomeMoeda = nomeMoeda;
	}

	public String getImgMoeda() {
		return imgMoeda;
	}

	public void setImgMoeda(String imgMoeda) {
		this.imgMoeda = imgMoeda;
	}

	public double getMinima() {
		return minima;
	}

	public void setMinima(double minima) {
		this.minima = minima;
	}

	public double getMaxima() {
		return maxima;
	}

	public void setMaxima(double maxima) {
		this.maxima = maxima;
	}

}
