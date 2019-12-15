package exceptions;

public class ProcessIncompleteException extends Exception {

    private String message;

    public ProcessIncompleteException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
