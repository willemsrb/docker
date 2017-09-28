package nl.futureedge.maven.docker.executor;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Default docker executor factory.
 */
public final class DockerExecutorFactoryImpl implements DockerExecutorFactory {

    private final Consumer<String> debugLogger;
    private final Consumer<String> infoLogger;
    private final BiConsumer<String, Exception> warnLogger;
    private final String dockerOptions;

    /**
     * Create a new docker executor factory.
     * @param debugLogger logger for debug messages
     * @param infoLogger logger for informational messages
     * @param warnLogger logger for warning messages
     * @param dockerOptions docker options
     */
    public DockerExecutorFactoryImpl(final Consumer<String> debugLogger, final Consumer<String> infoLogger, final BiConsumer<String, Exception> warnLogger,
                                     final String dockerOptions) {
        this.debugLogger = debugLogger;
        this.infoLogger = infoLogger;
        this.warnLogger = warnLogger;
        this.dockerOptions = dockerOptions;
    }

    @Override
    public DockerExecutor create() {
        return new DockerExecutorImpl(debugLogger, infoLogger, warnLogger, dockerOptions);
    }
}
