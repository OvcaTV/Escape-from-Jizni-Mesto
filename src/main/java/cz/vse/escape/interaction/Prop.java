package cz.vse.escape.interaction;

/** Věc v prostředí (automat, dveře...), která se dá používat, ale neumí mluvit. */
public abstract class Prop extends Interactable {

    protected Prop(String displayName, String... aliases) {
        super(displayName, aliases);
    }
}
