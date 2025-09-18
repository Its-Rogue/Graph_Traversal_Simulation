package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Edge {
    Node source;
    Node target;
    int weight = 0; // Default weight if none is inputted
    Color colour = Color.WHITE;

    // Constructors for the edges
    public Edge(Node source, Node target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    // Getters and setters for the edges
    public Node getSource() {
        return source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    // Render code for each edge of a node in the graph, with width 10
    public void render(ShapeRenderer sr) {
        sr.setColor(colour);
        sr.rectLine(source.getPos_x(), source.getPos_y(), target.getPos_x(), target.getPos_y(), 15);
    }
}