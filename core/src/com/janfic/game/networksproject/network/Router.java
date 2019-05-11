package com.janfic.game.networksproject.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.janfic.game.networksproject.RealTimeGameGame;

/**
 *
 * @author Jan Fic
 */
public class Router extends NetworkNode {

    float time;
    
    public Router(float x, float y) {
        super(x, y, RealTimeGameGame.Assets.ROUTER);
        time = 0;
    }

    @Override
    public int getMaxEdges() {
        return 10;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta;
    }
    
    public float getTime() {
        return time;
    }
    
    @Deprecated
    @Override
    protected void draw(ShapeRenderer sr) {
        if (!sr.isDrawing()) {
            sr.begin(ShapeRenderer.ShapeType.Filled);
        }
        sr.setColor(getColor());
        sr.triangle(getX() - 8, getY() - 8, getX() + 8, getY() - 8, getX(), getY() + 8);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        sr.triangle(getX() - 8, getY() - 8, getX() + 8, getY() - 8, getX(), getY() + 8);
        sr.end();
    }

    @Override
    public void recievePacket(Packet packet) {
        packet.arrive();
        if (packet.getNext() != null) {
            sendPacket(packet);
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        NetworkEdge connection = this.getEdge(packet.getNext());
        if (connection != null) {
            connection.sendData(packet);
        }
    }

}
