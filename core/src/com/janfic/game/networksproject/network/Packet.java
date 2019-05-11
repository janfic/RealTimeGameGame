package com.janfic.game.networksproject.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.janfic.game.networksproject.RealTimeGameGame;
import java.util.LinkedList;

/**
 *
 * @author Jan Fic
 */
public class Packet extends Actor {

    private float percentTraveled;
    private float distanceASecond;

    private LinkedList<NetworkNode> path;
    private int current;
    private float time;

    private Sprite sprite;

    public Packet(LinkedList<NetworkNode> path) {
        percentTraveled = 0;
        this.distanceASecond = 0;
        this.path = path;
        this.current = 0;
        sprite = new Sprite(RealTimeGameGame.Assets.PACKET);
        time = 0;
    }

    public void setSpeed(float speed) {
        this.distanceASecond = speed;
    }

    public float getTime() {
        return time;
    }
    
    public void update(float distance, float deltaTime) {
        float expectedDistance = deltaTime * distanceASecond;
        float change = expectedDistance / distance;
        percentTraveled += change;
        time += deltaTime;
    }

    public float getPercentTraveled() {
        return percentTraveled;
    }

    public NetworkNode getNext() {
        if (current >= path.size() - 1) {
            return null;
        }
        return path.get(current + 1);
    }

    public NetworkNode getCurrent() {
        return path.get(current);
    }

    public void arrive() {
        current++;
        percentTraveled = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha); //To change body of generated methods, choose Tools | Templates.
        batch.setColor(getColor());
        batch.draw(sprite.getTexture(), getX() - 3, getY() - 3);
        batch.setColor(Color.WHITE);
    }

}
