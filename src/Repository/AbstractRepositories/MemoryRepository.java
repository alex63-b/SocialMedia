package Repository.AbstractRepositories;

import Domain.Objects.Entity;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.Exceptions.RepositoryException;

import java.security.KeyException;
import java.util.HashMap;
import java.util.Map;

public class MemoryRepository<ID,E extends Entity<ID>> implements Repository<ID,E>{
    private final Map<ID,E> repoMap;
    protected final Validator<E> validator;

    public MemoryRepository(Validator<E> validator) {
        this.validator = validator;
        this.repoMap = new HashMap<>();
    }

    @Override
    public int size(){
        return repoMap.size();
    }
    @Override
    public Iterable<E> findAll() {
        return this.repoMap.values();
    }

    @Override
    public E findElement(ID id) throws RepositoryException {
        if(repoMap.containsKey(id)){
            return this.repoMap.get(id);
        }
        throw new RepositoryException("The entity with this ID couldn't be found!", new KeyException());
    }

    @Override
    public void addElement(E new_element) throws RepositoryException, ValidationException {
        validator.validate(new_element);
        if(repoMap.containsKey(new_element.getId())){
            throw new RepositoryException("The entity with this ID already exists in the list!", new KeyException());
        }
        repoMap.put(new_element.getId(),new_element);
    }

    @Override
    public void deleteElement(ID id) throws RepositoryException {
        if(!repoMap.containsKey(id)){
            throw new RepositoryException("The entity with this ID couldn't be found!", new KeyException());
        }
        repoMap.remove(id);
    }

    @Override
    public void modifyElement(E new_element) throws RepositoryException {
        validator.validate(new_element);
        if(!repoMap.containsKey(new_element.getId())){
            throw new RepositoryException("The entity with this ID couldn't be found!", new KeyException());
        }
        repoMap.replace(new_element.getId(),new_element);
    }
}
