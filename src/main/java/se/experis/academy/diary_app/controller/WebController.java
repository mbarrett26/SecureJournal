package se.experis.academy.diary_app.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import se.experis.academy.diary_app.model.Entry;
import se.experis.academy.diary_app.repository.EntryRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Class that handles all the views
 */
@Controller
public class WebController {

    @Autowired
    EntryRepository entryRepository;

    /**
     * Gets contacts and returns them to index.html
     * @param model
     * @return to index.html with list of contacts
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Entry> entries = entryRepository.findByActiveTrueOrderByDateDesc();
        model.addAttribute("entries", entries);
        return "index";
    }

}

