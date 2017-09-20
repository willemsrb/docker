package nl.futureedge.maven.docker.executor;

import java.util.ArrayList;
import java.util.List;
import nl.futureedge.maven.docker.exception.DockerExecutionException;

/**
 * Docker commands.
 */
public final class DockerCommands {

    private DockerCommands() {
    }

    public static List<String> listContainers(final DockerExecutor executor, String filter) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("ps");
        arguments.add("-a");
        arguments.add("-q");
        if ((filter != null) && !"".equals(filter)) {
            arguments.add("--filter");
            arguments.add(filter);
        }
        arguments.add("--format");
        arguments.add("{{.Names}}");

        return executor.execute(arguments, false, true);
    }

    public static void startContainer(final DockerExecutor executor, final String container) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("start");
        arguments.add(container);

        executor.execute(arguments, false, false);
    }

    public static void stopContainer(final DockerExecutor executor, final String container) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("stop");
        arguments.add(container);

        executor.execute(arguments, false, false);
    }

    public static void removeContainer(final DockerExecutor executor, final String container) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("rm");
        arguments.add("-vf");
        arguments.add(container);

        executor.execute(arguments, false, false);
    }

    public static List<String> listImages(final DockerExecutor executor, final String filter) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("images");
        arguments.add("-q");
        if ((filter != null) && !"".equals(filter)) {
            arguments.add("--filter");
            arguments.add(filter);
        }

        return executor.execute(arguments, false, true);
    }

    public static void removeImage(final DockerExecutor executor, final String image) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("rmi");
        arguments.add("-f");
        arguments.add(image);

        executor.execute(arguments, false, false);
    }

    public static List<String> listServices(final DockerExecutor executor, final String filter) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("service");
        arguments.add("ls");
        arguments.add("-q");
        if ((filter != null) && !"".equals(filter)) {
            arguments.add("--filter");
            arguments.add(filter);
        }

        return executor.execute(arguments, false, true);
    }

    public static void removeService(final DockerExecutor executor, final String service) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("service");
        arguments.add("rm");
        arguments.add(service);

        executor.execute(arguments, false, false);
    }

    public static List<String> listVolumes(final DockerExecutor executor, final String filter) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("volume");
        arguments.add("ls");
        arguments.add("-q");
        if ((filter != null) && !"".equals(filter)) {
            arguments.add("--filter");
            arguments.add(filter);
        }

        return executor.execute(arguments, false, true);
    }

    public static void removeVolume(final DockerExecutor executor, final String volume) throws DockerExecutionException {
        final List<String> arguments = new ArrayList<>();
        arguments.add("volume");
        arguments.add("rm");
        arguments.add(volume);

        executor.execute(arguments, false, false);
    }
}
