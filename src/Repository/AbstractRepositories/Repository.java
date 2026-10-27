package Repository.AbstractRepositories;

import Domain.Objects.Entity;
import Domain.Validation.ValidationException;
import Repository.Exceptions.RepositoryException;

/**
 * This interface defines operations for storing and retrieving entities.
 * @param <ID> the type of the entities' identifiers;
 * @param <E> the type of entities stored in the repository;
 */
public interface Repository<ID,E extends Entity<ID>>{
    /**
     * This function returns the number of entities in the repository.
     * @return the number of stored entities;
     */
    int size();

    /**
     * This function returns all entities in the repository.
     * @return an iterable containing the stored entities;
     */
    Iterable<E> findAll();

    /**
     * This function finds an entity by its identifier.
     * @param id the identifier of the entity to find;
     * @return the entity with the specified identifier;
     * @throws RepositoryException if no entity with the specified identifier exists;
     */
    E findElement(ID id);

    /**
     * This function validates and adds an entity to the repository.
     * @param new_element the entity to add;
     * @throws RepositoryException if an entity with the same identifier already exists;
     * @throws ValidationException if the entity does not satisfy its validation rules;
     */
    void addElement(E new_element);

    /**
     * This function removes an entity from the repository by its identifier.
     * @param id the identifier of the entity to remove;
     * @throws RepositoryException if no entity with the specified identifier exists;
     */
    void deleteElement(ID id);

    /**
     * This function validates and replaces an existing entity.
     * @param new_element the entity containing the replacement data;
     * @throws RepositoryException if no entity with the specified identifier exists;
     * @throws ValidationException if the entity does not satisfy its validation rules;
     */
    void modifyElement(E new_element);
}
