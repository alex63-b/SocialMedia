package Repository.AbstractRepositories;

import Domain.Objects.Entity;

public interface Repository<ID,E extends Entity<ID>>{
    int size();

    Iterable<E> findAll();

    E findElement(ID id);

    void addElement(E new_element);

    void deleteElement(ID id);

    void modifyElement(E new_element);
}
