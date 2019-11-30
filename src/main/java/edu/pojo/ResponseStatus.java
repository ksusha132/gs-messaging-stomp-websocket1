package edu.pojo;

public enum ResponseStatus {
    BAD("BAD"),
    OK("OK");

    private String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
