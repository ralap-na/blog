package blog.common;

public class OperationOutcome {
    private String id;
    private String message;
    private OutcomeState state;

    public OperationOutcome(String id, String message, OutcomeState state) {
        this.id = id;
        this.message = message;
        this.state = state;
    }

    public OperationOutcome create() {
        if (id == null) {
            throw new RuntimeException("Id should not be null");
        }

        if (state == null) {
            throw new RuntimeException("State should not be null");
        }

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
