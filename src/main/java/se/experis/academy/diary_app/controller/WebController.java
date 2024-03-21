package se.experis.academy.diary_app.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import se.experis.academy.diary_app.GCM;
import se.experis.academy.diary_app.model.BlogUser;
import se.experis.academy.diary_app.model.Entry;
import se.experis.academy.diary_app.repository.EntryRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import se.experis.academy.diary_app.service.UserService;

/**
 * Class that handles all the views
 */
@Controller
public class WebController {


    private final EntryRepository entryRepository;
    private final UserService userService; // Dependency on PostService

    @Autowired // Autowires the PostService dependency via constructor injection
    public WebController(EntryRepository entryRepository,UserService userService) {
        this.entryRepository = entryRepository;
        this.userService = userService;
    }
    /**
     * Gets contacts and returns them to index.html
     * @param model
     * @return to index.html with list of contacts
     */
    @GetMapping("/index")
    public String index(Model model,Principal principal) throws Exception {
        String authUsername = null;
        if (principal != null) {
            System.err.println("here");
            authUsername = principal.getName(); // Retrieves the logged-in username
            Optional<BlogUser> optionalBlogUser = userService.findByUsername(authUsername);
            if(optionalBlogUser.isPresent()) {
                Long userId = optionalBlogUser.get().getId();
                List<Entry> entries = entryRepository.findEntriesByUserIDOrderByDateDesc(userId);

                for(Entry e : entries){
                    String msg = e.getText();
                    String img = e.getImg();
                    String masterKey = optionalBlogUser.get().getPass();
                    String decodedMsg = GCM.decrypt(msg, masterKey);
                    String decodeImg = GCM.decrypt(img, masterKey);

                    e.setText(decodedMsg);
                    e.setImg(decodeImg);
                }

                model.addAttribute("entries", entries);
            }
        }
        return "index";
    }
    //test
    @GetMapping("/") // Handles GET requests to "/login"
    public String redirect() {
            return "login"; // Renders the login view if the user is not logged in
    }
    @GetMapping("/login") // Handles GET requests to "/login"
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/index"; // Redirects to the root context if the user is already logged in
        } else {
            return "login"; // Renders the login view if the user is not logged in
        }
    }

}

