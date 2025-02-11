package reclamations.services;

import java.util.List;

public interface Iservices<T> {
    void ajouter(T t); // Add an entity
    void modifier(T t); // Update an entity
    void supprimer(int id); // Delete an entity by ID
    List<T> getall(); // Get all entities
    T getone(int id); // Get one entity by ID
}