package br.com.project.controllers;

import java.io.IOException;

import br.com.project.crypto_portfolio.App;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}