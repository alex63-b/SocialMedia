package Domain.Validation;

import Domain.Objects.Entity;

public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}
