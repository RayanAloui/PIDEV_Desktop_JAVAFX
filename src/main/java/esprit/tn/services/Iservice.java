package esprit.tn.services;

import java.util.List;

public interface Iservice <T>{

    public void ajouter(T t);
    public void modifier(T t,int id);

    public void supprimer(int id);

    public List<T> getall();

    public T getone(int id);

    public T getoneByEmail(String email);

}
