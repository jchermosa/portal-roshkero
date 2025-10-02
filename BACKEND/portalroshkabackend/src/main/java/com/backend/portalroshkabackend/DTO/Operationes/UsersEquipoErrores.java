package com.backend.portalroshkabackend.DTO.Operationes;

import java.util.List;

public class UsersEquipoErrores {
    private int status;
    private String message;
    private List<String> details;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
