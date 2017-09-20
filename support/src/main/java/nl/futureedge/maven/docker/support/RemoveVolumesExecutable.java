package nl.futureedge.maven.docker.support;

import java.util.List;
import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.executor.DockerCommands;
import nl.futureedge.maven.docker.executor.DockerExecutor;

public final class RemoveVolumesExecutable extends DockerExecutable {

    private final String filter;

    public RemoveVolumesExecutable(final RemoveVolumesSettings settings) {
        super(settings);

        this.filter = settings.getFilter();
    }

    public void execute() throws DockerException {
        debug("Remove volumes configuration: ");
        debug("- filter: " + filter);

        final DockerExecutor executor = createDockerExecutor();
        final List<String> volumes = doIgnoringFailure(() -> DockerCommands.listVolumes(executor, filter));

        if (volumes != null) {
            for (final String volume : volumes) {
                if ("".equals(volume.trim())) {
                    continue;
                }
                info("Remove volume: " + volume);
                doIgnoringFailure(() -> DockerCommands.removeVolume(executor, volume));
            }
        }
    }
}
