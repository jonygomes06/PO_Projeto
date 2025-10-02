package bci.core.work;

import bci.core.Creator;

import java.io.Serial;
import java.util.List;
import java.util.stream.Collectors;

public class Book extends Work {

    @Serial
    private static final long serialVersionUID = -7478614181433616768L;

    private final String _isbn;
    private final List<Creator> _authors;

    public Book(String title, int price, WorkCategory category, int totalCopies, String isbn, List<Creator> authors) {
        super(title, price, category, totalCopies, WorkType.BOOK);
        _isbn = isbn;
        _authors = authors.stream().distinct().toList();
    }

    @Override
    public List<Creator> getCreators() {
        return _authors;
    }

    @Override
    public String toString() {
        String strAuthors = _authors
                .stream()
                .map(Creator::getName)
                .collect(Collectors.joining("; "));

        return String.format("%s - %s - %s", super.getGeneralDescription(), strAuthors, _isbn);
    }
}
