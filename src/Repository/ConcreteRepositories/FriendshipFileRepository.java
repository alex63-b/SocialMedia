package Repository.ConcreteRepositories;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
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

public class FriendshipFileRepository extends FileRepository<Pair<Long>, Friendship> implements FileRepoObserver {
    ObservableUserRepository user_repo;
    public FriendshipFileRepository(ObservableUserRepository user_repo, Validator<Friendship> validator, String fileName) {
        this.user_repo = user_repo;
        super(validator, fileName);
        user_repo.addObserver(this);
    }

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
    @Override
    public void deleteElement(Pair<Long> id) throws RepositoryException {
        Friendship friendship = findElement(id);

        friendship.getFriend1().removeFriend(friendship.getFriend2());
        friendship.getFriend2().removeFriend(friendship.getFriend1());

        super.deleteElement(id);
    }
    @Override
    public void addElement(Friendship new_element) throws RepositoryException, RepositoryException {
        validateRelations(new_element);
        super.addElement(new_element);
        addFriendsAfterAdd(new_element);
    }
    @Override
    protected void noStoreAddElement(Friendship new_element) throws RepositoryException, RepositoryException {
        validateRelations(new_element);
        super.noStoreAddElement(new_element);
        addFriendsAfterAdd(new_element);
    }
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
