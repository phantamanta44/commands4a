package io.github.phantamanta44.commands4a.annot;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(Prereq.MultiPrereq.class)
@Target(ElementType.METHOD)
public @interface Prereq {

    String value();

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Target(ElementType.METHOD)
    @interface MultiPrereq {

        Prereq[] value();

    }

}
