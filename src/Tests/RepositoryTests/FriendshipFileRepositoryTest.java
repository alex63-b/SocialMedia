package Tests.RepositoryTests;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.FriendshipValidator;
import Domain.Validation.UserValidator;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.Repository;
import Repository.ConcreteRepositories.FriendshipFileRepository;
import Repository.ConcreteRepositories.UserFileRepository;
import Repository.Exceptions.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FriendshipFileRepositoryTest {
    private static final String user_path = "test_data/Users/user_data.csv";
    private static final String friendship_path = "test_data/Friendships/friendship_data.csv";
    @BeforeEach
    public void setUp(){
        reloadFile();
    }
    @AfterEach
    public void tearDown(){
        reloadFile();
    }
    private void reloadFile(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("test_data/Friendships/friendship_data.csv"));
            writer.write("1,3,2025-12-13T17:02:22\n3,2,2024-07-01T21:03:24");
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("test_data/Users/user_data.csv"));
            writer.write("1,John Doe,johndoe@gmail.com\n" +
                    "2,Phil Smith,philsmith@gmail.com\n" +
                    "3,Luke Scott,lukescott@gmail.com");
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testLoadFromFile(){
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        UserFileRepository user_repo = new UserFileRepository(userValidator,user_path);

        new FriendshipFileRepository(user_repo,friendshipValidator,"test_data/Friendships/corrupted_friendship_1.csv");
        new FriendshipFileRepository(user_repo,friendshipValidator,"test_data/Friendships/corrupted_friendship_2.csv");
        new FriendshipFileRepository(user_repo,friendshipValidator,"test_data/Friendships/corrupted_friendship_3.csv");
        new FriendshipFileRepository(user_repo,friendshipValidator,"test_data/Friendships/corrupted_friendship_4.csv");
        new FriendshipFileRepository(user_repo,friendshipValidator,"test_data/Friendships/corrupted_friendship_5.csv");
        new FriendshipFileRepository(user_repo,friendshipValidator,"test_data/Friendships/corrupted_friendship_6.csv");

        FriendshipFileRepository friendship_repo = new FriendshipFileRepository(user_repo,friendshipValidator,friendship_path);

        Repository<Pair<Long>, Friendship> empty_repo = new FriendshipFileRepository(user_repo,friendshipValidator,"a");
        assertEquals(0,empty_repo.size());

        assertEquals("John Doe",friendship_repo.findElement(new Pair<>(1L,3L)).getFriend1().getName());
        assertEquals("Luke Scott",friendship_repo.findElement(new Pair<>(1L,3L)).getFriend2().getName());
        assertEquals("Luke Scott",friendship_repo.findElement(new Pair<>(3L,2L)).getFriend1().getName());
        assertEquals("Phil Smith",friendship_repo.findElement(new Pair<>(3L,2L)).getFriend2().getName());
    }
    @Test
    public void testStoreIntoFile(){
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        UserFileRepository user_repo = new UserFileRepository(userValidator,user_path);
        FriendshipFileRepository friendship_repo = new FriendshipFileRepository(user_repo,friendshipValidator,friendship_path);

        friendship_repo.addElement(new Friendship(user_repo.findElement(1L),user_repo.findElement(2L), LocalDateTime.now()));

        FriendshipFileRepository friendship_repo_2 = new FriendshipFileRepository(user_repo,friendshipValidator,friendship_path);

        int length = 0;
        for(Friendship f : friendship_repo_2.findAll((_) -> true)){
            length += 1;
            assertEquals(f,friendship_repo.findElement(f.getId()));
        }
        assertEquals(3,length);
    }
    @Test
    public void testCascadeDelete(){
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        UserFileRepository user_repo = new UserFileRepository(userValidator,user_path);
        FriendshipFileRepository friendship_repo = new FriendshipFileRepository(user_repo,friendshipValidator,friendship_path);

        user_repo.deleteElement(2L);

        assertEquals(1, friendship_repo.size());

        for(Friendship f : friendship_repo.findAll((_) -> true)){
            assertEquals(1L,f.getFriend1().getId());
            assertEquals(3L,f.getFriend2().getId());
        }
    }

    @Test
    public void testCascadeModify(){
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        UserFileRepository user_repo = new UserFileRepository(userValidator,user_path);
        FriendshipFileRepository friendship_repo = new FriendshipFileRepository(user_repo,friendshipValidator,friendship_path);

        friendship_repo.modifyElement(new Friendship(user_repo.findElement(1L),user_repo.findElement(3L),LocalDateTime.parse("2024-02-23T10:11:12")));

        assertEquals(LocalDateTime.parse("2024-02-23T10:11:12"),friendship_repo.findElement(new Pair<>(1L,3L)).getDate());

        user_repo.modifyElement(new User(2L,"Thor Odinson","thor@odinson.com"));

        assertEquals("Thor Odinson",friendship_repo.findElement(new Pair<>(3L,2L)).getFriend2().getName());
    }

    @Test
    public void testErrorAdd(){
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        UserFileRepository user_repo = new UserFileRepository(userValidator,user_path);
        FriendshipFileRepository friendship_repo = new FriendshipFileRepository(user_repo,friendshipValidator,friendship_path);

        assertThrows(RepositoryException.class,() -> friendship_repo.addElement(new Friendship(user_repo.findElement(1L),user_repo.findElement(3L),LocalDateTime.parse("2024-02-23T10:11:12"))));
        assertThrows(RepositoryException.class,() -> friendship_repo.addElement(new Friendship(user_repo.findElement(3L),user_repo.findElement(1L),LocalDateTime.parse("2024-02-23T10:11:12"))));
        assertThrows(ValidationException.class,() -> friendship_repo.addElement(new Friendship(user_repo.findElement(1L),user_repo.findElement(1L),LocalDateTime.parse("2024-02-23T10:11:12"))));

        assertThrows(RepositoryException.class,() -> friendship_repo.addElement(new Friendship(user_repo.findElement(3L),new User(9L,"a","b"),LocalDateTime.parse("2024-02-23T10:11:12"))));
    }
    @Test
    public void testDoubleFriendAdd(){
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        UserFileRepository userRepo = new UserFileRepository(userValidator,user_path);
        FriendshipFileRepository friendshipRepo = new FriendshipFileRepository(userRepo,friendshipValidator,friendship_path);

        assertEquals(1,userRepo.findElement(1L).getFriends().size());
        assertTrue(userRepo.findElement(1L).getFriends().contains(userRepo.findElement(3L)));

        assertEquals(2,userRepo.findElement(3L).getFriends().size());
        assertTrue(userRepo.findElement(3L).getFriends().contains(userRepo.findElement(1L)));
        assertTrue(userRepo.findElement(3L).getFriends().contains(userRepo.findElement(2L)));
    }
    @Test
    public void testFriendshipDelete(){
        Validator<User> userValidator = new UserValidator();
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        UserFileRepository userRepo = new UserFileRepository(userValidator,user_path);
        FriendshipFileRepository friendshipRepo = new FriendshipFileRepository(userRepo,friendshipValidator,friendship_path);

        friendshipRepo.deleteElement(new Pair<>(1L,3L));

        assertEquals(0,userRepo.findElement(1L).getFriends().size());
        assertEquals(1,userRepo.findElement(3L).getFriends().size());
        assertFalse(userRepo.findElement(1L).getFriends().contains(new User(3L,"Phil Smith","philsmith@gmail.com")));
    }
}
