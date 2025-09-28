package com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos;

public class DeviceAssignmentNotFoundException extends RuntimeException {
    public DeviceAssignmentNotFoundException(Integer id) {
        super("No se encuentra una asignacion relacionada relacionada al dispositivo con ID: " + id);
    }
}
