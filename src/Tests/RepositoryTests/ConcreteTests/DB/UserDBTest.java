package Tests.RepositoryTests.ConcreteTests.DB;

import Domain.Objects.User;
import Domain.Validation.UserValidator;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.AbstractRepositories.Repository;
import Repository.ConcreteRepositories.UserDBRepository;
import Repository.ConcreteRepositories.UserFileRepository;
import Repository.Factory.PropertyFactory;
import Repository.Factory.RepositoryFactory;
import Repository.Helpers.DefaultFilters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class UserDBTest {
    @BeforeEach
    public void setUp(){
        reloadDBData();
    }
    @AfterEach
    public void tearDown(){
        reloadDBData();
    }
    private void reloadDBData(){

    }
    @Test
    void testLoad() {
        PropertyFactory propertyFactory = new PropertyFactory();
        RepositoryFactory factory = new RepositoryFactory(propertyFactory.propertiesFromConfigFile("test_data/Resources/db_test.properties"));
        Validator<User> validator = new UserValidator();
        Repository<Long, User> new_repo = factory.createUserRepository(validator);

        assertEquals(3,new_repo.size());
        if(new_repo.findElement(1L).isPresent() && new_repo.findElement(2L).isPresent() && new_repo.findElement(3L).isPresent()) {
            assertEquals("John Doe", new_repo.findElement(1L).get().getName());
            assertEquals("philsmith@gmail.com", new_repo.findElement(2L).get().getFirstName());
            assertEquals("Luke Scott", new_repo.findElement(3L).get().getName());
        }
        else{
            fail();
        }
    }

    @Test
    void testStore() {
        PropertyFactory propertyFactory = new PropertyFactory();
        RepositoryFactory factory = new RepositoryFactory(propertyFactory.propertiesFromConfigFile("test_data/Resources/db_test.properties"));
        Validator<User> validator = new UserValidator();
        Repository<Long, User> new_repo = factory.createUserRepository(validator);

        new_repo.addElement(new User(4L,"Mike Forge","mikeforge@gmail.com"));

        Repository<Long,User> test_repo = factory.createUserRepository(validator);
        assertEquals(4,test_repo.size());
        for(User u : test_repo.findAll(DefaultFilters::filterTrue)){
            assertEquals(Optional.of(u),new_repo.findElement(u.getId()));
        }
    }
    @Test
    void testFindAll(){
        PropertyFactory propertyFactory = new PropertyFactory();
        RepositoryFactory factory = new RepositoryFactory(propertyFactory.propertiesFromConfigFile("test_data/Resources/db_test.properties"));
        Validator<User> validator = new UserValidator();
        Repository<Long, User> new_repo = factory.createUserRepository(validator);

        List<User> fullRepo = new_repo.findAll(DefaultFilters::filterTrue);

        assertEquals(3,fullRepo.size());
        assertEquals("John Doe",fullRepo.getFirst().getName());
        assertEquals("Phil Smith",fullRepo.get(1).getName());
        assertEquals("Luke Scott",fullRepo.getLast().getName());

        List<User> filterRepo = new_repo.findAll((u) -> u.getName().contains("P"));

        assertEquals(1,filterRepo.size());
        assertEquals("Phil Smith",filterRepo.getFirst().getName());
    }

    @Test
    void testAdd(){
        PropertyFactory propertyFactory = new PropertyFactory();
        RepositoryFactory factory = new RepositoryFactory(propertyFactory.propertiesFromConfigFile("test_data/Resources/db_test.properties"));
        Validator<User> validator = new UserValidator();
        Repository<Long, User> new_repo = factory.createUserRepository(validator);

        User right_user = new User(4L,"Mike","Forge");

        assertEquals(Optional.of(right_user),new_repo.addElement(right_user));

        if(new_repo.findElement(4L).isPresent()) {
            assertEquals(right_user, new_repo.findElement(4L).get());
        }
        else{
            fail();
        }

        User wrong_user = new User(3L,"John Doe","");

        assertThrows(ValidationException.class,() -> new_repo.addElement(wrong_user));
        assertFalse(new_repo.addElement(right_user).isPresent());
    }

    @Test
    void testDelete(){
        PropertyFactory propertyFactory = new PropertyFactory();
        RepositoryFactory factory = new RepositoryFactory(propertyFactory.propertiesFromConfigFile("test_data/Resources/db_test.properties"));
        Validator<User> validator = new UserValidator();
        Repository<Long, User> new_repo = factory.createUserRepository(validator);

        Optional<User> test_user = new_repo.findElement(2L);

        assertFalse(new_repo.deleteElement(5L).isPresent());
        assertEquals(test_user,new_repo.deleteElement(2L));

        assertFalse(new_repo.findElement(2L).isPresent());
    }

    @Test
    void testModify(){
        PropertyFactory propertyFactory = new PropertyFactory();
        RepositoryFactory factory = new RepositoryFactory(propertyFactory.propertiesFromConfigFile("test_data/Resources/db_test.properties"));
        Validator<User> validator = new UserValidator();
        Repository<Long, User> new_repo = factory.createUserRepository(validator);

        User new_user = new User(1L,"Mike","Loft");
        User wrong_user = new User(7L,"Mike","Loft");

        assertTrue(new_repo.modifyElement(new_user).isEmpty());
        assertEquals(Optional.of(wrong_user),new_repo.modifyElement(wrong_user));
        if(new_repo.findElement(new_user.getId()).isPresent()) {
            assertEquals("Mike",new_repo.findElement(new_user.getId()).get().getName());
            assertEquals("Loft",new_repo.findElement(new_user.getId()).get().getFirstName());
        }
        else{
            fail();
        }
    }
}
