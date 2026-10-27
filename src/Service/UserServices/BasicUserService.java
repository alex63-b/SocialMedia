package Service.UserServices;

import Domain.Objects.User;
import Domain.Validation.ValidationException;
import Domain.Validation.UserValidator;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.Repository;
import Repository.Exceptions.RepositoryException;

/**
 * This class provides basic operations for managing users.
 */
public class BasicUserService {
    protected final Repository<Long, User> repository;
    private final Validator<User> validator;
    private Long nextID;

    private void setNextID(){
        for(User u : repository.findAll()){
            if(u.getId() > nextID){
                nextID = u.getId();
            }
        }
        nextID += 1;
    }

    /**
     * This function creates a user service and sets the next available user ID.
     * @param repository the repository used to store and retrieve users;
     */
    public BasicUserService(Repository<Long, User> repository){
        this.repository = repository;
        this.validator = new UserValidator();
        nextID = 0L;
        setNextID();
    }

    /**
     * This function validates and adds a user using the next available ID.
     * The {@code email} value is stored as the user's first name.
     * @param name the user's name;
     * @param email the value used as the user's first name;
     * @throws ValidationException if either user name field is empty;
     * @throws RepositoryException if the user cannot be added to the repository;
     */
    public void addUser(String name, String email) {
        User new_user = new User(nextID,name,email);
        validator.validate(new_user);
        repository.addElement(new_user);
        nextID += 1;
    }

    /**
     * This function removes a user by ID.
     * @param id the ID of the user to remove;
     * @throws RepositoryException if no user with the specified ID exists;
     */
    public void deleteUser(Long id) {
        repository.deleteElement(id);
    }
}
