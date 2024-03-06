package se.experis.academy.diary_app.model;

/**
 * This class represents a response for the entry controller
 */
public class Response {
    private Object data;
    private String message;

    /**
     * Constructor
     * @param data Some data or null
     * @param message A string
     */
    public Response(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
