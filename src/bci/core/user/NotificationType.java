package bci.core.user;

public enum NotificationType {
    DISPONIBILIDADE("DISPONIBILIDADE"),
    REQUISICAO("REQUISIÇÃO");

    private final String _label;

    NotificationType(String label) {
        _label = label;
    }

    @Override
    public String toString() {
        return _label;
    }
}
