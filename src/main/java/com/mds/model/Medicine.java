package com.mds.model;

import java.util.Objects;

/**
 * Model representing an educational pharmaceutical entry.
 */
public class Medicine {

    private final int id;
    private final String name;
    private final String description;
    private final String uses;
    private final String sideEffects;
    private final String precautions;
    private final String referenceSource;

    public Medicine(int id, String name, String description, String uses,
                    String sideEffects, String precautions, String referenceSource) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Medicine name cannot be null");
        this.description = description != null ? description : "";
        this.uses = uses != null ? uses : "";
        this.sideEffects = sideEffects != null ? sideEffects : "";
        this.precautions = precautions != null ? precautions : "";
        this.referenceSource = referenceSource != null ? referenceSource : "WHO / BNF / MedlinePlus";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUses() {
        return uses;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public String getPrecautions() {
        return precautions;
    }

    public String getReferenceSource() {
        return referenceSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return id == medicine.id || name.equalsIgnoreCase(medicine.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
