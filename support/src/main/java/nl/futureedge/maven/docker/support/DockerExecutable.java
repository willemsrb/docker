package nl.futureedge.maven.docker.support;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.exception.DockerExecutionException;
import nl.futureedge.maven.docker.executor.DockerExecutor;
import nl.futureedge.maven.docker.executor.DockerExecutorFactory;
import nl.futureedge.maven.docker.executor.DockerExecutorFactoryImpl;

/**
 * Base for all docker command executions.
 */
public abstract class DockerExecutable {

    private DockerExecutorFactory factory;

    private final Consumer<String> debugLogger;
    private final Consumer<String> infoLogger;
    private final BiConsumer<String, Exception> warnLogger;

    private final boolean ignoreFailures;

    /**
     * Create a new docker command execution.
     * @param settings settings.
     */
    protected DockerExecutable(final DockerSettings settings) {
        factory = new DockerExecutorFactoryImpl(settings.getDebugLogger(), settings.getInfoLogger(), settings.getWarnLogger(), settings.getDockerOptions());
        this.debugLogger = settings.getDebugLogger();
        this.infoLogger = settings.getInfoLogger();
        this.warnLogger = settings.getWarnLogger();

        this.ignoreFailures = settings.isIgnoreFailures();
    }

    /**
     * Set a new docker executor factory to override the default configured factory.
     * @param factory docker executor factory
     */
    void setFactory(final DockerExecutorFactory factory) {
        this.factory = factory;
    }

    /**
     * Get the docker executor factory.
     * @return docker executor factory
     */
    DockerExecutorFactory getFactory() {
        return factory;
    }

    /**
     * Log a debug message.
     * @param message message
     */
    protected final void debug(final String message) {
        debugLogger.accept(message);
    }

    /**
     * Log an informational message.
     * @param message message
     */
    protected final void info(final String message) {
        infoLogger.accept(message);
    }

    /**
     * Log a warning message.
     * @param message message
     */
    protected final void warn(final String message) {
        warnLogger.accept(message, null);
    }

    /**
     * Log a warning message.
     * @param message message
     * @param cause cause
     */
    protected final void warn(final String message, final Exception cause) {
        warnLogger.accept(message, cause);
    }

    /**
     * Create a docker executor.
     * @return docker executor
     */
    protected final DockerExecutor createDockerExecutor() {
        return factory.create();
    }

    /**
     * Execute functionality, optionally ignoring the exception.
     * @param execution functionality to execute
     * @param <E> exception type
     * @throws E exception
     * @throws DockerException exception
     */
    protected final <E extends Exception> void doIgnoringFailure(final Execution<E> execution) throws E, DockerException {
        try {
            execution.execute();
        } catch (final DockerExecutionException e) {
            if (!ignoreFailures) {
                throw e;
            }
        }
    }

    /**
     * Execute functionality, optionally ignoring the exception, returning the result.
     * @param command functionality to execute
     * @param <T> result type
     * @param <E> exception type
     * @return result
     * @throws E exception
     * @throws DockerException exception
     */
    protected final <T, E extends Exception> T doIgnoringFailure(final Command<T, E> command) throws E, DockerException {
        try {
            return command.execute();
        } catch (final DockerExecutionException e) {
            if (!ignoreFailures) {
                throw e;
            }

            return null;
        }
    }

    /**
     * Execute the docker command.
     */
    public abstract void execute() throws DockerException;

    /**
     * Functionality with result.
     * @param <T> result type
     * @param <E> exeception type
     */
    @FunctionalInterface
    protected interface Command<T, E extends Exception> {
        /**
         * Execute the functionality
         * @return result
         * @throws DockerException exception
         * @throws E exception
         */
        T execute() throws DockerException, E;
    }

    /**
     * Functionality without result.
     * @param <E> exeception type
     */
    @FunctionalInterface
    protected interface Execution<E extends Exception> {
        /**
         * Execute the functionality
         * @throws DockerException exception
         * @throws E exception
         */
        void execute() throws DockerException, E;
    }
}
