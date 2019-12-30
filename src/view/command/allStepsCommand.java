package view.command;

import controller.controllerInterface;
import exception.exception;

public class allStepsCommand extends Command {
    public allStepsCommand(String key, String description, controllerInterface ctrl) {
        super(key, description, ctrl);
    }

    @Override
    public void execute() throws exception {
        this.ctrl.allSteps();
    }

}
