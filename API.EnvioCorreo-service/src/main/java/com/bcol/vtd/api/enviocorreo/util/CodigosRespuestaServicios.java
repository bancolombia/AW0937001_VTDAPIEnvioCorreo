package com.bcol.vtd.api.enviocorreo.util;

public enum CodigosRespuestaServicios {

    DDOC_002("02", "No se encuentra la plantilla"),
    DDOC_003("03", "Error generando archivo PDF"),
    DDOC_006("06", "Error enviando correo con servicio Sendgrid. metodo:sendMail"),
    DDOC_008("08", "Error adjuntando archivo. metodo:agregarArchivoAdjunto"),
    DDOC_010("10", "Parametros vacíos o nulos"),

    ECOR_001("01", "Error general envio correo"),
    ECOR_004("04", "El servicio se ha ejecutado satisfactoriamente");


    private final String codigo;
    private final String descripcion;

    private CodigosRespuestaServicios(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }


}
