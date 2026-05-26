package com.pastrymaster.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pastrymaster.GameState;

import java.util.ArrayList;

public class CookingScreen implements Screen {

    private final Game game;
    private Stage stage;

    private Texture bgTexture;
    private Texture whitePixelTexture;
    private Texture bowlTexture;

    private BitmapFont font;
    private DragAndDrop dragAndDrop;

    private ArrayList<String> requiredIngredients;
    private ArrayList<String> mixedIngredients;

    private final String[][] allIngredients = {
        {"flour", "milk", "egg", "sugar", "butter"},
        {"chocolate", "strawberry", "yeast", "cream cheese", "baking powder"}
    };

    public CookingScreen(Game game) {
        this.game = game;

        stage = new Stage(new FitViewport(1536, 1024));

        bgTexture = new Texture(Gdx.files.internal("bg/kitchen_bg.png"));
        bgTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        bowlTexture = new Texture(Gdx.files.internal("bowl.png"));
        bowlTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixelTexture = new Texture(pixmap);
        pixmap.dispose();

        font = new BitmapFont();
        font.getData().setScale(3.2f);

        dragAndDrop = new DragAndDrop();

        mixedIngredients = new ArrayList<>();
        requiredIngredients = GameState.getRequiredIngredients();
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

        Image textPanel = new Image(whitePixelTexture);
        textPanel.setBounds(330, 610, 876, 230);
        textPanel.setColor(1f, 1f, 1f, 0.82f);
        textPanel.setTouchable(Touchable.disabled);
        stage.addActor(textPanel);

        Label.LabelStyle labelStyle = new Label.LabelStyle(
            font,
            new Color(0.2f, 0.12f, 0.08f, 1f)
        );

        Label titleLabel = new Label("Cooking: " + GameState.getCurrentDessert(), labelStyle);
        titleLabel.setFontScale(1.25f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setWidth(876);
        titleLabel.setPosition(330, 760);
        titleLabel.setTouchable(Touchable.disabled);
        stage.addActor(titleLabel);

        Label infoLabel = new Label("Drag only correct ingredients into the bowl!", labelStyle);
        infoLabel.setFontScale(0.9f);
        infoLabel.setAlignment(Align.center);
        infoLabel.setWidth(876);
        infoLabel.setPosition(330, 700);
        infoLabel.setTouchable(Touchable.disabled);
        stage.addActor(infoLabel);

        Label recipeLabel = new Label("Recipe: " + getRecipeText(), labelStyle);
        recipeLabel.setFontScale(0.65f);
        recipeLabel.setAlignment(Align.center);
        recipeLabel.setWidth(876);
        recipeLabel.setPosition(330, 650);
        recipeLabel.setTouchable(Touchable.disabled);
        stage.addActor(recipeLabel);

        Image bowlImage = new Image(bowlTexture);
        bowlImage.setSize(330, 250);
        bowlImage.setPosition((1536 - 330) / 2f, 350);
        stage.addActor(bowlImage);

        float startX = 120;
        float firstRowY = 170;
        float secondRowY = 35;
        float gapX = 275;

        for (int row = 0; row < allIngredients.length; row++) {
            for (int col = 0; col < allIngredients[row].length; col++) {
                final String ingName = allIngredients[row][col];

                String fileName = ingName.replace(" ", "");
                Texture ingTexture = new Texture(Gdx.files.internal("ingredients/" + fileName + ".png"));
                ingTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

                Label ingLabel = new Label(toDisplayName(ingName), labelStyle);
                ingLabel.setFontScale(0.45f);
                ingLabel.setAlignment(Align.center);

                float currentX = startX + col * gapX;
                float currentY = row == 0 ? firstRowY : secondRowY;

                createDraggableIngredient(ingName, ingTexture, ingLabel, currentX, currentY);
            }
        }

        dragAndDrop.addTarget(new Target(bowlImage) {
            @Override
            public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(Source source, Payload payload, float x, float y, int pointer) {
                String ingredientName = (String) payload.getObject();

                if (isCorrectIngredient(ingredientName)) {
                    if (!isAlreadyMixed(ingredientName)) {
                        playSuccess();

                        mixedIngredients.add(normalizeIngredient(ingredientName));
                        source.getActor().remove();

                        if (mixedIngredients.size() == requiredIngredients.size()) {
                            game.setScreen(new DecorationScreen(game));
                        }
                    }
                } else {
                    playError();

                    GameState.hearts--;

                    if (GameState.hearts <= 0) {
                        GameState.hearts = 3;
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
            }
        });
    }

    private void createDraggableIngredient(final String name, Texture texture, Label label, float x, float y) {
        final Image img = new Image(texture);
        img.setSize(105, 105);

        final Table itemContainer = new Table();
        itemContainer.add(img).size(105, 105).row();
        itemContainer.add(label).width(170).padTop(5);
        itemContainer.setSize(170, 145);
        itemContainer.setPosition(x, y);

        stage.addActor(itemContainer);

        dragAndDrop.addSource(new Source(itemContainer) {
            @Override
            public Payload dragStart(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer) {
                playClick();

                Payload payload = new Payload();
                payload.setObject(name);

                Image dragImage = new Image(img.getDrawable());
                dragImage.setSize(100, 100);
                payload.setDragActor(dragImage);

                return payload;
            }
        });
    }

    private boolean isCorrectIngredient(String itemName) {
        for (String ingredient : requiredIngredients) {
            if (normalizeIngredient(ingredient).equals(normalizeIngredient(itemName))) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlreadyMixed(String itemName) {
        String normalized = normalizeIngredient(itemName);

        for (String mixed : mixedIngredients) {
            if (mixed.equals(normalized)) {
                return true;
            }
        }

        return false;
    }

    private String normalizeIngredient(String value) {
        return value.trim().toLowerCase().replace("_", " ");
    }

    private String toDisplayName(String value) {
        String[] parts = value.split(" ");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (part.length() > 0) {
                result.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1).toLowerCase())
                    .append(" ");
            }
        }

        return result.toString().trim();
    }

    private String getRecipeText() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < requiredIngredients.size(); i++) {
            result.append(toDisplayName(requiredIngredients.get(i)));

            if (i < requiredIngredients.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString();
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

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void hide() { Gdx.input.setInputProcessor(null); }
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        bgTexture.dispose();
        whitePixelTexture.dispose();
        bowlTexture.dispose();
        font.dispose();
    }
}
