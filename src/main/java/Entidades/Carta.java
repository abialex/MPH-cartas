/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author yalle
 */
@Entity
public class Carta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(insertable = true, updatable = true, name = "idproveedor", nullable = false)
    @ManyToOne
    private Proveedor proveedor;

    @Column(name = "numCartaConfianza", nullable = false)
    private String numCartaConfianza;

    @Column(name = "fechaVencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "referencia", nullable = false)
    private String referencia;

    @Column(name = "obra", nullable = false)
    private String obra;

    @Column(name = "importe", nullable = false)
    private String importe;

    @Column(name = "decimal", nullable = true)
    private int decimal;

    @Column(name = "importeInt", nullable = true)
    private int importeInt;

    @Column(name = "numCartaDevuelta", nullable = true)
    private String numCartaDevuelta;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "visto", nullable = false)
    private boolean visto;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "nameArchivo", nullable = true)
    private String nameArchivo;

    public Carta(Proveedor proveedor, String numCartaConfianza, LocalDate fechaVencimiento, String referencia, String obra, String importe,int decimal, int importeInt, String estado, String numCartaDevuelta) {
        this.proveedor = proveedor;
        this.numCartaConfianza = numCartaConfianza;
        this.fechaVencimiento = fechaVencimiento;
        this.referencia = referencia;
        this.obra = obra;
        this.importe = importe;
        this.decimal = decimal;
        this.importeInt = importeInt;
        this.numCartaDevuelta = numCartaDevuelta;
        this.estado = estado;
    }

    public Carta() {
    }

    public String getNumCartaConfianza() {
        return numCartaConfianza;
    }

    public void setNumCartaConfianza(String numCartaConfianza) {
        this.numCartaConfianza = numCartaConfianza;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObra() {
        return obra;
    }

    public void setObra(String obra) {
        this.obra = obra;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameArchivo() {
        return nameArchivo;
    }

    public void setNameArchivo(String nameArchivo) {
        this.nameArchivo = nameArchivo;
    }

    public int getImporteInt() {
        return importeInt;
    }

    public void setImporteInt(int importeInt) {
        this.importeInt = importeInt;
    }

    public String getNumCartaDevuelta() {
        return numCartaDevuelta;
    }

    public void setNumCartaDevuelta(String numCartaDevuelta) {
        this.numCartaDevuelta = numCartaDevuelta;
    }

    public int getDecimal() {
        return decimal;
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public Carta getCarta() {
        return this;
    }

}
