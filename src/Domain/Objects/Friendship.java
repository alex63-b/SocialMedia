package Domain.Objects;

import java.time.LocalDateTime;

public class Friendship extends Entity<Pair<Long>> {
    private final User friend1;
    private final User friend2;
    private final LocalDateTime date;
    public Friendship(User friend1, User friend2, LocalDateTime date) {
        super(new Pair<>(friend1.getId(),friend2.getId()));
        this.date = date;
        this.friend1 = friend1;
        this.friend2 = friend2;
    }
    @Override
    public String toString(){
        String hour = Integer.toString(date.getHour());
        if(date.getHour() < 10){
            hour = "0" + hour;
        }
        String minute = Integer.toString(date.getMinute());
        if(date.getMinute() < 10){
            minute = "0" + minute;
        }
        String second = Integer.toString(date.getSecond());
        if(date.getSecond() < 10){
            second = "0" + second;
        }
        return "Friendship between " + friend1.getName() + "(ID " + friend1.getId() + ") and " + friend2.getName() + "(ID " + friend2.getId() + ") started on the date "
                + date.getDayOfMonth() + " " + date.getMonth() + " " + date.getYear() + ", at " + hour + ":" + minute + ":" + second + ";";
    }
    public User getFriend1() {
        return friend1;
    }

    public User getFriend2() {
        return friend2;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
