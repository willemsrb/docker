package nl.futureedge.maven.docker.support;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

public interface RunConfigurationSettings extends DockerSettings {

    default Stack<String> getStack() {
        return new Stack<>();
    }

    default Set<String> getLoaded() {
        return new HashSet<>();
    }

    Properties getProjectProperties();

    String getConfigurationName();

    String getNetworkName();

    boolean isRandomPorts();
}
