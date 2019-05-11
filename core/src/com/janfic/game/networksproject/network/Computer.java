package com.janfic.game.networksproject.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.janfic.game.networksproject.RealTimeGameGame;

public class Computer extends NetworkNode {

    boolean playing;
    float ping;
    float waitTime;

    public Computer(float x, float y) {
        super(x, y, RealTimeGameGame.Assets.COMPUTER);
        ping = 0;
        playing = false;
        waitTime = 0;
    }

    public Computer(float x, float y, Texture texture) {
        super(x, y, texture);
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    @Override
    public int getMaxEdges() {
        return 1;
    }

    @Deprecated
    @Override
    protected void draw(ShapeRenderer sr) {
        if (!sr.isDrawing()) {
            sr.begin(ShapeRenderer.ShapeType.Filled);
        }
        sr.setColor(getColor());
        sr.rect(getX() - 16, getY() - 16, 32, 32);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        sr.rect(getX() - 16, getY() - 16, 32, 32);
        sr.end();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        waitTime -= delta;
        if (playing == false) {
            ping += delta;
        }
    }

    public float getWaitTime() {
        return waitTime;
    }

    @Override
    public void recievePacket(Packet packet) {
        ping = packet.getTime();
    }

    @Override
    public void sendPacket(Packet packet) {
        NetworkEdge connection = this.getEdge(packet.getNext());
        connection.sendData(packet);
    }

    public void setWaitTime(float waitTime) {
        this.waitTime = waitTime;
    }

    public Router getRouter() {
        return (Router) getEdge(this).getOther(this);
    }

    public float getDemand() {
        return Math.max(0, 1 - ping / 50);
    }

}
