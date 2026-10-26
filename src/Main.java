import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.FriendshipValidator;
import Domain.Validation.UserValidator;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.AbstractRepositories.Repository;
import Repository.ConcreteRepositories.FriendshipFileRepository;
import Repository.ConcreteRepositories.UserFileRepository;
import Service.FriendshipServices.ConsoleFriendshipService;
import Service.UserServices.ConsoleUserService;
import UI.Console.ConsoleMenu;
import UI.Console.MainMenu;

void main() {
    Validator<User> userValidator = new UserValidator();
    ObservableUserRepository userRepo = new UserFileRepository(userValidator,"data/users.csv");

    Validator<Friendship> friendshipValidator = new FriendshipValidator();
    Repository<Pair<Long>, Friendship> friendshipRepo = new FriendshipFileRepository(userRepo,friendshipValidator,"data/friendships.csv");

    ConsoleUserService userService = new ConsoleUserService(userRepo);
    ConsoleFriendshipService friendshipService = new ConsoleFriendshipService(friendshipRepo,userRepo);

    ConsoleMenu menu = new MainMenu(userService,friendshipService);
    menu.mainLoop();
}
