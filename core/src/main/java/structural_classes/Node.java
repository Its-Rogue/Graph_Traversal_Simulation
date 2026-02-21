package structural_classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int radius, id;
    private vec2 position;
    private final List<Node> neighbours;
    private Color colour;
    private String label;

    // Constructor for the nodes
    public Node(int radius, int id, vec2 position, List<Node> neighbours, Color colour, String label) {
        this.radius = radius;
        this.id = id;
        this.position = position;
        this.neighbours = neighbours;
        this.colour = colour;
        this.label = label;
    }

    // Getters and setters for the nodes
    public int getId() {
        return id;
    }

    public vec2 getPosition() {
        return position;
    }

    public void setPosition(vec2 position){this.position = position;}

    public List<Node> getNeighbours() {
        return new ArrayList<>(neighbours);
    }

    public void add_neighbour(Node neighbour) {
        neighbours.add(neighbour);
    }

    public void remove_neighbour(Node neighbour) {
        neighbours.remove(neighbour);
    }

    public Color getColour() {
        return colour;
    }

    public void setColour(Color color) {
        this.colour = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    // Render code for each node within the adj list in the graph
    public void render(ShapeRenderer sr) {
        sr.setColor(colour);
        sr.circle(position.getX(), position.getY(), radius);
    }
}