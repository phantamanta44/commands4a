package io.github.phantamanta44.commands4a.annot;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(Alias.MultiAlias.class)
@Target(ElementType.METHOD)
public @interface Alias {

    String value();

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Target(ElementType.METHOD)
    @interface MultiAlias {

        Alias[] value();

    }

}
