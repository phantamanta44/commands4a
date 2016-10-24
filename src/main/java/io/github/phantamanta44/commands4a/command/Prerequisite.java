package io.github.phantamanta44.commands4a.command;

import java.util.function.Predicate;

public class Prerequisite<T extends ICommandContext> {

    private final Predicate<T> test;
    private final String fail;

    public Prerequisite(Predicate<T> test, String fail) {
        this.test = test;
        this.fail = fail;
    }

    public boolean test(T tested) {
        return test.test(tested);
    }

    public String getFailMessage() {
        return fail;
    }

}
