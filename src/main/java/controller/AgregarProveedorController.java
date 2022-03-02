/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Proveedor;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class AgregarProveedorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane ap;
    @FXML
    private JFXTextField jtfProveedor;
    @FXML
    private TableView<Proveedor> tableProveedor;
    @FXML
    private TableColumn<Proveedor, Integer> columnId;
    @FXML
    private TableColumn<Proveedor, String> columnNombreProveedor;

    AlertController oAlert = new AlertController();
    CartaController cartaController;
    DetalleController oDetalleController;
    ObservableList<Proveedor> listProveedor = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableView();
        tableProveedor.setItems(listProveedor);
        updateListaProveedor();
    }

    void setController(CartaController aThis) {
        this.cartaController = aThis;
    }

    void setController(DetalleController aThis) {
        this.oDetalleController = aThis;
    }

    @FXML
    void agregarProveedor() throws IOException {
        if (jtfProveedor.getText().length() != 0) {
            Proveedor oProveedor = new Proveedor(jtfProveedor.getText().trim());
            App.jpa.getTransaction().begin();
            App.jpa.persist(oProveedor);
            App.jpa.getTransaction().commit();
            oAlert.Mostrar("successful", "GUARDADO");
            jtfProveedor.setText("");
            updateListaProveedor();

        } else {
            oAlert.Mostrar("warning", "escriba un proveedor");
        }

    }

    @FXML
    void eliminar() {
        int index = selectItem();
        if (index != -1) {
            Proveedor oProveedor = listProveedor.get(index);
            App.jpa.getTransaction().begin();
            App.jpa.remove(oProveedor);
            App.jpa.getTransaction().commit();
            listProveedor.remove(index);
            updateListaProveedor();
            cartaController.setProveedor(new Proveedor(""));
            oDetalleController.setProveedor(new Proveedor(""));

        }

    }

    @FXML
    void updateListaProveedor() {
        List<Proveedor> olistProve = App.jpa.createQuery("select p from Proveedor p where nombreProveedor like '%" + jtfProveedor.getText().trim() + "%'"
                + "order by id DESC").setMaxResults(4).getResultList();
        listProveedor.clear();
        for (Proveedor oproveedor : olistProve) {
            listProveedor.add(oproveedor);
        }

    }

    int selectItem() {
        return listProveedor.indexOf(tableProveedor.getSelectionModel().getSelectedItem());
    }

    @FXML
    void sendProveedor() {
        if (selectItem() != -1) {
            if (cartaController != null) {
                cartaController.setProveedor(listProveedor.get(selectItem()));
            }
            if (oDetalleController != null) {
                oDetalleController.setProveedor(listProveedor.get(selectItem()));
            }
            //cerrar();
        }
    }

    @FXML
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();
    }

    void initTableView() {
        columnId.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("id"));
        columnNombreProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombreProveedor"));
    }
}
