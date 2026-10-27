package cz.vse.escape.save;

/** Ukládání nebo načítání hry se nepodařilo. Zpráva je určená přímo hráči. */
public class SaveGameException extends Exception {

    private static final long serialVersionUID = 1L;

    public SaveGameException(String message) {
        super(message);
    }

    public SaveGameException(String message, Throwable cause) {
        super(message, cause);
    }
}
