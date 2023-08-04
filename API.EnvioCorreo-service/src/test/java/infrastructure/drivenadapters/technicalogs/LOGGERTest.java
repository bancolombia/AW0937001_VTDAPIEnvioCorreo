package infrastructure.drivenadapters.technicalogs;

import org.junit.Test;

public class LOGGERTest {

    LOGGER log = new LOGGER();

    @Test
    public void testErrorClassOfQString() {
        log.error(this.getClass(), "Test Error");
    }

    @Test
    public void testErrorClassOfQStringThrowable() {
        log.error(this.getClass(), "Test Error", new Exception());
    }

    @Test
    public void testInfo() {
        log.info(getClass(), "Info");
    }

    @Test
    public void testDebugClassOfQStringObject() {
        log.debug(getClass(), "Debug");
    }

    @Test
    public void testDebugClassOfQString() {
        log.debug(getClass(), "debug2", new Exception());
    }

    @Test
    public void testTrace() {
        log.trace(getClass(), "trace");
    }

    @Test
    public void testWarn() {
        log.warn(getClass(), "warn");
    }

}
