package fundamental_classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import helper_classes.*;

import java.util.List;

public class Node {
    int radius, id;
    vec2 position;
    List<Node> neighbours;
    Color colour;

    // Constructor for the nodes
    public Node(int radius, int id, vec2 position, List<Node> neighbours, Color colour) {
        this.radius = radius;
        this.id = id;
        this.position = position;
        this.neighbours = neighbours;
        this.colour = colour;
    }

    // Getters and setters for the nodes

    public int getId() {
        return id;
    }

    public vec2 getPosition() {
        return position;
    }

    public List<Node> getNeighbours() {
        return neighbours;
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color color) {
        this.colour = color;
    }

    // Render code for each node within the adj list in the graph
    public void render(ShapeRenderer sr){
        sr.setColor(colour);
        sr.circle(position.x, position.y, radius);
    }
}