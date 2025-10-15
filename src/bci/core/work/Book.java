package bci.core.work;

import bci.core.Creator;
import bci.core.exception.InvalidArgumentsException;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Book extends Work {

    @Serial
    private static final long serialVersionUID = -7478614181433616768L;

    private final String _isbn;
    private final List<Creator> _authors;

    private Book(Builder builder) {
        super(builder);
        this._isbn = builder._isbn;
        this._authors = builder._authors;
    }

    @Override
    protected Collection<Creator> getCreators() {
        return Collections.unmodifiableList(_authors);
    }

    @Override
    public String toString() {
        String strAuthors = _authors.stream()
                .map(Creator::getName)
                .collect(Collectors.joining("; "));

        return String.format("%s - %s - %s", super.getGeneralDescription(), strAuthors, _isbn);
    }

    public static class Builder extends Work.Builder<Book, Builder> {
        private String _isbn;
        private List<Creator> _authors;

        public Builder() {
            this._type = WorkType.BOOK;
        }

        public Builder isbn(String isbn) throws InvalidArgumentsException {
            if (isbn == null || isbn.isBlank()) {
                throw new InvalidArgumentsException("ISBN cannot be blank");
            }
            this._isbn = isbn;
            return self();
        }

        public Builder authors(List<Creator> authors) throws InvalidArgumentsException {
            if (authors == null || authors.isEmpty() || authors.stream().anyMatch(Objects::isNull)) {
                throw new InvalidArgumentsException("Authors list cannot be null, empty, or contain null elements.");
            }
            _authors = authors.stream().distinct().toList();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Book build() {
            if (_id == null | _title == null || _price == null || _category == null ||
                    _totalCopies == null || _isbn == null || _authors == null) {
                throw new IllegalStateException("All fields must be set before building Book");
            }
            return new Book(this);
        }
    }
}
