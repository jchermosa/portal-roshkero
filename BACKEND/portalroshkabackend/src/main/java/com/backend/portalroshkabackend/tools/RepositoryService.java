package com.backend.portalroshkabackend.tools;

import com.backend.portalroshkabackend.tools.errors.errorslist.DatabaseOperationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

import java.util.function.Supplier;

@Service
public class RepositoryService {

    public <T> T findByIdOrThrow(JpaRepository<T, Integer> repository, Integer id, Supplier<RuntimeException> exception){
        return repository.findById(id).orElseThrow(exception);
    }

    public <T> T save(JpaRepository<T, ?> repository, T entity, String errorMessage){
        try{
            return repository.save(entity);
        } catch (JpaSystemException ex){
            throw new DatabaseOperationException(errorMessage, ex);
        }

    }

    public <T> void delete(JpaRepository<T, ?> repository, T entity, String errorMessage) {
        try{
            repository.delete(entity);
        } catch (JpaSystemException ex){
            throw new DatabaseOperationException(errorMessage, ex);
        }
    }
}
