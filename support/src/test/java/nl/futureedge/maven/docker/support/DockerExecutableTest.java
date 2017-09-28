package nl.futureedge.maven.docker.support;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class DockerExecutableTest {

    private List<String> debugs = new ArrayList<>();
    private List<String> infos = new ArrayList<>();
    private List<String> warns = new ArrayList<>();
    private List<Exception> warnExceptions = new ArrayList<>();

    void debug(final String message) {
        debugs.add(message);
    }

    void info(final String message) {
        infos.add(message);
    }

    void warn(final String message, final Exception exception) {
        warns.add(message);
        warnExceptions.add(exception);
    }

    @Test
    public void test() throws DockerException {
        final DockerExecutor executor = Mockito.mock(DockerExecutor.class);

        final Consumer<String> debugLogger = this::debug;
        final Consumer<String> infoLogger = this::info;
        final BiConsumer<String, Exception> warnLogger = this::warn;

        final TestSettings settings = TestSettings.builder()
                .setDebugLogger(debugLogger)
                .setInfoLogger(infoLogger)
                .setWarnLogger(warnLogger)
                .setDockerOptions("docker options")
                .setIgnoreFailures(true)
                .build();

        final TestExecutable subject = new TestExecutable(settings);
        subject.setFactory(new StaticDockerExecutorFactory(executor));
        Assert.assertSame(executor, subject.createDockerExecutor());
        subject.execute();

        Assert.assertEquals(1, debugs.size());
        Assert.assertEquals("debug", debugs.get(0));
        Assert.assertEquals(1, infos.size());
        Assert.assertEquals("info", infos.get(0));
        Assert.assertEquals(2, warns.size());
        Assert.assertEquals("warn", warns.get(0));
        Assert.assertEquals("warn-with-exception", warns.get(1));
        Assert.assertEquals(2, warnExceptions.size());
        Assert.assertEquals(null, warnExceptions.get(0));
        Assert.assertEquals(IllegalArgumentException.class, warnExceptions.get(1).getClass());

        Mockito.verifyNoMoreInteractions(executor);
    }

    @Test(expected = DockerExecutionException.class)
    public void testFail() throws DockerException {
        final TestSettings settings = TestSettings.builder()
                .setIgnoreFailures(false)
                .build();

        final TestExecutable subject = new TestExecutable(settings);
        subject.execute();
    }

    public static class TestExecutable extends DockerExecutable {

        public TestExecutable(final TestSettings settings) {
            super(settings);
        }

        @Override
        public void execute() throws DockerException {
            debug("debug");
            info("info");
            warn("warn");
            warn("warn-with-exception", new IllegalArgumentException());

            doIgnoringFailure(() -> executeWithoutResult());
            doIgnoringFailure(() -> executeWithResult());
        }

        private void executeWithoutResult() throws DockerExecutionException {
            throw new DockerExecutionException("Fail!");
        }

        private Object executeWithResult() throws DockerExecutionException {
            throw new DockerExecutionException("Fail!");
        }
    }

    public static interface TestSettings extends DockerSettings {

        static Builder builder() {
            return new Builder();
        }

        final class Builder extends DockerSettings.Builder<Builder> {

            protected Builder() {
                super();
                super.setBuilder(this);
            }

            public TestSettings build() {
                return new TestSettingsImpl(this);
            }
        }

        final class TestSettingsImpl extends DockerSettingsImpl implements TestSettings {

            protected TestSettingsImpl(final TestSettings.Builder builder) {
                super(builder);
            }
        }
    }
}
