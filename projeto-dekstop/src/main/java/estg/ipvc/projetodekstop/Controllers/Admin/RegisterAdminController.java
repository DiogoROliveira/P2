package estg.ipvc.projetodekstop.Controllers.Admin;

import estg.ipvc.projeto.data.BLL.AdminBLL;
import estg.ipvc.projeto.data.BLL.DBConnect;
import estg.ipvc.projeto.data.Entity.*;
import estg.ipvc.projetodekstop.OtherClasses.LoadFXML;
import jakarta.persistence.EntityManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterAdminController implements Initializable {

    @FXML
    private TextField codpostal;

    @FXML
    private TextField email;

    @FXML
    private Text matchPassTxt;

    @FXML
    private TextField nome;

    @FXML
    private TextField nporta;

    @FXML
    private PasswordField pass;

    @FXML
    private PasswordField pass2;

    @FXML
    private TextField rua;

    @FXML
    private TextField telefone;

    @FXML
    private TextField username;

    @FXML
    void back(MouseEvent event) {
        LoadFXML.getInstance().loadResource("adminmenu.fxml", "Menu Admin", event);
    }

    @FXML
    void createAcc(ActionEvent event) {

        EntityManager em = DBConnect.getEntityManager();

        if(!verifyPass()){
            alert("Passwords diferentes.");
            return;
        }
        if(username.getText().isEmpty() ||
                pass.getText().isEmpty() ||
                pass2.getText().isEmpty() ||
                nome.getText().isEmpty() ||
                email.getText().isEmpty() ||
                telefone.getText().isEmpty() ||
                rua.getText().isEmpty() ||
                nporta.getText().isEmpty() ||
                codpostal.getText().isEmpty()){
            alert("Preencha todos os campos.");
            return;
        }

        if(username.getText().equals(em.createQuery("SELECT u.username FROM Utilizador u WHERE u.username = :username", String.class).setParameter("username", username.getText()).getResultList().stream().findFirst().orElse(null))){
            alert("Username já existe, tente outro.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de registo");
        alert.setHeaderText("Tem a certeza que deseja criar a conta?");
        alert.showAndWait();

        if(alert.getResult().getText().equals("Cancelar")){
            return;
        }

        Utilizador u = new Utilizador();
        setUserData(u);
        Admin admin = new Admin();

        try {
            AdminBLL.create(admin, u);
        } catch (Exception e) {
            alert("Erro ao criar conta");
            em.close();
            LoadFXML.getInstance().loadResource("adminmenu.fxml", "Menu Admin", event);
            return;
        }


        em.close();
        LoadFXML.getInstance().loadResource("adminmenu.fxml", "Menu Admin", event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public boolean verifyPass(){
        matchPassTxt.setVisible(true);
        if(pass.getText().equals(pass2.getText())){
            matchPassTxt.setText("Passwords iguais");
            matchPassTxt.setStyle("-fx-fill: green");
            return true;
        } else {
            matchPassTxt.setText("Passwords diferentes");
            return false;
        }
    }

    private void alert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de registo");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private void setUserData(Utilizador u){
        Codpostal cp = new Codpostal();
        cp.setCodpostal(codpostal.getText());
        u.setCodpostal(cp);
        u.setEmail(email.getText());
        u.setRua(rua.getText());
        u.setNumporta(Integer.parseInt(nporta.getText()));
        u.setNome(nome.getText());
        u.setPassword(pass.getText());
        u.setTelefone(telefone.getText());
        u.setUsername(username.getText());
    }

}