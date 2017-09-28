package nl.futureedge.maven.docker.support;

import nl.futureedge.maven.docker.executor.DockerExecutor;
import nl.futureedge.maven.docker.executor.DockerExecutorFactory;

public final class StaticDockerExecutorFactory implements DockerExecutorFactory {

    private final DockerExecutor executor;

    StaticDockerExecutorFactory(DockerExecutor executor) {
        this.executor = executor;
    }

    @Override
    public DockerExecutor create() {
        return executor;
    }
}
