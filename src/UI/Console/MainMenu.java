package UI.Console;

import Domain.Objects.User;
import Domain.Validation.ValidationException;
import Repository.Exceptions.RepositoryException;
import Service.FriendshipServices.ConsoleFriendshipService;
import Service.UserServices.ConsoleUserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainMenu implements ConsoleMenu{
    private final ConsoleUserService userService;
    private final ConsoleFriendshipService friendshipService;
    private final static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    public MainMenu(ConsoleUserService userService, ConsoleFriendshipService friendshipService){
        this.friendshipService = friendshipService;
        this.userService = userService;
    }
    private Long readNumber(String message, String errorMessage){
        while(true) {
            try {
                System.out.println(message);
                String id = read.readLine();
                return Long.parseLong(id);
            }
            catch (NumberFormatException e){
                System.out.println(errorMessage);
            }
            catch (IOException e) {
                System.out.println("Oops, something went wrong with the read.");
            }
        }
    }

    @Override
    public void printMenu() {
        System.out.println("Choose an option from the menu:");
        System.out.println("1. Show all users;");
        System.out.println("2. Add a new user;");
        System.out.println("3. Delete an existing user;");
        System.out.println("4. Show all friendships;");
        System.out.println("5. Add a new friendship;");
        System.out.println("6. Delete an existing friendship;");
        System.out.println("7. Show community count;");
        System.out.println("8. Show the most sociable community;");
        System.out.println("9. Exit the application;");
    }
    private void showUsers(){
        System.out.println(userService.showAllUsers());
    }

    private void addUser(){
        try {
            String name,firstName;
            System.out.println("Enter the name of the new user: ");
            name = read.readLine();
            System.out.println("Enter the first name of the new user: ");
            firstName = read.readLine();
            userService.addUser(name,firstName);
            System.out.println("The user has been added!");
        }
        catch (ValidationException | RepositoryException e){
            System.out.println(e.getMessage());
        }
        catch (IOException e){
            System.out.println("Oops, something went wrong with the read.");
        }
    }
    private void deleteUser(){
        try{
            Long idNumber = readNumber("Enter the id of the user to delete: ","The ID must be a number, please try again!");
            userService.deleteUser(idNumber);
            System.out.println("The user has been deleted!");
        }
        catch (ValidationException | RepositoryException e){
            System.out.println(e.getMessage());
        }
    }
    private void showFriendships(){
        System.out.println(friendshipService.showAllFriendships());
    }
    private void addFriendship(){
        try {
            Long id1 = readNumber("Enter the ID of the first friend: ", "The ID must be a number, please try again!");
            Long id2 = readNumber("Enter the ID of the second friend: ", "The ID must be a number, please try again!");
            friendshipService.addFriendship(id1,id2);
            System.out.println("The friendship has been added!");
        }
        catch (ValidationException | RepositoryException e){
            System.out.println(e.getMessage());
        }
    }
    private void deleteFriendship(){
        try{
            System.out.println("To delete a friendship, you must introduce the ID's of the friends in any order.");
            Long idFriend1 = readNumber("Enter the ID of the first friend: ","The ID must be a number, please try again!");
            Long idFriend2 = readNumber("Enter the ID of the second friend: ","The ID must be a number, please try again!");
            friendshipService.deleteFriendship(idFriend1,idFriend2);
            System.out.println("The friendship has been deleted!");
        }
        catch (ValidationException | RepositoryException e){
            System.out.println(e.getMessage());
        }
    }
    private void showCommunityCount(){
        System.out.println("There are " + friendshipService.communityCount() + " communities in this network.");
    }
    private void showMostSociableCommunity(){

        ArrayList<User> community = friendshipService.getMostSociableCommunity();
        if(community.isEmpty()){
            System.out.println("There aren't any communities in this social media!");
        }
        else {
            System.out.println("The most sociable community is:\n");
            for (User u : community) {
                System.out.println(u);
            }
        }
    }
    @Override
    public void mainLoop() {
        boolean appStopped = false;
        String input = "";
        while(!appStopped){
            printMenu();
            System.out.println("Enter the desired option: ");
            try{
                input = read.readLine();
            }
            catch (IOException e){
                System.out.println("Oops, something went wrong with the read.");
                input = "";
            }
            switch (input) {
                case "1" -> showUsers();
                case "2" -> addUser();
                case "3" -> deleteUser();
                case "4" -> showFriendships();
                case "5" -> addFriendship();
                case "6" -> deleteFriendship();
                case "7" -> showCommunityCount();
                case "8" -> showMostSociableCommunity();
                case "9" -> appStopped = true;
                default -> System.out.println("You did not enter a valid option, please try again.");
            }
        }
    }
}
