package bci.app.request;

/**
 * Provides factory methods for building Portuguese user-facing messages.
 * Not intended to be implemented; contains only static methods.
 */
interface Message {

    /**
     * Builds a fine message for a user.
     *
     * @param idUser user identifier
     * @param amount fine amount in EUR
     * @return Portuguese message informing the user about the fine
     */
    static String showFine(int idUser, int amount) {
        return "O utente " + idUser + " deve pagar uma multa de EUR " + amount + ".";
    }

    /**
     * Builds a message with the due return day for a work item.
     *
     * @param idWork work identifier
     * @param day day of the month by which the work must be returned
     * @return Portuguese message indicating the due date
     */
    static String workReturnDay(int idWork, int day) {
        return "A obra " + idWork + " deve ser devolvida até ao dia " + day + ".";
    }
}