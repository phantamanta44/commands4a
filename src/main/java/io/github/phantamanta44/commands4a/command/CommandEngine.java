package io.github.phantamanta44.commands4a.command;

import io.github.phantamanta44.commands4a.annot.Command;
import io.github.phantamanta44.commands4a.annot.Omittable;
import io.github.phantamanta44.commands4a.args.IArgumentTokenizer;
import io.github.phantamanta44.commands4a.exception.CommandExecutionException;
import io.github.phantamanta44.commands4a.exception.InvalidSyntaxException;
import io.github.phantamanta44.commands4a.exception.NoSuchCommandException;
import io.github.phantamanta44.commands4a.exception.PrereqNotMetException;
import io.github.phantamanta44.nomreflect.MethodFilter;
import io.github.phantamanta44.nomreflect.Reflect;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CommandEngine<T extends ICommandContext> {

    private List<CommandExecution<T>> commands;
    private final Map<String, Integer> aliasMap = new HashMap<>();

    public void scan() {
        if (commands == null)
            scan(Reflect.methods());
        else
            throw new IllegalStateException("Commands already scanned!");
    }

    public void scan(String... packages) {
        if (commands == null)
            scan(Reflect.methods(packages));
        else
            throw new IllegalStateException("Commands already scanned!");
    }

    private void scan(MethodFilter methods) {
        commands = methods.tagged(Command.class).find().stream()
                .map(m -> new CommandExecution<>(m, this))
                .collect(Collectors.toList());
        for (int i = 0; i < commands.size(); i++) {
            for (String alias : commands.get(i).getAliases())
                aliasMap.put(alias, i);
            aliasMap.put(commands.get(i).getCommand().name(), i);
        }
    }

    public void execute(T context, String command) throws NoSuchCommandException, InvalidSyntaxException, PrereqNotMetException {
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

    public void execute(T context, String cmdName, String[] args) throws NoSuchCommandException, InvalidSyntaxException, PrereqNotMetException {
        if (!aliasMap.containsKey(cmdName))
            throw new NoSuchCommandException(cmdName);
        CommandExecution<T> command = commands.get(aliasMap.get(cmdName));
        for (Prerequisite<T> prereq : command.getPrereqs()) {
            if (!prereq.test(context))
                throw new PrereqNotMetException(prereq);
        }
        Object[] params = new Object[command.getParams().length];
        IArgumentTokenizer tokenizer = tokenize(args, context);
        for (int i = 0; i < params.length; i++) {
            if (command.getParams()[i].getType() == String[].class)
                params[i] = args;
            else if (ICommandContext.class.isAssignableFrom(command.getParams()[i].getType()))
                params[i] = context;
            else {
                try {
                    params[i] = tokenizer.nextOfType(command.getParams()[i].getType());
                } catch (InvalidSyntaxException e) {
                    if (!command.getParams()[i].isAnnotationPresent(Omittable.class))
                        throw e;
                }
            }
        }
        try {
            command.getExecutor().invoke(null, params);
        } catch (InvocationTargetException e) {
            throw new CommandExecutionException(e.getCause());
        } catch (IllegalAccessException ignored) { }
    }

    protected abstract Prerequisite<T> resolvePrereq(String prereq);

    protected abstract IArgumentTokenizer tokenize(String[] args, T context);

}
