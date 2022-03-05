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
    
    @JoinColumn( insertable = true,updatable = true,name="idproveedor",nullable = false)
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
    
    @Column(name = "estado", nullable = false)
    private String estado;
    
    @Column(name = "visto", nullable = false)
    private boolean visto;
    

    public Carta(Proveedor proveedor, String numCartaConfianza, LocalDate fechaVencimiento, String referencia, String obra, String importe, String estado) {
        this.proveedor = proveedor;
        this.numCartaConfianza = numCartaConfianza;
        this.fechaVencimiento = fechaVencimiento;
        this.referencia = referencia;
        this.obra = obra;
        this.importe = importe;
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
    
    
    
    
    
    
    
    
}
