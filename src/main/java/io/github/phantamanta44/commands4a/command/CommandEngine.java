package io.github.phantamanta44.commands4a.command;

import io.github.phantamanta44.commands4a.exception.InvalidSyntaxException;
import io.github.phantamanta44.commands4a.exception.NoSuchCommandException;

import java.util.Arrays;
import java.util.function.Predicate;

public abstract class CommandEngine<T extends ICommandContext> {

    public void scan() {
        // TODO Implement
    }

    public void scan(String... packages) {
        // TODO Implement
    }

    public void execute(T context, String command) throws NoSuchCommandException, InvalidSyntaxException {
        if (command == null)
            throw new NullPointerException();
        if (command.length() == 0)
            throw new NoSuchCommandException();
        if (command.trim().indexOf(' ') == -1) {
            execute(context, command.trim(), new String[0]);
        } else {
            String[] parts = command.trim().split(" +");
            execute(context, parts[0], Arrays.copyOfRange(parts, 1, parts.length));
        }
    }

    abstract void execute(T context, String command, String[] args) throws NoSuchCommandException, InvalidSyntaxException;

    abstract Prerequisite<T> resolvePrereq(String prereq);

}
