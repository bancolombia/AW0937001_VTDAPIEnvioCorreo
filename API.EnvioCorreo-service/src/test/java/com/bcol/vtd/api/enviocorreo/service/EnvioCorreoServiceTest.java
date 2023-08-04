package com.bcol.vtd.api.enviocorreo.service;

import com.bcol.vtd.api.enviocorreo.util.ConstantesGeneracionPDF;
import com.bcol.vtd.api.enviocorreo.util.PlantillasUtil;
import domain.model.Parametro;
import domain.model.RespuestaServicioEnvioCorreo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import util.UtilTestSM;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


public class EnvioCorreoServiceTest {


    @Mock
    PlantillasUtil plantillasUtil;

    @InjectMocks
    EnvioCorreoServiceImpl envioCorreoServiceImpl = new EnvioCorreoServiceImpl();

    @Before
    public void initialice() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getDocumentTest() {
        //Arrange

        List<Parametro> parametros = UtilTestSM.getListaParametros();

        parametros.add(new Parametro(ConstantesGeneracionPDF.NOMBRE_PLANTILLA_PDF,
                ConstantesGeneracionPDF.NOMBRE_PLANTILLA_PDF));

        //Act
        RespuestaServicioEnvioCorreo respuesta = envioCorreoServiceImpl.getDocumento(parametros, "asdasdasdasdas234324324");

        //Assert
        if (respuesta != null && respuesta.getEstadoRespuesta()) {

            assertNotNull(respuesta.getObjetoRespuesta());

        } else {

            assertNotNull(respuesta);

        }

    }

    @Test
    public void enviarCorreoTest() {

        //Arrange
        List<Parametro> parametros = UtilTestSM.getListaParametros();

        parametros.add(new Parametro("FWK_ENVIAR_CORREO_ASUNTO",
                ConstantesGeneracionPDF.ASUNTO_CORREO));

        // Act
        RespuestaServicioEnvioCorreo respuesta = new RespuestaServicioEnvioCorreo();
        respuesta.setEstadoRespuesta(true);
        String idSesion = "asdasdasdasdas234324324";
        when(plantillasUtil.build(parametros)).thenReturn(true);

        respuesta = envioCorreoServiceImpl.enviarCorreo(parametros, idSesion);

        //Assert
        assertTrue(respuesta.getEstadoRespuesta());


    }


    @Test
    public void enviarCorreoFalseTest() {

        //Arrange
        List<Parametro> parametros = UtilTestSM.getListaParametros();

        parametros.add(new Parametro("FWK_ENVIAR_CORREO_ASUNTO",
                ConstantesGeneracionPDF.ASUNTO_CORREO));

        // Act
        RespuestaServicioEnvioCorreo respuesta = new RespuestaServicioEnvioCorreo();
        respuesta.setEstadoRespuesta(true);
        String idSesion = "asdasdasdasdas234324324";
        when(plantillasUtil.build(parametros)).thenReturn(false);

        respuesta = envioCorreoServiceImpl.enviarCorreo(parametros, idSesion);

        //Assert
        assertNotNull(respuesta.getCodigo());
    }

}
