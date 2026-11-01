package Repository.Helpers;

public class DefaultFilters{
    public static boolean filterTrue(Object element) {
        return true;
    }
    public static boolean filterFalse(Object element) {
        return false;
    }
    public static boolean filterNotEmptyLines(String line){
        return !line.isEmpty();
    }
}
