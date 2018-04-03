package nlp.analyzers;

public class NlpParseException extends Exception {
    public NlpParseException(String message) {
        super(message);
    }

    public NlpParseException(Throwable cause) {
        super(cause);
    }

    public NlpParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public NlpParseException(Exception e) {
        super(e.getMessage(), e.getCause());
    }
}
