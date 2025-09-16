package com.backend.portalroshkabackend.tools;

import com.backend.portalroshkabackend.tools.errors.errorslist.DatabaseOperationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.function.Supplier;

public class SaveManager {

    public static <T> T saveEntity(Supplier<T> saveOperation, String errorMessage){
        try{
            return saveOperation.get();
        } catch (JpaSystemException ex){
            throw new DatabaseOperationException(errorMessage, ex);
        }
    }
}
