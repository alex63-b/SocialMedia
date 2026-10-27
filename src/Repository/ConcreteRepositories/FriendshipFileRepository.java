package Repository.ConcreteRepositories;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.FileRepository;
import Repository.AbstractRepositories.ObservableUserRepository;
import Repository.Exceptions.RepositoryException;
import Repository.Observer.FileRepoObserver;

import java.io.IOException;
import java.security.KeyException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * This class stores friendships in a file and observes changes to the user repository.
 */
public class FriendshipFileRepository extends FileRepository<Pair<Long>, Friendship> implements FileRepoObserver {
    ObservableUserRepository user_repo;

    /**
     * This function creates a friendship repository backed by a file and registers it with the user repository.
     * @param user_repo the observable repository used to find and track users;
     * @param validator the validator used for friendships;
     * @param fileName the path to the friendship data file;
     * @throws RepositoryException if the file cannot be created or opened;
     */
    public FriendshipFileRepository(ObservableUserRepository user_repo, Validator<Friendship> validator, String fileName) {
        this.user_repo = user_repo;
        super(validator, fileName);
        user_repo.addObserver(this);
    }

    /**
     * This function converts a file line into a friendship.
     * @param line the line to convert;
     * @return the friendship represented by the line;
     * @throws RepositoryException if the line's fields, user IDs, or date cannot be parsed;
     */
    @Override
    protected Friendship lineToEntity(String line) throws RepositoryException {
        String[] splitLine = line.split(",");
        try {
            if (splitLine.length != 3) {
                throw new RepositoryException("A line in the file has the wrong number of fields!", new IOException());
            }
            Long userId1 = Long.parseLong(splitLine[0]);
            Long userId2 = Long.parseLong(splitLine[1]);
            LocalDateTime date = LocalDateTime.parse(splitLine[2]);
            return new Friendship(user_repo.findElement(userId1),user_repo.findElement(userId2),date);
        }
        catch (NumberFormatException e){
            throw new RepositoryException("One of the numeric fields does not contain a valid number!",e.getCause());
        }
        catch (RepositoryException e){
            throw new RepositoryException("A user with the respective id couldn't be found!",e.getCause());
        }
        catch (DateTimeParseException e){
            throw new RepositoryException("The date of the friendship isn't readable!",e.getCause());
        }
    }

    /**
     * This function converts a friendship into a comma-separated file line.
     * @param entity the friendship to convert;
     * @return the line representing the users' IDs and friendship date;
     */
    @Override
    protected String entityToLine(Friendship entity) {
        return entity.getFriend1().getId() + "," + entity.getFriend2().getId() + "," + entity.getDate();
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
     * @throws RepositoryException if the friendship does not exist or the file cannot be written;
     */
    @Override
    public void deleteElement(Pair<Long> id) throws RepositoryException {
        Friendship friendship = findElement(id);

        friendship.getFriend1().removeFriend(friendship.getFriend2());
        friendship.getFriend2().removeFriend(friendship.getFriend1());

        super.deleteElement(id);
    }

    /**
     * This function validates the relationship, adds a friendship, and links the users as friends.
     * @param new_element the friendship to add;
     * @throws RepositoryException if the users are not present, the relationship already exists,
     *                             or the file cannot be written;
     * @throws ValidationException if the friendship does not satisfy its validation rules;
     */
    @Override
    public void addElement(Friendship new_element) throws RepositoryException, RepositoryException {
        validateRelations(new_element);
        super.addElement(new_element);
        addFriendsAfterAdd(new_element);
    }

    /**
     * This function adds a friendship while loading the file and links its users as friends.
     * @param new_element the friendship to add;
     * @throws RepositoryException if the users are not present or the relationship already exists;
     * @throws ValidationException if the friendship does not satisfy its validation rules;
     */
    @Override
    protected void noStoreAddElement(Friendship new_element) throws RepositoryException, RepositoryException {
        validateRelations(new_element);
        super.noStoreAddElement(new_element);
        addFriendsAfterAdd(new_element);
    }

    /**
     * This function replaces a friendship and updates the users' friend links.
     * @param new_element the friendship containing the replacement data;
     * @throws RepositoryException if the friendship does not exist or the file cannot be written;
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
     * @throws RepositoryException if a repository operation fails while synchronizing;
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
