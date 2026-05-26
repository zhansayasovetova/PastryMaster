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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.HashMap;

public class DecorationScreen implements Screen {

    private final Game game;

    private Stage stage;

    private Texture bgTexture;
    private Texture whitePixelTexture;

    private BitmapFont font;

    private DragAndDrop dragAndDrop;

    private TextButton continueBtn;

    private ArrayList<String> placedDecorations;

    private HashMap<String, String[]> dessertDecorMap;

    private final String[] decors = {
        "banana",
        "blueberry",
        "chocolate_glaze",
        "cream",
        "mint",
        "powdered_sugar",
        "strawberry_decor",
        "syrup"
    };

    public DecorationScreen(Game game) {

        this.game = game;

        stage = new Stage(new FitViewport(1536, 1024));

        bgTexture = new Texture(Gdx.files.internal("bg/kitchen_bg.png"));

        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        whitePixelTexture = new Texture(pixmap);
        pixmap.dispose();

        font = new BitmapFont();
        font.getData().setScale(2.5f);

        dragAndDrop = new DragAndDrop();

        placedDecorations = new ArrayList<>();

        createDecorationRecipes();

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

    private void playError() {
        if (GameState.errorSound != null) {
            GameState.errorSound.play();
        }
    }

    private String getDessertKey() {

        return GameState.getCurrentDessert()
            .trim()
            .toLowerCase()
            .replace(" ", "_");
    }

    private void createDecorationRecipes() {

        dessertDecorMap = new HashMap<>();

        dessertDecorMap.put("pancake",
            new String[]{"strawberry_decor", "blueberry", "syrup"});

        dessertDecorMap.put("cheesecake",
            new String[]{"blueberry", "chocolate_glaze", "cream"});

        dessertDecorMap.put("croissant",
            new String[]{"powdered_sugar", "cream", "banana"});

        dessertDecorMap.put("cupcake",
            new String[]{"cream", "strawberry_decor", "mint"});

        dessertDecorMap.put("strawberry_cake",
            new String[]{"strawberry_decor", "cream", "mint"});
    }

    private void initUI() {

        Image bg = new Image(bgTexture);
        bg.setSize(1536,1024);
        stage.addActor(bg);

        Image topPanel = new Image(whitePixelTexture);
        topPanel.setBounds(100,850,1336,120);
        topPanel.setColor(1f,1f,1f,0.75f);
        stage.addActor(topPanel);

        Label.LabelStyle labelStyle =
            new Label.LabelStyle(font,
                new Color(0.2f,0.1f,0.08f,1));

        Label title =
            new Label(
                "Decoration Time! Drag decorations onto your dessert!",
                labelStyle
            );

        title.setPosition(140,895);

        stage.addActor(title);

        String dessertName = getDessertKey();

        Texture plainTexture =
            new Texture(
                Gdx.files.internal(
                    "desserts/" + dessertName + "_plane.png"
                )
            );

        final Image playerDessert = new Image(plainTexture);

        playerDessert.setSize(360,360);

        playerDessert.setPosition(320,390);

        stage.addActor(playerDessert);

        Texture exampleTexture =
            new Texture(
                Gdx.files.internal(
                    "desserts/" + dessertName + ".png"
                )
            );

        Image exampleDessert = new Image(exampleTexture);

        exampleDessert.setSize(360,360);

        exampleDessert.setPosition(860,390);

        stage.addActor(exampleDessert);

        Image bottomPanel = new Image(whitePixelTexture);

        bottomPanel.setBounds(90,40,1350,260);

        bottomPanel.setColor(1f,1f,1f,0.70f);

        stage.addActor(bottomPanel);

        float startX = 120;
        float y = 115;

        for(int i = 0; i < decors.length; i++) {

            final String decorName = decors[i];

            Texture texture =
                new Texture(
                    Gdx.files.internal(
                        "decor/" + decorName + ".png"
                    )
                );

            float currentX = startX + (i * 165);

            createDraggableDecor(
                decorName,
                texture,
                currentX,
                y
            );

            Label decorLabel =
                new Label(
                    decorName.replace("_"," "),
                    labelStyle
                );

            decorLabel.setFontScale(1.1f);

            decorLabel.setPosition(currentX,55);

            stage.addActor(decorLabel);
        }

        dragAndDrop.addTarget(new Target(playerDessert) {

            @Override
            public boolean drag(Source source,
                                Payload payload,
                                float x,
                                float y,
                                int pointer) {

                return true;
            }

            @Override
            public void drop(Source source,
                             Payload payload,
                             float x,
                             float y,
                             int pointer) {

                String decor =
                    (String) payload.getObject();

                String[] required =
                    dessertDecorMap.get(getDessertKey());

                if(required == null) {
                    return;
                }

                boolean correct = false;

                for(String r : required) {

                    if(r.equals(decor)) {
                        correct = true;
                        break;
                    }
                }

                if(!correct) {

                    playError();

                    return;
                }

                if(placedDecorations.contains(decor)) {

                    playError();

                    return;
                }

                placedDecorations.add(decor);

                playSuccess();

                Image decorImage =
                    new Image(
                        ((Image)source.getActor()).getDrawable()
                    );

                decorImage.setSize(90,90);

                float snapX =
                    playerDessert.getX()
                        + 80
                        + (placedDecorations.size() * 60);

                float snapY =
                    playerDessert.getY() + 180;

                decorImage.setPosition(snapX,snapY);

                stage.addActor(decorImage);

                checkCompletion();
            }
        });

        TextButton.TextButtonStyle btnStyle =
            new TextButton.TextButtonStyle();

        btnStyle.font = font;
        btnStyle.fontColor = Color.MAROON;

        continueBtn =
            new TextButton("[ CONTINUE ]", btnStyle);

        continueBtn.setPosition(620,330);

        continueBtn.setVisible(false);

        continueBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event,
                                float x,
                                float y) {

                playClick();

                GameState.currentLevel++;

                game.setScreen(
                    new LevelIntroScreen(game)
                );
            }
        });

        stage.addActor(continueBtn);
    }

    private void checkCompletion() {

        String[] required =
            dessertDecorMap.get(getDessertKey());

        if(required == null) {
            return;
        }

        if(placedDecorations.size() != required.length) {
            return;
        }

        for(String req : required) {

            if(!placedDecorations.contains(req)) {
                return;
            }
        }

        continueBtn.setVisible(true);
    }

    private void createDraggableDecor(
        final String name,
        Texture texture,
        float x,
        float y
    ) {

        final Image img = new Image(texture);

        img.setSize(110,110);

        img.setPosition(x,y);

        stage.addActor(img);

        dragAndDrop.addSource(new Source(img) {

            @Override
            public Payload dragStart(InputEvent event,
                                     float x,
                                     float y,
                                     int pointer) {

                playClick();

                Payload payload = new Payload();

                payload.setObject(name);

                Image dragActor =
                    new Image(img.getDrawable());

                dragActor.setSize(110,110);

                payload.setDragActor(dragActor);

                return payload;
            }
        });
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

        stage.getViewport().update(width,height,true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {

        stage.dispose();

        bgTexture.dispose();

        whitePixelTexture.dispose();

        font.dispose();
    }
}
