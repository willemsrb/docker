package nl.futureedge.maven.docker.support;

public interface CreateNetworkSettings extends DockerSettings {

    String getNetworkOptions();

    String getNetworkName();
}
