package Repository.Factory;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.MemoryRepository;
import Repository.ConcreteRepositories.FriendshipFileRepository;
import Repository.ConcreteRepositories.ObservableMemoryUserRepository;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.AbstractRepositories.Repository;
import Repository.ConcreteRepositories.ObserverMemoryFriendshipRepository;
import Repository.ConcreteRepositories.UserFileRepository;
import Repository.Exceptions.ConfigException;

import java.util.Properties;

public class RepositoryFactory {
    private final Properties properties;
    public RepositoryFactory(Properties properties){
        this.properties = properties;
    }
    public ObservableUserRepository createUserRepository(Validator<User> validator){
        String repoType = properties.getProperty("repository.type");
        if(repoType == null){
            throw new ConfigException("The repository type is empty!");
        }
        RepositoryType repositoryType = RepositoryType.valueOf(repoType.toUpperCase().trim());

        switch (repositoryType){
            case MEMORY: return new ObservableMemoryUserRepository(validator);
            case FILE: return new UserFileRepository(validator,properties.getProperty("file.users"));
            case DATABASE:
            default: throw new ConfigException("Repository type not found!");
        }
    }
    public Repository<Pair<Long>,Friendship> createFriendshipRepository(ObservableUserRepository userRepository, Validator<Friendship> friendshipValidator){
        String repoType = properties.getProperty("repository.type");
        if(repoType == null){
            throw new ConfigException("The repository type is empty!");
        }
        RepositoryType repositoryType = RepositoryType.valueOf(repoType.toUpperCase().trim());

        switch (repositoryType){
            case MEMORY: return new ObserverMemoryFriendshipRepository(userRepository,friendshipValidator);
            case FILE: return new FriendshipFileRepository(userRepository,friendshipValidator,properties.getProperty("file.friendships"));
            case DATABASE:
            default: throw new ConfigException("Repository type not found!");
        }
    }
}
