package br.com.project.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FormPortfolioController implements Initializable{
	
	private String teste;

	@FXML
	private Label lblNomePortifolio;
	
	FormPortfolioController(String teste) {
		this.teste = teste;
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(teste);
		
	}

}
