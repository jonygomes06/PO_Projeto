package bci.core.work;

public enum WorkType {
    BOOK("Livro"),
    DVD("DVD");

    private final String _label;

    WorkType(String label) {
        _label = label;
    }

    @Override
    public String toString() {
        return _label;
    }
}
