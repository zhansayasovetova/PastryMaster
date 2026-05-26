package com.pastrymaster.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pastrymaster.GameState;

public class RecipeScreen implements Screen {

    private final Game game;
    private Stage stage;
    private BitmapFont font;

    private float timer = 9.0f;
    private Label timerLabel;

    public RecipeScreen(Game game) {
        this.game = game;

        stage = new Stage(new FitViewport(1280, 720));

        font = new BitmapFont();
        font.getData().setScale(2f);

        initUI();
    }

    private void playClick() {
        if (GameState.clickSound != null) {
            GameState.clickSound.play();
        }
    }

    private void initUI() {
        Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);

        StringBuilder recipeText = new StringBuilder(
            "Recipe Paper\n\nToday we bake: "
                + GameState.getCurrentDessert()
                + "\n\nMemorize this recipe:\n"
        );

        for (String ing : GameState.getRequiredIngredients()) {
            recipeText.append("- ").append(ing).append("\n");
        }

        Label recipeLabel = new Label(recipeText.toString(), style);
        recipeLabel.setPosition(450, 350);
        stage.addActor(recipeLabel);

        timerLabel = new Label("Recipe closes in 9 seconds", style);
        timerLabel.setPosition(450, 150);
        stage.addActor(timerLabel);
    }

    @Override
    public void show() {
        playClick();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.95f, 0.92f, 0.88f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timer -= delta;
        timerLabel.setText("Recipe closes in " + (int) Math.ceil(timer) + " seconds");

        if (timer <= 0) {
            game.setScreen(new SupermarketScreen(game));
        }

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
        font.dispose();
    }
}
