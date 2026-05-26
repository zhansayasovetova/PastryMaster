package com.pastrymaster;

import com.badlogic.gdx.Game;
import com.pastrymaster.screens.MainMenuScreen;

public class Main extends Game {

    @Override
    public void create() {

        GameState.loadSounds();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {

        GameState.disposeSounds();

        super.dispose();
    }
}
