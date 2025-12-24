package helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import fundamental_classes.Runtime_Data;
import structural_classes.Node;
import structural_classes.Edge;
import structural_classes.Graph;
import structural_classes.Region;
import structural_classes.vec2;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Generate_Graphs {
    public static void generate_grid(Runtime_Data data) {
        int spacing_x = 110;
        int spacing_y = 110;
        int starting_x = 900;
        int starting_y = 300;

        Graph.clear();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                data.getGraph().add_node(data.getNode_radius(), starting_x + (spacing_x * j), starting_y + (spacing_y * i), data);
            }
        }

        // Normal grid layout creation
        for (int i = 0; i < 10; i++) { // loop over each node and add an edge with each of its neighbours
            for (int j = 0; j < 10; j++) {
                int id = i * 10 + j;
                if (i > 0) {
                    int up_id = (i - 1) * 10 + j;
                    data.getGraph().add_edge(id, up_id, 1, data);
                }
                if (i < 10 - 1) {
                    int down_id = (i + 1) * 10 + j;
                    data.getGraph().add_edge(id, down_id, 1, data);
                }
                if (j > 0) {
                    int left_id = i * 10 + (j - 1);
                    data.getGraph().add_edge(id, left_id, 1, data);
                }
                if (j < 10 - 1) {
                    int right_id = i * 10 + (j + 1);
                    data.getGraph().add_edge(id, right_id, 1, data);
                }
            }
        }

        data.getChange_edge_weight_popup().setVisible(false);
        data.setEdge_to_edit(null); // Override status of popup due to how the edges are created
    }

    public static void generate_random_graph(Runtime_Data data) {
        Graph.clear();
        List<Region> regions = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                regions.add(new Region(270 + data.getNode_radius() + 115 * j, 270 + data.getNode_radius() + 115 + 115 * j, data.getNode_radius() + 50 + 320 * i, data.getNode_radius() + 122 + 320 * i));
            }
        }

        List<Node> generated_nodes = new ArrayList<>();
        int node_id = 0;

        region_loop: // Iteration label so the iteration can be skipped if the node fails to regenerate 15 times
        for (Region r: regions) {
            if (ThreadLocalRandom.current().nextInt(0,100) < 15 && data.getGraph().get_total_edges() < 100) {
                int rand_x = ThreadLocalRandom.current().nextInt(r.getX1(), r.getX2()); // Generate random x y values based on the x y bounds of the region
                int rand_y = ThreadLocalRandom.current().nextInt(r.getY1(), r.getY2());

                int loop_count_break = 0;

                while (rand_x > Gdx.graphics.getWidth() - 2 * data.getNode_radius() || rand_x < 250 + 2 * data.getNode_radius()) {
                    rand_x = ThreadLocalRandom.current().nextInt(r.getX1(), r.getX2()); // Regenerate x if it is not fully within the bounds of the screen
                    loop_count_break ++;
                    if (loop_count_break > 15) {
                        continue region_loop; // Make sure it doesn't loop infinitely
                    }
                }

                loop_count_break = 0;
                while (rand_y > Gdx.graphics.getHeight() - 2 * data.getNode_radius() || rand_y < 2 * data.getNode_radius()) {
                    rand_y = ThreadLocalRandom.current().nextInt(r.getY1(), r.getY2()); // Regenerate y if it is not fully within the bounds of the screen
                    loop_count_break++;
                    if (loop_count_break > 15) {
                        continue region_loop; // Make sure it doesn't loop infinitely
                    }
                }

                generated_nodes.add(new Node(data.getNode_radius(), node_id, new vec2(rand_x, rand_y), new ArrayList<>(), Color.WHITE)); // Create the node
                node_id++; // Increment the next node id
            }
        }

        // Remove nodes that are too close to each other (within 4 * node_radius)
        for (int i = generated_nodes.size() - 1; i >= 0; i--) {
            Node n = generated_nodes.get(i);
            boolean invalid_position = false;

            for (int j = 0; j < i; j++) {
                Node n1 = generated_nodes.get(j);

                if (calculate_distance(n.getPosition().getX(), n1.getPosition().getX(), n.getPosition().getY(), n1.getPosition().getY()) < 4 * data.getNode_radius()) {
                    invalid_position = true;
                    break;
                }
            }

            if (invalid_position) {
                generated_nodes.remove(i);
            }
        }

        // Add edges between nodes that are less than 750px apart
        for (int i = generated_nodes.size() - 1; i >= 0; i--) {
            Node n = generated_nodes.get(i);
            for (int j = i + 1; j < generated_nodes.size(); j++) {
                Node n1 = generated_nodes.get(j);
                if (calculate_distance(n.getPosition().getX(), n1.getPosition().getX(), n.getPosition().getY(), n1.getPosition().getY()) <= 750) {
                    if (!n.getNeighbours().contains(n1)) {
                        n.getNeighbours().add(n1);
                    }
                    if (!n1.getNeighbours().contains(n)) {
                        n1.getNeighbours().add(n);
                    }
                }
            }
        }

        for (Node n: generated_nodes) {
            data.getGraph().add_node(n); // Add nodes to graph
        }

        List<Edge> edges = new ArrayList<>();
        for (Node n: generated_nodes) {
            for (Node neighbour: n.getNeighbours()) {
                if (n.getNeighbours().contains(neighbour)) {
                    edges.add(new Edge(n, neighbour, 1, "forward", Color.WHITE));
                    edges.add(new Edge(neighbour, n, 1, "reverse", Color.WHITE));
                }
            }
        }

        for (Edge e: edges) {
            data.getGraph().add_edge(e);
        }
    }

    private static double calculate_distance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidean distance calculation
    }
}
