package Repository.ConcreteRepositories;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.MemoryRepository;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.Exceptions.RepositoryException;
import Repository.Observer.FileRepoObserver;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class stores friendships in memory and synchronizes them with user repository changes.
 */
public class ObserverMemoryFriendshipRepository extends MemoryRepository<Pair<Long>, Friendship> implements FileRepoObserver {
    private final ObservableUserRepository user_repo;

    /**
     * This function creates an in-memory friendship repository associated with an observable user repository.
     * @param user_repo the observable repository used to find and track users;
     * @param validator the validator used for friendships;
     */
    public ObserverMemoryFriendshipRepository(ObservableUserRepository user_repo, Validator<Friendship> validator) {
        super(validator);
        this.user_repo = user_repo;
        user_repo.addObserver(this);
    }
    private void checkEqualFriendships(Friendship f1, Friendship f2) throws RepositoryException {
        if (f1.getFriend1().equals(f2.getFriend1()) && f1.getFriend2().equals(f2.getFriend2())) {
            throw new RepositoryException("There can't be more than one friendship with the same friends!",new KeyException());
        }
        if (f1.getFriend1().equals(f2.getFriend2()) && f1.getFriend2().equals(f2.getFriend1())) {
            throw new RepositoryException("There can't be more than one friendship with the same friends!",new KeyException());
        }
    }
    private void validateRelations(Friendship new_element){
        for(Friendship f1 : findAll()) {
            checkEqualFriendships(f1,new_element);
        }
        User friend1 = new_element.getFriend1();
        User friend2 = new_element.getFriend2();
        try {
            User trueFriend1 = user_repo.findElement(friend1.getId());
            User trueFriend2 = user_repo.findElement(friend2.getId());
            if(!trueFriend1.equalsEveryField(friend1)){
                throw new RepositoryException("The first friend in the friendship has wrong data for its ID!",new KeyException());
            }
            if(!trueFriend2.equalsEveryField(friend2)){
                throw new RepositoryException("The second friend in the friendship has wrong data for its ID!",new KeyException());
            }
        }
        catch (RepositoryException e){
            throw new RepositoryException("The added friendship isn't between 2 existing users!",e.getCause());
        }
    }
    private void addFriendsAfterAdd(Friendship new_element){
        new_element.getFriend1().addFriend(new_element.getFriend2());
        new_element.getFriend2().addFriend(new_element.getFriend1());
    }

    /**
     * This function removes a friendship and removes the corresponding friend links from both users.
     * @param id the pair of user IDs identifying the friendship;
     * @throws RepositoryException if the friendship does not exist;
     */
    @Override
    public void deleteElement(Pair<Long> id) throws RepositoryException {
        Friendship friendship = findElement(id);

        friendship.getFriend1().removeFriend(friendship.getFriend2());
        friendship.getFriend2().removeFriend(friendship.getFriend1());

        super.deleteElement(id);
    }

    /**
     * This function validates a friendship's users and prevents duplicate relationships.
     * @param new_element the friendship whose relationship to validate;
     * @throws RepositoryException if the users are not present, do not match their stored records,
     *                             or already have a friendship;
     */
    @Override
    public void addElement(Friendship new_element) throws RepositoryException, RepositoryException {
        validateRelations(new_element);
        super.addElement(new_element);
        addFriendsAfterAdd(new_element);
    }

    /**
     * This function replaces a friendship and updates the users' friend links.
     * @param new_element the friendship containing the replacement data;
     * @throws RepositoryException if the friendship does not exist;
     * @throws ValidationException if the friendship does not satisfy its validation rules;
     */
    @Override
    public void modifyElement(Friendship new_element){
        User oldFriend1 = findElement(new_element.getId()).getFriend1();
        User oldFriend2 = findElement(new_element.getId()).getFriend2();
        super.modifyElement(new_element);
        if(!oldFriend1.equals(new_element.getFriend1())){
            oldFriend1.removeFriend(oldFriend2);
            new_element.getFriend1().addFriend(new_element.getFriend2());
        }
        if(!oldFriend2.equals(new_element.getFriend2())){
            oldFriend2.removeFriend(oldFriend1);
            new_element.getFriend2().addFriend(new_element.getFriend1());
        }
    }

    /**
     * This function synchronizes stored friendships with user repository changes.
     * Friendships for removed users are deleted and friendships are updated with current user data.
     */
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
