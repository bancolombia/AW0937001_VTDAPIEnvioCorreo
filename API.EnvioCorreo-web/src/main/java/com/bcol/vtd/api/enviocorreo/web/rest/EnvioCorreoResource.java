package com.bcol.vtd.api.enviocorreo.web.rest;

import com.bcol.vtd.api.enviocorreo.service.EnvioCorreoServiceImpl;
import com.bcol.vtd.api.enviocorreo.util.CodigosRespuestaServicios;
import com.bcol.vtd.api.enviocorreo.util.Constant;
import com.bcol.vtd.api.enviocorreo.util.ConstantesGeneracionPDF;
import domain.model.Parametro;
import domain.model.RespuestaServicio;
import domain.model.RespuestaServicioEnvioCorreo;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("servicio")
@RequestScoped
public class EnvioCorreoResource {


    private static final String enconding = "UTF-8";
    private static final Logger LOGGER = LogManager.getLogger(EnvioCorreoResource.class);

    @Inject
    @Named("envioCorreoService")
    private EnvioCorreoServiceImpl envioCorreoService = new EnvioCorreoServiceImpl();

    @POST
    @Path("descargar")
    @ApiOperation(value = "Descargar Documento", nickname = "descargar", notes = "Recurso que permite la descarga de la plantilla como documento PDF", response = RespuestaServicioEnvioCorreo.class, tags = {
            "descarga-documento"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response descargadocumento(@Context HttpServletRequest requestContext, List<Parametro> listaParametros, @HeaderParam("idSesion") String idSesion) {
        ThreadContext.put("sessionId", idSesion);
        LOGGER.info("Inicio de microservicio " + ConstantesGeneracionPDF.enviarCorreo);

        RespuestaServicioEnvioCorreo respuestaServicioEnvioCorreo = null;
        if (null == listaParametros) {

            respuestaServicioEnvioCorreo = new RespuestaServicioEnvioCorreo();
            respuestaServicioEnvioCorreo.setError(CodigosRespuestaServicios.DDOC_010.getCodigo(),
                    CodigosRespuestaServicios.DDOC_010.getDescripcion());

            return Response.status(500).entity(respuestaServicioEnvioCorreo).build();
        }

        respuestaServicioEnvioCorreo = envioCorreoService.getDocumento(listaParametros, idSesion);

        if (respuestaServicioEnvioCorreo.getEstadoRespuesta()) {

            Response.ResponseBuilder response = Response.ok(respuestaServicioEnvioCorreo.getObjetoRespuesta());

            response.header("Content-Disposition", "attachment; filename=\"carta_bienvenida_pdf.pdf\"");

            return response.build();

        } else {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuestaServicioEnvioCorreo).build();

        }

    }

    @POST
    @Path("enviarCorreo")
    @ApiOperation(value = "Enviar Correo", nickname = "enviar correo", notes = "Recurso que permite el envio de correo de la plantilla", response = RespuestaServicio.class, tags = {
            "enviar-correo"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + enconding)
    public Response enviarCorreo(@Context HttpServletRequest requestContext, @HeaderParam("token") String token,
                                 @HeaderParam("sessionId") String sessionId,
                                 List<Parametro> listaParametros) {


        if (sessionId != null) {
            ThreadContext.put("sessionId", sessionId);
        } else {
            ThreadContext.put("sessionId", "");
        }
        LOGGER.info("Inicio de microservicio " + ConstantesGeneracionPDF.enviarCorreo);

        RespuestaServicio respuestaServicio = null;
        RespuestaServicioEnvioCorreo respuestaServicioEnvioCorreo = null;

        try {
            if (null == listaParametros)
                return Response.status(500).entity(new RespuestaServicio(CodigosRespuestaServicios.DDOC_010.getCodigo(),
                        CodigosRespuestaServicios.DDOC_010.getDescripcion())).build();

            respuestaServicioEnvioCorreo = envioCorreoService.enviarCorreo(listaParametros, sessionId);

            if (respuestaServicioEnvioCorreo.getEstadoRespuesta()) {

                respuestaServicio = new RespuestaServicio();
                respuestaServicio.setCodigo(CodigosRespuestaServicios.ECOR_004.getCodigo());
                respuestaServicio.setDescripcion(CodigosRespuestaServicios.ECOR_004.getDescripcion());

                return Response.status(Response.Status.OK).entity(respuestaServicio).build();

            } else {
                respuestaServicio = new RespuestaServicio();
                respuestaServicio.setCodigo(respuestaServicioEnvioCorreo.getCodigo());
                respuestaServicio.setDescripcion(respuestaServicioEnvioCorreo.getDescripcion());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuestaServicio).build();
            }
        } catch (Exception e) {
            LOGGER.error("Error enviando correo: " + Constant.DOS_PUNTOS.getValor()
                    + Constant.ESPACIO + e.getMessage());
            respuestaServicio = new RespuestaServicio();
            respuestaServicio.setCodigo(CodigosRespuestaServicios.ECOR_001.getCodigo());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuestaServicio).build();
        }

    }

    @GET
    @Path("health")
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + enconding)
    public Response health() {
        return Response.status(Status.OK).entity("Health OK").build();
    }


}
