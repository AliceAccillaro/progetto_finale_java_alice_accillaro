package it.aulab.progetto_finale_aliceaccillaro.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

import it.aulab.progetto_finale_aliceaccillaro.models.Article;
import it.aulab.progetto_finale_aliceaccillaro.models.Image;

public interface ImageService {

    CompletableFuture<Image> saveImage(MultipartFile file, Article article);

    CompletableFuture<Void> deleteImage(Image image);
}