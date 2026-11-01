package Tests.RepositoryTests;

import Domain.Objects.User;
import Domain.Validation.UserValidator;
import Domain.Validation.ValidationException;
import Repository.AbstractRepositories.MemoryRepository;
import Repository.AbstractRepositories.Repository;
import Repository.Exceptions.RepositoryException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryRepositoryTest {
    @Test
    void testFindAll(){
        Repository<Long, User> new_repo = new MemoryRepository<Long, User>(new UserValidator());
        User[] new_users = {new User(2L,"John Doe","johndoe@gmail.com"),
                new User(3L,"Luke Doe","lukedoe@gmail.com"),
                new User(1L,"Final Doe","finaldoe@gmail.com")};
        for(User u : new_users){
            new_repo.addElement(u);
        }
        Iterable<User> iter = new_repo.findAll((_) -> true);
        for(User u : new_users){
            boolean found = false;
            for(User u2 : iter){
                if(u.equals(u2)){
                    found = true;
                    break;
                }
            }
            if(!found){
                fail();
            }
        }
        List<User> list = new_repo.findAll((u) -> u.getName().startsWith("L"));
        assertEquals(1,list.size());
        assertEquals("Luke Doe",list.getFirst().getName());
    }

    @Test
    void testFindElement() {
        User new_user = new User(2L,"John Doe","johndoe@gmail.com");
        Repository<Long, User> new_repo = new MemoryRepository<Long, User>(new UserValidator());
        new_repo.addElement(new_user);
        assertEquals(new_user,new_repo.findElement(2L));
        assertThrows(RepositoryException.class,() -> new_repo.findElement(3L),"The entity with this ID couldn't be found!");
    }

    @Test
    void testAddElement() {
        User new_user = new User(2L,"John Doe","johndoe@gmail.com");
        User wrong_user = new User(3L,"John Doe","");
        Repository<Long, User> new_repo = new MemoryRepository<Long, User>(new UserValidator());
        new_repo.addElement(new_user);
        var iterator = new_repo.findAll((_) -> true).iterator();
        assertEquals(iterator.next(),new_user);
        assertEquals(new_user,new_repo.findElement(2L));
        assertThrows(RepositoryException.class,() -> new_repo.addElement(new_user),"The entity with this ID already exists in the list!");
        assertThrows(ValidationException.class,() -> new_repo.addElement(wrong_user));
    }

    @Test
    void testDeleteElement() {
        Repository<Long, User> new_repo = new MemoryRepository<Long, User>(new UserValidator());
        User[] new_users = {new User(2L,"John Doe","johndoe@gmail.com"),
                new User(3L,"Luke Doe","lukedoe@gmail.com")};
        for(User u : new_users){
            new_repo.addElement(u);
        }
        assertThrows(RepositoryException.class,() -> new_repo.deleteElement(1L),"The entity with this ID couldn't be found!");
        new_repo.deleteElement(2L);

        Iterable<User> iter = new_repo.findAll((_) -> true);

        assertThrows(RepositoryException.class,() -> new_repo.findElement(2L),"The entity with this ID couldn't be found!");
        assertEquals(iter.iterator().next(),new_repo.findElement(3L));
    }

    @Test
    void testModifyElement() {
        Repository<Long, User> new_repo = new MemoryRepository<Long, User>(new UserValidator());
        User[] new_users = {new User(2L,"John Doe","johndoe@gmail.com"),
                new User(3L,"Luke Doe","lukedoe@gmail.com")};
        for(User u : new_users){
            new_repo.addElement(u);
        }
        assertThrows(RepositoryException.class, () -> new_repo.modifyElement(new User(1L,"Mike Doe","mikedoe@gmail.com")), "The entity with this ID couldn't be found!");
        new_repo.modifyElement(new User(2L,"Mike Doe","mikedoe@gmail.com"));

        assertEquals("Mike Doe",new_repo.findElement(2L).getName());
        assertNotEquals("Mike Doe",new_repo.findElement(3L).getName());
    }
}