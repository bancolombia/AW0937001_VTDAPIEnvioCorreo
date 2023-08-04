package com.bcol.vtd.api.enviocorreo.util;

import domain.model.Parametro;
import infrastructure.drivenadapters.mail.ServiceMail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import util.UtilTestSM;

import java.io.File;
import java.util.List;

import static org.mockito.Mockito.mock;

public class PlantillasUtilTest {

    private static final Logger logger = LogManager.getLogger(PlantillasUtilTest.class);


    ServiceMail serviceMail = mock(ServiceMail.class);
    PlantillasUtil plantillasUtil;
    private List<Parametro> parametros;
    private CargadorPropiedades configuracion;

    @Before
    public void init() {

        UtilTestSM.setConfiguracion();
        plantillasUtil = new PlantillasUtil(serviceMail, CargadorPropiedades.getInstance().getPropiedades());

        String plantilla = "plantillacorreopdf.vm";

        parametros = UtilTestSM.getListaParametros1();


        File f = new File(getClass().getClassLoader().getResource(plantilla).getFile());


        String templateDir = f.getParent();

    }


}
