package fundamental_classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Edge {
    Node source;
    Node target;
    int weight = 1; // Default weight if none is inputted
    Color colour = Color.WHITE;

    // Constructors for the edges
    public Edge(Node source, Node target, int weight, Color colour) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        this.colour = colour;
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

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    // Render code for each edge of a node in the graph, with width 2
    public void render(ShapeRenderer sr) {
        sr.setColor(getColour());
        sr.rectLine(source.getPosition().x, source.getPosition().y, target.getPosition().x, target.getPosition().y, 2);
    }
}