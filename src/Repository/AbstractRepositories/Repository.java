package Repository.AbstractRepositories;

import Domain.Objects.Entity;
import Domain.Validation.ValidationException;
import Repository.Exceptions.RepositoryException;

import java.util.List;
import java.util.function.Predicate;

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
     * This function returns a list of the entities in the repository that are accepted by the filter.
     * @param filter a predicate which acts as a filter for the entities;
     * @return A list of the filtered entities in the repository.
     */
    List<E> findAll(Predicate<E> filter);

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
