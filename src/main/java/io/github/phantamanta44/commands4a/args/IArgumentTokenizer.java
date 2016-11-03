package io.github.phantamanta44.commands4a.args;

import io.github.phantamanta44.commands4a.exception.InvalidSyntaxException;

public interface IArgumentTokenizer {

    String nextString();

    <T> T nextOfType(Class<T> type) throws InvalidSyntaxException;

    boolean hasNext();

    void reset();

}
