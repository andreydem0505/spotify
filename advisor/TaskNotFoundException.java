package advisor;

public class TaskNotFoundException extends Exception {
    @Override
    public String toString() {
        return "No such command";
    }
}
