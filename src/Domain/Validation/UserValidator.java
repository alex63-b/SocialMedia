package Domain.Validation;

import Domain.Objects.User;

public class UserValidator implements Validator<User>{
    private void validateId(Long id) throws ValidationException{
        if(id <= 0){
            throw new ValidationException("The ID cannot be a negative number or zero!");
        }
    }
    private void validateName(String name) throws ValidationException{
        if(name.isEmpty()){
            throw new ValidationException("The name cannot be empty!");
        }
    }
    @Override
    public void validate(User user) throws ValidationException {
        StringBuilder error_builder = new StringBuilder();
        try{
            validateId(user.getId());
        }
        catch (ValidationException e){
            error_builder.append(e.getMessage());
            error_builder.append('\n');
        }
        try{
            validateName(user.getName());
        }
        catch (ValidationException e){
            error_builder.append(e.getMessage());
            error_builder.append('\n');
        }
        try{
            validateName(user.getFirstName());
        }
        catch (ValidationException e){
            error_builder.append(e.getMessage());
            error_builder.append('\n');
        }
        if(!error_builder.isEmpty()){
            throw new ValidationException(error_builder.toString());
        }
    }
}
