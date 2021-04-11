module br.com.project.crypto_portfolio {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.com.project.crypto_portfolio to javafx.fxml;
    exports br.com.project.crypto_portfolio;
}