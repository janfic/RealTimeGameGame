package com.janfic.game.networksproject.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.*;

public class NetworkEdge extends Actor {

    private final ShapeRenderer sr;

    private final NetworkNode head, tail;

    public final List<Packet> packets;
    private Map<Packet, NetworkNode> tos, froms;

    private float length;
    private float density;
    private float speed;

    public NetworkEdge(NetworkNode head, NetworkNode tail, float speed, ShapeRenderer sr) {
        this.sr = sr;
        this.head = head;
        this.tail = tail;
        this.speed = speed;
        this.length = calcLength();
        packets = new LinkedList<Packet>();
        tos = new HashMap<Packet, NetworkNode>();
        froms = new HashMap<Packet, NetworkNode>();
        density = 0;
    }

    private float calcLength() {
        float headX = head.getX();
        float headY = head.getY();
        float tailX = tail.getX();
        float tailY = tail.getY();

        double dist = Math.sqrt(Math.pow(headX - tailX, 2) + Math.pow(headY - tailY, 2));
        return (float) dist;
    }

    public float getLength() {
        return length;
    }

    public NetworkNode getHead() {
        return head;
    }

    public NetworkNode getTail() {
        return tail;
    }

    public boolean isEdgeOf(NetworkNode node) {
        return node.equals(head) || node.equals(tail);
    }

    public NetworkNode getOther(NetworkNode node) {
        if (head == node) {
            return tail;
        } else if (tail == node) {
            return head;
        } else {
            return null;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
        sr.rectLine(head.getX(), head.getY(), tail.getX(), tail.getY(), 5 + (speed > 50 ? 3 : 0));
        sr.setColor(getColor());
        sr.rectLine(head.getX(), head.getY(), tail.getX(), tail.getY(), 3 + (speed > 50 ? 3 : 0));
        if (speed > 50) {
            sr.line(head.getX(), head.getY(), tail.getX(), tail.getY());
        }
        sr.end();

        batch.begin();
        for (Packet packet : packets) {
            if (packet != null) {
                packet.draw(batch, Gdx.graphics.getDeltaTime());
            }
        }

    }

    @Override
    public Color getColor() {
        return new Color(1, 1 - packets.size() / 64f, 1 - packets.size() / 64f, 1);
    }

    public void sendData(Packet packet) {
        packets.add(packet);
        packet.setSpeed(speed);
        tos.put(packet, packet.getNext());
        froms.put(packet, packet.getCurrent());
    }

    public int getNumberOfPackets() {
        return packets.size();
    }

    public void act(float deltaTime) {
        for (int i = 0; i < packets.size(); i++) {
            Packet packet = packets.get(i);
            packet.update(length, deltaTime);
            NetworkNode to = tos.get(packet);
            NetworkNode from = froms.get(packet);
            float x = (to.getX() - from.getX()) * packet.getPercentTraveled();
            float y = (to.getY() - from.getY()) * packet.getPercentTraveled();
            packet.setPosition(x + from.getX(), y + from.getY());

            if (packet.getPercentTraveled() >= 1) {
                packets.remove(i);
                to.recievePacket(packet);
                tos.remove(packet);
                froms.remove(packet);
                i--;
            }
        }
    }
    
    public int getPacketAmount() {
        return packets.size();
    }
}