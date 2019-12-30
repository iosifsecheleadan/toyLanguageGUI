package exception;

public class exception extends Exception {
    public exception(String errorMessage) {
        super(errorMessage);
    }
    public exception() {
        super("an unknown error as occured");
    }
}
