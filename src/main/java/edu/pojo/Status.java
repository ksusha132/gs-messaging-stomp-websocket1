package edu.pojo;

import java.time.LocalDateTime;

public class Status {
    private String status;
    private LocalDateTime date;
    private double idUser;

    public Status() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getIdUser() {
        return idUser;
    }

    public void setIdUser(double idUser) {
        this.idUser = idUser;
    }
}
