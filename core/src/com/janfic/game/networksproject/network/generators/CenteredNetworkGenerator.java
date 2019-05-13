package com.janfic.game.networksproject.network.generators;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.janfic.game.networksproject.network.Computer;
import com.janfic.game.networksproject.network.Network;
import com.janfic.game.networksproject.network.NetworkEdge;
import com.janfic.game.networksproject.network.NetworkNode;
import com.janfic.game.networksproject.network.Router;
import com.janfic.game.networksproject.network.Server;
import java.util.ArrayList;

/**
 *
 * @author Jan Fic
 */
public class CenteredNetworkGenerator extends NetworkGenerator {

    private int numberOfNodes;
    private int radiusOver;

    public CenteredNetworkGenerator(int gridSize, int initialNodes, ShapeRenderer sr) {
        super(gridSize, 0, 0, sr);
        this.numberOfNodes = initialNodes;
        radiusOver = -1;
    }

    @Override
    public Network create(int width, int height) {
        //int numberOfEdges = random.nextInt(maxEdges - minEdges) + minEdges;
        ArrayList<NetworkNode> nodes = new ArrayList<NetworkNode>();
        ArrayList<NetworkEdge> edges = new ArrayList<NetworkEdge>();
        Network n = new Network(nodes, edges, width, height, sr);
        radiusOver = -1;
        for (int i = 0; i < numberOfNodes; i++) {
            addNode(n);
        }

        return n;
    }

    @Override
    public Network create(int width, int height, int overideRadius) {
        //int numberOfEdges = random.nextInt(maxEdges - minEdges) + minEdges;
        ArrayList<NetworkNode> nodes = new ArrayList<NetworkNode>();
        ArrayList<NetworkEdge> edges = new ArrayList<NetworkEdge>();
        Network n = new Network(nodes, edges, width, height, sr);
        this.radiusOver = overideRadius;

        for (int i = 0; i < numberOfNodes; i++) {
            addNode(n);
        }

        return n;
    }

    @Override
    public NetworkNode addNode(Network network) {
        float maxRadius = Math.min(network.getWidth() / 2, network.getHeight() / 2);
        float radius = getRadius(network);
        double rad = random.nextFloat() * 2 * Math.PI;
        float x = (float) (radius * Math.cos(rad)) + network.getWidth() / 2;
        float y = (float) (radius * Math.sin(rad)) + network.getHeight() / 2;

        if (network.isPositionNear(x, y, gridSize)) {
            return null;
        }
        NetworkNode node;
        float rollForComputer = random.nextFloat();
        float rate = maxRadius == -1 ? radius / maxRadius : 0.5f;
        if (rollForComputer < rate) {
            node = new Computer(x, y);
        } else {
            node = new Router(x, y);
        }
        if (network.getNumberOfNodes() == 0) {
            node = new Router(x, y);
        }

        NetworkNode n = addNode(network, node);

        return n;
    }

    @Override
    public NetworkNode addNode(Network network, NetworkNode node) {
        ArrayList<NetworkNode> nearest = network.getClosestNodes(node, 2 * gridSize);
        if (!nearest.isEmpty()) {
            int amountOfEdges = Math.min(2, random.nextInt(node.getMaxEdges()) + 1);
            for (int i = 0; i < amountOfEdges && i < nearest.size(); i++) {
                if (nearest.get(i).canAddEdge()) {
                    node.addConnection(new NetworkEdge(node, nearest.get(i), nearest.get(i) instanceof Router && node instanceof Router ? 100 : 50, sr));
                }
            }
        }

        if (node instanceof Computer) {
            for (NetworkNode connectedNode : node.getConnectedNodes()) {
                if (connectedNode instanceof Computer) {
                    return null;
                }
            }
        }

        if (node instanceof Router) {
            boolean computer = false;
            boolean router = false;
            for (NetworkNode connectedNode : node.getConnectedNodes()) {
                if (connectedNode instanceof Computer) {
                    computer = true;
                }
                if (connectedNode instanceof Router) {
                    router = true;
                }
            }
            if (computer && !router) {
                return null;
            }
        }

        if (!node.getConnections().isEmpty() || network.getNumberOfNodes() == 0) {
            for (NetworkEdge connection : node.getConnections()) {
                connection.getOther(node).addConnection(connection);
            }
            network.addNetworkNode(node);
        } else {
            return null;
        }

        return node;
    }

    @Override
    public NetworkNode addNode(Network network, float x, float y) {
        if (network.isPositionNear(x, y, gridSize)) {
            return null;
        }
        NetworkNode node;
        int rollForComputer = random.nextInt(2);
        if (rollForComputer == 0) {
            node = new Computer(x, y);
        } else {
            node = new Router(x, y);
        }
        if (network.getNumberOfNodes() == 0) {
            node = new Router(x, y);
        }

        return addNode(network, node);
    }

    @Override
    public NetworkNode addNode(Network network, Class<? extends NetworkNode> type, int x, int y) {
        if (network.isPositionNear(x, y, gridSize)) {
            return null;
        }
        NetworkNode node;
        if (type == Computer.class) {
            node = new Computer(x, y);
        } else if (type == Router.class) {
            node = new Router(x, y);
        } else {
            node = new Server(x, y);
        }

        return addNode(network, node);
    }

    @Override
    public NetworkNode addNode(Network network, Class<? extends NetworkNode> type) {
        float radius = getRadius(network);
        double rad = random.nextFloat() * 2 * Math.PI;
        float x = (float) (radius * Math.cos(rad)) + network.getWidth() / 2;
        float y = (float) (radius * Math.sin(rad)) + network.getHeight() / 2;

        return addNode(network, type, (int) x, (int) y);
    }

    public float getRadius(Network network) {
        float maxRadius = Math.min(network.getWidth() / 2, network.getHeight() / 2);
        float radius = (float) (Math.sqrt(network.getNumberOfNodes() * 2) * gridSize);
        radius = random.nextFloat() * radius;
        if (radiusOver == -1) {
            while (radius > maxRadius) {
                radius = random.nextFloat() * (float) (Math.sqrt(network.getNumberOfNodes() * 2) * gridSize);
            }
        }

        return radius;
    }

}
