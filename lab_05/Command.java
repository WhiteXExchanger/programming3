package lab_05;

/**
 * command
 */
@FunctionalInterface
interface Command {
    void execute(String[] args);
}
