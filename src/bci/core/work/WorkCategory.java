package bci.core.work;

public enum WorkCategory {
    REFERENCE("Referência"),
    FICTION("Ficção"),
    SCITECH("Técnica e Científica");

    private final String _label;

    WorkCategory(String label) {
        _label = label;
    }

    @Override
    public String toString() {
        return _label;
    }
}
