package io.github.phantamanta44.commands4a.exception;

public class NoSuchCommandException extends Exception {

    private String command;

    public NoSuchCommandException() {
        super("No command provided!");
        this.command = null;
    }

    public NoSuchCommandException(String command) {
        super("No such command: " + command);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

}
