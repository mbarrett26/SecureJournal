package se.experis.academy.diary_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.experis.academy.diary_app.model.BlogUser;
import se.experis.academy.diary_app.model.Entry;
import se.experis.academy.diary_app.model.Response;
import se.experis.academy.diary_app.repository.EntryRepository;
import se.experis.academy.diary_app.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Represents the Entry API
 */
@RestController
@RequestMapping("/api")
public class EntryController {


    private final EntryRepository entryRepository;
    private final UserService userService;

    @Autowired
    public EntryController(EntryRepository entryRepository, UserService userService) {
        this.entryRepository = entryRepository;
        this.userService = userService;
    }
    /**
     * Gets all the active entries and sorts them by date
     * @return ResponseEntity with all entries and message, or null and message
     */
    @GetMapping("/entries")
    public ResponseEntity<Response> getEntries(Principal principal) {
        Response response;
        HttpStatus status;
        String authUsername = null;
        //List<Entry> entries = entryRepository.findByActiveTrueOrderByDateDesc();
        if (principal != null) {
             System.err.println("here");
             authUsername = principal.getName(); // Retrieves the logged-in username
        }
        Optional<BlogUser> optionalBlogUser = userService.findByUsername(authUsername);

        List<Entry> entries = entryRepository.findEntriesByUserIDOrderByDateDesc(optionalBlogUser.get().getId());
        if (entries != null && !entries.isEmpty()){
            response = new Response(entries, "SUCCESS");
            status = HttpStatus.OK;
        } else {
            response = new Response(null, "NO ENTRIES");
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(response, status);
    }
    //test
    /**
     * Sets entry as inactive by id
     * @param id long
     * @return Response entity with null and message
     */
    @DeleteMapping("/entry/delete/{id}")
    public ResponseEntity<Response> deleteEntry(@PathVariable("id") long id) {
        Response response;
        HttpStatus status;
        if (entryRepository.existsById(id)) {
            Entry entry = entryRepository.findById(id);

            entryRepository.delete(entry);
            response = new Response(null, "DELETED");
            status = HttpStatus.OK;
        } else {
            response = new Response(null, "COULDN'T DELETE");
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(response, status);
    }

    /**
     * Creates and entry
     * @param entry entry
     * @return ResponseEntity with entry and message, otherwise null and message
     */
    @PostMapping("/entry/create")
    public ResponseEntity<Response> addEntry(@RequestBody Entry entry, Principal principal) {
        if (principal != null) {
            String authUsername = principal.getName(); // Retrieves the logged-in username
            Optional<BlogUser> optionalBlogUser = userService.findByUsername(authUsername);
            if (optionalBlogUser.isPresent()) {
                entry.setUserID(optionalBlogUser.get().getId()); // Set the user ID to the entry
                System.err.println(entry.getDate());
                Entry createdEntry = entryRepository.save(entry);
                Response response = new Response(createdEntry, "CREATED");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
        }

        // If user not found or principal is null, return error response
        Response response = new Response(null, "USER NOT FOUND OR UNAUTHORIZED");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Updates the entry
     * @param entry entry with modifications
     * @return ResponseEntity with modified entry and message, or null and message
     */
    @PatchMapping("/entry/update")
    public ResponseEntity<Response> updateEntry(@RequestBody Entry entry, Principal principal) {
        Response response;
        HttpStatus httpStatus;
        String authUsername ="";
        if (principal != null) {
            System.err.println("here");
            authUsername = principal.getName(); // Retrieves the logged-in username
        }
        Optional<BlogUser> optionalBlogUser = userService.findByUsername(authUsername);

        if (entryRepository.existsById(entry.getId())) {
            entry.setUserID(optionalBlogUser.get().getId());
            System.err.println(entry.getDate());
            Entry modifiedEntry = entryRepository.save(entry);
            response = new Response(modifiedEntry, "MODIFIED");
            httpStatus = HttpStatus.OK;
        } else {
            response = new Response(null, "ENTRY DOESN'T EXIST");
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
