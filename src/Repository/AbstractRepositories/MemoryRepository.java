package Repository.AbstractRepositories;

import Domain.Objects.Entity;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.Exceptions.RepositoryException;

import java.security.KeyException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class stores entities in memory, indexed by their identifiers.
 * @param <ID> the type of the entities' identifiers;
 * @param <E> the type of entities stored in the repository;
 */
public class MemoryRepository<ID,E extends Entity<ID>> implements Repository<ID,E>{
    private final Map<ID,E> repoMap;
    protected final Validator<E> validator;

    /**
     * This function creates an in-memory repository using the specified validator.
     * @param validator the validator used to check entities before they are stored;
     */
    public MemoryRepository(Validator<E> validator) {
        this.validator = validator;
        this.repoMap = new HashMap<>();
    }
    /**
     * This function returns the number of entities in the repository.
     * @return the number of stored entities;
     */
    @Override
    public int size(){
        return repoMap.size();
    }
    /**
     * This function returns all entities in the repository.
     * @return an iterable containing the stored entities;
     */
    @Override
    public Iterable<E> findAll() {
        return this.repoMap.values();
    }
    /**
     * This function finds an entity by its identifier.
     * @param id the identifier of the entity to find;
     * @return the entity with the specified identifier;
     * @throws RepositoryException if no entity with the specified identifier exists;
     */
    @Override
    public E findElement(ID id) throws RepositoryException {
        if(repoMap.containsKey(id)){
            return this.repoMap.get(id);
        }
        throw new RepositoryException("The entity with this ID couldn't be found!", new KeyException());
    }
    /**
     * This function validates and adds an entity to the repository.
     * @param new_element the entity to add;
     * @throws RepositoryException if an entity with the same identifier already exists;
     * @throws ValidationException if the entity does not satisfy its validation rules;
     */
    @Override
    public void addElement(E new_element) throws RepositoryException, ValidationException {
        validator.validate(new_element);
        if(repoMap.containsKey(new_element.getId())){
            throw new RepositoryException("The entity with this ID already exists in the list!", new KeyException());
        }
        repoMap.put(new_element.getId(),new_element);
    }
    /**
     * This function removes an entity from the repository by its identifier.
     * @param id the identifier of the entity to remove;
     * @throws RepositoryException if no entity with the specified identifier exists;
     */
    @Override
    public void deleteElement(ID id) throws RepositoryException {
        if(!repoMap.containsKey(id)){
            throw new RepositoryException("The entity with this ID couldn't be found!", new KeyException());
        }
        repoMap.remove(id);
    }
    /**
     * This function validates and replaces an existing entity.
     * @param new_element the entity containing the replacement data;
     * @throws RepositoryException if no entity with the specified identifier exists;
     * @throws ValidationException if the entity does not satisfy its validation rules;
     */
    @Override
    public void modifyElement(E new_element) throws RepositoryException {
        validator.validate(new_element);
        if(!repoMap.containsKey(new_element.getId())){
            throw new RepositoryException("The entity with this ID couldn't be found!", new KeyException());
        }
        repoMap.replace(new_element.getId(),new_element);
    }
}
