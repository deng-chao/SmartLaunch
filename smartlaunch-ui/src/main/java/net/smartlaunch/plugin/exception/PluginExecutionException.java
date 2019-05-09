package net.smartlaunch.plugin.exception;

public class PluginExecutionException extends RuntimeException {

    public PluginExecutionException(String message) {
        super(message);
    }

    public PluginExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
