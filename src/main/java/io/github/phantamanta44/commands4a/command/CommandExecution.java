package io.github.phantamanta44.commands4a.command;

import io.github.phantamanta44.commands4a.annot.Alias;
import io.github.phantamanta44.commands4a.annot.Command;
import io.github.phantamanta44.commands4a.annot.Desc;
import io.github.phantamanta44.commands4a.annot.Prereq;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CommandExecution<T extends ICommandContext> {

    private final Method executor;
    private final Command command;
    private final String description;
    private final String[] aliases;
    private final Collection<Prerequisite<T>> prereqs;
    private final Parameter[] params;

    public CommandExecution(Method executor, CommandEngine<T> engine) {
        this.executor = executor;
        this.executor.setAccessible(true);
        this.command = executor.getAnnotation(Command.class);
        Desc descAnnot = executor.getAnnotation(Desc.class);
        this.description = descAnnot != null ? descAnnot.value() : "No description.";
        if (executor.isAnnotationPresent(Alias.MultiAlias.class))
            this.aliases = Arrays.stream(executor.getAnnotation(Alias.MultiAlias.class).value())
                    .map(Alias::value).toArray(String[]::new);
        else if (executor.isAnnotationPresent(Alias.class))
            this.aliases = new String[] { executor.getAnnotation(Alias.class).value() };
        else
            this.aliases = new String[0];
        if (executor.isAnnotationPresent(Prereq.MultiPrereq.class))
            this.prereqs = Arrays.stream(executor.getAnnotation(Prereq.MultiPrereq.class).value())
                    .map(Prereq::value).map(engine::resolvePrereq).collect(Collectors.toList());
        else if (executor.isAnnotationPresent(Prereq.class))
            this.prereqs = Collections.singletonList(engine.resolvePrereq(executor.getAnnotation(Prereq.class).value()));
        else
            this.prereqs = Collections.emptyList();
        this.params = executor.getParameters();
    }

    public Method getExecutor() {
        return executor;
    }

    public Command getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Collection<Prerequisite<T>> getPrereqs() {
        return prereqs;
    }

    public Parameter[] getParams() {
        return params;
    }

}
