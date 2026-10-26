package Service.UserServices;

import Domain.Objects.User;
import Domain.Validation.UserValidator;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.Repository;

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
    public BasicUserService(Repository<Long, User> repository){
        this.repository = repository;
        this.validator = new UserValidator();
        nextID = 0L;
        setNextID();
    }
    public void addUser(String name, String email) {
        User new_user = new User(nextID,name,email);
        validator.validate(new_user);
        repository.addElement(new_user);
        nextID += 1;
    }

    public void deleteUser(Long id) {
        repository.deleteElement(id);
    }
}
