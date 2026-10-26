package Repository.AbstractRepositories;

import Domain.Objects.Entity;
import Domain.Validation.ValidationException;
import Domain.Validation.Validator;
import Repository.Exceptions.RepositoryException;

import java.io.*;

public abstract class FileRepository<ID, E extends Entity<ID>> extends MemoryRepository<ID, E>{
    String fileName;

    public FileRepository(Validator<E> validator, String fileName){
        super(validator);
        this.fileName = fileName;
        loadFromFile();
    }
    abstract protected E lineToEntity(String line);

    abstract protected String entityToLine(E entity);

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

    @Override
    public void addElement(E new_element) throws RepositoryException, ValidationException {
        super.addElement(new_element);
        storeIntoFile();
    }
    @Override
    public void deleteElement(ID id) throws RepositoryException {
        super.deleteElement(id);
        storeIntoFile();
    }

    @Override
    public void modifyElement(E new_element) throws RepositoryException {
        super.modifyElement(new_element);
        storeIntoFile();
    }

    protected void noStoreAddElement(E new_element) throws RepositoryException, ValidationException{
        super.addElement(new_element);
    }
}
