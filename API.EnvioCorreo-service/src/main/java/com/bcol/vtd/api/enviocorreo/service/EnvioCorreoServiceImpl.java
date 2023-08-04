package com.bcol.vtd.api.enviocorreo.service;

import com.bcol.vtd.api.enviocorreo.util.CodigosRespuestaServicios;
import com.bcol.vtd.api.enviocorreo.util.PlantillasUtil;
import domain.model.Parametro;
import domain.model.RespuestaServicioEnvioCorreo;
import infrastructure.drivenadapters.technicalogs.LOGGER;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named("envioCorreoService")
public class EnvioCorreoServiceImpl implements EnvioCorreoService {

    @Inject
    PlantillasUtil plantillasUtil;

    /**
     * Metodo que inicializa la injeccion
     */
    public EnvioCorreoServiceImpl() {
    }

    @Override
    public RespuestaServicioEnvioCorreo getDocumento(List<Parametro> listaParametros, String idSesion) {

        PlantillasUtil plantillasUtil = new PlantillasUtil(listaParametros, idSesion);

        plantillasUtil.createFile();

        RespuestaServicioEnvioCorreo respuestaServicioEnvioCorreo = plantillasUtil.getRespuestaServicioEnvioCorreo();

        if (respuestaServicioEnvioCorreo.getEstadoRespuesta()) {

            respuestaServicioEnvioCorreo.setObjetoRespuesta(plantillasUtil.getFile());

        }

        return respuestaServicioEnvioCorreo;
    }


    @Override
    public RespuestaServicioEnvioCorreo enviarCorreo(List<Parametro> listaParametros, String idSesion) {
        RespuestaServicioEnvioCorreo respuestaServicioEnvioCorreo = new RespuestaServicioEnvioCorreo();
        boolean sended = plantillasUtil.build(listaParametros);
        if (!sended) {
            respuestaServicioEnvioCorreo.setError(CodigosRespuestaServicios.DDOC_006.getCodigo(),
                    CodigosRespuestaServicios.DDOC_006.getDescripcion());
            LOGGER.error(this.getClass(), CodigosRespuestaServicios.DDOC_006.getCodigo());
        }
        respuestaServicioEnvioCorreo.setEstadoRespuesta(sended);
        return respuestaServicioEnvioCorreo;
    }

}
