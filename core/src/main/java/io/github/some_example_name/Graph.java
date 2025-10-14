package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;

import java.util.*;

public class Graph {
    private static Map<Node, List<Edge>> adj_list;
    private static int next_node_ID = 0;
    private static PriorityQueue<Integer> free_IDs;

    public Graph() {
        adj_list = new HashMap<>();
        free_IDs = new PriorityQueue<Integer>();
    }

    // Finds the lowest available ID from the priority queue, then creates a node with
    // that ID at the specified coords with the specified radius, then adds it to the
    // adj list
    public void add_node(int node_radius, int x_pos, int y_pos) {
        int id;
        if (!free_IDs.isEmpty()) {
            id = free_IDs.poll();
        } else {
            id = next_node_ID++;
        }

        if(adj_list.size() < 100){
            vec2 position  = new vec2(x_pos, y_pos);
            Node node = new Node(node_radius, id, position, new ArrayList<>(), new ArrayList<>(), Color.WHITE);
            adj_list.put(node, new ArrayList<>());
        }
    }

    // Gets the ID of the target and source node, checks they exist, then adds a forward
    // and reverse edge between them
    public void add_edge(int source_id, int target_id, int weight) {
        Node source = get_node_id(source_id);
        Node target = get_node_id(target_id);
        if (source == null || target == null) return;

        for(Edge edge: adj_list.get(source)) {
            if(edge.getTarget().getId() == target.getId()) {
                return;
            }
        }

        adj_list.get(source).add(new Edge(source, target, weight));
        adj_list.get(target).add(new Edge(target, source, weight));
        source.neighbours.add(target);
        target.neighbours.add(source);
    }

    // Gets the node from the ID, checks if it exists, then removes every edge it has
    // before remove the node itself and adding its ID back to the list of available IDs
    public void remove_node(int node_id) {
        Node node = get_node_id(node_id);
        if (node == null) return;
        for (List<Edge> edges : adj_list.values()) {
            edges.removeIf(edge -> edge.getTarget().getId() == node_id);
        }

        for (Node n: node.getNeighbours()) {
            n.getNeighbours().remove(node_id);
        }

        adj_list.remove(node);
        free_IDs.add(node_id);
    }

    // Get IDs for the source and target edge, checks if they exist and then removes
    // the forward and reverse edges for the pair
    public void remove_edge(int source_id, int target_id) {
        Node source = get_node_id(source_id);
        Node target = get_node_id(target_id);
        if (source == null || target == null) return;

        adj_list.get(source).removeIf(e -> e.getTarget().getId() == target_id);
        adj_list.get(target).removeIf(e -> e.getTarget().getId() == source_id);
        source.neighbours.remove(target);
        target.neighbours.remove(source);
    }

    // Checks the adj list and returns the node from its ID
    public Node get_node_id(int node_id) {
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

    public List<Node> get_neighbours(Node node) {
        return node.getNeighbours();
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
    public static void clear(){
        adj_list.clear();
        free_IDs.clear();
        next_node_ID = 0;
    }
}