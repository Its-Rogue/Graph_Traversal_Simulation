package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

public class Node {
    int radius, id;
    vec2 position;
    List<Edge> edges;
    List<Node> neighbours;
    Color colour = Color.WHITE;

    // Constructor for the nodes
    public Node(int radius, int id, vec2 position, List<Edge> edges, List<Node> neighbours, Color colour) {
        this.radius = radius;
        this.id = id;
        this.position = position;
        this.edges = edges;
        this.neighbours = neighbours;
        this.colour = colour;
    }

    // Getters and setters for the nodes
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public vec2 getPosition() {
        return position;
    }

    public void setPosition(vec2 position) {
        this.position = position;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public List<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Node> neighbours) {
        this.neighbours = neighbours;
    }

    public Color getColor() {
        return colour;
    }

    public void setColor(Color color) {
        this.colour = color;
    }

    // Render code for each node within the adj list in the graph
    public void render(ShapeRenderer sr){
        sr.setColor(colour);
        sr.circle(position.x, position.y, radius);
    }
}