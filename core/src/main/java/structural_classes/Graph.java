package structural_classes;

import com.badlogic.gdx.graphics.Color;
import essential_classes.Runtime_Data;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;

public class Graph {
    private final Map<Node, List<Edge>> adj_list;
    private int next_node_ID = 0;
    private final Priority_Queue free_IDs;

    public Graph() {
        adj_list = new HashMap<>();
        free_IDs = new Priority_Queue();
    }

    // Finds the lowest available ID from the priority queue, then creates a node - with that ID - at the specified coords
    // with the specified radius, then adds it to the adj list
    public void add_node(int node_radius, int x_pos, int y_pos, Runtime_Data data) {
        if (traversal_in_progress_check(data)) return;

        int id;
        if (!free_IDs.isEmpty()) {
            id = free_IDs.poll();
        } else {
            id = next_node_ID++;
        }

        if (adj_list.size() < 100) {
            vec2 position  = new vec2(x_pos, y_pos);
            Node node = new Node(node_radius, id, position, new ArrayList<>(), Color.WHITE);
            adj_list.put(node, new ArrayList<>());
        }
    }

    public void add_node(Node node) {
        for (Node n: adj_list.keySet()) { // Check to see if the node already exists, and if so prevent the node from being added
            if (n.getId() == node.getId()) {
                return;
            }
        }

        if (node.getId() >= next_node_ID) { // Update the next node ID
            next_node_ID = node.getId() + 1;
        }

        free_IDs.remove(node.getId()); // Remove the ID from the list of free IDs
        adj_list.put(node, new ArrayList<>()); // Add the node without its neighbours to the adj list
    }

    // Gets the ID of the target and source node, checks they exist, then adds a bidirectional edge between them
    public void add_edge(int source_id, int target_id, int weight, Runtime_Data data) {
        if (traversal_in_progress_check(data)) return;

        Node source = get_node_from_id(source_id);
        Node target = get_node_from_id(target_id);
        if (source == null || target == null) return;

        for (Edge edge: adj_list.get(source)) {
            if (edge.getTarget().getId() == target.getId()) {
                if (!(data.getSelected_traversal().equals("Breadth-First Search") || data.getSelected_traversal().equals("Depth-First Search") || data.getSelected_traversal().equals("Bidirectional Search"))) {
                    float midpoint_x = (edge.getSource().getPosition().getX() + edge.getTarget().getPosition().getX()) / 2f;
                    float midpoint_y = (edge.getSource().getPosition().getY() + edge.getTarget().getPosition().getY()) / 2f;
                    data.getChange_edge_weight_popup().setPosition(midpoint_x, midpoint_y);
                    data.getChange_edge_weight_popup().setVisible(true); // Find midpoint of existing edge, then set the edit text field's position to it
                    data.setEdge_to_edit(edge); // Cache edge to be updated after user has inputted new weight
                }
                return;
            }
        }

        adj_list.get(source).add(new Edge(source, target, weight, "forward", Color.WHITE));
        adj_list.get(target).add(new Edge(target, source, weight, "reverse", Color.WHITE));
        source.add_neighbour(target);
        target.add_neighbour(source);
    }

    public void add_edge(Edge edge) {
        Node source = edge.getSource(); // Get the node structure for the source and target
        Node target = edge.getTarget();

        for (Edge e: adj_list.getOrDefault(source, new ArrayList<>())) { // Make sure that the edge does not already exist
            if (e.getTarget().getId() == target.getId()) {
                return;
            }
        }

        Edge reverse_edge = new Edge(target, source, edge.getWeight(), "reverse", Color.WHITE);

        List<Edge> source_edges = adj_list.computeIfAbsent(source, k -> new ArrayList<>()); // Get or create edge lists for both nodes
        List<Edge> target_edges = adj_list.computeIfAbsent(target, k -> new ArrayList<>());
        source_edges.add(edge); // Add the edge to both the source and target
        target_edges.add(reverse_edge);
        source.add_neighbour(target); // Add the source / target as neighbours to each other
        target.add_neighbour(source);
    }

    // Gets the node from the ID, checks if it exists, then removes every edge it has
    // before remove the node itself and adding its ID back to the list of available IDs
    public void remove_node(int node_id, Runtime_Data data) {
        if (traversal_in_progress_check(data)) return;
        Node node = get_node_from_id(node_id);
        if (node == null) return;
        for (List<Edge> edges : adj_list.values()) {
            edges.removeIf(edge -> edge.getTarget().getId() == node_id);
        }

        for (Node n: node.getNeighbours()) {
            n.getNeighbours().remove(node);
        }

        adj_list.remove(node);
        free_IDs.add(node_id);
    }

    // Get IDs for the source and target edge, checks if they exist and then removes
    // the forward and reverse edges for the pair
    public void remove_edge(int source_id, int target_id, Runtime_Data data) {
        if (traversal_in_progress_check(data)) return;
        Node source = get_node_from_id(source_id);
        Node target = get_node_from_id(target_id);
        if (source == null || target == null) return;

        adj_list.get(source).removeIf(e -> e.getTarget().getId() == target_id);
        adj_list.get(target).removeIf(e -> e.getTarget().getId() == source_id);
        source.remove_neighbour(target);
        target.remove_neighbour(source);
    }

    // Checks the adj list and returns the node from its ID
    public Node get_node_from_id(int node_id) {
        for (Node node : adj_list.keySet()) {
            if (node.getId() == node_id) {
                return node;
            }
        }
        return null;
    }

    // Returns all nodes in the adj list
    public Set<Node> get_nodes() {
        return adj_list.keySet();
    }

    // Returns all the edges for a specific node in the graph
    public List<Edge> get_edges(Node node) {
        return adj_list.getOrDefault(node, Collections.emptyList());
    }

    // Returns the total number of edges in the graph
    public int get_total_edges() {
        int count = 0;
        for (List<Edge> edges : adj_list.values()) {
            count += edges.size();
        }
        return count / 2; // Divide by 2 to discount reverse direction edges
    }

    // Clear the adj list and reset next node id and free ids to start over
    public void clear() {
        adj_list.clear();
        free_IDs.clear();
        next_node_ID = 0;
    }

    // Check to see if a traversal is already in progress to prevent the graph being altered,
    // preventing null data being fed into traversal algorithms
    private static boolean traversal_in_progress_check(Runtime_Data data) {
        if (data.isTraversal_in_progress()) {
            data.getError_popup_label().setText("You cannot edit the graph at this \ntime, a traversal is in progress.");
            data.getError_popup_label().setVisible(true);
            data.getError_popup().setVisible(true);
            return true;
        }
        else return false;
    }
}