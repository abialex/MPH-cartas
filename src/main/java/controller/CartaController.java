/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Carta;
import Entidades.Proveedor;
import com.jfoenix.controls.JFXTextField;
import emergente.AlertController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author yalle
 */
public class CartaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField jtfproveedor, jtfdia, jtfmes, jtfanio, jtfnumCarta, jtfreferencia, jtfobra, jtfimporte;
    @FXML
    private AnchorPane ap;
    @FXML
    private Label lblnumVencido;

    private Stage stagePrincipal;
    private Proveedor oProveedor;
    private List<Carta> listCartaVencida;
    private double x = 0;
    private double y = 0;

    AlertController oAlert = new AlertController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jtfdia.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        jtfmes.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        jtfanio.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros(event));
        actualizarPorVencer();
    }

    void setStagePrincipall(Stage aThis) {
        this.stagePrincipal = aThis;
    }

    public void setProveedor(Proveedor o) {
        this.oProveedor = o;
        jtfproveedor.setText(oProveedor.getNombreProveedor());

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
    void mostrarDetalle() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DetalleController.class.getResource("Detalle.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(DetalleController.class.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        // stage.initOwner(stagePrincipal);
        stage.setScene(scene);
        DetalleController oDetalleController = (DetalleController) loader.getController(); //esto depende de (1)
        oDetalleController.setController(this);
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

        cerrar();
        //((Stage) ap.getScene().getWindow()).close();//cerrando la ventanada anterior
    }

    @FXML
    void mostrarEstado() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(EstadoController.class.getResource("Estado.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(EstadoController.class.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        // stage.initOwner(stagePrincipal);
        stage.setScene(scene);
        EstadoController oDetalleController = (EstadoController) loader.getController(); //esto depende de (1)
        oDetalleController.setController(this);
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

        cerrar();
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
        oAvisocontroller.sendListVencido(listCartaVencida);
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
    void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();
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

    @FXML
    void GuardarCarta() throws IOException {
        if (isCompleto()) {
            Carta oCarta = new Carta(
                    oProveedor,
                    jtfnumCarta.getText().trim(),
                    LocalDate.of(Integer.parseInt(jtfanio.getText().trim()) + 2000, Integer.parseInt(jtfmes.getText().trim()), Integer.parseInt(jtfdia.getText().trim())),
                    jtfreferencia.getText().trim(),
                    jtfobra.getText().trim(),
                    jtfimporte.getText().trim(),
                    "VIGENTE");

            App.jpa.getTransaction().begin();
            App.jpa.persist(oCarta);
            App.jpa.getTransaction().commit();
            oAlert.Mostrar("successful", "Guardado");
            limpiar();
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
                LocalDate.of(Integer.parseInt(jtfanio.getText().trim()) + 2000, Integer.parseInt(jtfmes.getText().trim()), Integer.parseInt(jtfdia.getText().trim()));
            }
        } catch (Exception e) {
            aux = false;
            oAlert.Mostrar("warning", "ingrese una fecha válida");

        }

        return aux;
    }

    void limpiar() {
        jtfnumCarta.setText("");
        jtfdia.setText("");
        jtfmes.setText("");
        jtfanio.setText("");
        jtfreferencia.setText("");
        jtfobra.setText("");
        jtfimporte.setText("");
    }

    //funcion importante 
    void actualizarPorVencer() {
        listCartaVencida = App.jpa.createQuery(""
                + "select p from Carta p where estado = 'VIGENTE'  and fechavencimiento  <='" + LocalDate.now() + "'").getResultList();
        lblnumVencido.setText(listCartaVencida.size() + "");
    }
}
