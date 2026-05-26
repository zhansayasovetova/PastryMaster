package com.pastrymaster.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pastrymaster.GameState;

public class WinScreen implements Screen {

    private final Game game;
    private Stage stage;
    private Texture bgTexture;
    private BitmapFont font;

    public WinScreen(Game game) {
        this.game = game;

        stage = new Stage(new FitViewport(1536, 1024));

        bgTexture = new Texture(Gdx.files.internal("bg/main_menu_bg.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        font = new BitmapFont();
        font.getData().setScale(3f);

        initUI();
    }

    private void playClick() {
        if (GameState.clickSound != null) {
            GameState.clickSound.play();
        }
    }

    private void playSuccess() {
        if (GameState.successSound != null) {
            GameState.successSound.play();
        }
    }

    private void initUI() {
        Image bg = new Image(bgTexture);
        bg.setSize(1536, 1024);
        stage.addActor(bg);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BROWN);

        String winMessage =
            "PERFECT! LEVEL " + GameState.currentLevel + " COMPLETED!\n\nScore: " + GameState.score;

        if (GameState.currentLevel == 5) {
            winMessage =
                "CONGRATULATIONS!\nYOU ARE THE ULTIMATE PASTRY MASTER!\n\nFinal Score: " + GameState.score;
        }

        Label winLabel = new Label(winMessage, labelStyle);
        winLabel.setPosition(350, 600);
        stage.addActor(winLabel);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.fontColor = Color.PINK;

        String buttonText = GameState.currentLevel < 5 ? "Next Level" : "Main Menu";

        TextButton actionBtn = new TextButton(buttonText, btnStyle);
        actionBtn.setBounds(580, 300, 380, 100);

        actionBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playClick();

                if (GameState.currentLevel < 5) {
                    GameState.currentLevel++;
                    GameState.hearts = 3;
                    game.setScreen(new LevelIntroScreen(game));
                } else {
                    GameState.currentLevel = 1;
                    GameState.score = 0;
                    GameState.hearts = 3;
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        });

        stage.addActor(actionBtn);
    }

    @Override
    public void show() {
        playSuccess();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        bgTexture.dispose();
        font.dispose();
    }
}
