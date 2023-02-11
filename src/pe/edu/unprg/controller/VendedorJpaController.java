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
import pe.edu.unprg.entity.Ventas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pe.edu.unprg.controller.exceptions.IllegalOrphanException;
import pe.edu.unprg.controller.exceptions.NonexistentEntityException;
import pe.edu.unprg.entity.Vendedor;

/**
 *
 * @author ACER
 */
public class VendedorJpaController implements Serializable {

    public VendedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public VendedorJpaController(){
        this.emf = Persistence.createEntityManagerFactory("ProyectoFinal01PU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vendedor vendedor) {
        if (vendedor.getVentasList() == null) {
            vendedor.setVentasList(new ArrayList<Ventas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Ventas> attachedVentasList = new ArrayList<Ventas>();
            for (Ventas ventasListVentasToAttach : vendedor.getVentasList()) {
                ventasListVentasToAttach = em.getReference(ventasListVentasToAttach.getClass(), ventasListVentasToAttach.getIdVentas());
                attachedVentasList.add(ventasListVentasToAttach);
            }
            vendedor.setVentasList(attachedVentasList);
            em.persist(vendedor);
            for (Ventas ventasListVentas : vendedor.getVentasList()) {
                Vendedor oldIdVendedorOfVentasListVentas = ventasListVentas.getIdVendedor();
                ventasListVentas.setIdVendedor(vendedor);
                ventasListVentas = em.merge(ventasListVentas);
                if (oldIdVendedorOfVentasListVentas != null) {
                    oldIdVendedorOfVentasListVentas.getVentasList().remove(ventasListVentas);
                    oldIdVendedorOfVentasListVentas = em.merge(oldIdVendedorOfVentasListVentas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vendedor vendedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vendedor persistentVendedor = em.find(Vendedor.class, vendedor.getIdVendedor());
            List<Ventas> ventasListOld = persistentVendedor.getVentasList();
            List<Ventas> ventasListNew = vendedor.getVentasList();
            List<String> illegalOrphanMessages = null;
            for (Ventas ventasListOldVentas : ventasListOld) {
                if (!ventasListNew.contains(ventasListOldVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventas " + ventasListOldVentas + " since its idVendedor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Ventas> attachedVentasListNew = new ArrayList<Ventas>();
            for (Ventas ventasListNewVentasToAttach : ventasListNew) {
                ventasListNewVentasToAttach = em.getReference(ventasListNewVentasToAttach.getClass(), ventasListNewVentasToAttach.getIdVentas());
                attachedVentasListNew.add(ventasListNewVentasToAttach);
            }
            ventasListNew = attachedVentasListNew;
            vendedor.setVentasList(ventasListNew);
            vendedor = em.merge(vendedor);
            for (Ventas ventasListNewVentas : ventasListNew) {
                if (!ventasListOld.contains(ventasListNewVentas)) {
                    Vendedor oldIdVendedorOfVentasListNewVentas = ventasListNewVentas.getIdVendedor();
                    ventasListNewVentas.setIdVendedor(vendedor);
                    ventasListNewVentas = em.merge(ventasListNewVentas);
                    if (oldIdVendedorOfVentasListNewVentas != null && !oldIdVendedorOfVentasListNewVentas.equals(vendedor)) {
                        oldIdVendedorOfVentasListNewVentas.getVentasList().remove(ventasListNewVentas);
                        oldIdVendedorOfVentasListNewVentas = em.merge(oldIdVendedorOfVentasListNewVentas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vendedor.getIdVendedor();
                if (findVendedor(id) == null) {
                    throw new NonexistentEntityException("The vendedor with id " + id + " no longer exists.");
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
            Vendedor vendedor;
            try {
                vendedor = em.getReference(Vendedor.class, id);
                vendedor.getIdVendedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vendedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ventas> ventasListOrphanCheck = vendedor.getVentasList();
            for (Ventas ventasListOrphanCheckVentas : ventasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vendedor (" + vendedor + ") cannot be destroyed since the Ventas " + ventasListOrphanCheckVentas + " in its ventasList field has a non-nullable idVendedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(vendedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vendedor> findVendedorEntities() {
        return findVendedorEntities(true, -1, -1);
    }

    public List<Vendedor> findVendedorEntities(int maxResults, int firstResult) {
        return findVendedorEntities(false, maxResults, firstResult);
    }

    private List<Vendedor> findVendedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vendedor.class));
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

    public Vendedor findVendedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vendedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getVendedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vendedor> rt = cq.from(Vendedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Vendedor findByEmailAndDni(String email, String dni){
        EntityManager cm = getEntityManager();
        try {
            List <Vendedor> vendedor = cm.createNamedQuery("Vendedor.findByEmailAndDni", Vendedor.class)
                    .setParameter("email", email)
                    .setParameter("dni", dni)
                    .getResultList();
            if (vendedor.size()>0) {
                return vendedor.get(0);
            }
            return null;
        } finally {
            cm.close();
        }
    }
    
}
