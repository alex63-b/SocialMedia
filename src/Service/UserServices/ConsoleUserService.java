package Service.UserServices;

import Domain.Objects.User;
import Repository.AbstractRepositories.Repository;

public class ConsoleUserService extends BasicUserService {
    public ConsoleUserService(Repository<Long, User> repository){
        super(repository);
    }
    public String showAllUsers(){
        StringBuilder result = new StringBuilder();
        for(User u : repository.findAll()){
            result.append(u);
            result.append("\n");
        }
        return result.toString();
    }
}
