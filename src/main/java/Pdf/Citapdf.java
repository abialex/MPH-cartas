/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pdf;

import Entidades.Carta;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import Pdf.style.style2;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.layout.border.SolidBorder;
import controller.App;
import java.awt.Label;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.VerticalAlignment;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author yalle
 */
public class Citapdf {

    public static String ImprimirCartas(List<Carta> listaCartas, String filtro) {
        filtro = filtro == null ? "nada" : filtro;
        LocalDate lc = LocalDate.now();
        int volumen = 180;
        PdfWriter writer = null;
        String urlWrite = "Reporte Cartas\\carta_" + filtro + "_" + lc.toString() + ".pdf";
        try {
            writer = new PdfWriter(urlWrite);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(new Label(), "agregue la carpeta Pdf");
        }
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4.rotate());
        document.setMargins(10, 10, 60, 10);

        style2 evento = new style2(document);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, evento);
        PdfFont bold = null, font = null;
        try {
            /*--------styles-------------*/
            font = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException ex) {
            Logger.getLogger(Citapdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*------formato----------*/
        Locale locale = new Locale("en", "UK");
        NumberFormat objNF = NumberFormat.getInstance(locale);

        /*---------------------Color----------*/
        Color prueba = new DeviceRgb(0, 204, 204);
        Color colorAzul = new DeviceRgb(255, 178, 102);
        Color colorSubtitulo = Color.BLACK;
        Color colorNegro = new DeviceRgb(0, 204, 204);
        Color colorRojo = Color.RED;
        Color colorBlanco = Color.WHITE;

        /*----------------Style letras---------*/
        Style styleCell = new Style().setBorder(Border.NO_BORDER);
        Style styleTextRight = new Style().setTextAlignment(TextAlignment.RIGHT).setFontSize(10f);
        Style styleTextLeft = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(10f);
        Style styleTextCenter = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(8f);
        Style styleTextLeft7 = new Style().setTextAlignment(TextAlignment.LEFT).setFontSize(7.5f);
        Style styleTextCenterRojo = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(13f).setFontColor(colorRojo).setFont(bold);
        Style styleTextCenterVertical = new Style().setVerticalAlignment(VerticalAlignment.MIDDLE);
        Style styleTextCenter8 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(8f).setFont(bold);
        Style styleTextCenter13 = new Style().setTextAlignment(TextAlignment.CENTER).setFontSize(13f);
        /*----------------Border--------------*/
        Border subrayado = new SolidBorder(0.5f);
        Border subrayadoNo = Border.NO_BORDER;

        /*-----------------Palabras default-----------*/
        String palabra1 = "desc.";
        String palabra2 = "no presenta";

        /*----------------Palabras vacías-------------*/
        Paragraph palabraEnBlancoLimpio = new Paragraph(".").setFontColor(colorBlanco);
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(0.5f));
        /*---------FIN----Palabras vacías-------------*/

 /* Contenido del documento  página 1*/
        //table raya
        //Cabecera
        Table CabeceraParrafo1 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo1.addCell(getCell(":", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo1.addCell(getCell("" + "", styleTextCenter, styleCell, subrayadoNo));

        Table CabeceraParrafo2 = new Table(new float[]{volumen * 0.5f, volumen * 0.7f});
        CabeceraParrafo2.addCell(getCell(":", styleTextRight, styleCell, subrayadoNo));
        CabeceraParrafo2.addCell(getCell("" + "", styleTextCenter, styleCell, subrayadoNo));

        Image imgUp = null;
        try {
            imgUp = new Image(ImageDataFactory.create("images\\logoUp.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Citapdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        Cell cellimagUp = new Cell().add(imgUp.setAutoScale(true)).setBorder(Border.NO_BORDER);
        /* new SolidBorder(Color.BLACK,1*/

        Table TableHC = new Table(new float[]{volumen * 5});
        TableHC.addCell(new Cell().add(CabeceraParrafo1).addStyle(styleCell));
        TableHC.addCell(new Cell().add(CabeceraParrafo2).addStyle(styleCell));

        Table Cabecera = new Table(new float[]{volumen * 1.786f, volumen * 1f, volumen * 1.786f});
        Cabecera.addCell(getCell("CARTAS", styleTextCenter, styleCell, subrayadoNo).addStyle(styleTextCenterVertical));
        Cabecera.addCell(new Cell().add(cellimagUp.setPaddingTop(-5)).addStyle(styleCell));
        Cabecera.addCell(new Cell().add(TableHC).addStyle(styleCell));
        Cabecera.setMarginBottom(2.5f);

        //Fin Cabecera
        // Tabla Cartas
        long importeTotal = 0;
        Table TableCartas = new Table(new float[]{volumen * 0.55f, volumen * 0.5f, volumen * 0.3f, volumen * 0.5f, volumen * 1.3f, volumen * 0.37f, volumen * 0.5f, volumen * 0.55f});
        TableCartas.addCell(getCell("Proveedor", styleTextCenter, styleTextCenter, subrayadoNo));
        TableCartas.addCell(getCell("N° CARTA F.", styleTextCenter, styleTextCenter, subrayadoNo));
        TableCartas.addCell(getCell("Fecha.", styleTextCenter, styleTextCenter, subrayadoNo));
        TableCartas.addCell(getCell("Referencia.", styleTextCenter, styleTextCenter, subrayadoNo));
        TableCartas.addCell(getCell("Obra.", styleTextCenter, styleTextCenter, subrayadoNo));
        TableCartas.addCell(getCell("Importe.", styleTextCenter, styleTextCenter, subrayadoNo));
        TableCartas.addCell(getCell("Estado.", styleTextCenter, styleTextCenter, subrayadoNo));
        TableCartas.addCell(getCell("N° CARTA DEV.", styleTextCenter, styleTextCenter, subrayadoNo));
        for (Carta ocarta : listaCartas) {
            TableCartas.addCell(getCell(ocarta.getProveedor().getNombreProveedor(), styleTextCenter, styleTextCenter, subrayadoNo));
            TableCartas.addCell(getCell(ocarta.getNumCartaConfianza(), styleTextCenter, styleTextCenter, subrayadoNo));
            TableCartas.addCell(getCell(ocarta.getFechaVencimiento() + "", styleTextCenter, styleTextCenter, subrayadoNo));
            TableCartas.addCell(getCell(ocarta.getReferencia(), styleTextCenter, styleTextCenter, subrayadoNo));
            TableCartas.addCell(getCell(ocarta.getObra(), styleTextCenter, styleTextCenter, subrayadoNo));
            TableCartas.addCell(getCell(ocarta.getImporte(), styleTextCenter, styleTextCenter, subrayadoNo));
            TableCartas.addCell(getCell(ocarta.getEstado(), styleTextCenter, styleTextCenter, subrayadoNo));
            TableCartas.addCell(getCell(ocarta.getNumCartaDevuelta() == null ? "" : ocarta.getNumCartaDevuelta(), styleTextCenter, styleTextCenter, subrayadoNo));
            importeTotal = importeTotal + ocarta.getImporteInt();
        }
        TableCartas.addCell(new Cell(1, 5).add(getCell("IMPORTE TOTAL", styleCell, styleTextCenter, subrayadoNo)));
        TableCartas.addCell(new Cell(1, 3).add(getCell(objNF.format(importeTotal)+".00", styleTextCenter, styleTextCenter, subrayadoNo)));
        /* Cuerpo del documentos*/
        document.add(Cabecera);
        document.add(TableCartas);

        document.close();
        /*----Fin Cuerpo del documentos-----*/

        return urlWrite;
    }
    //doctor id,fechainicio, fecha fin, order by minuto

    static Table getTable(String cadena, int volumen, Paragraph palabraEnBlanco, Style styleCell, Style styleTextLeft) {
        Table TablePrincipal = new Table(new float[]{volumen * 5});
        Table TableParrafo = new Table(new float[]{volumen * 5});
        Paragraph oParagrah;
        int numCharacteres = 108;
        int iteracion = cadena.length() / numCharacteres;
        for (int i = 0; i < iteracion; i++) {
            oParagrah = new Paragraph(cadena.substring(i * numCharacteres, (i + 1) * numCharacteres));
            Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableParrafo.addCell(cellParrafo1);
        }
        oParagrah = cadena.isEmpty() ? palabraEnBlanco : new Paragraph(cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length()));;
        Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableParrafo.addCell(cellParrafo1);
        for (int i = 0; i < 2 - iteracion; i++) {
            Cell cellraya = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
            TableParrafo.addCell(cellraya);
        }
        TablePrincipal.addCell(new Cell().add(TableParrafo).addStyle(styleCell));
        return TablePrincipal;
    }

    public static Table getTableField(String cadena, String titulo, float tamfield, float tamfield2, int volumen, Color colorNegro, Style styleCell, Style styleTextLeft, Paragraph palabraEnBlanco) {
        Table TableAtributo = new Table(new float[]{volumen * tamfield, volumen * tamfield2});
        Table TableParrafo = new Table(new float[]{volumen * 5});
        Paragraph oParagrah;
        int subnumCharacteres = 75;
        int numCharacteres = 100;
        int iteracion = cadena.length() / numCharacteres;
        int division = cadena.length() / subnumCharacteres;
        boolean aux = division == 0;
        Cell cellDi = new Cell().add(new Paragraph(titulo).setFontColor(colorNegro)).addStyle(styleCell).addStyle(styleTextLeft);
        TableAtributo.addCell(cellDi);
        Paragraph oParagra = cadena.length() == 0 ? palabraEnBlanco : new Paragraph(aux ? cadena : cadena.substring(0 * numCharacteres, (0 + 1) * subnumCharacteres));
        Cell cellParraf = new Cell().add(oParagra.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableAtributo.addCell(cellParraf);
        TableParrafo.addCell(new Cell().add(TableAtributo).addStyle(styleCell));
        for (int i = 1; i < iteracion; i++) {
            oParagrah = new Paragraph(cadena.substring(i * numCharacteres, (i + 1) * numCharacteres));
            Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
            TableParrafo.addCell(cellParrafo1);
        }
        oParagrah = aux ? palabraEnBlanco : new Paragraph(cadena.substring(cadena.length() - cadena.length() % numCharacteres, cadena.length()));;
        Cell cellParrafo1 = new Cell().add(oParagrah.setBorderBottom(new SolidBorder(0.5f))).addStyle(styleCell).addStyle(styleTextLeft);
        TableParrafo.addCell(cellParrafo1);
        if (iteracion == 1 || iteracion == 0) {
            Cell cellraya2 = new Cell().add(palabraEnBlanco).addStyle(styleTextLeft).addStyle(styleCell);
            TableParrafo.addCell(cellraya2);
        }
        return TableParrafo;
    }

    //si está vacío lo agrega en blanco
    static Cell getCell(String palabra, Style posicion, Style border, Border subrayado) {
        Color colorBlanco = Color.WHITE;
        Paragraph palabraEnBlanco = new Paragraph(".").setFontColor(colorBlanco).setBorderBottom(new SolidBorder(0.5f));
        Paragraph Parapalabra = palabra.isEmpty() ? palabraEnBlanco : new Paragraph(palabra);

        return new Cell().add(Parapalabra.setBorderBottom(subrayado)).addStyle(posicion).addStyle(border);
    }

}
