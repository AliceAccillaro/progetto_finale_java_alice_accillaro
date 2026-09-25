package it.aulab.progetto_finale_aliceaccillaro.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.progetto_finale_aliceaccillaro.dtos.ArticleDto;
import it.aulab.progetto_finale_aliceaccillaro.dtos.UserDto;
import it.aulab.progetto_finale_aliceaccillaro.models.User;
import it.aulab.progetto_finale_aliceaccillaro.models.Article;
import it.aulab.progetto_finale_aliceaccillaro.repositories.ArticleRepository;
import it.aulab.progetto_finale_aliceaccillaro.repositories.CareerRequestRepository;
import it.aulab.progetto_finale_aliceaccillaro.services.ArticleService;
import it.aulab.progetto_finale_aliceaccillaro.services.CategoryService;
import it.aulab.progetto_finale_aliceaccillaro.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Home
    @GetMapping("/")
    public String home(Model viewModel) {

        viewModel.addAttribute(
                "title",
                "Home"
        );

        List<ArticleDto> articles = new ArrayList<ArticleDto>();
        for (Article article: articleRepository.findByIsAcceptedTrue()) {
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

        Collections.sort(
                articles,
                Comparator
                        .comparing(ArticleDto::getPublishDate)
                        .reversed()
        );

        if (articles.size() > 3) {
            articles = articles.subList(0, 3);
        }

        viewModel.addAttribute(
                "articles",
                articles
        );

        return "home";
    }

    // Register
    @GetMapping("/register")
    public String showRegistrationForm(
            Model viewModel) {

        UserDto userDto =
                new UserDto();

        viewModel.addAttribute(
                "user",
                userDto
        );

        viewModel.addAttribute(
                "title",
                "Register"
        );

        return "auth/register";
    }

    // Login
    @GetMapping("/login")
    public String login(
            Model viewModel) {

        viewModel.addAttribute(
                "title",
                "Login"
        );

        return "auth/login";
    }

    // Salvataggio registrazione
    @PostMapping("/register/save")
    public String registration(
            @Valid
            @ModelAttribute("user")
            UserDto userDto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {

        User existingUser =
                userService.findUserByEmail(
                        userDto.getEmail()
                );

        if (existingUser != null
                && existingUser.getEmail() != null
                && !existingUser.getEmail().isEmpty()) {

            result.rejectValue(
                    "email",
                    null,
                    "There is already an account registered with the same email"
            );
        }

        if (result.hasErrors()) {

            model.addAttribute(
                    "user",
                    userDto
            );

            return "auth/register";
        }

        userService.saveUser(
                userDto,
                redirectAttributes,
                request,
                response
        );

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Registrazione avvenuta!"
        );

        return "redirect:/";
    }

    // Ricerca articoli per autore
    @GetMapping("/search/{id}")
    public String userArticlesSearch(
            @PathVariable("id") Long id,
            Model viewModel) {

        User user =
                userService.find(id);

        viewModel.addAttribute(
                "title",
                "Tutti gli articoli trovati per utente "
                        + user.getUsername()
        );

        List<ArticleDto> articles =
                articleService.searchByAuthor(user);

        List<ArticleDto> acceptedArticles = articles.stream().filter(article -> Boolean.TRUE.equals(article.getIsAccepted())).collect(Collectors.toList());

        viewModel.addAttribute(
                "articles",
                acceptedArticles
        );

        return "article/articles";
    }

    //Rotta per la dashboard dell'admin
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model viewModel) {
        viewModel.addAttribute("title", "Richieste ricevute");
        viewModel.addAttribute("requests", careerRequestRepository.findByIsCheckedFalse());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "admin/dashboard";
    }
    //Rotta per la dashboard del revisor
    @GetMapping("/revisor/dashboard")
    public String revisorDashboard(Model viewModel) {
        viewModel.addAttribute("title", "Articoli da revisionare");
        viewModel.addAttribute("articles", articleRepository.findByIsAcceptedIsNull());
        return "revisor/dashboard";
    }
    //Rotta per la dashboard del writer
    @GetMapping("/writer/dashboard")
    public String writerDashboard(Model viewModel, Principal principal) {
        viewModel.addAttribute("title", "I tuoi articoli");

        List<ArticleDto> userArticles = articleService.readAll()
                                                .stream()
                                                .filter(article -> article.getUser().getEmail().equals(principal.getName()))
                                                .toList();

        viewModel.addAttribute("articles", userArticles);
        return "writer/dashboard";
    }
}
