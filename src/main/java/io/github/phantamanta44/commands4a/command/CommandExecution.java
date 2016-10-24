package io.github.phantamanta44.commands4a.command;

import io.github.phantamanta44.commands4a.annot.Command;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class CommandExecution<T extends ICommandContext> {

    private final Method executor;
    private final Command command;
    private final Parameter[] params;

    public CommandExecution(Method executor) {
        this.executor = executor;
        this.command = executor.getAnnotation(Command.class);
        this.params = executor.getParameters();
    }

}
