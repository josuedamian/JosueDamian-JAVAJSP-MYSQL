/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unprg.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ACER
 */
@Entity
@Table(name = "ventas")
@NamedQueries({
    @NamedQuery(name = "Ventas.findAll", query = "SELECT v FROM Ventas v"),
    @NamedQuery(name = "Ventas.findByIdVentas", query = "SELECT v FROM Ventas v WHERE v.idVentas = :idVentas"),
    @NamedQuery(name = "Ventas.findMaxVentas", query = "SELECT MAX(v.idVentas) FROM Ventas v"),
    @NamedQuery(name = "Ventas.findByNumeroVenta", query = "SELECT v FROM Ventas v WHERE v.numeroVenta = :numeroVenta"),
    @NamedQuery(name = "Ventas.findByFecha", query = "SELECT v FROM Ventas v WHERE v.fecha = :fecha"),
    @NamedQuery(name = "Ventas.findByMonto", query = "SELECT v FROM Ventas v WHERE v.monto = :monto"),
    @NamedQuery(name = "Ventas.findByEstado", query = "SELECT v FROM Ventas v WHERE v.estado = :estado")})
public class Ventas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdVentas")
    private Integer idVentas;
    @Column(name = "NumeroVenta")
    private String numeroVenta;
    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Monto")
    private Double monto;
    @Column(name = "Estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVentas")
    private List<DetalleVentas> detalleVentasList;
    @JoinColumn(name = "IdCliente", referencedColumnName = "IdCliente")
    @ManyToOne(optional = false)
    private Cliente idCliente;
    @JoinColumn(name = "IdVendedor", referencedColumnName = "IdVendedor")
    @ManyToOne(optional = false)
    private Vendedor idVendedor;

    public Ventas() {
    }

    public Ventas(Integer idVentas) {
        this.idVentas = idVentas;
    }

    public Integer getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(Integer idVentas) {
        this.idVentas = idVentas;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetalleVentas> getDetalleVentasList() {
        return detalleVentasList;
    }

    public void setDetalleVentasList(List<DetalleVentas> detalleVentasList) {
        this.detalleVentasList = detalleVentasList;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Vendedor getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Vendedor idVendedor) {
        this.idVendedor = idVendedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVentas != null ? idVentas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ventas)) {
            return false;
        }
        Ventas other = (Ventas) object;
        if ((this.idVentas == null && other.idVentas != null) || (this.idVentas != null && !this.idVentas.equals(other.idVentas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.edu.unprg.entity.Ventas[ idVentas=" + idVentas + " ]";
    }
    
}
