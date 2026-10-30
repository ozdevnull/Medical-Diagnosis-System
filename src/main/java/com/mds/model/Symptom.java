package com.mds.model;

import java.util.Objects;

/**
 * Model representing a clinical symptom.
 */
public class Symptom {

    private final int id;
    private final String name;
    private final String description;

    public Symptom(int id, String name, String description) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Symptom name cannot be null");
        this.description = description != null ? description : "";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symptom symptom = (Symptom) o;
        return id == symptom.id || name.equalsIgnoreCase(symptom.name);
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
