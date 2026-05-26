package com.pastrymaster.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pastrymaster.GameState;

public class LevelIntroScreen implements Screen {

    private final Game game;

    private Stage stage;

    private Texture bgTexture;
    private Texture whitePixelTexture;
    private Texture chefTexture;
    private Texture customerTexture;

    private BitmapFont font;
    private BitmapFont levelFont;

    public LevelIntroScreen(Game game) {

        this.game = game;

        stage = new Stage(new FitViewport(1536, 1024));

        bgTexture = new Texture(Gdx.files.internal("bg/kitchen_bg.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        whitePixelTexture = new Texture(pixmap);

        pixmap.dispose();

        String selected =
            GameState.selectedChef != null
                ? GameState.selectedChef.toLowerCase()
                : "";

        String chefFileName = "chef_cherry.png";

        if (selected.contains("vanilla")) {
            chefFileName = "chef_vanilla.png";
        }
        else if (selected.contains("lemon")) {
            chefFileName = "chef_lemon.png";
        }

        chefTexture = new Texture(Gdx.files.internal(chefFileName));
        chefTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        customerTexture = new Texture(Gdx.files.internal("customer/customer_happy.png"));
        customerTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        font = new BitmapFont();
        font.getData().setScale(3.5f);

        levelFont = new BitmapFont();
        levelFont.getData().setScale(6.5f);
    }

    private void playClick() {

        if (GameState.clickSound != null) {
            GameState.clickSound.play();
        }
    }

    private void initUI() {

        stage.clear();

        Image bg = new Image(bgTexture);
        bg.setSize(1536, 1024);

        stage.addActor(bg);

        Image dialoguePanel = new Image(whitePixelTexture);
        dialoguePanel.setBounds(150, 60, 1236, 904);
        dialoguePanel.setColor(1f, 1f, 1f, 0.85f);

        stage.addActor(dialoguePanel);

        Label.LabelStyle textStyle =
            new Label.LabelStyle(
                font,
                new Color(0.2f, 0.12f, 0.08f, 1f)
            );

        Label.LabelStyle levelStyle =
            new Label.LabelStyle(
                levelFont,
                new Color(0.2f, 0.12f, 0.08f, 1f)
            );

        Label levelLabel =
            new Label(
                "Level " + GameState.currentLevel,
                levelStyle
            );

        levelLabel.setPosition(
            (1536 - levelLabel.getPrefWidth()) / 2,
            820
        );

        stage.addActor(levelLabel);

        Table characterTable = new Table();

        characterTable.setBounds(150, 420, 1236, 380);

        Image chefImage = new Image(chefTexture);

        Image customerImage = new Image(customerTexture);

        characterTable.add(chefImage)
            .size(220, 350)
            .padRight(180);

        characterTable.add(customerImage)
            .size(220, 350);

        stage.addActor(characterTable);

        String orderText =
            "\"Hi chef! I want fluffy "
                + GameState.getCurrentDessert()
                + "s for breakfast!\"\nBake it correctly and keep the customer happy!";

        Label infoLabel =
            new Label(orderText, textStyle);

        infoLabel.setWrap(true);

        infoLabel.setWidth(1100);

        infoLabel.setAlignment(Align.center);

        infoLabel.setPosition(
            (1536 - 1100) / 2,
            220
        );

        stage.addActor(infoLabel);

        TextButton.TextButtonStyle btnStyle =
            new TextButton.TextButtonStyle();

        btnStyle.font = font;

        btnStyle.fontColor = Color.WHITE;

        TextButton startBakingBtn =
            new TextButton("Start Baking", btnStyle);

        startBakingBtn.setColor(
            0.92f,
            0.45f,
            0.55f,
            1f
        );

        startBakingBtn.setBounds(
            (1536 - 400) / 2,
            90,
            400,
            85
        );

        startBakingBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event,
                                float x,
                                float y) {

                playClick();

                game.setScreen(
                    new RecipeScreen(game)
                );
            }
        });

        stage.addActor(startBakingBtn);
    }

    @Override
    public void show() {

        initUI();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);

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

        bgTexture.dispose();

        whitePixelTexture.dispose();

        chefTexture.dispose();

        customerTexture.dispose();

        font.dispose();

        levelFont.dispose();
    }
}
