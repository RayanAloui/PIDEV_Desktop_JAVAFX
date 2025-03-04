package esprit.tn.services;
import java.util.List;

public interface Iservice1 <T> {
    public void ajouter(T t);

    public void modifier(T t);

    public void supprimer(int id);

    public List<T> getall();

    public T getone(int id);
}
