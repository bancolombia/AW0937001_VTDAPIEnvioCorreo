package com.bcol.vtd.api.enviocorreo.util;

/**
 * Se crea clase de constantes
 */
public enum Constant {

    DOS_PUNTOS(":"),
    ESPACIO(" ");


    /**
     * Campo a retornar
     */
    private String value;

    /**
     * Metodo constructor de la clase
     *
     * @param valor
     */
    Constant(String valor) {
        this.value = value;
    }

    public String getValor() {
        return value;
    }

}
