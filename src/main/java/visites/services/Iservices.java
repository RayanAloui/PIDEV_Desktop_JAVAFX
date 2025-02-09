package visites.services;
import java.util.List;
public interface Iservices<T> {
    public void ajouter(T t);

    public void modifier(T t);

    public void supprimer(int id);

    public List<T> getall();

    public T getone(int id);
}

