package com.stockguard.stockguard.model.enums;

public enum JourSemaine {
    LUNDI("Lundi"),
    MARDI("Mardi"),
    MERCREDI("Mercredi"),
    JEUDI("Jeudi"),
    VENDREDI("Vendredi"),
    SAMEDI("Samedi"),
    DIMANCHE("Dimanche");

    private final String libelle;

    JourSemaine(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static JourSemaine fromString(String jour) {
        if (jour == null) {
            return null;
        }

        // Essayer de matcher avec le nom de l'enum
        try {
            return JourSemaine.valueOf(jour.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Essayer de trouver par libellé
            for (JourSemaine js : JourSemaine.values()) {
                if (js.getLibelle().equalsIgnoreCase(jour)) {
                    return js;
                }
            }
            throw new IllegalArgumentException("Jour de semaine invalide: " + jour);
        }
    }

    public static JourSemaine fromDayOfWeek(java.time.DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return LUNDI;
            case TUESDAY: return MARDI;
            case WEDNESDAY: return MERCREDI;
            case THURSDAY: return JEUDI;
            case FRIDAY: return VENDREDI;
            case SATURDAY: return SAMEDI;
            case SUNDAY: return DIMANCHE;
            default: throw new IllegalArgumentException("Jour de semaine invalide: " + dayOfWeek);
        }
    }

    public static JourSemaine fromLocalDate(java.time.LocalDate date) {
        return fromDayOfWeek(date.getDayOfWeek());
    }

    public java.time.DayOfWeek toDayOfWeek() {
        switch (this) {
            case LUNDI: return java.time.DayOfWeek.MONDAY;
            case MARDI: return java.time.DayOfWeek.TUESDAY;
            case MERCREDI: return java.time.DayOfWeek.WEDNESDAY;
            case JEUDI: return java.time.DayOfWeek.THURSDAY;
            case VENDREDI: return java.time.DayOfWeek.FRIDAY;
            case SAMEDI: return java.time.DayOfWeek.SATURDAY;
            case DIMANCHE: return java.time.DayOfWeek.SUNDAY;
            default: throw new IllegalStateException("Jour de semaine invalide: " + this);
        }
    }
}