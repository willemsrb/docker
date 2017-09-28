package nl.futureedge.maven.docker.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import nl.futureedge.maven.docker.exception.DockerExecutionException;

/**
 * Docker command executor.
 */
public final class DockerExecutor {

    private final Consumer<String> debugLogger;
    private final Consumer<String> infoLogger;
    private final BiConsumer<String, Exception> warnLogger;
    private final List<String> baseCommand;

    /**
     * Constructor.
     * @param debugLogger logger for debug messages
     * @param infoLogger logger for informational messages
     * @param warnLogger logger for warning messages
     * @param dockerOptions options for the docker command
     */
    public DockerExecutor(final Consumer<String> debugLogger, final Consumer<String> infoLogger, final BiConsumer<String, Exception> warnLogger,
                          final String dockerOptions) {
        this.debugLogger = debugLogger;
        this.infoLogger = infoLogger;
        this.warnLogger = warnLogger;

        baseCommand = new ArrayList<>();
        baseCommand.add("docker");
        baseCommand.addAll(Docker.splitOptions(dockerOptions));
    }

    /**
     * Execute a docker command.
     * @param arguments arguments (should not contain docker only the arguments)
     * @throws DockerExecutionException on failures
     */
    public void execute(final List<String> arguments) throws DockerExecutionException {
        execute(arguments, true, false);
    }

    /**
     * Execute a docker command.
     * @param arguments arguments (should not contain docker only the arguments)
     * @param logOut true, if output from StdOut should be logged
     * @param returnOut true if output from StdOut should be returned
     * @return the output
     * @throws DockerExecutionException on failures
     */
    public List<String> execute(final List<String> arguments, final boolean logOut, final boolean returnOut) throws DockerExecutionException {
        final List<String> command = new ArrayList<>(baseCommand);
        command.addAll(arguments);
        debugLogger.accept(String.format("Docker command: %s", command));

        // Bouw proces
        final ProcessBuilder pb = new ProcessBuilder(command);
        final Process process;
        try {
            process = pb.start();
        } catch (final IOException e) {
            throw new DockerExecutionException("Could not execute Docker command", e);
        }

        try {
            return executeProcess(process, logOut, returnOut);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    private List<String> executeProcess(final Process process, final boolean logOut, final boolean returnOut)
            throws DockerExecutionException, InterruptedException {
        try (final BufferedReader procesStdOut = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
             final BufferedReader procesStdErr = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"))) {

            final List<String> output = new ArrayList<>();
            final Thread stdOutReader = new Thread(new StdOutReader(procesStdOut, logOut, returnOut ? output : null));
            stdOutReader.start();

            final Thread stdErrReader = new Thread(new StdErrReader(procesStdErr));
            stdErrReader.start();

            final int resultCode = process.waitFor();
            debugLogger.accept(String.format("Result of docker process: %s", resultCode));
            if (resultCode != 0) {
                throw new DockerExecutionException("Docker command failed");
            }

            stdOutReader.join();
            stdErrReader.join();

            return output;
        } catch (final IOException e) {
            throw new DockerExecutionException("Unexpected exception in StdOut of StdErr during execution of Docker command", e);
        }
    }

    /**
     * StdOut reader.
     */
    private final class StdOutReader implements Runnable {

        private final BufferedReader procesStdOut;
        private final boolean logOut;
        private final List<String> output;

        /**
         * Constructor.
         * @param procesStdOut StdOut
         * @param output output, can be null if output shouldn't be appended
         */
        public StdOutReader(final BufferedReader procesStdOut, final boolean logOut, final List<String> output) {
            this.procesStdOut = procesStdOut;
            this.logOut = logOut;
            this.output = output;
        }

        @Override
        public void run() {
            String line;
            try {
                while ((line = procesStdOut.readLine()) != null) {
                    if (logOut) {
                        infoLogger.accept(line);
                    } else {
                        debugLogger.accept(line);
                    }

                    if (output != null) {
                        output.add(line);
                    }
                }
            } catch (final IOException e) {
                warnLogger.accept("Unexpected exception during read of StdOut", e);
            }
        }
    }

    /**
     * StdErr reader.
     */
    private final class StdErrReader implements Runnable {
        private final BufferedReader procesStdErr;

        /**
         * Constructor.
         * @param procesStdErr StdErr
         */
        public StdErrReader(final BufferedReader procesStdErr) {
            this.procesStdErr = procesStdErr;
        }

        @Override
        public void run() {
            String line;
            try {
                while ((line = procesStdErr.readLine()) != null) {
                    warnLogger.accept(line, null);
                }
            } catch (final IOException e) {
                warnLogger.accept("Unexpected exception during read of StdErr", e);
            }
        }
    }

}
