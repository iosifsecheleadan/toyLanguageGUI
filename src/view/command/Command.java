package view.command;

import controller.controllerInterface;
import exception.exception;

public abstract class Command {
    controllerInterface ctrl;
    private String key;
    private String description;

    Command(String key, String description, controllerInterface ctrl) {
        this.key = key;
        this.description = description;
        this.ctrl = ctrl;
    }

    public String getKey() {
        return this.key;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return "\t- " + this.key + " : " + this.description + "\n";
    }

    public abstract void execute() throws exception;

}
