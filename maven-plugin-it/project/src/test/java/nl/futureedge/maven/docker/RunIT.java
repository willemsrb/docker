package nl.futureedge.maven.docker;

import nl.futureedge.maven.docker.exception.DockerException;
import nl.futureedge.maven.docker.support.RunConfigurationExecutable;
import nl.futureedge.maven.docker.support.RunConfigurationSettings;
import org.junit.Test;

public class RunIT {

    @Test
    public void test() throws DockerException {
        final RunConfigurationSettings settings = RunConfigurationSettings.builder()
                .setDebugLogger(System.out::println)
                .setNetworkName(System.getProperty("docker.network"))
                .setConfigurationName("ping")
                .build();
        final RunConfigurationExecutable executable = new RunConfigurationExecutable(settings);
        executable.execute();
    }
}
