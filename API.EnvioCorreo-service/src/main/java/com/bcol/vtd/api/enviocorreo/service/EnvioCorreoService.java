package com.bcol.vtd.api.enviocorreo.service;


import domain.model.Parametro;
import domain.model.RespuestaServicioEnvioCorreo;

import java.util.List;


public interface EnvioCorreoService {

    RespuestaServicioEnvioCorreo getDocumento(List<Parametro> listaParametros, String idSesion);

    RespuestaServicioEnvioCorreo enviarCorreo(List<Parametro> listaParametros, String idSesion);

}
