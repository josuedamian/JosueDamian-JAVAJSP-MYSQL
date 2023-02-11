/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pe.edu.unprg.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.edu.unprg.entity.Cliente;
import pe.edu.unprg.entity.Vendedor;
import pe.edu.unprg.entity.DetalleVentas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pe.edu.unprg.controller.exceptions.IllegalOrphanException;
import pe.edu.unprg.controller.exceptions.NonexistentEntityException;
import pe.edu.unprg.entity.Ventas;

/**
 *
 * @author ACER
 */
public class VentasJpaController implements Serializable {

    public VentasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public VentasJpaController(){
        this.emf = Persistence.createEntityManagerFactory("ProyectoFinal01PU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ventas ventas) {
        if (ventas.getDetalleVentasList() == null) {
            ventas.setDetalleVentasList(new ArrayList<DetalleVentas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente idCliente = ventas.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                ventas.setIdCliente(idCliente);
            }
            Vendedor idVendedor = ventas.getIdVendedor();
            if (idVendedor != null) {
                idVendedor = em.getReference(idVendedor.getClass(), idVendedor.getIdVendedor());
                ventas.setIdVendedor(idVendedor);
            }
            List<DetalleVentas> attachedDetalleVentasList = new ArrayList<DetalleVentas>();
            for (DetalleVentas detalleVentasListDetalleVentasToAttach : ventas.getDetalleVentasList()) {
                detalleVentasListDetalleVentasToAttach = em.getReference(detalleVentasListDetalleVentasToAttach.getClass(), detalleVentasListDetalleVentasToAttach.getIdDetalleVentas());
                attachedDetalleVentasList.add(detalleVentasListDetalleVentasToAttach);
            }
            ventas.setDetalleVentasList(attachedDetalleVentasList);
            em.persist(ventas);
            if (idCliente != null) {
                idCliente.getVentasList().add(ventas);
                idCliente = em.merge(idCliente);
            }
            if (idVendedor != null) {
                idVendedor.getVentasList().add(ventas);
                idVendedor = em.merge(idVendedor);
            }
            for (DetalleVentas detalleVentasListDetalleVentas : ventas.getDetalleVentasList()) {
                Ventas oldIdVentasOfDetalleVentasListDetalleVentas = detalleVentasListDetalleVentas.getIdVentas();
                detalleVentasListDetalleVentas.setIdVentas(ventas);
                detalleVentasListDetalleVentas = em.merge(detalleVentasListDetalleVentas);
                if (oldIdVentasOfDetalleVentasListDetalleVentas != null) {
                    oldIdVentasOfDetalleVentasListDetalleVentas.getDetalleVentasList().remove(detalleVentasListDetalleVentas);
                    oldIdVentasOfDetalleVentasListDetalleVentas = em.merge(oldIdVentasOfDetalleVentasListDetalleVentas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ventas ventas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas persistentVentas = em.find(Ventas.class, ventas.getIdVentas());
            Cliente idClienteOld = persistentVentas.getIdCliente();
            Cliente idClienteNew = ventas.getIdCliente();
            Vendedor idVendedorOld = persistentVentas.getIdVendedor();
            Vendedor idVendedorNew = ventas.getIdVendedor();
            List<DetalleVentas> detalleVentasListOld = persistentVentas.getDetalleVentasList();
            List<DetalleVentas> detalleVentasListNew = ventas.getDetalleVentasList();
            List<String> illegalOrphanMessages = null;
            for (DetalleVentas detalleVentasListOldDetalleVentas : detalleVentasListOld) {
                if (!detalleVentasListNew.contains(detalleVentasListOldDetalleVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleVentas " + detalleVentasListOldDetalleVentas + " since its idVentas field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                ventas.setIdCliente(idClienteNew);
            }
            if (idVendedorNew != null) {
                idVendedorNew = em.getReference(idVendedorNew.getClass(), idVendedorNew.getIdVendedor());
                ventas.setIdVendedor(idVendedorNew);
            }
            List<DetalleVentas> attachedDetalleVentasListNew = new ArrayList<DetalleVentas>();
            for (DetalleVentas detalleVentasListNewDetalleVentasToAttach : detalleVentasListNew) {
                detalleVentasListNewDetalleVentasToAttach = em.getReference(detalleVentasListNewDetalleVentasToAttach.getClass(), detalleVentasListNewDetalleVentasToAttach.getIdDetalleVentas());
                attachedDetalleVentasListNew.add(detalleVentasListNewDetalleVentasToAttach);
            }
            detalleVentasListNew = attachedDetalleVentasListNew;
            ventas.setDetalleVentasList(detalleVentasListNew);
            ventas = em.merge(ventas);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getVentasList().remove(ventas);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getVentasList().add(ventas);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idVendedorOld != null && !idVendedorOld.equals(idVendedorNew)) {
                idVendedorOld.getVentasList().remove(ventas);
                idVendedorOld = em.merge(idVendedorOld);
            }
            if (idVendedorNew != null && !idVendedorNew.equals(idVendedorOld)) {
                idVendedorNew.getVentasList().add(ventas);
                idVendedorNew = em.merge(idVendedorNew);
            }
            for (DetalleVentas detalleVentasListNewDetalleVentas : detalleVentasListNew) {
                if (!detalleVentasListOld.contains(detalleVentasListNewDetalleVentas)) {
                    Ventas oldIdVentasOfDetalleVentasListNewDetalleVentas = detalleVentasListNewDetalleVentas.getIdVentas();
                    detalleVentasListNewDetalleVentas.setIdVentas(ventas);
                    detalleVentasListNewDetalleVentas = em.merge(detalleVentasListNewDetalleVentas);
                    if (oldIdVentasOfDetalleVentasListNewDetalleVentas != null && !oldIdVentasOfDetalleVentasListNewDetalleVentas.equals(ventas)) {
                        oldIdVentasOfDetalleVentasListNewDetalleVentas.getDetalleVentasList().remove(detalleVentasListNewDetalleVentas);
                        oldIdVentasOfDetalleVentasListNewDetalleVentas = em.merge(oldIdVentasOfDetalleVentasListNewDetalleVentas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ventas.getIdVentas();
                if (findVentas(id) == null) {
                    throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ventas ventas;
            try {
                ventas = em.getReference(Ventas.class, id);
                ventas.getIdVentas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleVentas> detalleVentasListOrphanCheck = ventas.getDetalleVentasList();
            for (DetalleVentas detalleVentasListOrphanCheckDetalleVentas : detalleVentasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ventas (" + ventas + ") cannot be destroyed since the DetalleVentas " + detalleVentasListOrphanCheckDetalleVentas + " in its detalleVentasList field has a non-nullable idVentas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente idCliente = ventas.getIdCliente();
            if (idCliente != null) {
                idCliente.getVentasList().remove(ventas);
                idCliente = em.merge(idCliente);
            }
            Vendedor idVendedor = ventas.getIdVendedor();
            if (idVendedor != null) {
                idVendedor.getVentasList().remove(ventas);
                idVendedor = em.merge(idVendedor);
            }
            em.remove(ventas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ventas> findVentasEntities() {
        return findVentasEntities(true, -1, -1);
    }

    public List<Ventas> findVentasEntities(int maxResults, int firstResult) {
        return findVentasEntities(false, maxResults, firstResult);
    }

    private List<Ventas> findVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ventas.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Ventas findVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ventas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ventas> rt = cq.from(Ventas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Integer findMaxVentas() {
        EntityManager em = getEntityManager();
        try {
            Integer num = (Integer) em.createQuery("SELECT MAX(v.idVentas) FROM Ventas v").getSingleResult();
            return num;
        } finally {
            em.close();
        }
    }
    
}
