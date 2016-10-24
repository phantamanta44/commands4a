package io.github.phantamanta44.commands4a;

import io.github.phantamanta44.commands4a.command.ICommandContext;
import io.github.phantamanta44.commands4a.command.CommandEngine;

public class EngineDescriptor<T extends ICommandContext, E extends CommandEngine<T>> {

    private final String name, className;

    public EngineDescriptor(String name, String className) {
        if (name == null)
            throw new NullPointerException();
        this.name = name;
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

}
