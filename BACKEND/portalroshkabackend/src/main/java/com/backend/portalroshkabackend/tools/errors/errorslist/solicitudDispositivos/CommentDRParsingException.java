package com.backend.portalroshkabackend.tools.errors.errorslist.solicitudDispositivos;

public class CommentDRParsingException extends RuntimeException {
    public CommentDRParsingException(String comentario, String error) {
        super("Error al procesar el comentario '" + comentario + "': " + error);
    }
}
