package nl.futureedge.maven.docker.executor;

/**
 * Executor factory.
 */
public interface DockerExecutorFactory {

    /**
     * Create a docker executor.
     * @return docker executor
     */
    DockerExecutor create();
}
