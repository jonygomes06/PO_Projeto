package bci.app.work;

/**
 * Messages for menu interactions.
 */
interface Message {

    /**
     * <code>noSuchWork</code> generates the message indicating that a work does not exist.
     *
     * @param idWork the identifier of the work (<code>int</code>)
     * @return <code>String</code> with the message
     */
    static String noSuchWork(int idWork) {
        return "A obra " + idWork + " não existe.";
    }

    /**
     * <code>invalidWorkId</code> generates the message indicating that a work number is invalid.
     *
     * @param idWork the identifier of the work (<code>int</code>)
     * @return <code>String</code> with the message
     */
    static String invalidWorkId(int idWork) {
        return "Número de obra inválido: " + idWork + ".";
    }

    /**
     * <code>workRequestedForDays</code> generates the message indicating the period (days) for which a work was requested.
     *
     * @param idWork the identifier of the work (<code>int</code>)
     * @param days   an <code>int</code> value
     * @return <code>String</code> with the message
     */
    static String workRequestedForDays(int idWork, int days) {
        return "A obra " + idWork + " foi requisitada por " + days + " dias.";
    }

    /**
     * <code>notEnoughInventory</code> generates the message indicating that the quantity of copies cannot be decremented by the specified amount (would exceed inventory).
     *
     * @param idWork the identifier of the work (<code>int</code>)
     * @param amount the amount to decrement (<code>int</code>)
     * @return <code>String</code> with the message
     */
    static String notEnoughInventory(int idWork, int amount) {
        return "A quantidade de exemplares da obra " + idWork + " não pode ser decrementada em " + amount + " (excederia o inventário).";
    }
}
