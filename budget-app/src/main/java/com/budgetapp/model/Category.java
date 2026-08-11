package com.budgetapp.model;

/**
 * Equivalente della costante Transaction::CATEGORIES del progetto PHP.
 */
public enum Category {
    AFFITTO("Affitto"),
    STIPENDIO("Stipendio"),
    SPESE_GENERALI("Spese Generali"),
    ALTRO("Altro");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Category fromLabel(String label) {
        for (Category c : values()) {
            if (c.label.equals(label)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria non valida: " + label);
    }

    public static boolean isValid(String label) {
        for (Category c : values()) {
            if (c.label.equals(label)) {
                return true;
            }
        }
        return false;
    }
}
