package domain.model;

public class RespuestaServicioEnvioCorreo extends RespuestaServicio {
    private static final long serialVersionUID = 1L;
    private Object objetoRespuesta;
    private boolean estadoRespuesta;

    public RespuestaServicioEnvioCorreo() {
    }

    public RespuestaServicioEnvioCorreo(String codigo, String descripcion, Object objetoRespuesta, boolean estadoRespuesta) {
        super(codigo, descripcion);
        this.objetoRespuesta = objetoRespuesta;
        this.estadoRespuesta = estadoRespuesta;
    }

    public RespuestaServicioEnvioCorreo(Object objetoRespuesta, boolean estadoRespuesta) {
        this.objetoRespuesta = objetoRespuesta;
        this.estadoRespuesta = estadoRespuesta;
    }

    public Object getObjetoRespuesta() {
        return this.objetoRespuesta;
    }

    public void setObjetoRespuesta(Object objetoRespuesta) {
        this.objetoRespuesta = objetoRespuesta;
    }

    public boolean getEstadoRespuesta() {
        return this.estadoRespuesta;
    }

    public void setEstadoRespuesta(boolean estadoRespuesta) {
        this.estadoRespuesta = estadoRespuesta;
    }

    public void setError(String codigo, String descripcion) {
        this.setCodigo(codigo);
        this.setDescripcion(descripcion);
        this.estadoRespuesta = false;
    }
}

