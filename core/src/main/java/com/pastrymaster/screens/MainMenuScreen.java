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

public class MainMenuScreen implements Screen {

    private final Game game;

    private Stage stage;

    private Texture backgroundTexture;

    public MainMenuScreen(Game game) {

        this.game = game;

        stage = new Stage(new FitViewport(1536, 1024));

        backgroundTexture =
            new Texture(
                Gdx.files.internal("bg/main_menu_bg.png")
            );

        backgroundTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        initUI();
    }

    private void playClick() {

        if (GameState.clickSound != null) {
            GameState.clickSound.play();
        }
    }

    private void initUI() {

        Image bg = new Image(backgroundTexture);

        bg.setSize(1536, 1024);

        stage.addActor(bg);

        Button startBtn =
            new Button(new Button.ButtonStyle());

        startBtn.setBounds(
            550,
            400,
            440,
            140
        );

        startBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event,
                                float x,
                                float y) {

                playClick();

                game.setScreen(
                    new ChefSelectScreen(game)
                );
            }
        });

        stage.addActor(startBtn);
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

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {

        Gdx.input.setInputProcessor(null);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {

        stage.dispose();

        backgroundTexture.dispose();
    }
}
