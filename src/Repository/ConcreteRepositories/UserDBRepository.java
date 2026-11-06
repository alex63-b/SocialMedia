package Repository.ConcreteRepositories;

import Domain.Objects.Friendship;
import Domain.Objects.User;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class represents a database-backed repository for users.
 * The repository operations are currently not implemented.
 */
public class UserDBRepository implements Repository<Long, User> {
    private final String url, user, password;
    private final Validator<User> userValidator;

    /**
     * This function creates a user database repository with the specified connection settings.
     * @param userValidator the validator used for users;
     * @param url the database connection URL;
     * @param user the database username;
     * @param password the database password;
     */
    public UserDBRepository(Validator<User> userValidator, String url, String user, String password){
        this.userValidator = userValidator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * This function returns the number of users in the repository.
     * @return the number of stored users;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public int size() {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }
    /**
     * This function returns all users in the repository.
     * @return the stored users;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public List<User> findAll(Predicate<User> filter) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function finds a user by ID.
     * @param aLong the ID of the user to find;
     * @return the user with the specified ID;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public Optional<User> findElement(Long aLong) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function adds a user to the repository.
     * @param new_element the user to add;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public Optional<User> addElement(User new_element) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function removes a user by ID.
     * @param aLong the ID of the user to remove;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public Optional<User> deleteElement(Long aLong) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function replaces an existing user.
     * @param new_element the user containing the replacement data;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public Optional<User> modifyElement(User new_element) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }
}
