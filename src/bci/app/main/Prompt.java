package bci.app.main;

/**
 * Messages for menu interactions.
 */
interface Prompt {

    /**
     * @return string with prompt for filename to open.
     */
    static String openFile() {
        return "Ficheiro a abrir: ";
    }

    /**
     * @return string confirming that user wants to save.
     */
    static String saveBeforeExit() {
        return "Guardar antes de fechar? ";
    }

    /**
     * @return string asking for a filename.
     */
    static String saveAs() {
        return "Guardar ficheiro como: ";
    }

    /**
     * @return string with a warning and a question.
     */
    static String newSaveAs() {
        return "Ficheiro sem nome. " + saveAs();
    }

/**
* <code>daysToAdvance</code> generates the message indicating that it is necessary
* to provide a number of days to the system, in order to advance the date. The value of
* the date is measured in days and is an integer.
*
* @return <code>String</code> with the message
*/
    static String daysToAdvance() {
        return "Introduza número de dias a avançar: ";
    }
}
