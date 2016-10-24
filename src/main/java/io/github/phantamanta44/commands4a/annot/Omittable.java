package io.github.phantamanta44.commands4a.annot;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.PARAMETER)
public @interface Omittable {
}
