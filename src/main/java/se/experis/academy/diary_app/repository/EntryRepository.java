package se.experis.academy.diary_app.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.experis.academy.diary_app.model.Entry;

import java.util.List;

/**
 * Represents all the methods available for the API
 */
@Repository
public interface EntryRepository extends CrudRepository<Entry, Long> {

    /**
     * Finds all the entries and sorts them by date
     * @return list of entries
     */
    List<Entry> findByActiveTrueOrderByDateDesc();
    
    List<Entry> findEntriesByUserIDOrderById(long userID);
    /**
     * finds entry by id
     * @param id long
     * @return entry
     */
    Entry findById(long id);


    /**
     * Creates or replaces entry
     * @param s Entry
     * @param <S>
     * @return Entry
     */
    @Override
    <S extends Entry> S save(S s);

    List<Entry> findEntriesByUserIDOrderByDateDesc(Long id);
}

