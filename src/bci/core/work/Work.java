package bci.core.work;

import bci.core.Creator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public abstract class Work implements Serializable {
    @Serial
    private static final long serialVersionUID = -7478614181433606768L;

    private static int _nextId = 0;

    private final int _id;
    private final String _title;
    private final int _price;
    private final WorkCategory _category;
    private int _totalCopies;
    private int _availableCopies;
    private final WorkType _type;


    protected Work(String title, int price, WorkCategory category, int totalCopies, WorkType type) {
        _id = _nextId++;
        _title = title;
        _price = price;
        _category = category;
        _totalCopies = totalCopies;
        _availableCopies = totalCopies;
        _type = type;
    }

    public String getTitle() {
        return _title;
    }

    protected String getGeneralDescription() {
        return String.format(" %d - %d de %d - %s - %s - %d - %s",
                _id, _availableCopies, _totalCopies, _type.toString(), _title, _price, _category.toString());
    }

    public abstract List<Creator> getCreators();

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Work work && _id == work._id;
    }

    @Override
    public abstract String toString();
}
