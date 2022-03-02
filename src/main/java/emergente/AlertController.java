/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package emergente;

import controller.App;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class AlertController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    HBox hboxAlert;
    @FXML
    Button jbtnOk;
    @FXML
    ImageView jimg;
    @FXML
    AnchorPane ap;
    @FXML
    Label lblmensaje;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setMensaje(String mensaje) {
        lblmensaje.setText(mensaje);
    }

    void Asignar(String alerta) {
        // #38940d verde
        // #db092f rojo
        // #e0d314 amarillo
        Image imag = null;
        if (alerta.equals("successful")) {
            jbtnOk.setStyle("-fx-background-color : #38940d");
            hboxAlert.setStyle("-fx-background-color : #38940d");
        } else if (alerta.equals("warning")) {
            jbtnOk.setStyle("-fx-background-color : #e0d314");
            hboxAlert.setStyle("-fx-background-color : #e0d314");
        } else {
            jbtnOk.setStyle("-fx-background-color : #db092f");
            hboxAlert.setStyle("-fx-background-color : #db092f");
        }
        imag = new Image(getClass().getResource("/images/" + alerta + ".png").toExternalForm());
        jimg.setImage(imag);
    }

    public void Mostrar(String alerta, String mensaje) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AlertController.class.getResource("/fxml/Alert.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();//creando la base vacía
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(App.stagePrincpal);
        stage.setTitle("Guardando");
        stage.setScene(scene);
        //
        AlertController oAlertController = (AlertController) loader.getController(); //esto depende de (1)
        oAlertController.Asignar(alerta);
        oAlertController.setMensaje(mensaje);
        stage.show();
    }
   
    @FXML
    void cambiar() {
        Image imag = new Image(getClass().getResource("../imagenes/geminis1.jpg").toString());
        jimg.setImage(imag);
    }

    @FXML
    void cerrar(ActionEvent event) throws FileNotFoundException, IOException {
        ((Stage) ap.getScene().getWindow()).close();
        /*FileImagUtil oFileUtil = new FileImagUtil("user.home", "holi");
        File fileImag = oFileUtil.buscarImagen();
        if (fileImag != null) {
            jimg.setImage(new Image(fileImag.getAbsolutePath()));
        }*/
    }

}
