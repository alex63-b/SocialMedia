package Domain.Objects;

import java.util.Objects;

public class Pair<E extends Comparable<E>> {
    private final E firstElement;
    private final E secondElement;

    public E getFirstElement() {
        return firstElement;
    }

    public E getSecondElement() {
        return secondElement;
    }
    public Pair(E firstElement, E secondElement){
        if(firstElement.compareTo(secondElement) <= 0) {
            this.firstElement = firstElement;
            this.secondElement = secondElement;
        }
        else{
            this.secondElement= firstElement;
            this.firstElement = secondElement;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstElement,secondElement);
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Pair<?> pair){
            return pair.firstElement == this.firstElement && pair.secondElement == this.secondElement;
        }
        return false;
    }
}
