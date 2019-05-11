package com.janfic.game.networksproject.network;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.*;

public class Network extends Actor {

    private ArrayList<NetworkNode> nodes;
    private ArrayList<NetworkEdge> edges;

    ShapeRenderer sr;

    public Network(List<NetworkNode> nodes, List<NetworkEdge> edges, int width, int height, ShapeRenderer sr) {
        this.setWidth(width);
        this.setHeight(height);
        this.nodes = new ArrayList<NetworkNode>();
        this.edges = new ArrayList<NetworkEdge>();
        for (NetworkNode node : nodes) {
            this.nodes.add(node);
        }
        for (NetworkEdge edge : edges) {
            this.edges.add(edge);
        }
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }

    public int getNumberOfEdges() {
        return edges.size();
    }

    public void addNetworkNode(NetworkNode node) {
        nodes.add(node);
        for (NetworkEdge connection : node.getConnections()) {
            addNetworkEdge(connection);
        }
    }

    public void remove(ArrayList<? extends NetworkNode> nodes) {
        this.nodes.removeAll(nodes);
        for (NetworkNode node : nodes) {
            this.edges.removeAll(node.getConnections());
            for (int i = node.getConnections().size() - 1; i >= 0; i--) {
                NetworkEdge connection = node.getConnections().get(i);
                connection.getOther(node).removeConnection(connection);
                connection.remove();
            }
            node.remove();

        }
    }

    public void removeNode(NetworkNode node) {
        this.nodes.remove(node);
        this.edges.removeAll(node.getConnections());
        for (int i = node.getConnections().size() - 1; i >= 0; i--) {
            NetworkEdge connection = node.getConnections().get(i);
            connection.getOther(node).removeConnection(connection);
            connection.remove();
        }
        node.remove();
    }

    public void addNetworkEdge(NetworkEdge edge) {
        edges.add(edge);
    }

    public boolean isPositionNear(float x, float y, float r) {
        for (NetworkNode node : nodes) {
            if (node.euclideanDistance(x, y) < r) {
                return true;
            }
        }
        return false;
    }

    public NetworkNode getNearestNode(float x, float y) {
        float dist = Float.MAX_VALUE;
        NetworkNode ret = null;
        for (NetworkNode node : nodes) {
            if (node.euclideanDistance(x, y) < dist) {
                dist = node.euclideanDistance(x, y);
                ret = node;
            }
        }
        return ret;
    }

    public NetworkNode getNearestNode(NetworkNode other) {
        float dist = Float.MAX_VALUE;
        NetworkNode ret = null;
        for (NetworkNode node : nodes) {
            if (node.euclideanDistanceFromNode(other) < dist) {
                dist = node.euclideanDistanceFromNode(other);
                ret = node;
            }
        }
        return ret;
    }

    @Deprecated
    public ArrayList<NetworkNode> getNodesInClosestOrder(NetworkNode node) {
        NodeDistanceComparator comparator = new NodeDistanceComparator(node);
        nodes.sort(comparator);
        return nodes;
    }

    public ArrayList<NetworkNode> getClosestNodes(NetworkNode n, float radius) {
        NodeDistanceComparator comparator = new NodeDistanceComparator(n);
        ArrayList<NetworkNode> ret = new ArrayList<NetworkNode>();
        for (NetworkNode node : nodes) {
            if (node.euclideanDistanceFromNode(n) < radius) {
                ret.add(node);
            }
        }
        if (ret.isEmpty() && !nodes.isEmpty()) {
            return getClosestNodes(n, 2 * radius);
        }

        ret.sort(comparator);
        return ret;
    }

    public void dijkstra(NetworkNode root, boolean mode) {
        Map<NetworkNode, Float> distances = new HashMap<NetworkNode, Float>();
        Map<NetworkNode, LinkedList<NetworkNode>> paths = new HashMap<NetworkNode, LinkedList<NetworkNode>>();
        Map<NetworkNode, NetworkNode> previous = new HashMap<NetworkNode, NetworkNode>();

        for (NetworkNode node : nodes) {
            distances.put(node, mode ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY);
        }

        Set<NetworkNode> settledNodes = new HashSet<NetworkNode>();
        Set<NetworkNode> unsettledNodes = new HashSet<NetworkNode>();

        distances.put(root, 0f);
        unsettledNodes.add(root);

        while (!unsettledNodes.isEmpty()) {
            NetworkNode node = mode ? getHighestDistance(unsettledNodes, distances) : getLowestDistance(unsettledNodes, distances);
            unsettledNodes.remove(node);
            //get adjacent nodes
            ArrayList<NetworkNode> adjacent = node.getConnectedNodes();
            if (!(node instanceof Computer) || node == root) {

                for (NetworkNode networkNode : adjacent) {
                    if (!settledNodes.contains(networkNode)) {
                        setDistance(networkNode, node, distances, previous, mode);
                        unsettledNodes.add(networkNode);
                    }
                }
            }
            settledNodes.add(node);
        }

        for (NetworkNode node : nodes) {
            LinkedList<NetworkNode> path = new LinkedList<NetworkNode>();
            NetworkNode n = node;
            while (n != root) {
                path.push(n);
                n = previous.get(n);
            }
            path.push(root);
            paths.put(node, path);
        }

        if (mode) {
            root.setLongestDistances(distances);
            root.setLongestPaths(paths);
        } else {
            root.setShortestDistances(distances);
            root.setShortestPaths(paths);
        }
    }

    private void setDistance(NetworkNode node, NetworkNode previousNode, Map<NetworkNode, Float> distances, Map<NetworkNode, NetworkNode> previous, boolean mode) {
        NetworkEdge edge = previousNode.getEdge(node);
        float distance = distances.get(previousNode);
        float edgeLength = edge.getLength();
        if (!mode) {
            if (distance + edgeLength < distances.get(node)) {
                distances.put(node, distance + edgeLength);
                previous.put(node, previousNode);
            }
        } else {
            if (distance + edgeLength > distances.get(node)) {
                distances.put(node, distance + edgeLength);
                previous.put(node, previousNode);
            }
        }

    }

    public NetworkNode getLowestDistance(Set<NetworkNode> unsettledNodes, Map<NetworkNode, Float> distances) {
        NetworkNode ret = null;
        float low = Float.POSITIVE_INFINITY;
        for (NetworkNode key : unsettledNodes) {

            float distance = distances.get(key);
            if (distance < low) {
                low = distance;
                ret = key;
            }
        }
        return ret;
    }

    public NetworkNode getHighestDistance(Set<NetworkNode> unsettledNodes, Map<NetworkNode, Float> distances) {
        NetworkNode ret = null;
        float high = Float.NEGATIVE_INFINITY;
        for (NetworkNode key : unsettledNodes) {

            float distance = distances.get(key);
            if (distance > high) {
                high = distance;
                ret = key;
            }
        }
        return ret;
    }

    public NetworkNode getNode(int i) {
        return nodes.get(i);
    }

    public ArrayList<NetworkNode> getNodes() {
        return nodes;
    }

    public Set<NetworkNode> getLocalNetwork(ArrayList<? extends NetworkNode> nodes) {
        Set<NetworkNode> localNetwork = new HashSet<NetworkNode>();
        for (NetworkNode node : nodes) {
            node.pingAll(this);
            for (NetworkNode n : nodes) {
                LinkedList<NetworkNode> path = node.getShortestPaths().get(n);
                localNetwork.addAll(path);
            }
        }
        return localNetwork;
    }

    public void drawPaths(NetworkNode root, ShapeRenderer sr) {
        Map<NetworkNode, LinkedList<NetworkNode>> paths = root.getShortestPaths();
        if (paths == null) {
            root.pingAll(this);
            paths = root.getShortestPaths();
        }

        sr.begin(ShapeRenderer.ShapeType.Line);

        for (NetworkNode node : nodes) {
            LinkedList<NetworkNode> path = paths.get(node);
            for (int i = 0; i < path.size() - 1; i++) {
                NetworkNode a = path.get(i);
                NetworkNode b = path.get(i + 1);
                //sr.line(a.getX(), a.getY(), b.getX(), b.getY(), new Color((i * 32) / 256f, 0, 0, 1), new Color(((i + 1) * 32) / 256f, 0, 0, 1));
                sr.line(a.getX(), a.getY(), b.getX(), b.getY(), Color.WHITE, Color.BLACK);
            }
        }
        sr.setColor(Color.WHITE);
        sr.end();

    }

    public ArrayList<Server> getServers() {
        ArrayList<Server> servers = new ArrayList<Server>();
        for (NetworkNode node : nodes) {
            if (node instanceof Server) {
                servers.add((Server) node);
            }
        }
        return servers;
    }

    public ArrayList<Computer> getComputers() {
        ArrayList<Computer> computers = new ArrayList<Computer>();
        for (NetworkNode node : nodes) {
            if (node instanceof Computer && !(node instanceof Server)) {
                computers.add((Computer) node);
            }
        }
        return computers;
    }
    
    public float getDemand() {
        float demand = 0;
        for (Computer computer : getComputers()) {
            demand += computer.getDemand();
        }
        demand /= getComputers().size();
        return demand;
    }
    
    public int getPackets() {
        int n = 0;
        for (NetworkEdge edge : edges) {
            n += edge.getNumberOfPackets();
        }
        return n;
    }
}
