package util;

import com.bcol.vtd.api.enviocorreo.util.CargadorPropiedades;
import com.bcol.vtd.api.enviocorreo.util.ConstantesGeneracionPDF;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.model.Parametro;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UtilTestSM {

    static String pathResources = "src/test/resources/";
    static String pathProperties = "variables.json";

    public static List<Parametro> getListaParametros1() {

        List<Parametro> parametros = new ArrayList<>();

        parametros.add(new Parametro("VAR_ENVIAR_CORREO_RUTA_PLANTILLA",
                "VAR_ENVIAR_CORREO_RUTA_PLANTILLA"));

        Object correoPara = "dddddd@gmail.com";

        parametros.add(new Parametro("VAR_ENVIAR_CORREO_PARA",
                correoPara));

        Object documento = "1111111";
        parametros.add(new Parametro("VAR_ENVIAR_CORREO_DOCUMENTO",
                documento));

        parametros.add(new Parametro("VAR_ENVIAR_CORREO_ASUNTO",
                ConstantesGeneracionPDF.ASUNTO_CORREO));

        List<String> prods = new ArrayList<>();
        prods.add("Tarjeta de crédito");
        parametros.add(new Parametro(ConstantesGeneracionPDF.NOMBRE_PRODUCTO, prods));

        List<String> beneficios = new ArrayList<>();
        beneficios.add("Beneficio1");
        beneficios.add("Beneficio2");
        List<List<String>> benef = new ArrayList<>();
        benef.add(beneficios);
        parametros.add(new Parametro(ConstantesGeneracionPDF.BENEFICIOS, benef));

        parametros.add(new Parametro(ConstantesGeneracionPDF.NOMBRE_CLIENTE, "Oscar"));
        parametros.add(new Parametro(ConstantesGeneracionPDF.DOCUMENTO, (Object) "778899101"));

        List<String> cupos = new ArrayList<>();
        cupos.add("300,000,000");
        parametros.add(new Parametro(ConstantesGeneracionPDF.CUPO_SOLICITADO, cupos));

        List<String> imagenes = new ArrayList<>();
        imagenes.add("325");
        parametros.add(new Parametro(ConstantesGeneracionPDF.CODIGO_IMAGEN, imagenes));

        parametros.add(new Parametro(ConstantesGeneracionPDF.IMAGENES, "spacer.png,banner.png,mobile_banner.png,cta.png,ft.png,fb.png,tw.png,link.png,instragram.png,youtube.png"));

        return parametros;
    }

    public static List<Parametro> getListaParametros() {

        List<Parametro> parametros = new ArrayList<>();

        String rutaPlantilla = "VAR_ENVIAR_CORREO_RUTA_PLANTILLA_LIBRANZA";

        parametros.add(new Parametro("VAR_ENVIAR_CORREO_RUTA_PLANTILLA",
                rutaPlantilla));

        Object correoPara = "laymiranda@gmail.com";
        parametros.add(new Parametro("VAR_ENVIAR_CORREO_PARA",
                correoPara));

        Object documento = "1111111";
        parametros.add(new Parametro("VAR_ENVIAR_CORREO_DOCUMENTO",
                documento));

        parametros.add(new Parametro("VAR_ENVIAR_CORREO_ASUNTO",
                ConstantesGeneracionPDF.ASUNTO_CORREO));

        return parametros;
    }

    public static InputStream pdfis() {
        try {
            return new FileInputStream(pathResources + "hola.pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setConfiguracion() {
        CargadorPropiedades configuracion = CargadorPropiedades.getInstance();
        try {
            configuracion.setPropiedades((Map<String, String>) readJsonFile(pathProperties, new TypeToken<Map<String, String>>() {
            }.getType()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            injectEnvironmentVariable("SENDGRID_API_KEY", "senggrikey");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //test to check function in configuracion.java
    public static Object readJsonFile(String path, Type typeToken) throws IOException {

        File file = new File(pathResources, FilenameUtils.getName(path));

        if (!file.exists()) {
            throw new FileNotFoundException("invalid fileName:" + path);
        }

        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        Gson gson = new Gson();
        return gson.fromJson(content, typeToken);

    }

    public static void injectEnvironmentVariable(String key, String value)
            throws Exception {

        Class<?> processEnvironment = Class.forName("java.lang.ProcessEnvironment");

        Field unmodifiableMapField = getAccessibleField(processEnvironment, "theUnmodifiableEnvironment");
        Object unmodifiableMap = unmodifiableMapField.get(null);
        injectIntoUnmodifiableMap(key, value, unmodifiableMap);

        Field mapField = getAccessibleField(processEnvironment, "theEnvironment");
        Map<String, String> map = (Map<String, String>) mapField.get(null);
        map.put(key, value);
    }

    private static Field getAccessibleField(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {

        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    private static void injectIntoUnmodifiableMap(String key, String value, Object map)
            throws ReflectiveOperationException {

        Class unmodifiableMap = Class.forName("java.util.Collections$UnmodifiableMap");
        Field field = getAccessibleField(unmodifiableMap, "m");
        Object obj = field.get(map);
        ((Map<String, String>) obj).put(key, value);
    }

}
