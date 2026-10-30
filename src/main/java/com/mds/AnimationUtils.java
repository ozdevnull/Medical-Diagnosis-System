package com.mds;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * Shared animation utilities for the MDS application.
 * Provides reusable, polished micro-animations for consistent UX across all screens.
 */
public final class AnimationUtils {

    private AnimationUtils() {}

    // ── Screen entrance ───────────────────────────────────────────────────────

    /**
     * Fade + slide-up entrance for a whole screen root.
     * Called once on initialize() so the screen "rises" into view.
     */
    public static void screenEntrance(Node node) {
        if (node == null) return;
        node.setOpacity(0);
        node.setTranslateY(18);

        FadeTransition fade = new FadeTransition(Duration.millis(320), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(320), node);
        slide.setFromY(18);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(fade, slide).play();
    }

    // ── Panel / content transitions ───────────────────────────────────────────

    /**
     * Fade-in with a tiny upward slide — used when switching dashboard panels.
     */
    public static void panelReveal(Node node) {
        if (node == null) return;
        node.setOpacity(0);
        node.setTranslateY(10);

        FadeTransition fade = new FadeTransition(Duration.millis(220), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(220), node);
        slide.setFromY(10);
        slide.setToY(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(fade, slide).play();
    }

    // ── Button interactions ───────────────────────────────────────────────────

    /**
     * Satisfying press-and-release bounce — attach to onMousePressed / onMouseReleased.
     * Scale down on press, spring back on release.
     */
    public static void buttonPress(Node node) {
        if (node == null) return;
        ScaleTransition down = new ScaleTransition(Duration.millis(80), node);
        down.setToX(0.94);
        down.setToY(0.94);
        down.setInterpolator(Interpolator.EASE_IN);
        down.play();
    }

    public static void buttonRelease(Node node) {
        if (node == null) return;
        ScaleTransition up = new ScaleTransition(Duration.millis(180), node);
        up.setToX(1.0);
        up.setToY(1.0);
        up.setInterpolator(Interpolator.EASE_OUT);
        up.play();
    }

    /**
     * One-shot elastic pop: scale up slightly then settle back to 1.
     * Use for confirmation actions (login success, analysis start, etc.).
     */
    public static void confirmPop(Node node) {
        if (node == null) return;
        ScaleTransition pop = new ScaleTransition(Duration.millis(200), node);
        pop.setFromX(1.0); pop.setFromY(1.0);
        pop.setToX(1.06);  pop.setToY(1.06);
        pop.setAutoReverse(true);
        pop.setCycleCount(2);
        pop.setInterpolator(Interpolator.EASE_BOTH);
        pop.play();
    }

    // ── Nav item tap ──────────────────────────────────────────────────────────

    /**
     * Quick scale-bounce on bottom nav tap.
     */
    public static void navTap(Node node) {
        if (node == null) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(90), node);
        st.setFromX(1.0); st.setFromY(1.0);
        st.setToX(0.86);  st.setToY(0.86);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.play();
    }

    // ── Validation / error ────────────────────────────────────────────────────

    /**
     * Horizontal shake — used to signal invalid input or empty selection errors.
     */
    public static void shake(Node node) {
        if (node == null) return;
        double original = node.getTranslateX();
        Timeline shake = new Timeline(
                new KeyFrame(Duration.ZERO,          new KeyValue(node.translateXProperty(), original)),
                new KeyFrame(Duration.millis(60),    new KeyValue(node.translateXProperty(), original - 8, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.millis(120),   new KeyValue(node.translateXProperty(), original + 7, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.millis(180),   new KeyValue(node.translateXProperty(), original - 5, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.millis(240),   new KeyValue(node.translateXProperty(), original + 4, Interpolator.EASE_OUT)),
                new KeyFrame(Duration.millis(300),   new KeyValue(node.translateXProperty(), original,    Interpolator.EASE_OUT))
        );
        shake.play();
    }

    // ── Checkbox / selection ──────────────────────────────────────────────────

    /**
     * Tiny pop when a symptom checkbox is ticked — the card itself springs briefly.
     */
    public static void checkboxSelect(Node card) {
        if (card == null) return;
        ScaleTransition pop = new ScaleTransition(Duration.millis(120), card);
        pop.setFromX(1.0); pop.setFromY(1.0);
        pop.setToX(1.025); pop.setToY(1.025);
        pop.setAutoReverse(true);
        pop.setCycleCount(2);
        pop.setInterpolator(Interpolator.EASE_BOTH);
        pop.play();
    }

    // ── Info / notification label ─────────────────────────────────────────────

    /**
     * Fade-in for labels that appear dynamically (info messages, error labels, etc.).
     */
    public static void labelFadeIn(Node node) {
        if (node == null) return;
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(250), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    // ── Progress bar ──────────────────────────────────────────────────────────

    /**
     * Animate a ProgressBar from 0 to target value.
     * Gives a satisfying "fill" effect on the results screen.
     */
    public static void animateProgressBar(javafx.scene.control.ProgressBar bar, double target) {
        if (bar == null) return;
        bar.setProgress(0);
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO,        new KeyValue(bar.progressProperty(), 0)),
                new KeyFrame(Duration.millis(600), new KeyValue(bar.progressProperty(), target, Interpolator.EASE_OUT))
        );
        tl.play();
    }

    // ── Staggered list reveal ─────────────────────────────────────────────────

    /**
     * Fades and slides each child node in with a small delay between them.
     * Used for result cards arriving one after another.
     */
    public static void staggerReveal(java.util.List<? extends Node> nodes, int delayMs) {
        if (nodes == null) return;
        int delayIndex = 0;
        for (Node node : nodes) {
            if (node == null) continue;
            node.setOpacity(0);
            node.setTranslateY(14);

            FadeTransition fade = new FadeTransition(Duration.millis(260), node);
            fade.setFromValue(0);
            fade.setToValue(1);

            TranslateTransition slide = new TranslateTransition(Duration.millis(260), node);
            slide.setFromY(14);
            slide.setToY(0);
            slide.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition pt = new ParallelTransition(fade, slide);
            pt.setDelay(Duration.millis((long) delayIndex * delayMs));
            pt.play();
            delayIndex++;
        }
    }

    // ── Counter update ────────────────────────────────────────────────────────

    /**
     * Brief scale pulse on the selected-count pill whenever the count changes.
     */
    public static void countPulse(Node node) {
        if (node == null) return;
        ScaleTransition pulse = new ScaleTransition(Duration.millis(140), node);
        pulse.setFromX(1.0); pulse.setFromY(1.0);
        pulse.setToX(1.12);  pulse.setToY(1.12);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(2);
        pulse.setInterpolator(Interpolator.EASE_BOTH);
        pulse.play();
    }

    // ── Search field focus ────────────────────────────────────────────────────

    /**
     * Gently grows the search field width-wise when focused.
     * Pair with CSS focus glow for maximum effect.
     */
    public static void searchFocus(Region field) {
        if (field == null) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(150), field);
        st.setToX(1.01);
        st.setToY(1.01);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();
    }

    public static void searchBlur(Region field) {
        if (field == null) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(150), field);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);
        st.play();
    }
}
