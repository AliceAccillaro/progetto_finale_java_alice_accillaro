package it.aulab.progetto_finale_aliceaccillaro.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import it.aulab.progetto_finale_aliceaccillaro.models.Article;
import it.aulab.progetto_finale_aliceaccillaro.models.Image;
import it.aulab.progetto_finale_aliceaccillaro.repositories.ImageRepository;
import it.aulab.progetto_finale_aliceaccillaro.utils.StringManipulation;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    @Value("${supabase.image}")
    private String supabaseImage;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    @Async
    public CompletableFuture<Image> saveImage(
            MultipartFile file,
            Article article) {

        try {

            String extension = StringManipulation
                    .getFileExtension(file.getOriginalFilename());

            String fileName =
                    UUID.randomUUID().toString() + extension;

            String url =
                    supabaseUrl + supabaseBucket + fileName;

            HttpHeaders headers = new HttpHeaders();

            headers.set(
                    "apikey",
                    supabaseKey
            );

            headers.setContentType(
                    MediaType.parseMediaType(
                            file.getContentType()
                    )
            );

            HttpEntity<byte[]> requestEntity =
                    new HttpEntity<>(
                            file.getBytes(),
                            headers
                    );

            RestTemplate restTemplate =
                    new RestTemplate();

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            requestEntity,
                            String.class
                    );

            if (response.getStatusCode().is2xxSuccessful()) {

                Image image = new Image();

                image.setPath(
                        supabaseUrl
                                + supabaseImage
                                + fileName
                );

                image.setArticle(article);

                return CompletableFuture.completedFuture(
                        imageRepository.save(image)
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteImage(
            Image image) {

        try {

            String fileName =
                    image.getPath()
                            .substring(
                                    image.getPath()
                                            .lastIndexOf("/") + 1
                            );

            String url =
                    supabaseUrl
                            + supabaseBucket
                            + fileName;

            HttpHeaders headers =
                    new HttpHeaders();

            headers.set(
                    "apikey",
                    supabaseKey
            );

            HttpEntity<Void> requestEntity =
                    new HttpEntity<>(headers);

            RestTemplate restTemplate =
                    new RestTemplate();

            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class
            );

            imageRepository.deleteByPath(
                    image.getPath()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(null);
    }
}