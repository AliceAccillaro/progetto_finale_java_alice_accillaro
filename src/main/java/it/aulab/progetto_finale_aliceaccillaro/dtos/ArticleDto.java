package it.aulab.progetto_finale_aliceaccillaro.dtos;

import java.time.LocalDate;

import it.aulab.progetto_finale_aliceaccillaro.models.Category;
import it.aulab.progetto_finale_aliceaccillaro.models.Image;
import it.aulab.progetto_finale_aliceaccillaro.models.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ArticleDto {

    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private LocalDate publishDate;
    private Boolean isAccepted;
    private User user;
    private Category category;
    private Image image;
}
