package com.stockguard.stockguard.model.enums;

public enum Unite {
    KG("Kilogramme"),
    G("Gramme"),
    L("Litre"),
    ML("Millilitre"),
    UNITE("Unité"),
    CARTON("Carton"),
    PALETTE("Palette");

    private final String libelle;

    Unite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Unite fromString(String unite) {
        try {
            return Unite.valueOf(unite.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unité invalide: " + unite);
        }
    }
}
