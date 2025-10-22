package bci.core;

import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a creator with a name and a list of works.
 * Implements Serializable for object serialization.
 */
public class Creator implements Serializable {

    @Serial
    private static final long serialVersionUID = 5840056146680662490L;

    private final String _name;
    private final List<Work> _works;

    /**
     * Constructs a Creator with the specified name.
     *
     * @param name the name of the creator
     */
    public Creator(String name) {
        _name = name;
        _works = new ArrayList<>();
    }

    /**
     * Returns the name of the creator.
     *
     * @return the creator's name
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns an unmodifiable, alphabetically sorted list of works by title.
     *
     * @return sorted, unmodifiable list of works
     */
    public List<Work> getWorks() {
        List<Work> worksCopy = new ArrayList<>(_works);
        worksCopy.sort(Comparator.comparing(w -> w.getTitle().toLowerCase()));
        return Collections.unmodifiableList(worksCopy);
    }

    /**
     * Adds a work to the creator's list if it is not null and not already present.
     *
     * @param work the work to add
     */
    public void addWork(Work work) {
        if (work != null && !_works.contains(work)) {
            _works.add(work);
        }
    }

    /**
     * Removes a work from the creator's list.
     *
     * @param work the work to remove
     */
    public void removeWork(Work work) {
        _works.remove(work);
    }

    /**
     * Checks if the creator should be removed from the system (i.e., has no works).
     *
     * @return true if the creator has no works, false otherwise
     */
    public boolean shouldBeRemovedFromSystem() {
        return _works.isEmpty();
    }

    /**
     * Checks equality based on the creator's name.
     *
     * @param obj the object to compare
     * @return true if the object is a Creator with the same name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Creator creator && _name.equals(creator._name);
    }
}
