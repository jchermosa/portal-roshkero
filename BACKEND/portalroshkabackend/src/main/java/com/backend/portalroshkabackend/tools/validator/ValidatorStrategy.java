package com.backend.portalroshkabackend.tools.validator;

public interface ValidatorStrategy<T> {
    void validate(T target);
}
