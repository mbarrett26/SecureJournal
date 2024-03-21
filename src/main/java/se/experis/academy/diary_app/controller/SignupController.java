package se.experis.academy.diary_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import se.experis.academy.diary_app.model.BlogUser;
import se.experis.academy.diary_app.service.UserService;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("blogUser") // Controls Signup Functionality
public class SignupController {

    private final UserService userService;

    @Autowired // Autowires the UserService dependency via constructor injection
    public SignupController(UserService userService) {
        this.userService = userService;
    }

    // Displays the registration form
    @GetMapping("/signup")
    public String getRegisterForm(Model model) {
        // Creates a new BlogUser instance and passes it to the registerForm view template
        BlogUser blogUser = new BlogUser();
        model.addAttribute("blogUser", blogUser);
        return "registerForm"; // Returns the name of the view to be rendered
    }

    // Handles the submission of the registration form
    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute BlogUser blogUser, BindingResult bindingResult, SessionStatus sessionStatus) throws RoleNotFoundException {
        // Code to handle registration of a new user...

        // Check if the username is available
        if (userService.findByUsername(blogUser.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.username","Username is already registered, please try another one");
            System.err.println("Username already taken error message");
        }

        // Validate user's fields
        if (bindingResult.hasErrors()) {
            System.err.println("New user did not validate");
            return "registerForm"; // Returns the registration form to display errors
        }

        // Validate Password
        String password = blogUser.getPassword();
        List<String> missingRequirements = new ArrayList<>();

        if(password != null && password.length() < 8){
            missingRequirements.add("at least 8 Characters Long");
        }
        if(password != null && !password.matches(".*\\d.*")){
            missingRequirements.add("at least one number");
        }
        if(password != null && !password.matches(".*[A-Z].*")){
            missingRequirements.add("at least one Uppercase Letter");
        }
        if(password != null && !password.matches(".*[a-z].*")){
            missingRequirements.add("at least one Lowercase Letter");
        }
        if(password != null && !password.matches(".*[!@#$%^&*()-+=].*")){
            missingRequirements.add("at least one Special Character ie.-_+@!#");
        }

        if (!missingRequirements.isEmpty()){
            String errorMsg = "Password Must be: ";
            errorMsg += String.join(",", missingRequirements);
            bindingResult.rejectValue("password", "error.password", errorMsg);
            return "registerForm";
        }

        // Persist the new blog user
        this.userService.saveNewUser(blogUser);

        // Create Authentication token and log in after registering the new blog user
        Authentication auth = new UsernamePasswordAuthenticationToken(blogUser, blogUser.getPassword(), blogUser.getAuthorities());
        System.err.println("AuthToken: " + auth); // For testing and debugging purposes
        SecurityContextHolder.getContext().setAuthentication(auth); // Sets authentication in SecurityContextHolder
        System.err.println("SecurityContext Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal()); // For testing and debugging purposes
        sessionStatus.setComplete(); // Marks the session as complete

        return "redirect:/"; // Redirects to the home page after successful registration
    }
}
