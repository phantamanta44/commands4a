package io.github.phantamanta44.commands4a.exception;

public class InvalidSyntaxException extends Exception {

    private String[] args;
    private String reason;

    public InvalidSyntaxException(String[] args, String reason) {
        super("Invalid syntax: " + reason);
        this.args = args;
        this.reason = reason;
    }

    public String[] getArgs() {
        return args;
    }

    public String getReason() {
        return reason;
    }

}
