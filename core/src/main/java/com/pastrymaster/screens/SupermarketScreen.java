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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pastrymaster.GameState;

import java.util.ArrayList;

public class SupermarketScreen implements Screen {

    private final Game game;

    private Stage stage;

    private Texture bgTexture;
    private Texture whitePixelTexture;

    private BitmapFont font;

    private ArrayList<String> selectedIngredients;
    private ArrayList<String> correctRecipe;

    private Label infoLabel;
    private Label basketLabel;

    private final String[] marketItems = {
        "flour",
        "milk",
        "egg",
        "sugar",
        "butter",
        "chocolate",
        "strawberry",
        "yeast",
        "cream cheese",
        "baking powder"
    };

    public SupermarketScreen(Game game) {

        this.game = game;

        stage = new Stage(new FitViewport(1536, 1024));

        bgTexture =
            new Texture(
                Gdx.files.internal("bg/supermarket_bg.png")
            );

        bgTexture.setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        Pixmap pixmap =
            new Pixmap(
                1,
                1,
                Pixmap.Format.RGBA8888
            );

        pixmap.setColor(Color.WHITE);

        pixmap.fill();

        whitePixelTexture = new Texture(pixmap);

        pixmap.dispose();

        font = new BitmapFont();

        font.getData().setScale(7.0f);

        selectedIngredients = new ArrayList<>();

        correctRecipe = GameState.getRequiredIngredients();
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

    private void playError() {

        if (GameState.errorSound != null) {
            GameState.errorSound.play();
        }
    }

    private void initUI() {

        stage.clear();

        Image bg = new Image(bgTexture);

        bg.setSize(1536, 1024);

        stage.addActor(bg);

        Image mainPanel = new Image(whitePixelTexture);

        mainPanel.setBounds(120, 80, 1296, 850);

        mainPanel.setColor(1f, 1f, 1f, 0.78f);

        mainPanel.setTouchable(Touchable.disabled);

        stage.addActor(mainPanel);

        Image topPanel = new Image(whitePixelTexture);

        topPanel.setBounds(220, 760, 1096, 95);

        topPanel.setColor(1f, 1f, 1f, 0.9f);

        topPanel.setTouchable(Touchable.disabled);

        stage.addActor(topPanel);

        Label.LabelStyle labelStyle =
            new Label.LabelStyle(
                font,
                new Color(0.23f, 0.12f, 0.08f, 1f)
            );

        infoLabel =
            new Label(
                "Hearts: "
                    + GameState.hearts
                    + "     Score: 0     Level: "
                    + GameState.currentLevel,
                labelStyle
            );

        infoLabel.setFontScale(0.8f);

        infoLabel.setPosition(500, 790);

        infoLabel.setTouchable(Touchable.disabled);

        stage.addActor(infoLabel);

        Label titleLabel =
            new Label(
                "Ingredient Shop",
                labelStyle
            );

        titleLabel.setFontScale(1.6f);

        titleLabel.setPosition(
            (1536 - titleLabel.getPrefWidth() * 1.6f) / 2,
            545
        );

        titleLabel.setTouchable(Touchable.disabled);

        stage.addActor(titleLabel);

        Label chooseLabel =
            new Label(
                "Choose ingredients for: "
                    + GameState.getCurrentDessert(),
                labelStyle
            );

        chooseLabel.setFontScale(0.9f);

        chooseLabel.setPosition(
            (1536 - chooseLabel.getPrefWidth() * 0.9f) / 2,
            490
        );

        chooseLabel.setTouchable(Touchable.disabled);

        stage.addActor(chooseLabel);

        basketLabel =
            new Label(
                "Basket:",
                labelStyle
            );

        basketLabel.setFontScale(0.9f);

        basketLabel.setPosition(640, 155);

        basketLabel.setTouchable(Touchable.disabled);

        stage.addActor(basketLabel);

        TextButton.TextButtonStyle buttonStyle =
            new TextButton.TextButtonStyle();

        buttonStyle.font = font;

        buttonStyle.fontColor =
            new Color(0.25f, 0.12f, 0.08f, 1f);

        float startX = 230;

        float firstRowY = 395;

        float secondRowY = 275;

        float buttonW = 220;

        float buttonH = 85;

        float gapX = 235;

        for (int i = 0; i < marketItems.length; i++) {

            final String itemName = marketItems[i];

            final String displayName =
                toDisplayName(itemName);

            final TextButton itemButton =
                new TextButton(displayName, buttonStyle);

            itemButton.getLabel().setFontScale(1.0f);

            itemButton.setColor(
                0.95f,
                0.55f,
                0.65f,
                1f
            );

            float x =
                startX + ((i % 5) * gapX);

            float y =
                i < 5
                    ? firstRowY
                    : secondRowY;

            itemButton.setBounds(
                x,
                y,
                buttonW,
                buttonH
            );

            itemButton.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event,
                                    float x,
                                    float y) {

                    playClick();

                    if (isCorrectIngredient(itemName)) {

                        if (!selectedIngredients.contains(itemName)) {

                            playSuccess();

                            selectedIngredients.add(itemName);

                            itemButton.setVisible(false);

                            updateBasket();

                            if (selectedIngredients.size()
                                == correctRecipe.size()) {

                                game.setScreen(
                                    new CookingScreen(game)
                                );
                            }
                        }

                    } else {

                        playError();

                        GameState.hearts--;

                        infoLabel.setText(
                            "Hearts: "
                                + GameState.hearts
                                + "     Score: 0     Level: "
                                + GameState.currentLevel
                        );

                        if (GameState.hearts <= 0) {

                            GameState.hearts = 3;

                            game.setScreen(
                                new MainMenuScreen(game)
                            );
                        }
                    }
                }
            });

            stage.addActor(itemButton);
        }
    }

    private boolean isCorrectIngredient(String itemName) {

        for (String ingredient : correctRecipe) {

            if (normalizeIngredient(ingredient)
                .equals(normalizeIngredient(itemName))) {

                return true;
            }
        }

        return false;
    }

    private String normalizeIngredient(String value) {

        return value.trim()
            .toLowerCase()
            .replace("_", " ");
    }

    private String toDisplayName(String value) {

        String[] parts = value.split(" ");

        StringBuilder result =
            new StringBuilder();

        for (String part : parts) {

            if (part.length() > 0) {

                result.append(
                        part.substring(0, 1).toUpperCase()
                    )
                    .append(
                        part.substring(1).toLowerCase()
                    )
                    .append(" ");
            }
        }

        return result.toString().trim();
    }

    private void updateBasket() {

        if (selectedIngredients.isEmpty()) {

            basketLabel.setText("Basket:");

            return;
        }

        StringBuilder basket =
            new StringBuilder("Basket: ");

        for (int i = 0; i < selectedIngredients.size(); i++) {

            basket.append(
                toDisplayName(
                    selectedIngredients.get(i)
                )
            );

            if (i < selectedIngredients.size() - 1) {

                basket.append(", ");
            }
        }

        basketLabel.setText(
            basket.toString()
        );
    }

    @Override
    public void show() {

        initUI();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);

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

        font.dispose();
    }
}
