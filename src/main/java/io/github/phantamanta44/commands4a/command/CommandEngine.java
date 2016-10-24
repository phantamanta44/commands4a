package io.github.phantamanta44.commands4a.command;

import io.github.phantamanta44.commands4a.annot.Command;
import io.github.phantamanta44.commands4a.exception.InvalidSyntaxException;
import io.github.phantamanta44.commands4a.exception.NoSuchCommandException;
import io.github.phantamanta44.nomreflect.MethodFilter;
import io.github.phantamanta44.nomreflect.Reflect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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
