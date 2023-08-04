package com.bcol.vtd.api.enviocorreo.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import domain.model.Parametro;
import domain.model.RespuestaServicioEnvioCorreo;
import infrastructure.drivenadapters.mail.ServiceMail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/***
 * Clase de utilidades para la api de envio de correo
 */
public class PlantillasUtil {
    private static final Logger LOGGER = LogManager.getLogger(PlantillasUtil.class);
    private static final String MSG_ERROR = "->  error inesperado: ";
    ServiceMail serviceMail;
    Mail mail;
    /***
     * Lista de Parametros para agregar a la plantilla
     */
    private List<Parametro> listaParametros;
    private String rutaplantilla = null;
    private String plantilla = null;
    private String asuntoCorreo = null;
    private String deCorreo = null;
    private String paraCorreo = null;
    private List<String> imagen_tarjeta = null;
    private String numeroIdentificacion = null;
    private VelocityEngine velocityEngine = null;
    private VelocityContext context = null;
    private Template template = null;
    private StringWriter writer = null;
    private String nombreArchivo1 = null;
    private String contenidoArchivo1 = null;
    private byte[] archivoDecodificado = null;
    private ByteArrayInputStream bis = null;
    private byte[] archivoAdjunto = null;
    private PDDocument pdfDoc;
    private ByteArrayOutputStream out = null;
    private InputStream is = null;
    private String usuarioSL = null;
    private String claveSL = null;
    private String imagenes[] = null;
    private SendGrid sendgrid = null;
    private byte[] pdf;
    private RespuestaServicioEnvioCorreo respuestaServicioEnvioCorreo;
    private String idSesion;
    private CargadorPropiedades configuracion;

    @Inject
    public PlantillasUtil(ServiceMail serviceMail) {
        configuracion = CargadorPropiedades.getInstance();
        this.serviceMail = serviceMail;
    }

    public PlantillasUtil(ServiceMail serviceMail, Map<String, String> propiedades) {
        this.serviceMail = serviceMail;
        configuracion = CargadorPropiedades.getInstance();
        configuracion.setPropiedades(propiedades);
    }

    public PlantillasUtil(List<Parametro> listaParametros, String idSesion) {
        if (idSesion != null) {
            ThreadContext.put("sessionId", idSesion);
        } else {
            ThreadContext.put("sessionId", "");
        }
        this.idSesion = idSesion;
        this.listaParametros = listaParametros;
        respuestaServicioEnvioCorreo = new RespuestaServicioEnvioCorreo();
        respuestaServicioEnvioCorreo.setEstadoRespuesta(true);
        configuracion = CargadorPropiedades.getInstance();
    }


    /***
     * Procedimiento que mapea los parametros recibidos y crea el cuerpo html del correo
     * @return
     */

    public boolean createBodyHtml() {

        writer = new StringWriter();


        plantilla = configuracion
                .getValue(ConstantesGeneracionPDF.NOMBRE_PLANTILLA_CORREO) != null ? configuracion
                .getValue(ConstantesGeneracionPDF.NOMBRE_PLANTILLA_CORREO)
                .toString() : "";

        usuarioSL = configuracion
                .getValue(ConstantesGeneracionPDF.USUARIO_CORREO) != null ? configuracion
                .getValue(ConstantesGeneracionPDF.USUARIO_CORREO)
                .toString() : "";
        claveSL = configuracion
                .getValue(ConstantesGeneracionPDF.CLAVE_CORREO) != null ? configuracion
                .getValue(ConstantesGeneracionPDF.CLAVE_CORREO).toString()
                : "";
        deCorreo = configuracion
                .getValue(ConstantesGeneracionPDF.DE_CORREO) != null ? configuracion
                .getValue(ConstantesGeneracionPDF.DE_CORREO).toString()
                : "";

        context = new VelocityContext();

        for (Parametro param : listaParametros) {

            context.put(param.getClave(), param.getValor());

            if (param.getObject() != null) {
                context.put(param.getClave(), param.getObject());
            }
            if (param.getClave().equals(ConstantesGeneracionPDF.PARA)) {
                paraCorreo = param.getObject().toString();
            }
            if (param.getClave().equals(ConstantesGeneracionPDF.ARCHIVO1)) {
                contenidoArchivo1 = param.getObject().toString();
            }
            if (param.getClave().equals(ConstantesGeneracionPDF.DOCUMENTO)) {
                numeroIdentificacion = param.getObject().toString();
            }
            if (param.getClave().equals(ConstantesGeneracionPDF.IMAGENES)) {
                if (param.getValor() != null) {
                    imagenes = param.getValor().split(",");
                } else {
                    imagenes = param.getObject().toString().split(",");
                }

            }
            if (param.getClave().equals(ConstantesGeneracionPDF.CODIGO_IMAGEN)) {
                imagen_tarjeta = ((ArrayList<String>) param.getObject());
            }
            if (param.getClave().equals(ConstantesGeneracionPDF.ASUNTO_CORREO)) {
                if (param.getValor() != null) {
                    asuntoCorreo = configuracion.getValue(param.getValor());
                } else {
                    asuntoCorreo = configuracion.getValue(param.getObject().toString());
                }

            }
            if (param.getClave().equals(ConstantesGeneracionPDF.RUTA_PLANTILLA_CORREO)) {
                if (param.getValor() != null) {
                    rutaplantilla = configuracion.getValue(param.getValor());
                } else {
                    rutaplantilla = configuracion.getValue(param.getObject().toString());
                }
                context.put("RUTA_IMG", rutaplantilla);
            }
            if (param.getClave().equals(ConstantesGeneracionPDF.NOMBRE_PLANTILLA_PDF)) {
                plantilla = "pdf_" + plantilla;
            }

        }

        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("input.encoding", StandardCharsets.UTF_8.name());
        velocityEngine.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
        velocityEngine.setProperty("file.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        velocityEngine.setProperty("file.resource.loader.path", rutaplantilla);
        velocityEngine.setProperty("file.resource.loader.cache", "false");
        velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");
        velocityEngine.init();

        try {
            template = velocityEngine.getTemplate(plantilla);
        } catch (Exception e) {
            respuestaServicioEnvioCorreo.setError(CodigosRespuestaServicios.DDOC_002.getCodigo(),
                    CodigosRespuestaServicios.DDOC_002.getDescripcion());
            LOGGER.error(CodigosRespuestaServicios.DDOC_002.getCodigo() + MSG_ERROR + e.getMessage(), e);
        }

        template.merge(context, writer);

        return true;
    }


    /**
     * ;
     * Procedimiento que agrega las imagenes como archivos adjuntos al correo
     */
    public boolean adjuntarImagenes() {

        // Si la plantilla tiene más imagens adicionales al header y bottom,
        // y estas se han enviado como parámetro
        if (imagenes != null && imagenes.length > 0) {
            String rutaImagenes = rutaplantilla + File.separator + "images" + File.separator;

            for (int i = 1; i <= imagenes.length; i++) {
                LOGGER.debug("Cargando imagen -> " + rutaImagenes + imagenes[i - 1]);

                try {
                    serviceMail.addAttachment(mail, rutaImagenes,
                            imagenes[i - 1], "IMAGE_" + (i), "png");
                } catch (IOException e) {
                    LOGGER.error("No se cargo imagen ".concat(imagenes[i - 1]) + " " + e.getMessage());
                }
            }
        }
        return true;
    }

    /***
     * Procedimiento que agrega el archivo recibido como archivo adjunto en el email
     */
    public boolean agregarArchivoAdjunto() {

        if (null != contenidoArchivo1 && !"".equals(contenidoArchivo1)) {

            nombreArchivo1 = "ArchivoAdjunto.pdf";

            try {
                archivoAdjunto = contenidoArchivo1.getBytes(StandardCharsets.UTF_8.name());
                archivoDecodificado = Base64.getDecoder().decode(archivoAdjunto);
                bis = new ByteArrayInputStream(archivoDecodificado);

                int keyLength = 128;
                AccessPermission ap = new AccessPermission();
                StandardProtectionPolicy spp = new StandardProtectionPolicy("",
                        numeroIdentificacion, ap);
                spp.setEncryptionKeyLength(keyLength);
                spp.setPermissions(ap);

                pdfDoc = PDDocument.load(bis);
                pdfDoc.protect(spp);
                out = new ByteArrayOutputStream();
                pdfDoc.save(out);

                byte[] data = out.toByteArray();

                is = new ByteArrayInputStream(data);

                serviceMail.addAttachment(mail, nombreArchivo1, is);

                return true;

            } catch (IOException e) {
                LOGGER.error(CodigosRespuestaServicios.DDOC_008.getCodigo() + MSG_ERROR + e.getMessage());
                respuestaServicioEnvioCorreo.setError(CodigosRespuestaServicios.DDOC_008.getCodigo(),
                        CodigosRespuestaServicios.DDOC_008.getDescripcion());
                return false;
            }
        }
        return true;
    }

    /***
     * Método que agrega una imagen tarjeta como adjunto en el email
     */
    public boolean addImagenTarjeta() {

        if (imagen_tarjeta != null) {
            int i = 1;
            for (String imagen : imagen_tarjeta) {
                String pathBase = rutaplantilla + File.separator + "images" + File.separator + "cards" + File.separator;
                LOGGER.debug("Agregando imagen:".concat(pathBase).concat(imagen));
                try {
                    serviceMail.addAttachment(mail, pathBase,
                            imagen.concat(".png"), "CARD_IMAGE_" + (i), "png");
                } catch (IOException e) {
                    LOGGER.error("No se cargo imagen ".concat(imagen) + " " + e.getMessage());
                }
                i++;
            }
        }

        return true;
    }

    /***
     * procedimiento que se encarga de convertir la plantilla html en un archivo PDF
     */
    public void createFile() {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            document.setPageSize(PageSize.LETTER);

            PdfWriter pdfWriter = null;
            pdfWriter = PdfWriter.getInstance(document, baos);
            document.open();

            createBodyHtml();

            if (respuestaServicioEnvioCorreo.getEstadoRespuesta()) {
                String content = this.writer.toString();
                XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, new StringReader(content));
                document.close();
                pdfWriter.flush();
                pdf = baos.toByteArray();
                pdfWriter.close();
                LOGGER.info("PDF generated successfully");
            }
        } catch (Exception e) {
            respuestaServicioEnvioCorreo.setError(CodigosRespuestaServicios.DDOC_003.getCodigo(),
                    CodigosRespuestaServicios.DDOC_003.getDescripcion());
            LOGGER.error(CodigosRespuestaServicios.DDOC_003.getCodigo() + MSG_ERROR + e.getMessage(), e);
        }
    }

    public byte[] getFile() {
        return pdf;
    }

    public RespuestaServicioEnvioCorreo getRespuestaServicioEnvioCorreo() {
        return this.respuestaServicioEnvioCorreo;
    }

    public boolean build(List<Parametro> listaParametros) {

        this.listaParametros = listaParametros;

        mail = new Mail();
        createBodyHtml();
        agregarArchivoAdjunto();
        adjuntarImagenes();
        addImagenTarjeta();
        Content content = new Content("text/html", writer.toString());

        return serviceMail.send(mail, deCorreo, asuntoCorreo, paraCorreo, content);

    }
}
