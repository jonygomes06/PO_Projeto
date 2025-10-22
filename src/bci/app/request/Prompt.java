package bci.app.request;

/**
 * Utility interface that provides user-facing prompt messages related to
 * fine payment and return notification preferences.
 * <p>
 * This interface contains only static helper methods and is not meant to be instantiated.
 */
interface Prompt {

    /**
     * Returns the prompt asking whether the patron wants to pay a fine.
     *
     * @return prompt text asking for a yes/no choice about fine payment
     */
    static String finePaymentChoice() {
        return "O utente deseja pagar multa (s/n)? ";
    }

    /**
     * Returns the prompt asking whether the patron wants to be notified when a copy is returned.
     *
     * @return prompt text asking for a yes/no choice about return notifications
     */
    static String returnNotificationPreference() {
        return "Deseja ser avisado quando algum exemplar for devolvido (s/n)? ";
    }
}
