/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author UTIC
 */
public class AvisoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane ap;
    CartaController oCartaController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void setController(CartaController aThis) {
        this.oCartaController=aThis;
       
    }
    @FXML
    void getposicion(){
       Stage s= ((Stage) ap.getScene().getWindow());
       
        System.out.println("x: "+s.getX()+" y: "+s.getY());
        
    }    
}
