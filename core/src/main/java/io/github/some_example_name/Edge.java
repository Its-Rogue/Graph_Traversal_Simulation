package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Edge {
    Node source;
    Node target;
    int weight = 0;
    Color colour = Color.WHITE;

    public Edge(Node source, Node target, int weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

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

    public void render(ShapeRenderer sr) {
        sr.setColor(colour);
        sr.rectLine(source.getPos_x(), source.getPos_y(), target.getPos_x(), target.getPos_y(), 10);
    }
}
