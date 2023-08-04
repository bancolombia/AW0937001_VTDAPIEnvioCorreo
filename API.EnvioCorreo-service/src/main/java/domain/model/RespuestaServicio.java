package domain.model;


import com.bcol.vtd.api.enviocorreo.validator.InyeccionSQL;

import java.io.Serializable;

public class RespuestaServicio implements Serializable {
    private static final long serialVersionUID = -7459224536650697439L;
    @InyeccionSQL(
            message = "Intento de inyeccion SQL en el campo codigo"
    )
    private String codigo;
    @InyeccionSQL(
            message = "Intento de inyeccion SQL en el campo descripcion"
    )
    private String descripcion;
    @InyeccionSQL(
            message = "Intento de inyeccion SQL en el campo redirectUrl"
    )
    private String redirectUrl;

    public RespuestaServicio() {
    }

    public RespuestaServicio(String codigo) {
        this.codigo = codigo;
    }

    public RespuestaServicio(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
