package Tests.RepositoryTests;

import Domain.Objects.Friendship;
import Domain.Objects.User;
import Repository.Filters.DefaultFilters;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DefaultFiltersTest {

    @Test
    void filterTrue() {
        User friend1 = new User(1L,"a","b");
        User friend2 = new User(2L, "c","d");
        Friendship friendship = new Friendship(friend1,friend2, LocalDateTime.now());
        assertTrue(DefaultFilters.filterTrue(friend1));
        assertTrue(DefaultFilters.filterTrue(friend2));
        assertTrue(DefaultFilters.filterTrue(friendship));
        assertTrue(DefaultFilters.filterTrue(null));
    }
    @Test
    void filterNotEmptyLines(){
        assertTrue(DefaultFilters.filterNotEmptyLines("kfld"));
        assertFalse(DefaultFilters.filterNotEmptyLines(""));
        assertTrue(DefaultFilters.filterNotEmptyLines("318924dkfs"));
    }
}