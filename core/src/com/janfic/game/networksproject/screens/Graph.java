package com.janfic.game.networksproject.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *
 * @author Jan Fic
 */
public class Graph extends Actor {

    float[] points;
    ShapeRenderer shapeRenderer;
    int current;

    public Graph(int width, int height, ShapeRenderer sr) {
        setWidth(width);
        setHeight(height);
        points = new float[width];
        this.shapeRenderer = sr;
        this.current = 0;
    }

    public void next(float f) {
        points[current] = f;
        current++;
        if (current >= points.length) {
            current = 0;
        }
    }

    public float getAverage() {
        float average = 0;
        for (float point : points) {
            average += point;
        }
        return average /= points.length;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        //shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.rectLine(getX(), getY(), getX() + getWidth(), getY(), 1);
        shapeRenderer.rectLine(getX(), getY(), getX(), getY() + getHeight(), 1);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rectLine(getX() + current, getY(), getX() + current, getY() + getHeight(), 1);
        shapeRenderer.rectLine(getX(), getY() + getAverage() * getHeight(), getX() + getWidth(), getY() + getAverage() * getHeight(), 1);
        shapeRenderer.setColor(Color.BLUE);
        for (int i = 0; i < points.length - 1; i++) {
            shapeRenderer.rectLine(getX() + i, getY() + points[i] * getHeight(), getX() + i + 1, getY() + points[i + 1] * getHeight(), 2);
        }
        shapeRenderer.end();
        batch.begin();
    }

}
