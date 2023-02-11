/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unprg.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ACER
 */
@Entity
@Table(name = "detalle_ventas")
@NamedQueries({
    @NamedQuery(name = "DetalleVentas.findAll", query = "SELECT d FROM DetalleVentas d"),
    @NamedQuery(name = "DetalleVentas.findByIdDetalleVentas", query = "SELECT d FROM DetalleVentas d WHERE d.idDetalleVentas = :idDetalleVentas"),
    @NamedQuery(name = "DetalleVentas.findByCantidad", query = "SELECT d FROM DetalleVentas d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleVentas.findByPrecioVenta", query = "SELECT d FROM DetalleVentas d WHERE d.precioVenta = :precioVenta")})
public class DetalleVentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdDetalleVentas")
    private Integer idDetalleVentas;
    @Column(name = "Cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PrecioVenta")
    private Double precioVenta;
    @JoinColumn(name = "IdProducto", referencedColumnName = "IdProducto")
    @ManyToOne(optional = false)
    private Producto idProducto;
    @JoinColumn(name = "IdVentas", referencedColumnName = "IdVentas")
    @ManyToOne(optional = false)
    private Ventas idVentas;

    public DetalleVentas() {
    }

    public DetalleVentas(Integer idDetalleVentas) {
        this.idDetalleVentas = idDetalleVentas;
    }

    public Integer getIdDetalleVentas() {
        return idDetalleVentas;
    }

    public void setIdDetalleVentas(Integer idDetalleVentas) {
        this.idDetalleVentas = idDetalleVentas;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Ventas getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(Ventas idVentas) {
        this.idVentas = idVentas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleVentas != null ? idDetalleVentas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleVentas)) {
            return false;
        }
        DetalleVentas other = (DetalleVentas) object;
        if ((this.idDetalleVentas == null && other.idDetalleVentas != null) || (this.idDetalleVentas != null && !this.idDetalleVentas.equals(other.idDetalleVentas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.edu.unprg.entity.DetalleVentas[ idDetalleVentas=" + idDetalleVentas + " ]";
    }
    
}
