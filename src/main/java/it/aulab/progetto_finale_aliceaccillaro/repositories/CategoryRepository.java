package it.aulab.progetto_finale_aliceaccillaro.repositories;

import org.springframework.data.repository.ListCrudRepository;

import it.aulab.progetto_finale_aliceaccillaro.models.Category;

public interface CategoryRepository extends ListCrudRepository<Category, Long> {

}