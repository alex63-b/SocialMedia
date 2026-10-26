package Repository.ConcreteRepositories;

import Domain.Objects.User;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.MemoryRepository;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.Exceptions.RepositoryException;
import Repository.Observer.FileRepoObserver;

import java.util.ArrayList;
import java.util.List;

public class ObservableMemoryUserRepository extends MemoryRepository<Long, User> implements ObservableUserRepository {
    private final List<FileRepoObserver> observers = new ArrayList<>();

    public ObservableMemoryUserRepository(Validator<User> validator) {

        super(validator);
    }

    @Override
    public void addObserver(FileRepoObserver new_observer) {

        observers.add(new_observer);
    }

    @Override
    public void removeObserver(FileRepoObserver deleted_observer) {

        observers.remove(deleted_observer);
    }

    @Override
    public void notifyObservers() {
        for (FileRepoObserver observer : List.copyOf(observers)) {
            observer.update();
        }
    }

    @Override
    public void addElement(User new_element) throws RepositoryException {
        super.addElement(new_element);
        notifyObservers();
    }

    @Override
    public void deleteElement(Long id) throws RepositoryException {
        super.deleteElement(id);
        notifyObservers();
    }

    @Override
    public void modifyElement(User new_element) throws RepositoryException {
        super.modifyElement(new_element);
        notifyObservers();
    }
}
