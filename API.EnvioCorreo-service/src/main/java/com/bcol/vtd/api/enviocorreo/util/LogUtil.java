package com.bcol.vtd.api.enviocorreo.util;

public class LogUtil {
    public LogUtil() {
    }

    public static String rutaVariables() {
        return System.getProperty("PATH_VARIABLES");
    }

    public static String rutaVariablesSeguridad() {
        return System.getProperty("PATH_VARIABLES_SEGURIDAD");
    }
}
