/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Carta;
import Entidades.Proveedor;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private TableView<Carta> tableCarta;

    @FXML
    private TableColumn<Carta, Proveedor> columnProveedor;

    @FXML
    private TableColumn<Carta, String> columNumCarta, columnObra, columnEstado, columnImporte;

    @FXML
    private TableColumn<Carta, LocalDate> columnFecha;

    DetalleController oDetalleController;
    ObservableList<Carta> listCarta = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //sendListVencido ejecutado en mostrarAviso en CartaController
        initTableView();
        tableCarta.setItems(listCarta);
    }

    void setController(DetalleController aThis) {
        this.oDetalleController = aThis;

    }

    @FXML
    void getposicion() {
        Stage s = ((Stage) ap.getScene().getWindow());

        System.out.println("x: " + s.getX() + " y: " + s.getY());
    }

    @FXML
    void cerrar() {
        vistear();
        ((Stage) ap.getScene().getWindow()).close();
    }

    void sendListVencido(List<Carta> listCartaVencida) {
        for (Carta oalarma : listCartaVencida) {
            listCarta.add(oalarma);
        }

    }

    void vistear() {
        try {
            for (Carta carta : listCarta) {
                carta.setVisto(true);
                App.jpa.getTransaction().begin();
                App.jpa.persist(carta);
                App.jpa.getTransaction().commit();

            }
            oDetalleController.actualizarPorVencer();

        } catch (Exception e) {
            System.out.println(e.toString()+"problem en vistear()");
        }
    }

    void initTableView() {
        columnProveedor.setCellValueFactory(new PropertyValueFactory<Carta, Proveedor>("proveedor"));
        columNumCarta.setCellValueFactory(new PropertyValueFactory<Carta, String>("numCartaConfianza"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<Carta, LocalDate>("fechaVencimiento"));
        columnObra.setCellValueFactory(new PropertyValueFactory<Carta, String>("obra"));     
        columnEstado.setCellValueFactory(new PropertyValueFactory<Carta, String>("estado"));    
        columnImporte.setCellValueFactory(new PropertyValueFactory<Carta, String>("importe")); 
        columnObra.setCellFactory(column -> {
            TableCell<Carta, String> cell = new TableCell<Carta, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label ola = new Label();
                        //algoritmo para separar despues de 51 caracteres
                        String cadena = item;
                        String linea = "";
                        int numCharacteres = 51;//20 primeros
                        for (int i = 0; i < cadena.length() / numCharacteres; i++) {
                            linea = linea + cadena.substring(i * numCharacteres, (i + 1) * numCharacteres) + "\n";
                        }
                        linea = linea + cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length());
                        ola.setText(linea);
                        //fin
                        ola.setStyle("-fx-font-size: 9");
                        setGraphic(ola);
                        setText(null);
                    }
                }
            };

            return cell;
        });
    }
}
