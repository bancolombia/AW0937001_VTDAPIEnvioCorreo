package com.bcol.vtd.api.enviocorreo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CargadorPropiedades {
    private static final Logger logger = Logger.getLogger(CargadorPropiedades.class.getName());
    private static CargadorPropiedades instance;
    private Map<String, String> propiedades;

    private CargadorPropiedades(String path) {
        try {
            this.propiedades = readJsonFile(path);
            logger.log(Level.INFO, "Creando mapa de propiedades");
            logger.info("Carga del conjunto de propiedades del API");
        } catch (Exception var3) {
            logger.log(Level.SEVERE, "Error cargando mapa de propiedades, detalles: " + var3.getMessage());
        }

    }

    public static Map<String, String> readJsonFile(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonMap = (Map) mapper.readValue(new File(path), new TypeReference<Map<String, String>>() {
        });
        return jsonMap;
    }

    public static CargadorPropiedades getInstance() {
        if (instance == null) {
            instance = new CargadorPropiedades(LogUtil.rutaVariables());
        }
        return instance;
    }

    public Map<String, String> getPropiedades() {
        return this.propiedades;
    }

    public void setPropiedades(Map<String, String> propiedades) {
        this.propiedades = propiedades;
    }

    public String getValue(String key) {
        return (String) this.propiedades.get(key);
    }
}
