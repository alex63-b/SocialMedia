package Domain.Objects;

import java.util.HashSet;

public class User extends Entity<Long>{
    private final String name;
    private final String firstName;
    private final HashSet<User> friends;
    public User(Long id, String name, String firstName){
        super(id);
        this.name = name;
        this.firstName = firstName;
        friends = new HashSet<>();
    }
    public boolean equalsEveryField(User user2){
        return name.equals(user2.name) && firstName.equals(user2.getFirstName());
    }
    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public HashSet<User> getFriends(){
        return friends;
    }

    public void addFriend(User newFriend){
        friends.add(newFriend);
    }

    public void removeFriend(User newFriend){
        friends.remove(newFriend);
    }
    @Override
    public String toString(){
        return id + ". The user " + name + " " + firstName + ";";
    }
}
