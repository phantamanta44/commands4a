package io.github.phantamanta44.commands4a;

import io.github.phantamanta44.commands4a.command.ICommandContext;
import io.github.phantamanta44.commands4a.command.CommandEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class CommandEngineProvider { // TODO Document everything

    private static final CommandEngineProvider INSTANCE = new CommandEngineProvider();

    public static <T extends ICommandContext, E extends CommandEngine<T>> E getEngine(EngineDescriptor<T, E> descriptor) {
        if (descriptor == null)
            throw new NullPointerException();
        return INSTANCE.getEngine0(descriptor);
    }

    public static <T extends ICommandContext, E extends CommandEngine<T>> void register(EngineDescriptor<T, E> descriptor, Supplier<E> factory) {
        if (descriptor == null)
            throw new NullPointerException();
        if (factory == null)
            throw new NullPointerException();
        INSTANCE.register0(descriptor, factory);
    }

    private final Map<String, Supplier<? extends CommandEngine>> registry;

    private CommandEngineProvider() {
        this.registry = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    private <T extends ICommandContext, E extends CommandEngine<T>> E getEngine0(EngineDescriptor<T, E> descriptor) {
        try {
            if (registry.containsKey(descriptor.getName()))
                return (E) registry.get(descriptor.getName()).get();
            if (descriptor.getClassName() == null)
                throw new NoSuchElementException("Engine could not be found for descriptor: " + descriptor.getName());
            return (E)Class.forName(descriptor.getClassName()).newInstance();
        } catch (ClassNotFoundException e) {
            throw new NoSuchElementException("Engine could not be found for descriptor: " + descriptor.getName());
        } catch (Exception e) {
            throw new RuntimeException("Engine initialization failed!", e);
        }
    }

    private <T extends ICommandContext, E extends CommandEngine<T>> void register0(EngineDescriptor<T, E> descriptor, Supplier<E> factory) {
        if (!registry.containsKey(descriptor.getName()))
            registry.put(descriptor.getName(), factory);
        throw new IllegalArgumentException("Engine already registered for descriptor: " + descriptor.getName());
    }

}
