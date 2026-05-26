package com.pastrymaster.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pastrymaster.GameState;

public class ChefSelectScreen implements Screen {
    private final Game game;
    private Stage stage;
    private Texture bgTexture;

    public ChefSelectScreen(Game game) {
        this.game = game;
        stage = new Stage(new FitViewport(1536, 1024));

        bgTexture = new Texture(Gdx.files.internal("bg/chef_select_bg.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        initUI();
    }

    private void playClick() {
        if (GameState.clickSound != null) {
            GameState.clickSound.play();
        }
    }

    private void initUI() {
        Image bg = new Image(bgTexture);
        bg.setSize(1536, 1024);
        stage.addActor(bg);

        Button roseBtn = new Button(new Button.ButtonStyle());
        roseBtn.setBounds(375, 140, 240, 100);
        roseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                GameState.selectedChef = "Rose";
                game.setScreen(new LevelIntroScreen(game));
            }
        });
        stage.addActor(roseBtn);

        Button vanillaBtn = new Button(new Button.ButtonStyle());
        vanillaBtn.setBounds(645, 140, 240, 100);
        vanillaBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                GameState.selectedChef = "Vanilla";
                game.setScreen(new LevelIntroScreen(game));
            }
        });
        stage.addActor(vanillaBtn);

        Button butterBtn = new Button(new Button.ButtonStyle());
        butterBtn.setBounds(920, 140, 240, 100);
        butterBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();
                GameState.selectedChef = "Butter";
                game.setScreen(new LevelIntroScreen(game));
            }
        });
        stage.addActor(butterBtn);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void hide() { Gdx.input.setInputProcessor(null); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() { stage.dispose(); bgTexture.dispose(); }
}
