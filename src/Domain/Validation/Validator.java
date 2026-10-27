package Domain.Validation;

import Domain.Objects.Entity;

/**
 * This interface defines validation for objects of a specified type.
 * @param <E> the type of the objects to validate;
 */
public interface Validator<E> {
    /**
     * This function checks whether an object satisfies its validation rules.
     * @param entity the object to validate;
     * @throws ValidationException if the object does not satisfy its validation rules;
     */
    void validate(E entity) throws ValidationException;
}
