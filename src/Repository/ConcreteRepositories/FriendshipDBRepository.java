package Repository.ConcreteRepositories;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.Repository;

import java.util.List;
import java.util.function.Predicate;

/**
 * This class represents a database-backed repository for friendships.
 * The repository operations are currently not implemented.
 */
public class FriendshipDBRepository implements Repository<Pair<Long>, Friendship> {
    private final String url, user, password;
    private final Validator<Friendship> friendshipValidator;

    /**
     * This function creates a friendship database repository with the specified connection settings.
     * @param friendshipValidator the validator used for friendships;
     * @param url the database connection URL;
     * @param user the database username;
     * @param password the database password;
     */
    public FriendshipDBRepository(Validator<Friendship> friendshipValidator, String url, String user, String password){
        this.friendshipValidator = friendshipValidator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * This function returns the number of friendships in the repository.
     * @return the number of stored friendships;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public int size() {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function returns all friendships in the repository.
     * @return the stored friendships;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public List<Friendship> findAll(Predicate<Friendship> filter) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function finds a friendship by the pair of user IDs.
     * @param longPair the pair of user IDs identifying the friendship;
     * @return the friendship with the specified ID;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public Friendship findElement(Pair<Long> longPair) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function adds a friendship to the repository.
     * @param new_element the friendship to add;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public void addElement(Friendship new_element) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function removes a friendship by its pair of user IDs.
     * @param longPair the pair of user IDs identifying the friendship;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public void deleteElement(Pair<Long> longPair) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }

    /**
     * This function replaces an existing friendship.
     * @param new_element the friendship containing the replacement data;
     * @throws UnsupportedOperationException because this operation is not implemented;
     */
    @Override
    public void modifyElement(Friendship new_element) {
        throw new UnsupportedOperationException("This operation is not implemented yet!");
    }
}
