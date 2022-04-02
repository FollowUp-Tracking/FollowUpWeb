package es.upm.dit.isst.followmeweb.model;

import java.util.List;

public class Pedido {
    
    private String numeroSeguimiento;

    private int idCliente;

    private int idVendedor;

    private int idRepartidor;
    
    private List<Traza> trazas;

    public Pedido() {
    }

    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }

    public void setNumeroSeguimiento(String numeroSeguimiento) {
        this.numeroSeguimiento = numeroSeguimiento;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public List<Traza> getTrazas() {
        return trazas;
    }

    public void setTrazas(List<Traza> trazas) {
        this.trazas = trazas;
    }
    
}
