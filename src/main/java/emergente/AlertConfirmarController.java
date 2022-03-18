/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package emergente;

import Entidades.Carta;
import controller.DetalleController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author UTIC
 */
public class AlertConfirmarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    AnchorPane ap;
    @FXML
    Label lblmensaje;
    
    DetalleController oDetalleController;
    Carta oCarta;
    int index;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setMensaje(String mensaje) {
        lblmensaje.setText(mensaje);
    }
    
    public void setController(DetalleController odc) {
        this.oDetalleController = odc;
    }
    public void setCartaIndex(Carta oCarta, int index) {
        this.index = index;
        this.oCarta = oCarta;
    }

    @FXML
    void eliminar() {  
        oDetalleController.eliminar(oCarta, index);
        oDetalleController.ap.setDisable(false);
        cerrar();
    }

    @FXML
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();
        oDetalleController.ap.setDisable(false);
    }

}
