package Repository.AbstractRepositories;

import Domain.Objects.User;
import Repository.Observer.FileRepoObserver;

public interface ObservableUserRepository extends Repository<Long, User>{
    void addObserver(FileRepoObserver new_observer);
    void removeObserver(FileRepoObserver deleted_observer);
    void notifyObservers();
}
