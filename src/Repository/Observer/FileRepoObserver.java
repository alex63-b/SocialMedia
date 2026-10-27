package Repository.Observer;

/**
 * This interface defines an observer notified when a repository changes.
 */
public interface FileRepoObserver {
    /**
     * This function handles a repository update notification.
     */
    void update();
}
