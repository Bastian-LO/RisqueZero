package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PageInscriptionController {

    @FXML
    private TextField nomTextField;
    @FXML
    private TextField prenomTextField;
    @FXML
    private TextField mailTextField;
    @FXML
    private TextField dateNaissanceTextField;
    @FXML
    private TextField telephoneTextField;
    @FXML
    private Button inscriptionButton;

    public void inscriptionHandle(ActionEvent actionEvent) {
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String mail = mailTextField.getText();
        String dateNaissance = dateNaissanceTextField.getText();
        String telephone = telephoneTextField.getText();
    }
}
