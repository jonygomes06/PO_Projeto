package bci.core.work;

import bci.core.Creator;
import bci.core.exception.InvalidArgumentsException;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

public class Dvd extends Work {

    @Serial
    private static final long serialVersionUID = -7471614191533616768L;

    private final String _igac;
    private final Creator _director;

    private Dvd(Builder builder) {
        super(builder);
        this._igac = builder._igac;
        this._director = builder._director;
    }

    @Override
    protected Collection<Creator> getCreators() {
        return Collections.singleton(_director);
    }

    @Override
    public String toString() {
        String directorName = (_director != null) ? _director.getName() : "Unknown Director";
        return String.format("%s - %s - %s", super.getGeneralDescription(), directorName, _igac);
    }

    public static class Builder extends Work.Builder<Dvd, Builder> {
        private String _igac;
        private Creator _director;

        public Builder() {
            this._type = WorkType.DVD;
        }

        public Builder igac(String igac) throws InvalidArgumentsException {
            if (igac == null || igac.isBlank()) {
                throw new InvalidArgumentsException("IGAC cannot be blank");
            }
            this._igac = igac;
            return self();
        }

        public Builder director(Creator director) throws InvalidArgumentsException {
            if (director == null) {
                throw new InvalidArgumentsException("Director cannot be null");
            }
            this._director = director;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Dvd build() {
            if (_title == null || _price == null || _category == null ||
                    _totalCopies == null || _igac == null || _director == null) {
                throw new IllegalStateException("All fields must be set before building Dvd");
            }
            return new Dvd(this);
        }
    }
}
