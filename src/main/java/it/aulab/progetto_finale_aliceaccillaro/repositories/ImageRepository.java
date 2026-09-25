package it.aulab.progetto_finale_aliceaccillaro.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import it.aulab.progetto_finale_aliceaccillaro.models.Image;

public interface ImageRepository extends ListCrudRepository<Image, Long> {

    @Transactional
    void deleteByPath(String path);
}