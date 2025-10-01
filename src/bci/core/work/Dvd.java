package bci.core.work;

import bci.core.Creator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Dvd extends Work {
    @Serial
    private static final long serialVersionUID = -7471614191533616768L;

    private final String _igac;
    private final Creator _director;

    public Dvd(String title, int price, WorkCategory category, int totalCopies, String igac, Creator director) {
        super(title, price, category, totalCopies, WorkType.DVD);
        _igac = igac;
        _director = director;
    }

    @Override
    public List<Creator> getCreators() {
        return Collections.singletonList(_director);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s", super.getGeneralDescription(), _director, _igac);
    }
}
