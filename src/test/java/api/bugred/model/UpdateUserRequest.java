package api.bugred.model;

public class UpdateUserRequest extends RegisterUserRequest {
     private String value;
     private String field;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
