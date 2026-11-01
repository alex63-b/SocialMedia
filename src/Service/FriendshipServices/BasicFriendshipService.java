package Service.FriendshipServices;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.ValidationException;
import Repository.AbstractRepositories.Repository;
import Repository.Exceptions.RepositoryException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * This class provides operations for managing friendships and analyzing user communities.
 */
public class BasicFriendshipService {
    protected final Repository<Pair<Long>, Friendship> friendshipRepo;
    private final Repository<Long, User> userRepo;

    /**
     * This function creates a friendship service using the specified repositories.
     * @param repo the repository used to store and retrieve friendships;
     * @param userRepo the repository used to store and retrieve users;
     */
    public BasicFriendshipService(Repository<Pair<Long>, Friendship> repo, Repository<Long, User> userRepo) {
        this.friendshipRepo = repo;
        this.userRepo = userRepo;
    }

    /**
     * This function creates a friendship between two existing users using the current date and time.
     * @param userId1 the ID of the first user;
     * @param userId2 the ID of the second user;
     * @throws ValidationException if either user does not exist;
     * @throws RepositoryException if the friendship cannot be added to the repository;
     */
    public void addFriendship(Long userId1, Long userId2){
        try{
            userRepo.findElement(userId1);
            userRepo.findElement(userId2);
        }
        catch (RepositoryException e){
            throw new ValidationException("The users with these ID's don't exist in the list of users!");
        }
        User friend1 = userRepo.findElement(userId1);
        User friend2 = userRepo.findElement(userId2);
        Friendship new_friendship = new Friendship(friend1,friend2,LocalDateTime.now());
        friendshipRepo.addElement(new_friendship);
    }

    /**
     * This function removes the friendship between two users.
     * @param friendId1 the ID of the first user;
     * @param friendId2 the ID of the second user;
     * @throws RepositoryException if the friendship does not exist;
     */
    public void deleteFriendship(Long friendId1, Long friendId2){
        friendshipRepo.deleteElement(new Pair<>(friendId1,friendId2));
    }

    private void BFS(Map<Long,Boolean> idTracker, Long startID){
        Queue<Long> bfsQueue = new ArrayDeque<>();
        bfsQueue.add(startID);
        while(!bfsQueue.isEmpty()){
            Long id = bfsQueue.element();
            User currentUser = userRepo.findElement(id);
            idTracker.put(id,true);
            bfsQueue.poll();
            for(User friend : currentUser.getFriends()){
                if(idTracker.get(friend.getId()) == false){
                    bfsQueue.add(friend.getId());
                }
            }
        }
    }

    /**
     * This function counts the connected communities in the user network.
     * @return the number of connected communities;
     * @throws RepositoryException if a user referenced by a friendship cannot be found;
     */
    public int communityCount(){
        Map<Long,Boolean> idTracker = new HashMap<>();
        for(User u : userRepo.findAll((_) -> true)){
            idTracker.put(u.getId(),false);
        }
        int count = 0;
        for(User u : userRepo.findAll((_) -> true)){
            if(idTracker.get(u.getId()) == false) {
                count += 1;
                BFS(idTracker, u.getId());
            }
        }
        return count;
    }

    /**
     * This function returns the community with the greatest shortest-path distance between its members.
     * @return the users in the most sociable community, or an empty list if no friendships exist;
     * @throws RepositoryException if a user referenced by a friendship cannot be found;
     */
    public ArrayList<User> getMostSociableCommunity(){
        Map<Long,Integer> idToPosition = new HashMap<>();
        Map<Integer,Long> positionToId = new HashMap<>();
        int position = 0;
        for(User u : userRepo.findAll((_) -> true)){
            idToPosition.put(u.getId(),position);
            positionToId.put(position, u.getId());
            position += 1;
        }
        int maxLenRoad = 0;
        int maxPosition = -1;

        int[][] adjacencyMatrix = new int[position][position];
        for(Friendship f : friendshipRepo.findAll((_) -> true)){
            int i = idToPosition.get(f.getFriend1().getId());
            int j = idToPosition.get(f.getFriend2().getId());
            adjacencyMatrix[i][j] = 1;
            adjacencyMatrix[j][i] = 1;
            maxLenRoad = 1;
            maxPosition = i;
        }
        int len_max = 1000000000;
        for(int i = 0; i < position; i++){
            for(int j = 0; j < position; j++){
                if(i != j && adjacencyMatrix[i][j] != 1) {
                    adjacencyMatrix[i][j] = len_max;
                }
            }
        }
        if(maxLenRoad == 0) {
            return new ArrayList<>();
        }
        for(int k = 0; k < position; k++){
            for(int i = 0; i < position; i++){
                for(int j = 0; j < position; j++){
                    if(adjacencyMatrix[i][j] > adjacencyMatrix[i][k] + adjacencyMatrix[k][j]) {
                        adjacencyMatrix[i][j] = adjacencyMatrix[i][k] + adjacencyMatrix[k][j];
                        if(adjacencyMatrix[i][j] > maxLenRoad){
                            maxLenRoad = adjacencyMatrix[i][j];
                            maxPosition = i;
                        }
                    }
                }
            }
        }

        Long idStart = positionToId.get(maxPosition);
        Map<Long,Boolean> idTracker = new TreeMap<>();
        for(User u : userRepo.findAll((_) -> true)){
            idTracker.put(u.getId(),false);
        }
        ArrayList<User> userList = new ArrayList<>();
        BFS(idTracker,idStart);
        for(Long id : idTracker.keySet()){
            if(idTracker.get(id)){
                userList.add(userRepo.findElement(id));
            }
        }
        return userList;
    }
}
