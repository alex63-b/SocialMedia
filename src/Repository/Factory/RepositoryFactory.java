package Repository.Factory;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.MemoryRepository;
import Repository.ConcreteRepositories.*;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.AbstractRepositories.Repository;
import Repository.Exceptions.ConfigException;
import Repository.Exceptions.RepositoryException;

import java.util.Properties;

/**
 * This class creates repositories according to the supplied configuration properties.
 */
public class RepositoryFactory {
    private final Properties properties;

    /**
     * This function creates a repository factory using the specified properties.
     * @param properties the configuration properties used to select and configure repositories;
     */
    public RepositoryFactory(Properties properties){
        this.properties = properties;
    }

    /**
     * This function creates a user repository for the configured storage type.
     * @param validator the validator used for users;
     * @return the configured user repository;
     * @throws ConfigException if required configuration is missing or the repository type is unsupported;
     */
    public Repository<Long,User> createUserRepository(Validator<User> validator){
        String repoType = properties.getProperty("repository.type");
        if(repoType == null){
            throw new ConfigException("The repository type is empty!");
        }
        RepositoryType repositoryType = RepositoryType.MEMORY;
        try {
            repositoryType = RepositoryType.valueOf(repoType.toUpperCase().trim());
        }
        catch (IllegalArgumentException e) {
            throw new ConfigException("This repository type doesn't exist!");
        }
        switch (repositoryType){
            case MEMORY: return new ObservableMemoryUserRepository(validator);
            case FILE:
                if(properties.getProperty("file.users") == null){
                    throw new ConfigException("The configuration file doesn't have the user file path!");
                }
                return new UserFileRepository(validator,properties.getProperty("file.users"));
            case DATABASE:
                if(properties.getProperty("db.url") == null){
                    throw new ConfigException("The configuration file doesn't have the database URL!");
                }
                if(properties.getProperty("db.user") == null){
                    throw new ConfigException("The configuration file doesn't have the database user!");
                }
                if(properties.getProperty("db.password") == null){
                    throw new ConfigException("The configuration file doesn't have the database password!");
                }
                return new UserDBRepository(validator,properties.getProperty("db.url"),
                    properties.getProperty("db.user"),properties.getProperty("db.password"));
            default: throw new ConfigException("Repository type not found!");
        }
    }

    /**
     * This function creates a friendship repository for the configured storage type.
     * @param userRepository the user repository associated with the friendship repository;
     * @param friendshipValidator the validator used for friendships;
     * @return the configured friendship repository;
     * @throws ConfigException if required configuration is missing, repository types do not match,
     *                         or the configured type is unsupported;
     */
    public Repository<Pair<Long>,Friendship> createFriendshipRepository(Repository<Long,User> userRepository, Validator<Friendship> friendshipValidator){
        String repoType = properties.getProperty("repository.type");
        if(repoType == null){
            throw new ConfigException("The repository type is empty!");
        }
        RepositoryType repositoryType = RepositoryType.MEMORY;
        try {
            repositoryType = RepositoryType.valueOf(repoType.toUpperCase().trim());
        }
        catch (IllegalArgumentException e) {
            throw new ConfigException("This repository type doesn't exist!");
        }
        if(repositoryType == RepositoryType.MEMORY){
            if(userRepository instanceof ObservableMemoryUserRepository) {
                ObservableMemoryUserRepository userRepo = (ObservableMemoryUserRepository) userRepository;
                return new ObserverMemoryFriendshipRepository(userRepo, friendshipValidator);
            }
            else{
                throw new ConfigException("The repository types are different!");
            }
        }
        else if(repositoryType == RepositoryType.FILE){
            if(properties.getProperty("file.friendships") == null){
                throw new ConfigException("The configuration file doesn't have the friendship file path!");
            }
            if(userRepository instanceof UserFileRepository) {
                UserFileRepository userRepo = (UserFileRepository) userRepository;
                return new FriendshipFileRepository(userRepo,friendshipValidator,properties.getProperty("file.friendships"));
            }
            else{
                throw new ConfigException("The repository types are different!");
            }
        }
        else if(repositoryType == RepositoryType.DATABASE){
            if(properties.getProperty("db.url") == null){
                throw new ConfigException("The configuration file doesn't have the database URL!");
            }
            if(properties.getProperty("db.user") == null){
                throw new ConfigException("The configuration file doesn't have the database user!");
            }
            if(properties.getProperty("db.password") == null){
                throw new ConfigException("The configuration file doesn't have the database password!");
            }
            return new FriendshipDBRepository(friendshipValidator,properties.getProperty("db.url"),
                    properties.getProperty("db.user"),properties.getProperty("db.password"));
        }
        else{
            throw new ConfigException("Repository type not found!");
        }
    }
}
