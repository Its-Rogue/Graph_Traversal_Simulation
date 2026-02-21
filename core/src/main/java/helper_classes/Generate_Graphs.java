package helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import essential_classes.Runtime_Data;
import structural_classes.Node;
import structural_classes.Edge;
import structural_classes.vec2;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.concurrent.ThreadLocalRandom;

public class Generate_Graphs {
    public static void generate_grid(Runtime_Data data) {
        if (data.isTraversal_in_progress()) {
            data.getError_popup_label().setText("Cannot generate graph while traversal is running");
            data.getError_popup().setVisible(true);
            return;
        }

        data.getGraph().clear(); // Reset the graph to allow new data to be written to it

        // Calculate the available area to draw the graph, and the spaces required to clearly display the nodes and edges
        int left_margin = 270 + 2 * data.getNode_radius();
        int scene_width = Gdx.graphics.getWidth() - left_margin - 2 * data.getNode_radius();
        int scene_height = Gdx.graphics.getHeight() - 4 * data.getNode_radius();
        int spacing = Math.min(scene_width / 11, scene_height / 11);
        int grid_width = spacing * 9;
        int grid_height = spacing * 9;
        int starting_x = left_margin + (scene_width - grid_width) / 2;
        int starting_y = 2 * data.getNode_radius() + (scene_height - grid_height) / 2;

        // Create each of the nodes in the 10 * 10 grid
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                data.getGraph().add_node(data.getNode_radius(), starting_x + (spacing * j), starting_y + (spacing * i), data);
            }
        }

        // Create the 2-4 edges between each node and it's adjacent nodes
        for (int i = 0; i < 10; i++) {
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
        if (data.isTraversal_in_progress()) {
            data.getError_popup_label().setText("Cannot generate graph while traversal is running");
            data.getError_popup().setVisible(true);
            return;
        }

        data.getGraph().clear(); // Clear the graph to allow for a new one to be generated

        final int columns = 20; // Constant values used throughout the function
        final int rows = 20;
        final int variance = 35;
        final int cell_width = 115;
        final int cell_height = 320;
        final int max_neighbours = 4;
        final int node_spawn_percentage = 35;
        final int max_edge_length = 500;
        final int start_x = 270 + 2 * data.getNode_radius();
        final int start_y = 2 * data.getNode_radius();

        List<Node> generated_nodes = new ArrayList<>();
        int node_id = 0; // Iterated id so the nodes can be rendered and altered correctly

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (ThreadLocalRandom.current().nextInt(100) < node_spawn_percentage) { // Only generate a node every 1 in 100/percentage times
                    continue;
                }

                int center_x = start_x + j * cell_width;
                int center_y = start_y + i * cell_height;
                int rand_x = center_x + ThreadLocalRandom.current().nextInt(-variance, variance);
                int rand_y = center_y + ThreadLocalRandom.current().nextInt(-variance, variance); // Generate random position within defined bounds

                if (rand_x < 250 + 2 * data.getNode_radius()) { // Make sure node is in the screen bounds, fail generation if not
                    continue;
                } else if (rand_x > Gdx.graphics.getWidth() - 2 * data.getNode_radius()) {
                    continue;
                } else if (rand_y < 2 * data.getNode_radius()) {
                    continue;
                } else if (rand_y > Gdx.graphics.getHeight() - 2 * data.getNode_radius()) {
                    continue;
                }

                boolean valid = true; // Assume valid placement
                for (Node n: generated_nodes) {
                    if (calculate_distance(rand_x, n.getPosition().getX(), rand_y, n.getPosition().getY()) < 6 * data.getNode_radius()) {
                        valid = false; // Invalid if too close to any other node
                        break;
                    }
                }

                if (!valid) {
                    continue; // Do not generate node if placement is invalid
                }

                generated_nodes.add(new Node(data.getNode_radius(), node_id++, new vec2(rand_x, rand_y), new ArrayList<>(), Color.WHITE, " "));
            }
        }

        List<Object[]> new_edges = new ArrayList<>(); // Temp list to hold data which will be used to make the edges

        for (int i = 0; i < generated_nodes.size(); i++) {
            Node n = generated_nodes.get(i);
            for (int j = i + 1; j < generated_nodes.size(); j++) {
                Node n1 = generated_nodes.get(j);
                double distance_between = calculate_distance(n.getPosition().getX(), n1.getPosition().getX(), n.getPosition().getY(), n1.getPosition().getY());
                if (distance_between <= max_edge_length) {
                    new_edges.add(new Object[]{n, n1, distance_between}); // Create an edge if the distance between the 2 nodes is less than the max distance
                }
            }
        }

        new_edges.sort(Comparator.comparingDouble(e -> (double) e[2])); // Sort the edges by their distance

        for (Object[] e: new_edges) { // Loop through the edges and get the source / target for each
            Node n = (Node) e[0];
            Node n1 = (Node) e[1];

            if (n.getNeighbours().size() > max_neighbours) { // Make sure a node has less than max_neighbours + 1 neighbours
                continue;
            } else if (n1.getNeighbours().size() > max_neighbours) {
                continue;
            }

            if (!n.getNeighbours().contains(n1)) {
                n.add_neighbour(n1); // Add each node as a neighbour to each other
                n1.add_neighbour(n);
            }
        }

        generated_nodes.removeIf(n -> n.getNeighbours().isEmpty()); // Remove the nodes that have no neighbours

        keep_largest_chunk(generated_nodes);

        for (Node n: generated_nodes) {
            data.getGraph().add_node(n); // Add nodes to graph
        }

        Set<String> processed_pairs = new HashSet<>();
        List<Edge> edges = new ArrayList<>(); // Temporary container for new edges so they can be added to the graph
        for (Node n: generated_nodes) {
            for (Node neighbour: n.getNeighbours()) {
                String pair_key = Math.min(n.getId(), neighbour.getId()) + "-" + Math.max(n.getId(), neighbour.getId()); // Create unique id for node pair
                if (processed_pairs.contains(pair_key)) { // Do not create edge if pair already processed
                    continue;
                } processed_pairs.add(pair_key);

                int weight_base = Math.round((long) ((calculate_distance(n.getPosition().getX(), neighbour.getPosition().getX(), n.getPosition().getY(), neighbour.getPosition().getY()) / 100)));
                int weight = ThreadLocalRandom.current().nextInt(-2 * weight_base, 2 * weight_base);
                weight = Math.abs(weight); // Get absolute value of edge weight to prevent errors with Dijkstra's and A*
                edges.add(new Edge(n, neighbour, weight, "forward", Color.WHITE)); // Add forward and reverse edges between
                edges.add(new Edge(neighbour, n, weight, "reverse", Color.WHITE)); // Neighbouring nodes
            }
        }

        for (Edge e: edges) {
            data.getGraph().add_edge(e); // Add edges to the graph sequentially
        }
    }

    private static double calculate_distance(int x1, int x2, int y1, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)); // Euclidean distance calculation
    }

    private static List<List<Node>> find_chunks(List<Node> nodes) {
        List<List<Node>> chunks = new ArrayList<>();
        Set<Node> visited = new HashSet<>();

        for (Node n: nodes) {
            if (visited.contains(n)) {
                continue;
            }

            List<Node> chunk = new ArrayList<>();
            Deque<Node> stack = new ArrayDeque<>();
            stack.push(n); // Add node to stack and visited lists
            visited.add(n);

            while (!stack.isEmpty()) {
                Node n1 = stack.pop(); // Get the top node and add it to the current chunk
                chunk.add(n1);

                for (Node n2: n1.getNeighbours()) { // Loop through each neighbour of n1 and add them to the lists if they aren't already present
                    if (!visited.contains(n2)) {
                        visited.add(n2);
                        stack.add(n2);
                    }
                }
            }

            chunks.add(chunk); // Add the current chunk to the chunks collection
        }

        return chunks; // Return the list of chunks
    }

    private static void keep_largest_chunk(List<Node> nodes) {
        List<List<Node>> chunks = find_chunks(nodes); // Call find_chunks to get the various chunks in the graph
        if (chunks.size() <= 1) {
            return; // Return if no action is needed, when there is only one chunk
        }

        List<Node> largest_chunk = chunks.get(0); // Assume the largest chunk is in position 0
        for (List<Node> c: chunks) { // Loop through all the chunks in the collection
            if (c.size() > largest_chunk.size()) { // Check to see if the current chunk is bigger than the assumed
                largest_chunk = c; // Update accordingly
            }
        }

        Set<Node> chunk_to_keep = new HashSet<>(largest_chunk); // Mark the largest chunk as the chunk to be kept and not culled
        nodes.removeIf(n -> !chunk_to_keep.contains(n)); // Cull the nodes not in the largest chunk

        for (Node n: nodes) {
            List<Node> nodes_to_remove = new ArrayList<>();
            for (Node n1: n.getNeighbours()) {
                if (!chunk_to_keep.contains(n1)) {
                    nodes_to_remove.add(n1);
                }
            }

            for (Node n1: nodes_to_remove) {
                n.remove_neighbour(n1);
            }
        }
    }
}