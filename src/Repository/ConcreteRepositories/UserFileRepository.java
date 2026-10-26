package Repository.ConcreteRepositories;

import Domain.Objects.User;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.FileRepository;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.Exceptions.RepositoryException;
import Repository.Observer.FileRepoObserver;

import java.util.ArrayList;
import java.util.List;

public class UserFileRepository extends FileRepository<Long, User> implements ObservableUserRepository {
    private final List<FileRepoObserver> observers = new ArrayList<>();

    public UserFileRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected User lineToEntity(String line) throws ValidationException {
        String[] split = line.split(",");
        try {
            if(split.length != 3){
                throw new ValidationException("A line in the file has the wrong number of fields!");
            }
            Long id = Long.parseLong(split[0]);
            return new User(id,split[1],split[2]);
        }
        catch (NumberFormatException e){
            throw new ValidationException("The ID in a line of the file is not valid!");
        }
    }

    @Override
    protected String entityToLine(User entity) {
        return entity.getId() + "," + entity.getName() + "," + entity.getFirstName() ;
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
    public void addElement(User element) throws RepositoryException {
        super.addElement(element);
        notifyObservers();
    }

    @Override
    public void deleteElement(Long id) throws RepositoryException {
        super.deleteElement(id);
        notifyObservers();
    }

    @Override
    public void modifyElement(User element) throws RepositoryException {
        super.modifyElement(element);
        notifyObservers();
    }
}
