package infrastructure.drivenadapters.technicalogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

/**
 *
 */

public class LOGGER implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(LOGGER.class);

    public static void error(final Class<?> thisClass, final String srcErrorMessage) {
        error(thisClass, srcErrorMessage, null);
    }

    /**
     * Log Error
     *
     * @param thisClass
     * @param srcErrorMessage
     * @param srcException
     */
    public static void error(final Class<?> thisClass, final String srcErrorMessage,
                             final Throwable srcException) {
        String errorMessage = "Error en [" + thisClass.getCanonicalName() + "]";
        if (srcErrorMessage != null) {
            errorMessage = errorMessage + " " + srcErrorMessage;
        }
        Exception exception;
        if (srcException == null) {
            logger.error(errorMessage);
        } else {
            exception = new Exception(errorMessage, srcException);
            logger.error(errorMessage, exception);
        }
    }

    /**
     * Metodo que permite ingresar un mensaje informativo al log.
     *
     * @param thisClass   Clase en la que se genera el error.
     * @param infoMessage Mensaje.
     */
    public static void info(final Class<?> thisClass, final String infoMessage) {
        String message = "INFO en [" + thisClass.getCanonicalName() + "]";
        if (infoMessage != null) {
            message = message + " " + infoMessage;
        }
        logger.info(message);
    }

    /**
     * Metodo que permite ingresar un mensaje informativo al log.
     *
     * @param thisClass Clase en la que se genera el error.
     * @param message   Mensaje.
     */
    public static void debug(final Class<?> thisClass, String message, final Object object) {
        if (object != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                String objectString = mapper.writeValueAsString(object);
                message = message + "################## " + objectString + " ####################\n\n";
                debug(thisClass, message);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static void debug(final Class<?> thisClass, final String infoMessage) {
        String message = "DEBUG en [" + thisClass.getCanonicalName() + "]";
        if (infoMessage != null) {
            message = message + " " + infoMessage;
        }
        logger.debug(message);
    }

    public static void trace(final Class<?> thisClass, final String infoMessage) {
        String message = "TRACE en [" + thisClass.getCanonicalName() + "]";
        if (infoMessage != null) {
            message = message + " " + infoMessage;
        }
        logger.trace(message);
    }

    public static void warn(final Class<?> thisClass, final String infoMessage) {
        String message = "WARN en [" + thisClass.getCanonicalName() + "]";
        if (infoMessage != null) {
            message = message + " " + infoMessage;
        }
        logger.warn(message);
    }
}
