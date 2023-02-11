/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unprg.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author ACER
 */
@Entity
@Table(name = "vendedor")
@NamedQueries({
    @NamedQuery(name = "Vendedor.findAll", query = "SELECT v FROM Vendedor v"),
    @NamedQuery(name = "Vendedor.findByIdVendedor", query = "SELECT v FROM Vendedor v WHERE v.idVendedor = :idVendedor"),
    @NamedQuery(name = "Vendedor.findByDni", query = "SELECT v FROM Vendedor v WHERE v.dni = :dni"),
    @NamedQuery(name = "Vendedor.findByEmailAndDni", query = "SELECT v FROM Vendedor v WHERE v.email = :email and v.dni = :dni and v.estado='1' "),
    @NamedQuery(name = "Vendedor.findByNombres", query = "SELECT v FROM Vendedor v WHERE v.nombres = :nombres"),
    @NamedQuery(name = "Vendedor.findByTelefono", query = "SELECT v FROM Vendedor v WHERE v.telefono = :telefono"),
    @NamedQuery(name = "Vendedor.findByEstado", query = "SELECT v FROM Vendedor v WHERE v.estado = :estado"),
    @NamedQuery(name = "Vendedor.findByUser", query = "SELECT v FROM Vendedor v WHERE v.user = :user"),
    @NamedQuery(name = "Vendedor.findByEmail", query = "SELECT v FROM Vendedor v WHERE v.email = :email")})
public class Vendedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdVendedor")
    private Integer idVendedor;
    @Basic(optional = false)
    @Column(name = "Dni")
    private String dni;
    @Column(name = "Nombres")
    private String nombres;
    @Column(name = "Telefono")
    private String telefono;
    @Column(name = "Estado")
    private String estado;
    @Column(name = "User")
    private String user;
    @Column(name = "Email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVendedor")
    private List<Ventas> ventasList;

    public Vendedor() {
    }

    public Vendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Vendedor(Integer idVendedor, String dni) {
        this.idVendedor = idVendedor;
        this.dni = dni;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Ventas> getVentasList() {
        return ventasList;
    }

    public void setVentasList(List<Ventas> ventasList) {
        this.ventasList = ventasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVendedor != null ? idVendedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vendedor)) {
            return false;
        }
        Vendedor other = (Vendedor) object;
        if ((this.idVendedor == null && other.idVendedor != null) || (this.idVendedor != null && !this.idVendedor.equals(other.idVendedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.edu.unprg.entity.Vendedor[ idVendedor=" + idVendedor + " ]";
    }
    
}
