package com.backend.portalroshkabackend.tools.errors.errorslist.solicitudDispositivos;

public class InvalidCommentFormatDRException extends RuntimeException {
    public InvalidCommentFormatDRException(String comentario) {
        super("No se pudo extraer el tipo de dispositivo del comentario: " + comentario);
    }
}
