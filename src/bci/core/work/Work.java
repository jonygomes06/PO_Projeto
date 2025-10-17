package bci.core.work;

import bci.core.Creator;
import bci.core.request.Request;
import bci.core.exception.InvalidArgumentsException;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class Work implements Serializable {
    @Serial
    private static final long serialVersionUID = -7478614181433606768L;

    private final int _id;
    private final String _title;
    private final int _price;
    private final WorkCategory _category;
    private int _totalCopies;
    private int _availableCopies;
    private final WorkType _type;
    private final List<Request> _requests;

    protected Work(Builder<?, ?> builder) {
        _id = builder._id;
        _title = builder._title;
        _price = builder._price;
        _category = builder._category;
        _totalCopies = builder._totalCopies;
        _availableCopies = builder._totalCopies;
        _type = builder._type;
        _requests = new LinkedList<>();
    }

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public int getPrice() {
        return _price;
    }

    public WorkCategory getCategory() {
        return _category;
    }

    public int getTotalCopies() {
        return _totalCopies;
    }

    public int getAvailableCopies() {
        return _availableCopies;
    }

    public boolean hasTerm(String term) {
        if (_title.toLowerCase().contains(term)) {
            return true;
        }
        for (Creator creator : getCreators()) {
            if (creator.getName().toLowerCase().contains(term)) {
                return true;
            }
        }
        return false;
    }

    public void requestWork(Request request) {
        _requests.add(request);
        _availableCopies--;
    }

    public void returnWork() {
        _availableCopies++;
    }

    protected abstract Collection<Creator> getCreators();

    protected String getGeneralDescription() {
        return String.format("%d - %d de %d - %s - %s - %d - %s",
                _id, _availableCopies, _totalCopies, _type.toString(), _title, _price, _category.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Work work && _id == work._id;
    }

    @Override
    public abstract String toString();

    /**
     * Generic abstract Builder to be extended by concrete Work subclasses.
     * @param <T> Concrete Work type
     * @param <B> Concrete Builder type
     */
    public static abstract class Builder<T extends Work, B extends Builder<T, B>> {
        protected Integer _id;
        protected String _title;
        protected Integer _price;
        protected WorkCategory _category;
        protected Integer _totalCopies;
        protected WorkType _type;

        public B id(int id) throws InvalidArgumentsException {
            if (id < 1) {
                throw new InvalidArgumentsException("ID must be greater than 0");
            }
            this._id = id;
            return self();
        }

        public B title(String title) throws InvalidArgumentsException {
            if (title == null || title.isBlank()) {
                throw new InvalidArgumentsException("Title cannot be blank");
            }
            this._title = title;
            return self();
        }

        public B price(int price) throws InvalidArgumentsException {
            if (price <= 0) {
                throw new InvalidArgumentsException("Price must be positive");
            }
            this._price = price;
            return self();
        }

        public B category(WorkCategory category) throws InvalidArgumentsException {
            if (category == null) {
                throw new InvalidArgumentsException("Category cannot be null");
            }
            this._category = category;
            return self();
        }

        public B totalCopies(int totalCopies) throws InvalidArgumentsException {
            if (totalCopies <= 0) {
                throw new InvalidArgumentsException("Total copies must be positive");
            }
            this._totalCopies = totalCopies;
            return self();
        }

        protected abstract B self();

        public abstract T build();
    }
}
