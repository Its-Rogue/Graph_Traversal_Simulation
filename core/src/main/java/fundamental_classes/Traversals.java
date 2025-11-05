package fundamental_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import helper_classes.Priority_Queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Traversals {
    static long operation_speed_base = 250;

    public static void bfs(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        long operation_speed = (long) (operation_speed_base / data.getTraversal_speed());

        Node start = data.getGraph().get_node_id(data.getStart_node());   // Cache start and end node to update colour at the end
        Node end = data.getGraph().get_node_id(data.getEnd_node());     // in the case they are overwritten

        ArrayList<Node> queue = new ArrayList<>(); // Initialise needed lists
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();
        final boolean[] found = {false};

        new Thread(() -> { // New local render thread to allow for nodes to change colours

            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight the chosen start and end node
                end.setColour(Color.RED);
            });

            queue.add(start); // Add the first node to the discovered and queue
            discovered.add(start);

            while (!queue.isEmpty() && !found[0] && !data.isTraversal_canceled()) { // Loop through until found or no more nodes in graph
                Node current_node = queue.remove(0); // Remove the node at the front of the queue

                if (data.isTraversal_canceled()) {
                    return;
                }

                Gdx.app.postRunnable(() -> {
                    if (current_node != start && current_node != end) {
                        current_node.setColour(Color.CYAN); // Highlight the current node if it is not start / end
                    }
                });

                for (Node neighbour : current_node.getNeighbours()) { // Check each neighbour of the current node
                    if (data.isTraversal_canceled()) {
                        return;
                    }

                    sleep(operation_speed);

                    if (!discovered.contains(neighbour) && !data.isTraversal_canceled()) {
                        discovered.add(neighbour); // Add the neighbour to the queue if not already discovered
                        queue.add(neighbour);

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data.getGraph(), current_node, neighbour, Color.YELLOW);
                            if(neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.YELLOW);
                            }
                        });

                        if (neighbour.equals(end)) {
                            Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), current_node, neighbour, Color.RED));
                            found[0] = true; // Update to true if neighbour is the desired end node, and immediately break from loop
                            data.setTraversal_in_progress(false);                             // To prevent unnecessary computation
                            break;
                        }
                    }
                }

                if (!visited.contains(current_node) && !data.isTraversal_canceled()) { // If end node not found in neighbours of current node
                    visited.add(current_node); // Add to visited list
                    if (current_node != start && current_node != end) {
                        Gdx.app.postRunnable(() -> current_node.setColour(Color.PURPLE)); // Highlight node purple to show no more operations
                    }                                                                        // will be performed with it
                    for (Node node : current_node.getNeighbours()) {
                        if (visited.contains(node)) {
                            Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), current_node, node, Color.PURPLE));
                        }
                    }
                }
            }

        }).start(); // Ensure the thread is switched to, instead of main render thread
    }

    public static void dfs(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        long operation_speed = (long) (operation_speed_base / data.getTraversal_speed());

        Node start = data.getGraph().get_node_id(data.getStart_node()); // Cache start and end node to update colour at the end
        Node end = data.getGraph().get_node_id(data.getEnd_node());     // in the case they are overwritten

        ArrayList<Node> stack = new ArrayList<>();
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>(); // Specifically for traversal colour updating, not needed for algorithm
        final boolean[] found = {false};

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight the chosen start and end node
                end.setColour(Color.RED);
            });

            stack.add(start);
            discovered.add(start);

            if (data.isTraversal_canceled()) {
                return;
            }

            while (!stack.isEmpty() && !found[0] && !data.isTraversal_canceled()) {
                Node current_node = stack.remove(stack.size() - 1);
                ArrayList<Node> neighbours = new ArrayList<>(current_node.getNeighbours());

                Gdx.app.postRunnable(() -> {
                    if (current_node != start && current_node != end) {
                        current_node.setColour(Color.CYAN); // Highlight the current node if it is not start / end
                    }
                });

                for (Node neighbour : neighbours) {
                    if (data.isTraversal_canceled()) {
                        return; // Stop traversal if the user has reset it
                    }

                    sleep(operation_speed);

                    if (!discovered.contains(neighbour) && !data.isTraversal_canceled()) {
                        stack.add(neighbour); // Add neighbour to stack if not already discovered
                        discovered.add(neighbour);

                        Gdx.app.postRunnable(() -> {
                            highlight_edge(data.getGraph(), current_node, neighbour, Color.YELLOW);
                            if(neighbour != start && neighbour != end) {
                                neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                            }
                        });

                        if (neighbour.equals(end)){                                     // Highlight edge between current node and end
                            Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), current_node, neighbour, Color.RED)); // To show the path
                            found[0] = true;
                            break;
                        }
                    }
                }

                if (!visited.contains(current_node) && !data.isTraversal_canceled()) {
                    visited.add(current_node); // Help track whether all the neighbours to a node have been visited
                }

                for (Node node : visited) {
                    if (node != start && node != end && !data.isTraversal_canceled()) {
                        boolean all_neighbours_visited = true;
                        for (Node neighbour : node.getNeighbours()) {
                            if (!visited.contains(neighbour)) {
                                all_neighbours_visited = false;
                                break;
                            }
                        }

                        if (all_neighbours_visited) { // Highlight edge and node purple if all neighbours visited
                            Gdx.app.postRunnable(() -> node.setColour(Color.PURPLE));
                            if (visited.contains(node) && discovered.contains(node)) {
                                for (Node neighbour : node.getNeighbours()) {
                                    Gdx.app.postRunnable(() -> highlight_edge(data.getGraph(), node, neighbour, Color.PURPLE));
                                }
                            }
                        } else {
                            if (node == current_node) { // Else, highlight the node orange if it has been visited but its neighbours haven't
                                Gdx.app.postRunnable(() -> node.setColour(Color.ORANGE));
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public static void bidirectional(Runtime_Data data) {

    }

    public static void dijkstra(Runtime_Data data) {
        int num_nodes = data.getGraph().get_nodes().size();
        int[] costs = new int[num_nodes];
        Arrays.fill(costs, (int) Double.POSITIVE_INFINITY);
        costs[data.getStart_node()] = 0;

        Priority_Queue costs_pq = new Priority_Queue();
        Priority_Queue nodes_pq = new Priority_Queue();
        costs_pq.add(0);
        nodes_pq.add(data.getStart_node());

        Boolean[] visited = new Boolean[num_nodes];
        Arrays.fill(visited, false);

        Node[] previous_nodes = new Node[num_nodes];

        long operation_speed = (long) (operation_speed_base / data.getTraversal_speed());

        Node start = data.getGraph().get_node_id(data.getStart_node());
        Node end = data.getGraph().get_node_id(data.getEnd_node());

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN);
                end.setColour(Color.RED);
            });

            if (data.isTraversal_canceled()) {
                return;
            }

            while (!nodes_pq.isEmpty() && !costs_pq.isEmpty() && !data.isTraversal_canceled()) {
                System.out.println("loop start");
                int current_cost = costs_pq.poll();
                Node current_node = data.getGraph().get_node_id(nodes_pq.poll());

                if (current_node == null){
                    System.out.println("Current node is null \nExiting traversal");
                    return;
                }

                Gdx.app.postRunnable(() -> current_node.setColour(Color.CYAN));

                if (!visited[current_node.getId()]) {
                    System.out.println("not visited");
                    visited[current_node.getId()] = true;

                    for (Node neighbour : current_node.getNeighbours()) {
                        System.out.println("neighbour: " + neighbour.getId());
                        if (data.isTraversal_canceled()) {
                            return;
                        }

                        sleep(operation_speed);
                        Edge edge_between = null;

                        for (Edge edge : data.getGraph().get_edges(current_node)) {
                            if (edge.getTarget() == neighbour) {
                                edge_between = edge;
                            }
                        }

                        if (edge_between == null) {
                            System.out.println("No edge exists between " + neighbour.getId() + " and " + current_node.getId());
                            continue;
                        }

                        int edge_cost = edge_between.getWeight();

                        if (edge_cost > 0){
                            int new_cost = current_cost + edge_cost;

                            if (new_cost < costs[neighbour.getId()]) {
                                costs[neighbour.getId()] = new_cost;
                                previous_nodes[neighbour.getId()] = current_node;
                                costs_pq.add(new_cost);
                                nodes_pq.add(neighbour.getId());

                                Gdx.app.postRunnable(() -> current_node.setColour(Color.ORANGE));
                            }
                        }
                    }
                }
            }
        }).start();
    }

    public static void A_star(Runtime_Data data) {

    }

    public static void Bellman_Ford(Runtime_Data data) {

    }

    public static void highlight_edge(Graph graph, Node node, Node neighbour, Color colour) {
        List<Edge> edge = graph.get_edges(node);
        for (Edge e: edge) {
            if (e.getTarget().equals(neighbour)) {
                e.setColour(colour);
                break;
            }
        }
        edge = graph.get_edges(neighbour);
        for (Edge e: edge) {
            if (e.getTarget().equals(node)) {
                e.setColour(colour);
                break;
            }
        }
    }

    private static void sleep(long operation_speed){
        try {
            Thread.sleep(operation_speed); // Wait the set time between each step so the user can follow what is happening
        } catch (InterruptedException e) {
            System.out.println("Sleep time of " +  operation_speed + "ms failed or was interrupted");
        }
    }
}
