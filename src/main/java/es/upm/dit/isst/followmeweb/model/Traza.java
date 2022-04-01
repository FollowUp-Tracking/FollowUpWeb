package es.upm.dit.isst.followmeweb.model;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

public class Traza {
    
    private int id;
    
    private double latitud;
    
    private double longitud;
    
    private Date fecha;

    @NotEmpty
    private int idPedido;
    
    public Traza() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
}
