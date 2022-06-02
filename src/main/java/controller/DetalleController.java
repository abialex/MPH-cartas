/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Carta;
import Entidades.Proveedor;
import Pdf.Citapdf;
import Util.FileImagUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertConfirmarController;
import emergente.AlertController;
import java.awt.Button;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javax.swing.JOptionPane;

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
    public AnchorPane ap;
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
    private TableColumn<Carta, String> columnEstado,columnNumCartaDevuelta;

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
    private JFXTextField jtfimporte, jtfNumCartaDevuelta;

    @FXML
    private JFXComboBox<String> jcbestado;

    @FXML
    private ImageView imgadd, imglimpiar, imagporexpirar, imageditar, imgvencido, imgImprimir;

    @FXML
    private Label lblnumVencido, lblPorVencer, lblpdf;

    @FXML
    private JFXComboBox<String> jcbMesBuscar, jcbAnioBuscar;

    @FXML
    private JFXComboBox<Proveedor> jcbProveedorBuscar;

    @FXML
    private JFXComboBox<String> jcbEstadoBuscar;

    @FXML
    private JFXTextField jtfObraBuscar, jtfNcartaBuscar;

    Stage stagePrincipal;
    CartaController cartaController;
    ObservableList<Carta> listCarta = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    Proveedor oProveedor;
    AlertController oAlert = new AlertController();
    private List<Carta> listCartaVencidaNoVista;
    AlertConfirmarController oAlertConfimarController1 = new AlertConfirmarController();
    DetalleController odc = this;
    FileImagUtil oFileImagUtil = new FileImagUtil("user.home", "buscar pdf .pdf");
    File oPdf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initRestricciones();
        actualizarPorVencer();
        updateListaComprobante();
        initTableView();
        tableCarta.setItems(listCarta);
        jtfdia.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        jtfmes.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        jtfanio.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        ObservableList<String> ESTADO = FXCollections.observableArrayList("VIGENTE", "VENCIDO", "POR DEVOLVER", "DEVUELTO", "RENOVADO");
        jcbestado.setItems(ESTADO);
        jcbestado.getSelectionModel().select("VIGENTE");

        //actualizar
        cargarProveedores();
        cargarMesAnio();
        cargarEstado();
    }

    public void setController(CartaController cpc) {
        this.cartaController = cpc;

    }

    @FXML
    void updateListaComprobante() {
        List<Carta> olistCarta = App.jpa.createQuery("select p from Carta p order by id DESC").getResultList();
        listCarta.clear();
        for (Carta ocarta : olistCarta) {
            listCarta.add(ocarta);
        }
    }
    String filtro = "";

    @FXML
    public void buscar() {
        String oproveedor = jcbProveedorBuscar.getSelectionModel().getSelectedItem().getNombreProveedor().equals("ninguno") ? " id>0 " : " idproveedor = " + jcbProveedorBuscar.getSelectionModel().getSelectedItem().getId();
        String fechaMes = jcbMesBuscar.getSelectionModel().getSelectedItem() == "ninguno" ? "" : " AND MONTH(fechavencimiento) = " + jcbMesBuscar.getSelectionModel().getSelectedItem();
        String fechaAnio = jcbAnioBuscar.getSelectionModel().getSelectedItem() == "ninguno" ? "" : " AND YEAR(fechavencimiento) =" + jcbAnioBuscar.getSelectionModel().getSelectedItem();
        String estado = jcbEstadoBuscar.getSelectionModel().getSelectedItem() == "ninguno" ? "" : " AND estado= '" + jcbEstadoBuscar.getSelectionModel().getSelectedItem() + "'";
        String obra = jtfObraBuscar.getText().trim().length() == 0 ? "" : "AND obra like '" + jtfObraBuscar.getText() + "%'";
        String cartaconfianza = jtfNcartaBuscar.getText().trim().length() == 0 ? "" : "AND numcartaconfianza like '" + jtfNcartaBuscar.getText() + "%'";
        List<Carta> olistCarta = App.jpa.createQuery("select p from Carta p where"
                + oproveedor
                + fechaMes + fechaAnio
                + cartaconfianza
                + estado
                + obra
                + " order by id DESC").getResultList();
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
            jtfimporte.setText(listCarta.get(index).getImporteInt() + "");
            jtfNumCartaDevuelta.setText(listCarta.get(index).getNumCartaDevuelta() == null ? "" : listCarta.get(index).getNumCartaDevuelta() + "");
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
            Locale locale = new Locale("en", "UK");
            NumberFormat objNF = NumberFormat.getInstance(locale);
            String cadena = objNF.format(999999.99);
            Carta oCarta = new Carta();
            oCarta.setProveedor(oProveedor);
            oCarta.setNumCartaConfianza(jtfnumCarta.getText().trim());
            oCarta.setFechaVencimiento(LocalDate.of(Integer.parseInt(jtfanio.getText().trim()) + 2000,
                    Integer.parseInt(jtfmes.getText().trim()), Integer.parseInt(jtfdia.getText().trim())));
            oCarta.setReferencia(jtfreferencia.getText().trim());
            oCarta.setObra(jtfobra.getText().trim());
            oCarta.setImporte(objNF.format(Integer.parseInt(jtfimporte.getText().trim())) + ".00");
            oCarta.setImporteInt(Integer.parseInt(jtfimporte.getText().trim()));
            oCarta.setEstado(jcbestado.getSelectionModel().getSelectedItem());
            oCarta.setNumCartaDevuelta(jtfNumCartaDevuelta.getText());
            oCarta.setUrl(oPdf == null ? "" : oFileImagUtil.guardarPdf(oPdf));
            oCarta.setNameArchivo(oPdf == null ? "" : oPdf.getName());
            App.jpa.getTransaction().begin();
            App.jpa.persist(oCarta);
            App.jpa.getTransaction().commit();
            updateListaComprobante();
            actualizarPorVencer();
            limpiar();
        }
    }

    void initRestricciones() {
        jtfimporte.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEntero(event));

    }

    void SoloNumerosEntero(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
    }

    @FXML
    void imprimir() {
        File file = new File(Citapdf.ImprimirCartas(listCarta, "s"));
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(DetalleController.class.getName()).log(Level.SEVERE, null, ex);
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

    public void eliminar(Carta oCarta, int index) {
        if (index != -1) {
            App.jpa.getTransaction().begin();
            App.jpa.remove(oCarta);
            App.jpa.getTransaction().commit();
            listCarta.remove(index);
            updateListaComprobante();
            actualizarPorVencer();
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
    }

    @FXML
    void mostrarAviso() throws IOException {
        AvisoController oAvisocontroller = (AvisoController) mostrarVentana(AvisoController.class, "Aviso");
        oAvisocontroller.setController(this);
        oAvisocontroller.sendListVencido(listCartaVencidaNoVista);
    }

    @FXML
    void mostrarEstado() throws IOException {
        EstadoController oVerController = (EstadoController) mostrarVentana(EstadoController.class, "Estado");
        oVerController.setController(this);
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
        jtfNumCartaDevuelta.setText("");
        lblpdf.setText("pdf");
        oPdf = null;
    }

    @FXML
    void minimizar() {
        ((Stage) ap.getScene().getWindow()).setIconified(true);
    }

    //traen la lista de cartas vencidas pero que no han sido vistas.
    public void actualizarPorVencer() {
        listCartaVencidaNoVista = new ArrayList<>();
        List<Carta> ListCartaVencida = App.jpa.createQuery(""
                + "select p from Carta p where (estado <> 'VENCIDO' or estado <> 'DEVUELTO' or visto = false) and fechavencimiento  <='" + LocalDate.now() + "' ").getResultList();
        //cambiando todas las cartas a VENCIDO
        for (Carta carta : ListCartaVencida) {
            carta.setEstado("VENCIDO");
            App.jpa.getTransaction().begin();
            App.jpa.persist(carta);
            App.jpa.getTransaction().commit();
        }
        mostrarNoVisto(ListCartaVencida);
        mostrarporVencer3Days();
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

    void mostrarporVencer3Days() {
        LocalDate lc = LocalDate.now();
        List<Carta> listCarta3D = App.jpa.createQuery("select p from Carta p where fechavencimiento BETWEEN '"
                + lc.plusDays(1).toString() + "' and '" + lc.plusDays(2).toString() + "' order by fechavencimiento asc").getResultList();

        if (listCarta3D.isEmpty()) {
            lblPorVencer.setVisible(false);
        } else {
            lblPorVencer.setVisible(true);
        }
        lblPorVencer.setText(listCarta3D.size() + "");

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
        columnNumCartaDevuelta.setCellValueFactory(new PropertyValueFactory<Carta, String>("numCartaDevuelta"));

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
                        int tam = 20;

                        ImageView deleteIcon = new ImageView(new Image(getClass().getResource("/images/delete-1.png").toExternalForm()));
                        deleteIcon.setFitHeight(tam);
                        deleteIcon.setFitWidth(tam);
                        deleteIcon.setUserData(item);
                        deleteIcon.setStyle(
                                " -fx-cursor: hand;"
                        );
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> eliminar(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagEliminarMoved(event));
                        deleteIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagEliminarFuera(event));
                        //deleteIcon.setText("Eliminar");

                        ImageView editIcon = new ImageView(new Image(getClass().getResource("/images/modify-1.png").toExternalForm()));
                        editIcon.setFitHeight(tam);
                        editIcon.setFitWidth(tam);
                        editIcon.setUserData(item);
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                        );
                        editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarModificar(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                        ImageView imprimirIcon = new ImageView(new Image(getClass().getResource("/images/pdf.png").toExternalForm()));
                        imprimirIcon.setFitHeight(tam);
                        imprimirIcon.setFitWidth(tam);
                        imprimirIcon.setUserData(item);
                        imprimirIcon.setStyle(
                                " -fx-cursor: hand ;"
                        );
                        imprimirIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> imprimir(event));
                        HBox managebtn = null;
                        for (Carta carta : listCarta) {
                            if (carta.getId() == item) {
                                if (carta.getUrl().isEmpty()) {
                                    managebtn = new HBox(editIcon, deleteIcon);
                                    HBox.setMargin(deleteIcon, new Insets(0, 0, 0, 5));
                                    HBox.setMargin(editIcon, new Insets(0, 5, 0, 27));
                                } else {
                                    managebtn = new HBox(imprimirIcon, editIcon, deleteIcon);
                                    HBox.setMargin(imprimirIcon, new Insets(0, 5, 0, 0));
                                    HBox.setMargin(deleteIcon, new Insets(0, 0, 0, 5));
                                    HBox.setMargin(editIcon, new Insets(0, 5, 0, 0));
                                }
                            }
                        }
                        managebtn.setStyle("-fx-alignment:center");
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                private void mostrarModificar(MouseEvent event) {
                    ap.setDisable(true);
                    ImageView buton = (ImageView) event.getSource();
                    for (Carta carta : listCarta) {
                        if (carta.getId() == (Integer) buton.getUserData()) {
                            CartaController oDetalleController = (CartaController) mostrarVentana(CartaController.class, "Carta");
                            oDetalleController.setController(odc);
                            oDetalleController.setCarta(carta);
                            break;
                        }
                    }
                }

                void mostrar(Carta oCarta, int index) throws IOException {
                    oAlertConfimarController1 = (AlertConfirmarController) mostrarVentana(AlertConfirmarController.class, "/fxml/AlertConfirmar");
                    oAlertConfimarController1.setController(odc);
                    oAlertConfimarController1.setMensaje(" ¿Está seguro de eliminar?");
                    oAlertConfimarController1.setCartaIndex(oCarta, index);
                }

                void imprimir(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listCarta.size(); i++) {
                        if (listCarta.get(i).getId() == (Integer) imag.getUserData()) {
                            Carta carta = listCarta.get(i);
                            File file = new File(carta.getUrl());
                            try {
                                Desktop.getDesktop().open(file);
                            } catch (IOException ex) {
                                Logger.getLogger(DetalleController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (selectItem() != -1) {
                                if (listCarta.get(selectItem()) == carta) {
                                    limpiar();
                                }
                            }
                            break;
                        }
                    }
                }

                void eliminar(MouseEvent event) {
                    ap.setDisable(true);
                    ImageView imag = (ImageView) event.getSource();
                    for (int i = 0; i < listCarta.size(); i++) {
                        if (listCarta.get(i).getId() == (Integer) imag.getUserData()) {
                            Carta carta = listCarta.get(i);
                            try {
                                mostrar(carta, i);
                            } catch (IOException ex) {
                                Logger.getLogger(DetalleController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            //si lo que eliminan es igual a lo que está seleccionado: eliminar
                            if (selectItem() != -1) {
                                if (listCarta.get(selectItem()) == carta) {
                                    limpiar();
                                }
                            }
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

    void cargarProveedores() {
        List<Proveedor> listProveedor = App.jpa.createQuery("select p from Proveedor p order by nombreproveedor ASC").getResultList();
        ObservableList<Proveedor> listProveedorO = FXCollections.observableArrayList();
        Proveedor op = new Proveedor("ninguno");
        listProveedorO.add(op);
        for (Proveedor proveedor : listProveedor) {
            listProveedorO.add(proveedor);
        }
        jcbProveedorBuscar.setItems(listProveedorO);
        jcbProveedorBuscar.getSelectionModel().select(op);

    }

    void cargarMesAnio() {
        ObservableList<String> MES = FXCollections.observableArrayList("ninguno", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        ObservableList<String> ANIO = FXCollections.observableArrayList("ninguno", "2022", "2023", "2024", "2025");
        jcbMesBuscar.setItems(MES);
        jcbAnioBuscar.setItems(ANIO);
        LocalDate lc = LocalDate.now();
        jcbMesBuscar.getSelectionModel().select("ninguno");
        jcbAnioBuscar.getSelectionModel().select("ninguno");

    }

    void cargarEstado() {
        ObservableList<String> ESTADO = FXCollections.observableArrayList("ninguno", "VIGENTE", "VENCIDO", "POR DEVOLVER", "DEVUELTO", "SUSTITUIDA POR RI");
        jcbEstadoBuscar.setItems(ESTADO);
        jcbEstadoBuscar.getSelectionModel().select("ninguno");

    }

    int selectItem() {
        return listCarta.indexOf(tableCarta.getSelectionModel().getSelectedItem());
    }

    void setProveedor(Proveedor get) {
        this.oProveedor = get;
        jtfproveedor.setText(oProveedor.getNombreProveedor());
        //actualizando la lista de proveedores
        cargarProveedores();
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
        boolean auxfecha2 = isfechavalid(auxfecha);
        if (!aux) {
            oAlert.Mostrar("error", "Llene los cuadros en rojo");
        }

        return aux && auxfecha && auxfecha2;
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
    void seleccionarPdf() throws IOException {
        oPdf = oFileImagUtil.buscarPdf();
        lblpdf.setText(oPdf.getName());
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
    void imagImprimirDentro() {
        imgImprimir.setImage(new Image(getClass().getResource("/images/upload-2.png").toExternalForm()));
    }

    @FXML
    void imagImprimirFuera() {
        imgImprimir.setImage(new Image(getClass().getResource("/images/upload-1.png").toExternalForm()));
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

    public Object mostrarVentana(Class generico, String nameFXML) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(generico.getResource(nameFXML + ".fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(generico.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(generico.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(stagePrincipal);
        stage.setScene(scene);
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
        return loader.getController();
    }

}
