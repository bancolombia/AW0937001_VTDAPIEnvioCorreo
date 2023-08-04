package domain.model;

import java.io.Serializable;

public class Parametro implements Serializable {
    private static final long serialVersionUID = -4326381988672235949L;
    private String clave;
    private String valor;
    private Object object;

    public Parametro(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public Parametro(String clave, Object object) {
        this.clave = clave;
        this.object = object;
    }

    public Parametro() {
    }

    public String getClave() {
        return this.clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return this.valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
