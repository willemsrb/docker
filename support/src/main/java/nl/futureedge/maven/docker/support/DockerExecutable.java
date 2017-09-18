package nl.futureedge.maven.docker.support;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import nl.futureedge.maven.docker.executor.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public abstract class DockerExecutable {

    private final Consumer<String> debugLogger;
    private final Consumer<String> infoLogger;
    private final BiConsumer<String, Exception> warnLogger;

    private final String dockerOptions;
    private final boolean ignoreFailures;

    protected DockerExecutable(final DockerSettings settings) {
        this.debugLogger = settings.getDebugLogger();
        this.infoLogger = settings.getInfoLogger();
        this.warnLogger = settings.getWarnLogger();
        this.dockerOptions = settings.getDockerOptions();
        this.ignoreFailures = settings.isIgnoreFailures();
    }

    protected final void debug(final String message) {
        debugLogger.accept(message);
    }

    protected final void info(final String message) {
        infoLogger.accept(message);
    }

    protected final void warn(final String message) {
        warnLogger.accept(message, null);
    }

    protected final void warn(final String message, final Exception exception) {
        warnLogger.accept(message, exception);
    }


    protected final DockerExecutor createDockerExecutor() {
        return new DockerExecutor(debugLogger, infoLogger, warnLogger, dockerOptions);
    }

    protected final <E extends Exception> void doIgnoringFailure(final Execution<E> execution) throws E, DockerExecutionException {
        try {
            execution.execute();
        } catch (final DockerExecutionException e) {
            if (!ignoreFailures) {
                throw e;
            }
        }
    }

    protected final <T, E extends Exception> T doIgnoringFailure(final Command<T, E> command) throws E, DockerExecutionException {
        try {
            return command.execute();
        } catch (final DockerExecutionException e) {
            if (!ignoreFailures) {
                throw e;
            }

            return null;
        }
    }

    public abstract void execute() throws DockerExecutionException;

    @FunctionalInterface
    protected interface Command<T, E extends Exception> {
        T execute() throws DockerExecutionException, E;
    }

    @FunctionalInterface
    protected interface Execution<E extends Exception> {
        void execute() throws DockerExecutionException, E;
    }
}
