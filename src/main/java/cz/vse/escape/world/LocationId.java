package cz.vse.escape.world;

/**
 * Jednoznačné identifikátory všech místností (používají se i při ukládání hry).
 * Názvy v komentářích odpovídají Game Design Documentu.
 */
public enum LocationId {
    /** JM357B – startovní učebna. */
    JM357B,
    /** JM357A – propojená učebna s Ing. Mandíkem. */
    JM357A,
    /** Chodba ve 3. patře. */
    UPPER_CORRIDOR,
    /** WC ve 3. patře. */
    UPPER_TOILET,
    /** Kabinet vyučujícího – prohra. */
    TEACHERS_OFFICE,
    /** Chodba 2 (2. patro) – automat na kávu a spící student. */
    CORRIDOR_2,
    /** Chodba do 2. části (2. patro) – školník. */
    JANITOR_CORRIDOR,
    /** Chodba 3 (2. patro, druhá část). */
    CORRIDOR_3,
    /** Studijní oddělení (2. patro). */
    STUDY_DEPARTMENT,
    /** Chodba 4 (přízemí) – východ z budovy. */
    CORRIDOR_4,
    /** JM104 – přednášková místnost Ing. Chlapka. */
    JM104
}
