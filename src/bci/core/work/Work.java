package bci.core.work;

import bci.core.exception.InvalidArgumentsException;

import java.io.Serial;
import java.io.Serializable;

public abstract class Work implements Serializable {
    @Serial
    private static final long serialVersionUID = -7478614181433606768L;

    private static int _nextId = 1;

    private final int _id;
    private final String _title;
    private final int _price;
    private final WorkCategory _category;
    private int _totalCopies;
    private int _availableCopies;
    private final WorkType _type;

    protected Work(Builder<?, ?> builder) {
        this._id = _nextId++;
        this._title = builder._title;
        this._price = builder._price;
        this._category = builder._category;
        this._totalCopies = builder._totalCopies;
        this._availableCopies = builder._totalCopies;
        this._type = builder._type;
    }

    public int getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

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
        protected String _title;
        protected Integer _price;
        protected WorkCategory _category;
        protected Integer _totalCopies;
        protected WorkType _type;

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
