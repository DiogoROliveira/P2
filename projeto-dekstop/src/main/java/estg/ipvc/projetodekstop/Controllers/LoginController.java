package estg.ipvc.projetodekstop.Controllers;

import estg.ipvc.projeto.data.BLL.AdminBLL;
import estg.ipvc.projeto.data.BLL.DBConnect;
import estg.ipvc.projeto.data.Entity.*;
import estg.ipvc.projetodekstop.OtherClasses.LoadFXML;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private PasswordField passwordPF;

    @FXML
    private TextField usernameTF;

    private final EntityManager em = DBConnect.getEntityManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(em == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de ligação");
            alert.setHeaderText("Não foi possível ligar à base de dados");
            alert.showAndWait();
            System.exit(0);
        }
        Admin admin = new Admin();
        if(em.createQuery("select a from Admin a", Admin.class).getResultList().isEmpty()){
            Utilizador user = new Utilizador();
            user.setUsername("admin");
            user.setPassword("admin");
            user.setNome("admin");
            Codpostal cp = new Codpostal();
            cp.setCodpostal("1000-001");
            cp.setLocalidade("Lisboa");
            user.setCodpostal(cp);
            AdminBLL.create(admin, user);
        }
    }

    public void login(ActionEvent event){

        if(usernameTF.getText().isEmpty() || passwordPF.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de login");
            alert.setHeaderText("Preencha todos os campos");
            alert.showAndWait();
            return;
        }

        try {
            String username = usernameTF.getText();
            String password = passwordPF.getText();



            Utilizador user = em.createQuery("SELECT u FROM Utilizador u WHERE u.username = :username", Utilizador.class)
                    .setParameter("username", username)
                    .getSingleResult();


            if (user != null) {

                if (user.getPassword().equals(password)) {

                    GestorVenda gestorVenda = new GestorVenda();
                    GestorProducao gestorProducao = new GestorProducao();
                    Admin admin = new Admin();

                    try {
                        gestorVenda = em.createQuery("SELECT g FROM GestorVenda g WHERE g.utilizador = :User", GestorVenda.class)
                                .setParameter("User", user)
                                .getSingleResult();
                    } catch (NoResultException ex){
                        System.out.println("Gestor de venda não encontrado");
                    }

                    try {
                        gestorProducao = em.createQuery("SELECT g FROM GestorProducao g WHERE g.utilizador = :User", GestorProducao.class)
                                .setParameter("User", user)
                                .getSingleResult();
                    } catch (NoResultException ex){
                        System.out.println("Gestor de produção não encontrado");
                    }

                    try {
                        admin = em.createQuery("SELECT a FROM Admin a WHERE a.utilizador = :User", Admin.class)
                                .setParameter("User", user)
                                .getSingleResult();
                    }catch (NoResultException ex) {
                        System.out.println("Admin não encontrado");
                    }

                    if (gestorVenda != null) {
                        LoadFXML.getInstance().loadResource("gestorvendamenu.fxml", "Menu Gestor de Venda", event);
                        return;
                    }

                    if (gestorProducao != null) {
                        LoadFXML.getInstance().loadResource("gestorprodmenu.fxml", "Menu Gestor de Venda", event);
                        return;
                    }

                    if(admin != null) {
                        LoadFXML.getInstance().loadResource("adminmenu.fxml", "Menu Gestor de Venda", event);
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro de login");
                    alert.setHeaderText("Password incorreta");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de login");
                alert.setHeaderText("Utilizador não encontrado");
                alert.showAndWait();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clear(){
        usernameTF.clear();
        passwordPF.clear();
    }

    public void register(MouseEvent event){
        LoadFXML.getInstance().loadResource("register.fxml", "Registar", event);
    }

}
