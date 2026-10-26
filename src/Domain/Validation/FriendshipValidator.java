package Domain.Validation;

import Domain.Objects.Friendship;

import java.time.LocalDateTime;

public class FriendshipValidator implements Validator<Friendship>{
    private void validateFriends(Friendship friendship){
        UserValidator validator = new UserValidator();
        try {
            validator.validate(friendship.getFriend1());
        }
        catch (ValidationException e){
            throw new ValidationException("The first friend doesn't have valid fields!");
        }
        try {
            validator.validate(friendship.getFriend2());
        }
        catch (ValidationException e){
            throw new ValidationException("The second friend doesn't have valid fields!");
        }
        if(friendship.getFriend1().equals(friendship.getFriend2())){
            throw new ValidationException("You cannot form a friendship between a person and themselves!");
        }
    }
    private void validateDate(Friendship friendship){
        if(friendship.getDate().isAfter(LocalDateTime.now())){
            throw new ValidationException("The date on which the friendship started cannot be in the future!");
        }
    }
    @Override
    public void validate(Friendship entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        try{
            validateFriends(entity);
        }
        catch (ValidationException e){
            errors.append(e.getMessage());
            errors.append("\n");
        }
        try{
            validateDate(entity);
        }
        catch (ValidationException e){
            errors.append(e.getMessage());
            errors.append("\n");
        }
        if(!errors.isEmpty()){
            throw new ValidationException(errors.toString());
        }
    }
}
