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
import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private ImageView imgadd, imglimpiar, imagporexpirar, imageditar, imgvencido;
    
    @FXML
    private Label lblnumVencido;
    
    Stage stagePrincipal;
    CartaController cartaController;
    ObservableList<Carta> listCarta = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    Proveedor oProveedor;
    AlertController oAlert = new AlertController();
    private List<Carta> listCartaVencidaNoVista;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        actualizarPorVencer();
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
            actualizarPorVencer();
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
    void mostrarAviso() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AvisoController.class.getResource("Aviso.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(EstadoController.class.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(stagePrincipal);
        stage.setScene(scene);
        AvisoController oAvisocontroller = (AvisoController) loader.getController(); //esto depende de (1)
        oAvisocontroller.setController(this);
        oAvisocontroller.sendListVencido(listCartaVencidaNoVista);
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
    }
    
       @FXML
    void mostrarEstado() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AgregarProveedorController.class.getResource("Estado.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(AgregarProveedorController.class.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(stagePrincipal);
        stage.setScene(scene);
        EstadoController oVerController = (EstadoController) loader.getController(); //esto depende de (1)
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
    
    @FXML
    void minimizar() {
        ((Stage) ap.getScene().getWindow()).setIconified(true);
    }

    //traen la lista de cartas vencidas pero que no han sido vistas.
    public void actualizarPorVencer() {
        listCartaVencidaNoVista = new ArrayList<>();
        List<Carta> ListCartaVencida = App.jpa.createQuery(""
                + "select p from Carta p where (estado <> 'VENCIDO' or visto = false) and fechavencimiento  <='" + LocalDate.now() + "' ").getResultList();
        //cambiando todas las cartas a VENCIDO
        for (Carta carta : ListCartaVencida) {
            carta.setEstado("VENCIDO");
            App.jpa.getTransaction().begin();
            App.jpa.persist(carta);
            App.jpa.getTransaction().commit();
        }
        mostrarNoVisto(ListCartaVencida);
    }
    
    void mostrarNoVisto(List<Carta> ListCart) {
        for (Carta carta : ListCart) {
            //filtrando las cartas vencidas pero que no han sido vistas.
            if (!carta.isVisto()) {
                listCartaVencidaNoVista.add(carta);
            }
        }
        if (listCartaVencidaNoVista.isEmpty()) {
            lblnumVencido.setVisible(false);
        } else {
            lblnumVencido.setVisible(true);
        }
        lblnumVencido.setText(listCartaVencidaNoVista.size() + "");    
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
        
        columnReferencia.setCellFactory(column -> {
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
                        ola.setText(item);
                        ola.setStyle("-fx-font-size: 8; -fx-alignment: center; -fx-max-width:999;");
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
                        ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/images/delete-1.png").toExternalForm()));
                        deleteIcon.setFitHeight(35);
                        deleteIcon.setFitWidth(35);
                        deleteIcon.setUserData(item);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand;"
                        );
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> eliminar(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagEliminarMoved(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagEliminarFuera(event));
                        //deleteIcon.setText("Eliminar");

                        ImageView editIcon = new ImageView(new Image(getClass().getResource("/images/modify-1.png").toExternalForm()));
                        editIcon.setFitHeight(35);
                        editIcon.setFitWidth(35);
                        editIcon.setUserData(item);
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                        );
                        editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarModificar(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));
                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(0, 0, 0, 5));
                        HBox.setMargin(editIcon, new Insets(0, 5, 0, 0));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }
                
                private void mostrarModificar(MouseEvent event) {
                    ImageView buton = (ImageView) event.getSource();
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
                
                private void eliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listCarta.size(); i++) {
                        if (listCarta.get(i).getId() == (Integer) imag.getUserData()) {
                            
                            Carta carta = listCarta.get(i);
                            //si lo que eliminan es igual a lo que está seleccionado: eliminar
                            if (selectItem() != -1) {
                                if (listCarta.get(selectItem()) == carta) {
                                    limpiar();
                                }
                            }
                            App.jpa.getTransaction().begin();
                            App.jpa.refresh(carta);//recuperando enlace ORM
                            App.jpa.remove(carta);
                            App.jpa.getTransaction().commit();
                            listCarta.remove(i);
                            actualizarPorVencer();
                            updateListaComprobante();
                            //getitem para limpiar
                            getItem();
                            break;
                        }
                    }
                }
                
                private void imagEliminarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/images/delete-2.png").toExternalForm()));
                }
                
                private void imagEliminarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/images/delete-1.png").toExternalForm()));
                }
                
                private void imagModificarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/images/modify-2.png").toExternalForm()));
                }
                
                private void imagModificarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/images/modify-1.png").toExternalForm()));
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
    void imagPorexpirarDentro() {
        imagporexpirar.setImage(new Image(getClass().getResource("/images/expired-2.png").toExternalForm()));
    }
    
    @FXML
    void imagPorexpirarFuera() {
        imagporexpirar.setImage(new Image(getClass().getResource("/images/expired-1.png").toExternalForm()));
    }
    
    @FXML
    void imagEditarDentro() {
        imageditar.setImage(new Image(getClass().getResource("/images/editar-2.png").toExternalForm()));
    }
    
    @FXML
    void imagEditarFuera() {
        imageditar.setImage(new Image(getClass().getResource("/images/editar-1.png").toExternalForm()));
    }
    
    @FXML
    void imagVencidoDentro() {
        imgvencido.setImage(new Image(getClass().getResource("/images/caution-2.png").toExternalForm()));
    }
    
    @FXML
    void imagVencidoFuera() {
        imgvencido.setImage(new Image(getClass().getResource("/images/caution-1.png").toExternalForm()));
    }
    
    @FXML
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }
    
    @FXML
    void test() {
        System.out.println("Proveedor: " + columnProveedor.getWidth() + " carta f:" + columNumCarta.getWidth() + " fecha: " + columnFecha.getWidth());
        System.out.println("Prueba: " + columnprueba.getWidth() + " estado f:" + columnEstado.getWidth() + " importe: " + columnImporte.getWidth());
    }
    
}
