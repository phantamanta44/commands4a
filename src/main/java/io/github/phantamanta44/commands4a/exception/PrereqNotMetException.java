package io.github.phantamanta44.commands4a.exception;

import io.github.phantamanta44.commands4a.command.Prerequisite;

public class PrereqNotMetException extends Exception {

    private Prerequisite<?> prereq;

    public PrereqNotMetException(Prerequisite<?> prereq) {
        this.prereq = prereq;
    }

    public Prerequisite<?> getPrerequisite() {
        return prereq;
    }

}
