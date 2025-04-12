package blog.common;

public class OperationOutcome {
    private static String id;
    private static String message;
    private static OutcomeState state;

    public OperationOutcome(String id, String message, OutcomeState state) {
        this.id = id;
        this.message = message;
        this.state = state;
    }

    public static OperationOutcome create() {
        return new OperationOutcome(id, message, state);
    }

    public String getId() {
        return id;
    }

    public OperationOutcome setId(String id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public OperationOutcome setMessage(String message) {
        this.message = message;
        return this;
    }

    public OutcomeState getState() {
        return state;
    }

    public OperationOutcome setState(OutcomeState state) {
        this.state = state;
        return this;
    }
}
