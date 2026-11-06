package Tests.RepositoryTests;

import Domain.Objects.User;
import Domain.Validation.Validator;
import Repository.ConcreteRepositories.MemoryUserRepository;

public class MemoryUserRepoTest {
    private MemoryUserRepository createUserRepo(Validator<User> validator){
        MemoryUserRepository userRepo = new MemoryUserRepository(validator);
        userRepo.addElement(new User(1L,"John Doe","johndoe@gmail.com"));
        userRepo.addElement(new User(2L,"Phil Smith","philsmith@gmail.com"));
        userRepo.addElement(new User(3L,"Luke Scott","lukescott@gmail.com"));
        return userRepo;
    }

}
