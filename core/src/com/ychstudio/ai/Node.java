package com.ychstudio.ai;

public class Node {

    int gCost;
    int hCost;

    Node prevNode;
    Node nextNode;

    float x;
    float y;

    public Node(float x, float y) {
        this.x = x;
        this.y = y;

        gCost = 0;
        hCost = 0;

        prevNode = null;
        nextNode = null;
    }

    public int fCost() {
        return gCost + hCost;
    }

}
