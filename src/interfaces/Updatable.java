package interfaces;

public interface Updatable <T> {
    public void add(T update);
    public void applyUpdates();
}
