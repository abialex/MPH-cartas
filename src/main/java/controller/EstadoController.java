/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Carta;
import Entidades.Proveedor;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class EstadoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField jtfbuscar;

    @FXML
    private JFXComboBox<String> jcbtiempo;

    @FXML
    private TableView<Carta> tableCarta;

    @FXML
    private TableColumn<Carta, Proveedor> columnProveedor;

    @FXML
    private TableColumn<Carta, String> columNumCarta;

    @FXML
    private TableColumn<Carta, LocalDate> columnFecha;

    @FXML
    private TableColumn<?, ?> columnReferencia;

    @FXML
    private TableColumn<?, ?> columnObra;

    @FXML
    private TableColumn<?, ?> columnImporte;

    ObservableList<Carta> listCarta = FXCollections.observableArrayList();
    CartaController oCartaController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableView();
        tableCarta.setItems(listCarta);
        updateListaComprobante();
        ObservableList<String> ESTADO = FXCollections.observableArrayList("1 DÍA", "3 DÍAS", "1 SEMANA", "1 MES");
        jcbtiempo.setItems(ESTADO);
    }

    public void setController(CartaController cpc) {
        this.oCartaController = cpc;

    }

    @FXML
    void test() {
       LocalDate local=LocalDate.parse(jtfbuscar.getText().trim());
       LocalDate local2=local.minusDays(1+1);
       local2=local.minusDays(2+1);
       local2=local.minusDays(3+1);
       local2=local.minusDays(7+1);
       local2=local.minusDays(30+1);
           System.out.println( local2.toString()+" / "+LocalDate.now().toString()+" : " +local2.isBefore(LocalDate.now()));
           
       //para cambiar de vigente a vencido minusday(1) y luego comparar before;
                
     
    }

    @FXML
    void updateListaComprobante() {
        List<Carta> olistAlarma = App.jpa.createQuery("select p from Carta p where estado = 'VIGENTE' ORDER BY fechavencimiento asc").getResultList();
        List<Carta> listFilter=new ArrayList<Carta>();
        listCarta.clear();
        for (Carta oCarta : olistAlarma) {
            if(oCarta.getFechaVencimiento().minusDays(3+1).isBefore(LocalDate.now())){
                listCarta.add(oCarta);
            }
            
        }
    }

    void initTableView() {
        columnProveedor.setCellValueFactory(new PropertyValueFactory<Carta, Proveedor>("proveedor"));
        columNumCarta.setCellValueFactory(new PropertyValueFactory<Carta, String>("numCartaConfianza"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<Carta, LocalDate>("fechaVencimiento"));
    }
    void filtrarBy(List<Carta> list){
        
        
    }

}
