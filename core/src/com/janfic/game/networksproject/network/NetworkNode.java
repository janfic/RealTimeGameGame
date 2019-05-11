package com.janfic.game.networksproject.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;

public abstract class NetworkNode extends Actor {

    private final Sprite sprite;
    private final ArrayList<NetworkEdge> connections;
    private final EdgeDistanceComparator comparator;
    private Map<NetworkNode, Float> shortestDistances, longestDistances;
    private Map<NetworkNode, LinkedList<NetworkNode>> shortestPaths, longestPaths;

    public NetworkNode(float x, float y, Texture texture) {
        this.sprite = new Sprite(texture);
        this.setX(x);
        this.setY(y);
        this.setBounds(x, y, 0, 0);
        connections = new ArrayList<NetworkEdge>();
        comparator = new EdgeDistanceComparator();
        this.setColor(Color.WHITE);
    }

    public abstract int getMaxEdges();

    public abstract void recievePacket(Packet packet);

    public abstract void sendPacket(Packet packet);

    public ArrayList<NetworkEdge> getConnections() {
        return connections;
    }

    public ArrayList<NetworkNode> getConnectedNodes() {
        ArrayList<NetworkNode> ret = new ArrayList<NetworkNode>();
        for (NetworkEdge connection : connections) {
            NetworkNode node = connection.getOther(this);
            if (isConnectedTo(node)) {
                ret.add(node);
            }
        }
        return ret;
    }

    public boolean isConnectedTo(NetworkNode node) {
        for (NetworkEdge connection : connections) {
            if (connection.isEdgeOf(node)) {
                return true;
            }
        }
        return false;
    }

    public boolean canAddEdge() {
        return connections.size() < getMaxEdges();
    }

    public void addConnection(NetworkEdge edge) {
        this.connections.add(edge);
        connections.sort(comparator);
    }
    
    public void removeConnection(NetworkEdge edge) {
        this.connections.remove(edge);
        connections.sort(comparator);
    }

    public NetworkEdge getEdge(NetworkNode node) {
        for (NetworkEdge connection : connections) {
            if (connection.isEdgeOf(node)) {
                return connection;
            }
        }
        return null;
    }

    public float euclideanDistance(float x, float y) {
        return (float) Math.sqrt(Math.pow(getX() - x, 2) + Math.pow(getY() - y, 2));
    }

    public float euclideanDistanceFromNode(NetworkNode node) {
        return (float) Math.sqrt(Math.pow(getX() - node.getX(), 2) + Math.pow(getY() - node.getY(), 2));
    }

    public void pingAll(Network network) {
        network.dijkstra(this, false);
        //network.dijkstra(this, true);
    }

    public float getMinimumDelay(NetworkNode node) {
        if (shortestDistances.containsKey(node)) {
            return shortestDistances.get(node);
        }
        return -1;
    }

    public float getMaximumDelay(NetworkNode node) {
        if (longestDistances.containsKey(node)) {
            return longestDistances.get(node);
        }
        return -1;
    }

    public Map<NetworkNode, LinkedList<NetworkNode>> getShortestPaths() {
        return shortestPaths;
    }

    public Map<NetworkNode, LinkedList<NetworkNode>> getLongestPaths() {
        return longestPaths;
    }

    public void setShortestDistances(Map<NetworkNode, Float> shortestDistances) {
        this.shortestDistances = shortestDistances;
    }

    public void setLongestDistances(Map<NetworkNode, Float> longestDistances) {
        this.longestDistances = longestDistances;
    }

    public void setShortestPaths(Map<NetworkNode, LinkedList<NetworkNode>> shortestPaths) {
        this.shortestPaths = shortestPaths;
    }

    public void setLongestPaths(Map<NetworkNode, LinkedList<NetworkNode>> longestPaths) {
        this.longestPaths = longestPaths;
    }

    //actor + drawing
    @Deprecated
    protected void draw(ShapeRenderer sr) {
        if (!sr.isDrawing()) {
            sr.begin(ShapeRenderer.ShapeType.Filled);
        }
        sr.circle(getX(), getY(), 5);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());
        batch.draw(sprite.getTexture(), getX() - 16, getY() - 16);
        batch.setColor(Color.WHITE);
    }

    //UTILS
    public class EdgeDistanceComparator implements Comparator<NetworkEdge> {

        @Override
        public int compare(NetworkEdge o1, NetworkEdge o2) {
            float delta = o1.getLength() - o2.getLength();
            if (delta < 0) {
                return -1;
            }
            if (delta > 0) {
                return 1;
            }
            return 0;
        }

    }
}
