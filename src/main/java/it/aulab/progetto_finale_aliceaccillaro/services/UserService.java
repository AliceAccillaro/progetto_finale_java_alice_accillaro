package it.aulab.progetto_finale_aliceaccillaro.services;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.progetto_finale_aliceaccillaro.dtos.UserDto;
import it.aulab.progetto_finale_aliceaccillaro.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    void saveUser(UserDto userDto, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response);

    User findUserByEmail(String email);

    User find(Long id);
}
