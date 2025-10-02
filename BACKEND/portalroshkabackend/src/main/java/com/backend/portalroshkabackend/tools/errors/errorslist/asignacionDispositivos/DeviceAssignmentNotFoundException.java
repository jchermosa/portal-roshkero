package com.backend.portalroshkabackend.tools.errors.errorslist.asignacionDispositivos;

public class DeviceAssignmentNotFoundException extends RuntimeException {
    public DeviceAssignmentNotFoundException(Integer id) {
        super("No se encontro una asignacion relacionada al ID: " + id);
    }
}
