package visites.services;
import java.sql.SQLException;
import java.util.List;
public interface Iservices<T> {
    public void ajouter(T t)throws SQLException;

    public void modifier(T t);

    public void supprimer(int id);

    public List<T> getall();

    public T getone(int id);
}

