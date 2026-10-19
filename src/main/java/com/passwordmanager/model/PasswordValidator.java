package com.passwordmanager.model;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo encargado de evaluar la fortaleza de las contraseñas
 * y proveer retroalimentación de seguridad.
 */
public class PasswordValidator {
    private static final Pattern MAYUSCULAS =
        Pattern.compile(".*[A-Z].*");

    private static final Pattern MINUSCULAS =
        Pattern.compile(".*[a-z].*");

    private static final Pattern NUMEROS =
        Pattern.compile(".*[0-9].*");

    private static final Pattern ESPECIALES =
        Pattern.compile(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>?].*");
    public enum Strength {
        DEBIL,
        MEDIA,
        FUERTE
    }

    public static class ValidationResult {
        private final Strength strength;
        private final List<String> feedback;
        private final int score;

        public ValidationResult(Strength strength, List<String> feedback, int score) {
            this.strength = strength;
            this.feedback = feedback;
            this.score = score;
        }

        public Strength getStrength() {
            return strength;
        }

        public List<String> getFeedback() {
            return feedback;
        }

        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return "Fortaleza: " + strength + " (Score: " + score + "/5) - Retroalimentación: " + feedback;
        }
    }

    /**
     * Valida la fortaleza de una contraseña y proporciona retroalimentación.
     *
     * @param password Contraseña a validar.
     * @return ValidationResult con el puntaje, nivel de fortaleza y retroalimentación detallada.
     */
    public ValidationResult validate(String password) {
        List<String> feedback = new ArrayList<>();
        
        if (password == null || password.trim().isEmpty()) {
            feedback.add("La contraseña no puede estar vacía.");
            return new ValidationResult(Strength.DEBIL, feedback, 0);
        }

        int score = 0;

        // Regla 1: Longitud
        if (password.length() >= 8) {
            score++;
        } else {
            feedback.add("Debe tener al menos 8 caracteres.");
        }

        // Regla 2: Letras Mayúsculas
        if (MAYUSCULAS.matcher(password).matches()) {
            score++;
        } else {
            feedback.add("Debe contener al menos una letra mayúscula.");
        }

        // Regla 3: Letras Minúsculas
        if (MINUSCULAS.matcher(password).matches()) {
            score++;
        } else {
            feedback.add("Debe contener al menos una letra minúscula.");
        }

        // Regla 4: Números
        if (NUMEROS.matcher(password).matches()) {
            score++;
        } else {
            feedback.add("Debe contener al menos un número.");
        }

        // Regla 5: Caracteres Especiales
        // Usamos una expresión regular que busca símbolos comunes
        if (ESPECIALES.matcher(password).matches()) {
            score++;
        } else {
            feedback.add("Debe contener al menos un carácter especial (ej. !, @, #, $, etc.).");
        }

        // Clasificar fortaleza basado en el score (las contraseñas de longitud menor a 8 son siempre DÉBILES)
        Strength strength;
        if (password.length() < 8) {
            strength = Strength.DEBIL;
        } else if (score <= 2) {
            strength = Strength.DEBIL;
        } else if (score <= 4) {
            strength = Strength.MEDIA;
        } else {
            strength = Strength.FUERTE;
        }

        return new ValidationResult(strength, feedback, score);
    }
}
