package Repository.Filters;

import Domain.Objects.Entity;

public class DefaultFilters{
    public static boolean filterTrue(Object element) {
        return true;
    }
    public static boolean filterNotEmptyLines(String line){
        return !line.isEmpty();
    }
}
