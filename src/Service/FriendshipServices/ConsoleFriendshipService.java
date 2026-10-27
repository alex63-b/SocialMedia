package Service.FriendshipServices;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Repository.AbstractRepositories.Repository;

/**
 * This class adds console-oriented display functionality to the friendship service.
 */
public class ConsoleFriendshipService extends BasicFriendshipService {
    /**
     * This function creates a console friendship service using the specified repositories.
     * @param friendshipRepo the repository used to store and retrieve friendships;
     * @param userRepo the repository used to store and retrieve users;
     */
    public ConsoleFriendshipService(Repository<Pair<Long>, Friendship> friendshipRepo, Repository<Long, User> userRepo) {
        super(friendshipRepo,userRepo);
    }

    /**
     * This function returns a string containing all friendships in the repository.
     * @return the friendships, one per line;
     */
    public String showAllFriendships(){
        StringBuilder text = new StringBuilder();
        for(Friendship f : friendshipRepo.findAll()){
            text.append(f.toString());
            text.append("\n");
        }
        return text.toString();
    }
}
