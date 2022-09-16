package diginamic.gdm.services;

import diginamic.gdm.dao.Nature;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface to be implemented by a mission nature service class
 *
 * @author DorianBoel
 */
public interface NatureService {

    /**
     * Gets the full list of registered mission natures.
     *
     * @return A list of all natures
     */
    List<Nature> list();

    /**
     * Saves a new {@link Nature} instance.
     * The nature will have its start date of validity set to now
     *
     * @param nature The new nature to be registered
     */
    void create(Nature nature);

    /**
     * Gets a specific registered mission nature.
     *
     * @param id The id corresponding to the nature to get
     * @return The registered nature corresponding to the given id
     */
    Nature read(int id);

    /**
     * Updates the data for a specific registered mission nature.
     * The nature will have its start date of validity set to now
     *
     * @param id     The id corresponding to the nature to update
     * @param nature The nature to update with modified info
     * @return The resulting nature with updated info
     */
    Nature update(int id, Nature nature);

    /**
     * Deletes a specific registered mission nature.
     *
     * @param id The id corresponding to the nature to delete
     */
    void delete(int id);

    /**
     * Return all the currently active natures, ie whose endDate is null
     *
     * @return
     */
    List<Nature> getActiveNatures();

    /**
     * Check if a nature was or is active at the given date
     *  if the date is null, returns true if the nature is currently active
     *
     * @param nature
     * @param date
     * @return
     */
    boolean isNatureActive(Nature nature, LocalDateTime date);

    /**
     * Returns true if the nature is currently active
     *
     * @param nature
     * @return
     */
    default boolean isNatureActive(Nature nature) {
        return isNatureActive(nature, null);
    }
    /**
     * The end date must be later than the start date, or null
     *
     * @param nature
     * @return
     */
    boolean isAValidNature(Nature nature);

    /**
     * Check if no other nature has the same description
     *
     * @param nature
     * @return true if no nature has the same description, false otherwise
     */
    boolean canBeAdded(Nature nature);

    /**
     * Check that the id is coherent with the given nature (maybe this will be removed)
     * Check that the given nature is the last of the natures with the same description (active or not)
     *
     * @param id
     * @param nature
     * @return true if the nature can be updated, on condition it is valid
     */
    boolean canBeUpdated(int id, Nature nature);


    /**
     * For a nature to be deleted, created or modified, this must be false
     * If true, the nature must be rendered inactive instead
     *
     * @param nature
     * @return true if a mission has this nature
     */
    boolean isThisNatureInUse(Nature nature);

}
