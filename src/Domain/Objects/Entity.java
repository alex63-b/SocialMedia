package Domain.Objects;

import java.util.Objects;

public class Entity<ID> {
    protected ID id;

    public Entity(ID new_id){
        id = new_id;
    }

    public ID getId(){
        return id;
    }

    public void setId(ID new_id){
        id = new_id;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object new_object){
        if(new_object instanceof Entity<?> entity){
            return entity.id.equals(this.id);
        }
        return false;
    }
}
