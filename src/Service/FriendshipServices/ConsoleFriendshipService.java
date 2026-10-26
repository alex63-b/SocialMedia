package Service.FriendshipServices;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Repository.AbstractRepositories.Repository;

public class ConsoleFriendshipService extends BasicFriendshipService {
    public ConsoleFriendshipService(Repository<Pair<Long>, Friendship> friendshipRepo, Repository<Long, User> userRepo) {
        super(friendshipRepo,userRepo);
    }

    public String showAllFriendships(){
        StringBuilder text = new StringBuilder();
        for(Friendship f : friendshipRepo.findAll()){
            text.append(f.toString());
            text.append("\n");
        }
        return text.toString();
    }
}
