package Repository.ConcreteRepositories;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.MemoryRepository;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.Exceptions.RepositoryException;
import Repository.Observer.FileRepoObserver;

import java.util.ArrayList;
import java.util.List;

public class ObserverMemoryFriendshipRepository extends MemoryRepository<Pair<Long>, Friendship> implements FileRepoObserver {
    private final ObservableUserRepository user_repo;
    public ObserverMemoryFriendshipRepository(ObservableUserRepository user_repo, Validator<Friendship> validator) {
        super(validator);
        this.user_repo = user_repo;
    }
    @Override
    public void update() {
        List<Pair<Long>> deleteList = new ArrayList<>();
        for(Friendship f : findAll()){
            Long friendId1 = f.getFriend1().getId();
            Long friendId2 = f.getFriend2().getId();
            try{
                User newFriend1 = user_repo.findElement(friendId1);
                User newFriend2 = user_repo.findElement(friendId2);
                if(!f.getFriend1().equalsEveryField(newFriend1) || !f.getFriend2().equalsEveryField(newFriend2)){
                    modifyElement(new Friendship(newFriend1,newFriend2,f.getDate()));
                }
            }
            catch (RepositoryException e) {
                deleteList.add(f.getId());
            }
        }
        for(Pair<Long> id : deleteList){
            deleteElement(id);
        }
    }
}
