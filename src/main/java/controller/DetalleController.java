/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Carta;
import Entidades.Proveedor;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class DetalleController implements Initializable {

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
    private TableColumn<Carta, String> columNumCarta;

    @FXML
    private TableColumn<Carta, LocalDate> columnFecha;

    @FXML
    private TableColumn<Carta, String> columnReferencia;

    @FXML
    private TableColumn<Carta, String> columnObra;

    @FXML
    private TableColumn<Carta, String> columnImporte;

    @FXML
    private TableColumn<Carta, Integer> columnprueba;

    @FXML
    private TableColumn<Carta, String> columnEstado;

    @FXML
    private JFXTextField jtfbuscar;

    @FXML
    private JFXTextField jtfproveedor;

    @FXML
    private JFXTextField jtfnumCarta;

    @FXML
    private JFXTextField jtfdia;

    @FXML
    private JFXTextField jtfmes;

    @FXML
    private JFXTextField jtfanio;

    @FXML
    private JFXTextField jtfreferencia;

    @FXML
    private JFXTextField jtfobra;

    @FXML
    private JFXTextField jtfimporte;

    @FXML
    private JFXComboBox<String> jcbestado;

    @FXML
    private ImageView imgadd, imglimpiar;

    Stage stagePrincipal;
    CartaController cartaController;
    ObservableList<Carta> listCarta = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    Proveedor oProveedor;
    AlertController oAlert = new AlertController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        updateListaComprobante();
        initTableView();
        tableCarta.setItems(listCarta);
        jtfdia.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        jtfmes.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        jtfanio.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        ObservableList<String> ESTADO = FXCollections.observableArrayList("VIGENTE", "VENCIDO", "POR DEVOLVER", "DEVUELTO", "SUSTITUIDA POR RI");
        jcbestado.setItems(ESTADO);
        jcbestado.getSelectionModel().select("VIGENTE");
    }

    public void setController(CartaController cpc) {
        this.cartaController = cpc;

    }

    @FXML
    void updateListaComprobante() {
        List<Carta> olistCarta = App.jpa.createQuery("select p from Carta p where numCartaConfianza like '%" + jtfbuscar.getText().trim() + "%'"
                + "order by id DESC").setMaxResults(10).getResultList();
        listCarta.clear();
        for (Carta ocarta : olistCarta) {
            listCarta.add(ocarta);
        }
    }

    @FXML
    void getItem() {

        int index = selectItem();
        if (index != -1) {
            oProveedor = listCarta.get(index).getProveedor();
            jtfproveedor.setText(listCarta.get(index).getProveedor().getNombreProveedor());
            jtfnumCarta.setText(listCarta.get(index).getNumCartaConfianza());
            jtfdia.setText(listCarta.get(index).getFechaVencimiento().getDayOfMonth() + "");
            jtfmes.setText(listCarta.get(index).getFechaVencimiento().getMonthValue() + "");
            jtfanio.setText((listCarta.get(index).getFechaVencimiento().getYear() - 2000) + "");
            jtfreferencia.setText(listCarta.get(index).getReferencia());
            jtfobra.setText(listCarta.get(index).getObra());
            jtfimporte.setText(listCarta.get(index).getImporte());
            //jcbestado.getSelectionModel().select(listCarta.get(index).getEstado());

        } else {
            oProveedor = null;
            jtfproveedor.setText("");
            jtfnumCarta.setText("");
            jtfdia.setText("");
            jtfmes.setText("");
            jtfanio.setText("");
            jtfreferencia.setText("");
            jtfobra.setText("");
            jtfimporte.setText("");
        }
    }

    @FXML
    void agregar() throws IOException {
        if (isCompleto()) {
            Carta oCarta = new Carta();
            oCarta.setProveedor(oProveedor);
            oCarta.setNumCartaConfianza(jtfnumCarta.getText().trim());
            oCarta.setFechaVencimiento(LocalDate.of(Integer.parseInt(jtfanio.getText().trim()) + 2000,
                    Integer.parseInt(jtfmes.getText().trim()), Integer.parseInt(jtfdia.getText().trim())));
            oCarta.setReferencia(jtfreferencia.getText().trim());
            oCarta.setObra(jtfobra.getText().trim());
            oCarta.setImporte(jtfimporte.getText().trim());
            oCarta.setEstado(jcbestado.getSelectionModel().getSelectedItem());
            App.jpa.getTransaction().begin();
            App.jpa.persist(oCarta);
            App.jpa.getTransaction().commit();
            updateListaComprobante();
            getItem();
        }
    }

    @FXML
    void modificar() throws IOException {
        int index = selectItem();
        if (index != -1 && isCompleto()) {

            Carta oCarta = listCarta.get(index);
            oCarta.setProveedor(oProveedor);
            oCarta.setNumCartaConfianza(jtfnumCarta.getText().trim());
            oCarta.setFechaVencimiento(LocalDate.of(Integer.parseInt(jtfanio.getText().trim()) + 2000,
                    Integer.parseInt(jtfmes.getText().trim()), Integer.parseInt(jtfdia.getText().trim())));
            oCarta.setReferencia(jtfreferencia.getText().trim());
            oCarta.setObra(jtfobra.getText().trim());
            oCarta.setImporte(jtfimporte.getText().trim());
            oCarta.setEstado(jcbestado.getSelectionModel().getSelectedItem());
            App.jpa.getTransaction().begin();
            App.jpa.persist(oCarta);
            App.jpa.getTransaction().commit();
            listCarta.set(index, oCarta);
            updateListaComprobante();
            getItem();
        }
    }

    @FXML
    void eliminar() {
        int index = selectItem();
        if (index != -1) {
            Carta oCarta = listCarta.get(index);
            App.jpa.getTransaction().begin();
            App.jpa.remove(oCarta);
            App.jpa.getTransaction().commit();
            listCarta.remove(index);
            updateListaComprobante();
            //getitem para limpiar
            getItem();
        }
    }

    @FXML
    void mostrarAgregarProveedor() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgregarProveedorController.class.getResource("AgregarProveedor.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(AgregarProveedorController.class.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(stagePrincipal);
        stage.setScene(scene);
        AgregarProveedorController oVerController = (AgregarProveedorController) loader.getController(); //esto depende de (1)
        oVerController.setController(this);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = event.getX();
                y = event.getY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            }
        });

        stage.show();
        //((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }

    @FXML
    void limpiar() {
        jtfnumCarta.setText("");
        jtfdia.setText("");
        jtfmes.setText("");
        jtfanio.setText("");
        jtfreferencia.setText("");
        jtfobra.setText("");
        jtfimporte.setText("");
    }

    void initTableView() {
        columnprueba.setCellValueFactory(new PropertyValueFactory<Carta, Integer>("id"));
        columnProveedor.setCellValueFactory(new PropertyValueFactory<Carta, Proveedor>("proveedor"));
        columNumCarta.setCellValueFactory(new PropertyValueFactory<Carta, String>("numCartaConfianza"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<Carta, LocalDate>("fechaVencimiento"));
        columnReferencia.setCellValueFactory(new PropertyValueFactory<Carta, String>("referencia"));
        columnObra.setCellValueFactory(new PropertyValueFactory<Carta, String>("obra"));
        columnImporte.setCellValueFactory(new PropertyValueFactory<Carta, String>("importe"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<Carta, String>("estado"));

        columnObra.setCellFactory(column -> {
            TableCell<Carta, String> cell = new TableCell<Carta, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        if (item.length() > 100) {

                        }
                        Label ola = new Label();
                        ola.setText("Sistem \n as de información \n ramani \n sincero");
                        ola.setStyle("-fx-font-size: 8");
                        setGraphic(ola);
                        setText(null);
                    }
                }
            };

            return cell;
        });
        columnEstado.setCellFactory(column -> {
            TableCell<Carta, String> cell = new TableCell<Carta, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        String stilo = "-fx-alignment:center; -fx-max-width:999;";
                        Label ola = new Label();
                        if (item.equals("VENCIDO")) {
                            ola.setStyle(stilo + " -fx-text-fill: RED;");
                        } else {
                            ola.setStyle(stilo + " -fx-text-fill: PURPLE");
                        }
                        ola.setText(item);
                        setGraphic(ola);
                        setText(null);
                    }
                }
            };

            return cell;
        });

        Callback<TableColumn<Carta, Integer>, TableCell<Carta, Integer>> cellFoctory = (TableColumn<Carta, Integer> param) -> {
            // make cell containing buttons
            final TableCell<Carta, Integer> cell = new TableCell<Carta, Integer>() {
                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows                    
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        JFXButton deleteIcon = new JFXButton();
                        deleteIcon.setUserData(item);
                        deleteIcon.addEventHandler(ActionEvent.ACTION, event -> eliminar(event));
                        deleteIcon.setText("Eliminar");

                        JFXButton editIcon = new JFXButton();
                        editIcon.setUserData(item);
                        editIcon.addEventHandler(ActionEvent.ACTION, event -> modificar(event));
                        editIcon.setText("Modificar");
                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-fx-text-fill: #ffffff;"
                                + "-fx-background-color: RED"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-fx-text-fill: #ffffff;"
                                + "-fx-background-color: GREEN"
                        );
                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        //  HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                private void modificar(ActionEvent event) {

                    JFXButton buton = (JFXButton) event.getSource();
                    for (Carta carta : listCarta) {
                        if (carta.getId() == (Integer) buton.getUserData()) {

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(AgregarProveedorController.class.getResource("Carta.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(CartaController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            Scene scene = new Scene(root);//instancia el controlador (!)
                            scene.getStylesheets().add(CartaController.class.getResource("/css/bootstrap3.css").toExternalForm());;
                            Stage stage = new Stage();//creando la base vací
                            stage.initStyle(StageStyle.UNDECORATED);
                            stage.initOwner(stagePrincipal);
                            stage.setScene(scene);
                            CartaController oDetalleController = (CartaController) loader.getController(); //esto depende de (1)
                            oDetalleController.setController(DetalleController.this);
                            oDetalleController.setCarta(carta);
                            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    x = event.getX();
                                    y = event.getY();
                                }
                            });
                            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    stage.setX(event.getScreenX() - x);
                                    stage.setY(event.getScreenY() - y);
                                }
                            });

                            stage.show();
                            //((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior

                            break;

                        }
                    }
                }

                private void eliminar(ActionEvent event) {
                    JFXButton buton = (JFXButton) event.getSource();
                    for (int i = 0; i < listCarta.size(); i++) {
                        if (listCarta.get(i).getId() == (Integer) buton.getUserData()) {

                            Carta carta = listCarta.get(i);
                            //si lo que eliminan es igual a lo que está seleccionado: eliminar
                            if (listCarta.get(selectItem()) == carta) {
                                limpiar();
                            }
                            App.jpa.getTransaction().begin();
                            App.jpa.refresh(carta);//recuperando enlace ORM
                            App.jpa.remove(carta);
                            App.jpa.getTransaction().commit();
                            listCarta.remove(i);

                            updateListaComprobante();
                            //getitem para limpiar
                            getItem();
                            break;
                        }
                    }
                }
            };
            return cell;
        };
        columnprueba.setCellFactory(cellFoctory);
    }

    int selectItem() {
        return listCarta.indexOf(tableCarta.getSelectionModel().getSelectedItem());
    }

    void setProveedor(Proveedor get) {
        this.oProveedor = get;
        jtfproveedor.setText(oProveedor.getNombreProveedor());
    }

    private void SoloNumerosEnteros(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 2) {
            event.consume();
        }
    }

    boolean isCompleto() throws IOException {
        boolean aux = true;
        if (oProveedor == null) {
            jtfproveedor.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfproveedor.setStyle("");
        }
        if (jtfnumCarta.getText().trim().length() == 0) {
            jtfnumCarta.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfnumCarta.setStyle("");
        }

        if (jtfreferencia.getText().trim().length() == 0) {
            jtfreferencia.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfreferencia.setStyle("");
        }

        if (jtfobra.getText().trim().length() == 0) {
            jtfobra.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfobra.setStyle("");
        }

        if (jtfimporte.getText().trim().length() == 0) {
            jtfimporte.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfimporte.setStyle("");
        }
        boolean auxfecha = true;
        if (jtfdia.getText().trim().length() == 0) {
            jtfdia.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfdia.setStyle("");
        }

        if (jtfmes.getText().trim().length() == 0) {
            jtfmes.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfmes.setStyle("");
        }

        if (jtfanio.getText().trim().length() == 0) {
            jtfanio.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtfanio.setStyle("");
        }

        if (!aux || !auxfecha) {
            oAlert.Mostrar("error", "Llene los cuadros en rojo");
        }
        aux = isfechavalid(auxfecha);
        return aux;
    }

    boolean isfechavalid(boolean aux) throws IOException {
        try {
            if (aux) {
                LocalDate.of(Integer.parseInt(jtfanio.getText().trim()), Integer.parseInt(jtfmes.getText().trim()), Integer.parseInt(jtfdia.getText().trim()));
            }
        } catch (Exception e) {
            aux = false;
            oAlert.Mostrar("warning", "ingrese una fecha válida");
        }
        return aux;
    }

    void setStagePrincipall(Stage aThis) {
        this.stagePrincipal = aThis;
    }

    @FXML
    void imagAddDentro() {
        imgadd.setImage(new Image(getClass().getResource("/images/add-2.png").toExternalForm()));
    }

    @FXML
    void imagAddFuera() {
        imgadd.setImage(new Image(getClass().getResource("/images/add-1.png").toExternalForm()));
    }

    @FXML
    void imaglimpiarDentro() {
        imglimpiar.setImage(new Image(getClass().getResource("/images/limpiar-2.png").toExternalForm()));
    }

    @FXML
    void imaglimpiarFuera() {
        imglimpiar.setImage(new Image(getClass().getResource("/images/limpiar-1.png").toExternalForm()));
    }

    @FXML
    void test() {
        System.out.println("Proveedor: " + columnProveedor.getWidth() + " carta f:" + columNumCarta.getWidth() + " fecha: " + columnFecha.getWidth());
        System.out.println("Prueba: " + columnprueba.getWidth() + " estado f:" + columnEstado.getWidth() + " importe: " + columnImporte.getWidth());
    }

}
