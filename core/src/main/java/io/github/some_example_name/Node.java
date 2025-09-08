package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.List;

public class Node {
    int radius, id, pos_x, pos_y;
    List<Edge> edges;
    Color colour = Color.WHITE;

    public Node(int radius, int id, int pos_x, int pos_y, List<Edge> edges) {
        this.radius = radius;
        this.id = id;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.edges = edges;
    }

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

    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Color getColor() {
        return colour;
    }

    public void setColor(Color color) {
        this.colour = color;
    }

    public void render(ShapeRenderer sr){
        sr.setColor(colour);
        sr.circle(pos_x, pos_y, radius);
    }
}
