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
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
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
 * @author yalle
 */
public class EstadoController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfbuscar;

    @FXML
    private JFXComboBox<String> jcbtiempo;

    @FXML
    private TableView<Carta> tableCarta;

    @FXML
    private TableColumn<Carta, Proveedor> columnProveedor;

    @FXML
    private TableColumn<Carta, String> columNumCarta, columnEstado, columnObra, columnImporte;

    @FXML
    private TableColumn<Carta, LocalDate> columnFecha;

    @FXML
    private TableColumn<?, ?> columnReferencia;

    @FXML
    private TableColumn<Carta, LocalDate> columnEn;

    @FXML
    private Label lbl1d, lbl2d, lbl7d, lbl15d;

    ObservableList<Carta> listCarta = FXCollections.observableArrayList();
    DetalleController oCartaController;
    List<Carta> listCarta1D;
    List<Carta> listCarta2D;
    List<Carta> listCarta7D;
    List<Carta> listCarta15D;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableView();
        getListas();
        cargarDatoaPantalla();
        tableCarta.setItems(listCarta);
        //updateListaComprobante();
        ObservableList<String> ESTADO = FXCollections.observableArrayList("MAÑANA", "2 DÍAS A MENOS", "1 SEMANA A MENOS", "15 MES A MENOS");
        jcbtiempo.setItems(ESTADO);
        jcbtiempo.getSelectionModel().select("2 DÍAS A MENOS");
        seleccionarRango();
    }

    public void setController(DetalleController cpc) {
        this.oCartaController = cpc;

    }

    @FXML
    void test() {
        LocalDate local = LocalDate.parse(jtfbuscar.getText().trim());
        LocalDate local2 = local.minusDays(1 + 1);
        local2 = local.minusDays(2 + 1);
        local2 = local.minusDays(3 + 1);
        local2 = local.minusDays(7 + 1);
        local2 = local.minusDays(30 + 1);
        System.out.println(local2.toString() + " / " + LocalDate.now().toString() + " : " + local2.isBefore(LocalDate.now()));

        //para cambiar de vigente a vencido minusday(1) y luego comparar before;
    }

    @FXML
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }

    @FXML
    void updateListaComprobante() {
        List<Carta> olistAlarma = App.jpa.createQuery("select p from Carta p where estado = 'VIGENTE' ORDER BY fechavencimiento asc").getResultList();
        List<Carta> listFilter = new ArrayList<Carta>();
        listCarta.clear();
        for (Carta oCarta : olistAlarma) {
            if (oCarta.getFechaVencimiento().minusDays(3 + 1).isBefore(LocalDate.now())) {
                listCarta.add(oCarta);
            }

        }
    }

    void initTableView() {
        columnProveedor.setCellValueFactory(new PropertyValueFactory<Carta, Proveedor>("proveedor"));
        columNumCarta.setCellValueFactory(new PropertyValueFactory<Carta, String>("numCartaConfianza"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<Carta, LocalDate>("fechaVencimiento"));
        columnEn.setCellValueFactory(new PropertyValueFactory<Carta, LocalDate>("fechaVencimiento"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<Carta, String>("estado"));
        columnObra.setCellValueFactory(new PropertyValueFactory<Carta, String>("obra"));
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
        columnEn.setCellFactory(column -> {
            TableCell<Carta, LocalDate> cell = new TableCell<Carta, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {

                        Period period = Period.between(LocalDate.now(), item);
                        long diffDays = period.getDays();
                        Label oLabel = new Label();
                        oLabel.setText(diffDays + " Día(s)");
                        String style = "-fx-font-size: 11; -fx-alignment: center; -fx-max-width:999;";
                        if (2 >= diffDays) {
                            style = style + " -fx-text-fill: RED;";
                        }
                        oLabel.setStyle(style);
                        setGraphic(oLabel);
                    }
                }
            };
            return cell;
        });
    }

    void filtrarBy(List<Carta> list) {

    }

    void getListas() {
        LocalDate lc = LocalDate.now();
        listCarta1D = App.jpa.createQuery("select p from Carta p where fechavencimiento BETWEEN '"
                + lc.plusDays(1).toString() + "' and '" + lc.plusDays(1) + "' order by fechavencimiento asc").getResultList();
        listCarta2D = App.jpa.createQuery("select p from Carta p where fechavencimiento BETWEEN '"
                + lc.plusDays(1).toString() + "' and '" + lc.plusDays(2) + "' order by fechavencimiento asc").getResultList();
        listCarta7D = App.jpa.createQuery("select p from Carta p where fechavencimiento BETWEEN '"
                + lc.plusDays(1).toString() + "' and '" + lc.plusDays(7) + "' order by fechavencimiento asc").getResultList();
        listCarta15D = App.jpa.createQuery("select p from Carta p where fechavencimiento BETWEEN '"
                + lc.plusDays(1).toString() + "' and '" + lc.plusDays(15) + "' order by fechavencimiento asc").getResultList();
    }

    void cargarDatoaPantalla() {
        lbl1d.setText(listCarta1D.size() + "");
        lbl2d.setText(listCarta2D.size() + "");
        lbl7d.setText(listCarta7D.size() + "");
        lbl15d.setText(listCarta15D.size() + "");
    }

    @FXML
    void seleccionarRango() {
        listCarta.clear();
        if (jcbtiempo.getSelectionModel().getSelectedItem().equals("MAÑANA")) {
            for (Carta carta : listCarta1D) {
                listCarta.add(carta);
            }
        } else if (jcbtiempo.getSelectionModel().getSelectedItem().equals("2 DÍAS A MENOS")) {
            for (Carta carta : listCarta2D) {
                listCarta.add(carta);
            }
        } else if (jcbtiempo.getSelectionModel().getSelectedItem().equals("1 SEMANA A MENOS")) {
            for (Carta carta : listCarta7D) {
                listCarta.add(carta);
            }
        } else {
            for (Carta carta : listCarta15D) {
                listCarta.add(carta);
            }
        }
    }

}
