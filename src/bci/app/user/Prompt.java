package bci.app.user;

/**
 * Provides localized prompt messages for user input related to users.
 */
public interface Prompt {

    /**
     * Returns the prompt for the user's identifier.
     *
     * @return prompt asking for the user's ID
     */
    static String userId() {
        return "Introduza o número de utente: ";
    }

    /**
     * Returns the prompt for the user's name.
     *
     * @return prompt asking for the user's name
     */
    static String userName() {
        return "Introduza o nome do utente: ";
    }

    /**
     * Returns the prompt for the user's e-mail address.
     *
     * @return prompt asking for the user's e-mail address
     */
    static String userEMail() {
        return "Introduza o endereço de correio do utente: ";
    }
}