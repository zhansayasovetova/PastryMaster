package com.pastrymaster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.ArrayList;

public class GameState {

    public static String selectedChef = "Rose";

    public static int currentLevel = 1;

    public static int score = 0;

    public static int hearts = 3;


    public static Sound clickSound;

    public static Sound successSound;

    public static Sound errorSound;

    public static Music bgMusic;


    public static final String[] DESSERTS = {
        "Pancake",
        "Cupcake",
        "Croissant",
        "Cheesecake",
        "Strawberry Cake"
    };

    // =========================
    // CURRENT DESSERT
    // =========================

    public static String getCurrentDessert() {

        return DESSERTS[currentLevel - 1];
    }

    public static ArrayList<String> getRequiredIngredients() {

        ArrayList<String> list = new ArrayList<>();

        String dessert = getCurrentDessert();

        // PANCAKE
        if (dessert.equals("Pancake")) {

            list.add("Flour");
            list.add("Milk");
            list.add("Egg");

        }

        // CUPCAKE
        else if (dessert.equals("Cupcake")) {

            list.add("Flour");
            list.add("Sugar");
            list.add("Egg");
            list.add("Butter");

        }

        // CROISSANT
        else if (dessert.equals("Croissant")) {

            list.add("Flour");
            list.add("Butter");
            list.add("Yeast");

        }

        // CHEESECAKE
        else if (dessert.equals("Cheesecake")) {

            list.add("Cream Cheese");
            list.add("Sugar");
            list.add("Egg");

        }

        // STRAWBERRY CAKE
        else if (dessert.equals("Strawberry Cake")) {

            list.add("Flour");
            list.add("Milk");
            list.add("Strawberry");
        }

        return list;
    }

    public static void loadSounds() {

        // CLICK
        clickSound =
            Gdx.audio.newSound(
                Gdx.files.internal("sounds/click.wav")
            );

        // SUCCESS
        successSound =
            Gdx.audio.newSound(
                Gdx.files.internal("sounds/success.wav")
            );

        // ERROR
        errorSound =
            Gdx.audio.newSound(
                Gdx.files.internal("sounds/error.wav")
            );

        // BACKGROUND MUSIC
        bgMusic =
            Gdx.audio.newMusic(
                Gdx.files.internal("sounds/music.wav")
            );

        bgMusic.setLooping(true);

        bgMusic.setVolume(0.3f);

        bgMusic.play();
    }

    public static void disposeSounds() {

        if (clickSound != null) {
            clickSound.dispose();
        }

        if (successSound != null) {
            successSound.dispose();
        }

        if (errorSound != null) {
            errorSound.dispose();
        }

        if (bgMusic != null) {
            bgMusic.dispose();
        }
    }
}
