package Repository.AbstractRepositories;

import Domain.Objects.Entity;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.Exceptions.RepositoryException;

import java.io.*;

/**
 * This class stores entities in a file and provides repository operations for them.
 * @param <ID> the type of the entities' identifiers;
 * @param <E> the type of entities stored in the repository;
 */
public abstract class FileRepository<ID, E extends Entity<ID>> extends MemoryRepository<ID, E>{
    String fileName;

    /**
     * This function creates a file-backed repository and loads its entities from the specified file.
     * @param validator the validator used to check loaded and stored entities;
     * @param fileName the path to the file used by the repository;
     * @throws RepositoryException if the file cannot be created or opened;
     * @throws ValidationException if an entity cannot be validated;
     */
    public FileRepository(Validator<E> validator, String fileName){
        super(validator);
        this.fileName = fileName;
        loadFromFile();
    }

    /**
     * This function converts a line from the repository file into an entity.
     * @param line the line to convert;
     * @return the entity represented by the line;
     * @throws RepositoryException if the line cannot be parsed as an entity;
     * @throws ValidationException if the parsed entity is invalid;
     */
    abstract protected E lineToEntity(String line);

    /**
     * This function converts an entity into a line for the repository file.
     * @param entity the entity to convert;
     * @return the line representing the entity;
     */
    abstract protected String entityToLine(E entity);

    /**
     * This function loads and validates entities from the repository file.
     * Invalid or duplicate entries are reported and skipped.
     * @throws RepositoryException if the file cannot be created or opened;
     */
    protected void loadFromFile() throws RepositoryException, ValidationException {
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            int counter = 0;
            while((line = reader.readLine()) != null){
                counter += 1;
                if(line.isEmpty()){
                    continue;
                }
                try {
                    E new_entity = lineToEntity(line);
                    validator.validate(new_entity);
                    noStoreAddElement(new_entity);
                }
                catch (ValidationException | RepositoryException e) {
                    System.out.println("Line " + counter + ": " + e.getMessage());
                }
            }
        }
        catch (IOException e) {
            try {
                File newFile = new File(fileName);
                if(!newFile.createNewFile()){
                    throw new RepositoryException("The file already exists but couldn't be opened!",e.getCause());
                }
            }
            catch (IOException ex) {
                throw new RepositoryException("The file couldn't be created!",ex.getCause());
            }
        }
    }

    private void storeIntoFile() throws RepositoryException, ValidationException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            boolean first = true;
            for(E e : findAll()){
                validator.validate(e);
                if(!first){
                    writer.write("\n");
                }
                writer.write(entityToLine(e));
                first = false;
            }
        }
        catch (IOException e) {
            throw new RepositoryException("The file couldn't be opened for writing!",e.getCause());
        }
    }

    /**
     * This function adds a validated entity and saves the repository to its file.
     * @param new_element the entity to add;
     * @throws RepositoryException if an entity with the same identifier exists or the file cannot be written;
     * @throws ValidationException if the entity does not satisfy its validation rules;
     */
    @Override
    public void addElement(E new_element) throws RepositoryException, ValidationException {
        super.addElement(new_element);
        storeIntoFile();
    }

    /**
     * This function removes an entity and saves the repository to its file.
     * @param id the identifier of the entity to remove;
     * @throws RepositoryException if the entity does not exist or the file cannot be written;
     */
    @Override
    public void deleteElement(ID id) throws RepositoryException {
        super.deleteElement(id);
        storeIntoFile();
    }

    /**
     * This function replaces an entity and saves the repository to its file.
     * @param new_element the entity containing the replacement data;
     * @throws RepositoryException if the entity does not exist or the file cannot be written;
     * @throws ValidationException if the entity does not satisfy its validation rules;
     */
    @Override
    public void modifyElement(E new_element) throws RepositoryException {
        super.modifyElement(new_element);
        storeIntoFile();
    }

    /**
     * This function adds a validated entity without immediately saving the repository to its file.
     * @param new_element the entity to add;
     * @throws RepositoryException if an entity with the same identifier already exists;
     * @throws ValidationException if the entity does not satisfy its validation rules;
     */
    protected void noStoreAddElement(E new_element) throws RepositoryException, ValidationException{
        super.addElement(new_element);
    }
}
