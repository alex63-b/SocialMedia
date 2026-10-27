package Repository.ConcreteRepositories;

import Domain.Objects.User;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.MemoryRepository;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.Exceptions.RepositoryException;
import Repository.Observer.FileRepoObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores users in memory and notifies observers when users change.
 */
public class ObservableMemoryUserRepository extends MemoryRepository<Long, User> implements ObservableUserRepository {
    private final List<FileRepoObserver> observers = new ArrayList<>();

    /**
     * This function creates an observable in-memory user repository.
     * @param validator the validator used for users;
     */
    public ObservableMemoryUserRepository(Validator<User> validator) {

        super(validator);
    }

    /**
     * This function registers an observer to receive user repository updates.
     * @param new_observer the observer to register;
     */
    @Override
    public void addObserver(FileRepoObserver new_observer) {

        observers.add(new_observer);
    }

    /**
     * This function removes an observer from user repository notifications.
     * @param deleted_observer the observer to remove;
     */
    @Override
    public void removeObserver(FileRepoObserver deleted_observer) {

        observers.remove(deleted_observer);
    }

    /**
     * This function notifies all registered observers about a repository update.
     */
    @Override
    public void notifyObservers() {
        for (FileRepoObserver observer : List.copyOf(observers)) {
            observer.update();
        }
    }

    /**
     * This function validates and adds a user, then notifies observers.
     * @param new_element the user to add;
     * @throws RepositoryException if a user with the same ID already exists;
     * @throws ValidationException if the user does not satisfy its validation rules;
     */
    @Override
    public void addElement(User new_element) throws RepositoryException {
        super.addElement(new_element);
        notifyObservers();
    }

    /**
     * This function removes a user and notifies observers.
     * @param id the ID of the user to remove;
     * @throws RepositoryException if the user does not exist;
     */
    @Override
    public void deleteElement(Long id) throws RepositoryException {
        super.deleteElement(id);
        notifyObservers();
    }

    /**
     * This function replaces a user and notifies observers.
     * @param new_element the user containing the replacement data;
     * @throws RepositoryException if the user does not exist;
     * @throws ValidationException if the user does not satisfy its validation rules;
     */
    @Override
    public void modifyElement(User new_element) throws RepositoryException {
        super.modifyElement(new_element);
        notifyObservers();
    }
}
