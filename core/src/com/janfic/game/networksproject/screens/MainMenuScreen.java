package com.janfic.game.networksproject.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.janfic.game.networksproject.RealTimeGameGame;

/**
 *
 * @author Jan Fic
 */
public class MainMenuScreen extends Stage implements Screen {

    RealTimeGameGame game;
    
    ShapeRenderer shapeRenderer;

    Table mainTable;
    Label title, subTitle, authorTitle;
    Image leftPlay, rightPlay, leftHow, rightHow, leftCredits, rightCredits;

    TextButton howToPlayButton, playButton, creditsButton, exitButton;

    public MainMenuScreen(RealTimeGameGame game) {
        this.game = game;
    }

    
    
    @Override
    public void show() {

        shapeRenderer = new ShapeRenderer();

        Skin skin = RealTimeGameGame.Assets.SKIN;
        mainTable = new Table(skin) {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
                shapeRenderer.end();
                batch.begin();
                super.draw(batch, parentAlpha);
            }
        };
        
        mainTable.defaults().pad(2);
        
        title = new Label("Real Time Game Tycoon", skin);
        title.setFontScale(3);

        mainTable.add(title).colspan(3).pad(10).row();

        subTitle = new Label("A  Computer Networks Project", skin);
        subTitle.setFontScale(2);

        mainTable.add(subTitle).colspan(3).pad(5).row();

        authorTitle = new Label("By Jan Fic", skin);
        authorTitle.setFontScale(2f);

        mainTable.add(authorTitle).colspan(3).pad(5).row();

        mainTable.add().colspan(3).pad(25).row();

        leftPlay = new Image(RealTimeGameGame.Assets.COMPUTER);
        rightPlay = new Image(RealTimeGameGame.Assets.COMPUTER);
        leftHow = new Image(RealTimeGameGame.Assets.ROUTER);
        rightHow = new Image(RealTimeGameGame.Assets.ROUTER);
        leftCredits = new Image(RealTimeGameGame.Assets.SERVER);
        rightCredits = new Image(RealTimeGameGame.Assets.SERVER);

        howToPlayButton = new TextButton("How To Play", skin);
        playButton = new TextButton("Play", skin);
        creditsButton = new TextButton("Credits", skin);
        exitButton = new TextButton("Exit", skin);

        playButton.addListener(new ClickListener(){ 
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button); //To change body of generated methods, choose Tools | Templates.
                System.out.println("CLICKED");
                game.setScreen(new PlayScreen());
            }
            
        
        });
        
        howToPlayButton.getLabel().setFontScale(2);
        playButton.getLabel().setFontScale(2);
        creditsButton.getLabel().setFontScale(2);
        exitButton.getLabel().setFontScale(2);

        mainTable.add(leftPlay).right();
        mainTable.add(playButton).width(250);
        mainTable.add(rightPlay).left().row();

        mainTable.add(leftHow).right();
        mainTable.add(howToPlayButton).width(250);
        mainTable.add(rightHow).left().row();

        mainTable.add(leftCredits).right();
        mainTable.add(creditsButton).width(250);
        mainTable.add(rightCredits).left().row();

        mainTable.add();
        mainTable.add(exitButton).width(250).row();

        mainTable.top();
        mainTable.setSize(700, 700);
        mainTable.setX(350);
        mainTable.setY(50);

        addActor(mainTable);
        
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float f) {
        act();
        draw();
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

}
