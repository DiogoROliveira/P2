package estg.ipvc.projetodekstop.OtherClasses;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoadFXML {

    private static LoadFXML instance = null;

    public static LoadFXML getInstance(){
        if(instance == null){
            instance = new LoadFXML();
        }
        return instance;
    }


    public void loadResource(String resource, String stageTitle, Event event){
        try {
            URL resourceUrl = getClass().getResource("/estg/ipvc/projetodekstop/" + resource);

            if(resourceUrl != null) {
                Parent root = FXMLLoader.load(resourceUrl);
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.resizableProperty().setValue(Boolean.FALSE);
                stage.setScene(scene);
                stage.setTitle(stageTitle);
                stage.show();
            }else {
                System.err.println("FXML file not found!");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}