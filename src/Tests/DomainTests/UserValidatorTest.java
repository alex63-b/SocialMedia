package Tests.DomainTests;

import Domain.Objects.User;
import Domain.Validation.UserValidator;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    @Test
    void test_validate() {
        Validator<User> validator = new UserValidator();
        User idError = new User(-34L,"John Smith","a@b.c");
        assertThrows(ValidationException.class,() -> validator.validate(idError));
        User nameError = new User(34L,"","a@b.c");
        assertThrows(ValidationException.class,() -> validator.validate(nameError));
        User perfect = new User(34L,"John Smith","a@b.c");
        validator.validate(perfect);
    }
}