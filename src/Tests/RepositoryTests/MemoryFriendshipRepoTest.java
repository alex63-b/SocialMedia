package Tests.RepositoryTests;

import Domain.Objects.Friendship;
import Domain.Objects.User;
import Domain.Validation.Validator;
import Repository.ConcreteRepositories.MemoryFriendshipRepository;
import Repository.ConcreteRepositories.MemoryUserRepository;

import java.time.LocalDateTime;

public class MemoryFriendshipRepoTest {
    private MemoryUserRepository createUserRepo(Validator<User> validator){
        MemoryUserRepository userRepo = new MemoryUserRepository(validator);
        userRepo.addElement(new User(1L,"John Doe","johndoe@gmail.com"));
        userRepo.addElement(new User(2L,"Phil Smith","philsmith@gmail.com"));
        userRepo.addElement(new User(3L,"Luke Scott","lukescott@gmail.com"));
        return userRepo;
    }
    private MemoryFriendshipRepository createFriendshipRepo(Validator<Friendship> validator, MemoryUserRepository userRepo){
        MemoryFriendshipRepository friendshipRepo = new MemoryFriendshipRepository(userRepo,validator);
        friendshipRepo.addElement(new Friendship(new User(1L,"John Doe","johndoe@gmail.com"),new User(3L,"Luke Scott","lukescott@gmail.com"), LocalDateTime.parse("2025-12-13T17:02:22")));
        friendshipRepo.addElement(new Friendship(new User(3L,"Luke Scott","lukescott@gmail.com"),new User(2L,"Phil Smith","philsmith@gmail.com"),LocalDateTime.parse("2024-07-01T21:03:24")));
        return friendshipRepo;
    }

}
