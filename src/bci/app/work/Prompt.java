package bci.app.work;

/**
 * Messages for menu interactions.
 */
public interface Prompt {

    /**
     * @return message with request for work id.
     */
    static String workId() {
        return "Introduza o número da obra: ";
    }
    /**
     * @return message requesting the amount to update.
     */
    static String amountToUpdate() {
        return "Introduza a quantidade a actualizar: ";
    }

    /**
     * <code>searchTerm</code> generates the message indicating that a search term
     * must be provided to the system.
     *
     * @return <code>String</code> with the message
     */
    static String searchTerm() {
        return "Introduza o termo de pesquisa: ";
    }
    /**
     * @return message requesting the creator's identifier.
     */
    static String creatorId() {
        return "Introduza o identificador do criador: ";
    }
}
