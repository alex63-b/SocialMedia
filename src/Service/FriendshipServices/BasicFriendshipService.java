package Service.FriendshipServices;

import Domain.Objects.Friendship;
import Domain.Objects.Pair;
import Domain.Objects.User;
import Domain.Validation.ValidationException;
import Repository.AbstractRepositories.Repository;
import Repository.Exceptions.RepositoryException;

import java.time.LocalDateTime;
import java.util.*;

public class BasicFriendshipService {
    protected final Repository<Pair<Long>, Friendship> friendshipRepo;
    private final Repository<Long, User> userRepo;
    public BasicFriendshipService(Repository<Pair<Long>, Friendship> repo, Repository<Long, User> userRepo) {
        this.friendshipRepo = repo;
        this.userRepo = userRepo;
    }
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
    public int communityCount(){
        Map<Long,Boolean> idTracker = new HashMap<>();
        for(User u : userRepo.findAll()){
            idTracker.put(u.getId(),false);
        }
        int count = 0;
        for(User u : userRepo.findAll()){
            if(idTracker.get(u.getId()) == false) {
                count += 1;
                BFS(idTracker, u.getId());
            }
        }
        return count;
    }
    public ArrayList<User> getMostSociableCommunity(){
        Map<Long,Integer> idToPosition = new HashMap<>();
        Map<Integer,Long> positionToId = new HashMap<>();
        int position = 0;
        for(User u : userRepo.findAll()){
            idToPosition.put(u.getId(),position);
            positionToId.put(position, u.getId());
            position += 1;
        }
        int maxLenRoad = 0;
        int maxPosition = -1;

        int[][] adjacencyMatrix = new int[position][position];
        for(Friendship f : friendshipRepo.findAll()){
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
        for(User u : userRepo.findAll()){
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
