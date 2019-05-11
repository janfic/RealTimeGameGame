package com.janfic.game.networksproject.network;

import java.util.Comparator;

public class NodeDistanceComparator implements Comparator<NetworkNode> {

    NetworkNode node;

    public NodeDistanceComparator(NetworkNode node) {
        this.node = node;
    }

    @Override
    public int compare(NetworkNode n1, NetworkNode n2) {
        return (int) (n1.euclideanDistanceFromNode(node) - n2.euclideanDistanceFromNode(node));
    }

}
