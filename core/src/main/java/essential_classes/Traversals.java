package essential_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import structural_classes.Edge;
import structural_classes.Node;
import structural_classes.Priority_Queue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Traversals{
    private final static long operation_speed_base = 200;

    public static void dfs(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Node start = data.getGraph().get_node_from_id(data.getStart_node()); // Cache start and end node to update colour at the end
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());     // in the case they are overwritten

        ArrayList<Node> stack = new ArrayList<>();
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>(); // Specifically for traversal colour updating, not needed for algorithm
        final boolean[] found ={false};

        new Thread(() ->{
            Gdx.app.postRunnable(() ->{
                start.setColour(Color.GREEN); // Highlight the chosen start and end node
                end.setColour(Color.RED);
            });

            stack.add(start);
            discovered.add(start);

            if (data.isTraversal_canceled()){
                data.setTraversal_in_progress(false);
                return; // Return if the user cancels the traversal by pressing the reset traversal button
            }

            if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                operation_speed.set((long) (operation_speed_base / data.getTraversal_speed()));
                traversal_speed_cache.set(data.getTraversal_speed());
            }

            while (!stack.isEmpty() && !found[0] && !data.isTraversal_canceled()){
                Node current_node = stack.remove(stack.size() - 1);
                ArrayList<Node> neighbours = new ArrayList<>(current_node.getNeighbours());

                Gdx.app.postRunnable(() ->{
                    if (current_node != start && current_node != end){
                        current_node.setColour(Color.CYAN); // Highlight the current node if it is not start / end
                    }
                });

                for (Node neighbour: neighbours){
                    if (data.isTraversal_canceled()){
                        data.setTraversal_in_progress(false);
                        return; // Stop traversal if the user has pressed the reset traversal button
                    }

                    if (data.Should_sleep()) {
                        sleep(operation_speed.get());
                    }

                    if (!discovered.contains(neighbour) && !data.isTraversal_canceled()){
                        stack.add(neighbour); // Add neighbour to stack if not already discovered
                        discovered.add(neighbour);

                        Gdx.app.postRunnable(() ->{
                            highlight_edge(data, current_node, neighbour, Color.YELLOW);
                            if(neighbour != start && neighbour != end){
                                neighbour.setColour(Color.YELLOW); // Set neighbour to yellow
                            }
                        });

                        if (neighbour.equals(end)){                                               // Highlight edge between current node and end
                            Gdx.app.postRunnable(() -> highlight_edge(data, current_node, neighbour, Color.RED)); // To show the path
                            found[0] = true;
                            break;
                        }
                    }
                }

                if (!visited.contains(current_node) && !data.isTraversal_canceled()){
                    visited.add(current_node); // Help track whether all the neighbours to a node have been visited
                }

                element_highlight(data, visited, discovered, current_node, start, end);

                if (found[0]) {
                    break;
                }
            }
            data.setTraversal_in_progress(false);  // Allow a new traversal to be run after this one has completed
            data.setTraversal_canceled(false);
        }).start();
    }

    public static void bfs(Runtime_Data data){
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Node start = data.getGraph().get_node_from_id(data.getStart_node());   // Cache start and end node to update colour at the end
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());       // in the case they are overwritten

        ArrayList<Node> queue = new ArrayList<>(); // Initialise the various lists
        ArrayList<Node> discovered = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();
        final boolean[] found ={false};

        new Thread(() ->{ // New local render thread to allow for nodes to change colours

            Gdx.app.postRunnable(() ->{
                start.setColour(Color.GREEN); // Highlight the chosen start and end node
                end.setColour(Color.RED);
            });

            queue.add(start); // Add the first node to the discovered and queue
            discovered.add(start);

            while (!queue.isEmpty() && !found[0] && !data.isTraversal_canceled()){ // Loop through until found or no more nodes in graph
                Node current_node = queue.remove(0); // Remove the node at the front of the queue

                if (data.isTraversal_canceled()){
                    data.setTraversal_in_progress(false);
                    return; // Check for cancellation of traversal
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed()));
                    traversal_speed_cache.set(data.getTraversal_speed());
                }

                Gdx.app.postRunnable(() ->{
                    if (current_node != start && current_node != end){
                        current_node.setColour(Color.CYAN); // Highlight the current node if it is not start / end
                    }
                });

                for (Node neighbour: current_node.getNeighbours()){ // Check each neighbour of the current node
                    if (data.isTraversal_canceled()){
                        data.setTraversal_in_progress(false);
                        return; // Check for cancellation of traversal
                    }

                    if (data.Should_sleep()) {
                        sleep(operation_speed.get());
                    }

                    if (!discovered.contains(neighbour) && !data.isTraversal_canceled()){
                        discovered.add(neighbour); // Add the neighbour to the queue if not already discovered
                        queue.add(neighbour);

                        Gdx.app.postRunnable(() ->{
                            highlight_edge(data, current_node, neighbour, Color.YELLOW);
                            if(neighbour != start && neighbour != end){
                                neighbour.setColour(Color.YELLOW);
                            }
                        });

                        if (neighbour.equals(end)){
                            Gdx.app.postRunnable(() -> highlight_edge(data, current_node, neighbour, Color.RED));
                            found[0] = true; // Update to true if neighbour is the desired end node
                            break;
                        }
                    }
                }

                if (!visited.contains(current_node) && !data.isTraversal_canceled()){
                    visited.add(current_node); // Add to visited list
                }

                element_highlight(data, visited, discovered, current_node, start, end);

                if (found[0]) {
                    break;
                }
            }
            data.setTraversal_in_progress(false); // Allow a new traversal to be run after this one has completed
            data.setTraversal_canceled(false);
        }).start(); // Ensure the thread is switched to, instead of main render thread
    }

    public static void bidirectional(Runtime_Data data){
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Node forward_start = data.getGraph().get_node_from_id(data.getStart_node());
        Node reverse_start = data.getGraph().get_node_from_id(data.getEnd_node());

        ArrayList<Node> forward_queue = new ArrayList<>();
        ArrayList<Node> reverse_queue = new ArrayList<>();
        Set<Node> forward_discovered = new HashSet<>();
        Set<Node> reverse_discovered = new HashSet<>();
        ArrayList<Node> forward_visited = new ArrayList<>();
        ArrayList<Node> reverse_visited = new ArrayList<>();
        final boolean[] found = {false};

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                forward_start.setColour(Color.GREEN);
                reverse_start.setColour(Color.RED);
            });

            forward_queue.add(forward_start);
            reverse_queue.add(reverse_start);
            forward_discovered.add(forward_start);
            reverse_discovered.add(reverse_start);

            while (!forward_queue.isEmpty() && !reverse_queue.isEmpty() && !found[0] && !data.isTraversal_canceled()){
                if (data.isTraversal_canceled()){
                    data.setTraversal_in_progress(false);
                    return;
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed()));
                    traversal_speed_cache.set(data.getTraversal_speed());
                }

                if (!forward_queue.isEmpty() && !found[0]) {
                    Node forward_current_node = forward_queue.remove(0);

                    Gdx.app.postRunnable(() -> {
                        if (forward_current_node != forward_start && forward_current_node != reverse_start){
                            forward_current_node.setColour(Color.CYAN);
                        }
                    });

                    for (Node neighbour : forward_current_node.getNeighbours()){
                        if (data.isTraversal_canceled()){
                            data.setTraversal_in_progress(false);
                            return;
                        }

                        if (data.Should_sleep()) {
                            sleep(operation_speed.get());
                        }

                        if (!forward_discovered.contains(neighbour)){
                            forward_discovered.add(neighbour);
                            forward_queue.add(neighbour);

                            Gdx.app.postRunnable(() -> {
                                highlight_edge(data, forward_current_node, neighbour, Color.YELLOW);
                                if(neighbour != forward_start && neighbour != reverse_start){
                                    neighbour.setColour(Color.YELLOW);
                                }
                            });

                            if (reverse_discovered.contains(neighbour)){
                                found[0] = true;
                                break;
                            }
                        }
                    }

                    if (!forward_visited.contains(forward_current_node)){
                        forward_visited.add(forward_current_node);
                    }

                    element_highlight(data, forward_visited, new ArrayList<>(forward_discovered), forward_current_node, forward_start, reverse_start);
                }

                if (found[0]) {
                    break;
                }

                if (!reverse_queue.isEmpty()) {
                    Node reverse_current_node = reverse_queue.remove(0);

                    Gdx.app.postRunnable(() -> {
                        if (reverse_current_node != reverse_start && reverse_current_node != forward_start){
                            reverse_current_node.setColour(Color.CYAN);
                        }
                    });

                    for (Node neighbour : reverse_current_node.getNeighbours()){
                        if (data.isTraversal_canceled()){
                            data.setTraversal_in_progress(false);
                            return;
                        }

                        if (data.Should_sleep()) {
                            sleep(operation_speed.get());
                        }

                        if (!reverse_discovered.contains(neighbour)){
                            reverse_discovered.add(neighbour);
                            reverse_queue.add(neighbour);

                            Gdx.app.postRunnable(() -> {
                                highlight_edge(data, reverse_current_node, neighbour, Color.YELLOW);
                                if(neighbour != reverse_start && neighbour != forward_start){
                                    neighbour.setColour(Color.YELLOW);
                                }
                            });

                            if (forward_discovered.contains(neighbour)){
                                found[0] = true;
                                break;
                            }
                        }
                    }

                    if (!reverse_visited.contains(reverse_current_node)){
                        reverse_visited.add(reverse_current_node);
                    }

                    element_highlight(data, reverse_visited, new ArrayList<>(reverse_discovered), reverse_current_node, reverse_start, forward_start);
                }

                if (found[0]) {
                    break;
                }
            }

            if (!found[0]) {
                Gdx.app.postRunnable(() -> {
                    data.getError_popup_label().setText("No path found");
                    data.getError_popup().setVisible(true);
                });
            }
            data.setTraversal_in_progress(false);
            data.setTraversal_canceled(false);
        }).start();
    }

    public static void dijkstra(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Map<Node, Integer> distances = new HashMap<>(); // Maps for distances between nodes, and what node was last accessed before itself
        Map<Node, Node> previous = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        Priority_Queue pq = new Priority_Queue();

        ArrayList<Node> discovered = new ArrayList<>(); // Helper for visualisation

        Node start = data.getGraph().get_node_from_id(data.getStart_node());
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());

        for (Node n: data.getGraph().get_nodes()) {
            distances.put(n, Integer.MAX_VALUE); // Add all nodes to the maps
            previous.put(n, null);
        }

        distances.put(start, 0); // Update the map for the start node
        pq.add(start.getId(), 0);

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN); // Highlight the start and end node green and red respectively each frame to ensure they are
                end.setColour(Color.RED);     // Correctly highlighted throughout
            });

            while (!pq.isEmpty()) {
                if (data.isTraversal_canceled()) {
                    data.setTraversal_in_progress(false);
                    return; // Check for cancellation of traversal
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed())); // Update operation speed if it has been altered by the user using the slider
                    traversal_speed_cache.set(data.getTraversal_speed());
                }


                Node current = data.getGraph().get_node_from_id(pq.poll());

                if (visited.contains(current)) {
                    continue; // Skip if the node has been targeted before
                }

                if (current != start && current != end) {
                    Gdx.app.postRunnable(() -> current.setColour(Color.CYAN)); // Set the current node to be cyan
                }

                visited.add(current);

                if (data.Should_sleep()) {
                    sleep(operation_speed.get()); // Sleep the determinate amount of time
                }

                for (Edge e: data.getGraph().get_edges(current)) {
                    Node neighbour = e.getTarget(); // Get neighbour from edges of current node
                    if (visited.contains(neighbour)) {
                        continue;
                    }
                    if (!discovered.contains(neighbour)) {
                        discovered.add(neighbour);
                    }
                    if (!start.equals(neighbour) && !end.equals(neighbour)) {
                        Gdx.app.postRunnable(() -> neighbour.setColour(Color.YELLOW)); // Set colour to yellow if not visited already
                    }
                    int alt = distances.get(current) + e.getWeight(); // Calculate new distance cost

                    if (alt < distances.get(neighbour)) { // Check to see if the new cost is cheaper than the old cost
                        distances.put(neighbour, alt);
                        previous.put(neighbour, current);
                        pq.add(neighbour.getId(), alt); // Update accordingly if true
                    }

                    if (data.Should_sleep()) {
                        sleep(operation_speed.get());
                    }

                    if (data.isTraversal_canceled()) {
                        data.setTraversal_in_progress(false);
                        return; // Check for cancellation of traversal
                    }
                }

                ArrayList<Node> visited_list = new ArrayList<>(visited); // Convert set to a list for re-colouring of nodes in graph
                element_highlight(data, visited_list, discovered, current, start, end); // Reusing the previously used logic

                if (current.equals(end)) {
                    break;
                }

                if (data.isTraversal_canceled()) {
                    data.setTraversal_in_progress(false);
                    return; // Check for cancellation of traversal
                }

                Gdx.app.postRunnable(() -> current.setColour(Color.ORANGE)); // Set the current node to orange to mark it as visited at the end of the loop
            }

            List<Node> path = reconstruct_path(previous, start, end); // Reconstruct the determined path between the 2 nodes
            Node last_node = null;
            for(Node n: path) {
                if (n.equals(start)) {
                    Gdx.app.postRunnable(() -> n.setColour(Color.GREEN)); // Keep start / end node green / red, and colour sky if not
                } else if (n.equals(end)) {
                    Gdx.app.postRunnable(() -> n.setColour(Color.RED));
                } else {
                    Gdx.app.postRunnable(() -> n.setColour(Color.SKY));
                }

                if (last_node != null) {
                    Node final_last_node = last_node;
                    Gdx.app.postRunnable(() -> highlight_edge(data, n, final_last_node, Color.SKY)); // Highlight edges between nodes of the path
                }

                last_node = n; // Update last node to update pointer for the next edge to be highlighted
            }

            data.setTraversal_in_progress(false); // Allow a new traversal to be run after this one has completed
            data.setTraversal_canceled(false);
        }).start();
    }

    public static void A_star(Runtime_Data data) {
        data.setTraversal_in_progress(true);
        AtomicLong operation_speed = new AtomicLong((long) (operation_speed_base / data.getTraversal_speed()));
        AtomicReference<Float> traversal_speed_cache = new AtomicReference<>(data.getTraversal_speed());

        Map<Node, Integer> g_score = new HashMap<>();
        Map<Node, Double> f_score = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        Priority_Queue pq = new Priority_Queue();

        ArrayList<Node> visited = new ArrayList<>();
        ArrayList<Node> discovered = new ArrayList<>();
        Set<Node> closed_set = new HashSet<>();

        Node start = data.getGraph().get_node_from_id(data.getStart_node());
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());

        for (Node n: data.getGraph().get_nodes()) {
            g_score.put(n, Integer.MAX_VALUE);
            f_score.put(n, Double.MAX_VALUE);
            previous.put(n, null);
        }

        g_score.put(start, 0);
        f_score.put(start, euclidean_heuristic(start.getPosition().getX(), start.getPosition().getY(), end.getPosition().getX(), end.getPosition().getY()));
        pq.add(start.getId(), (int) f_score.get(start).doubleValue());

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN);
                end.setColour(Color.RED);
            });

            boolean path_found = false;

            while (!pq.isEmpty()) {
                if (data.isTraversal_canceled()) {
                    data.setTraversal_in_progress(false);
                    return;
                }

                if (data.getTraversal_speed() != traversal_speed_cache.get()) {
                    operation_speed.set((long) (operation_speed_base / data.getTraversal_speed()));
                    traversal_speed_cache.set(data.getTraversal_speed());
                }

                Node current = data.getGraph().get_node_from_id(pq.poll());

                if (closed_set.contains(current)) {
                    continue;
                }

                if (current != start && current != end) {
                    Gdx.app.postRunnable(() -> current.setColour(Color.CYAN));
                }

                if (!visited.contains(current)) {
                    visited.add(current);
                }

                if (data.Should_sleep()) {
                    sleep(operation_speed.get());
                }

                if (current.equals(end)) {
                    path_found = true;
                    break;
                }

                boolean has_valid_neighbors = false;

                for (Edge e: data.getGraph().get_edges(current)) {
                    Node neighbour = e.getTarget();

                    if (data.isTraversal_canceled()) {
                        data.setTraversal_in_progress(false);
                        return;
                    }

                    if (closed_set.contains(neighbour)) {
                        continue;
                    }

                    if (!visited.contains(neighbour) && !discovered.contains(neighbour) && !start.equals(neighbour) && !end.equals(neighbour)) {
                        Gdx.app.postRunnable(() -> neighbour.setColour(Color.YELLOW));
                    }

                    int intermediate_g_score = g_score.get(current) + e.getWeight();

                    if (!discovered.contains(neighbour)) {
                        discovered.add(neighbour);
                    }

                    if (intermediate_g_score < g_score.get(neighbour)) {
                        has_valid_neighbors = true;
                        previous.put(neighbour, current);
                        g_score.put(neighbour, intermediate_g_score);
                        double intermediate_f_score = intermediate_g_score + euclidean_heuristic(neighbour.getPosition().getX(), neighbour.getPosition().getY(), end.getPosition().getX(), end.getPosition().getY());
                        f_score.put(neighbour, intermediate_f_score);
                        pq.add(neighbour.getId(), (int) intermediate_f_score);
                    }
                }

                closed_set.add(current);

                if (!has_valid_neighbors && !current.equals(start)) {
                    Gdx.app.postRunnable(() -> current.setColour(Color.GRAY));
                } else if (!current.equals(start) && !current.equals(end)) {
                    Gdx.app.postRunnable(() -> current.setColour(Color.ORANGE));
                }

                element_highlight(data, visited, discovered, current, start, end);

                if (data.isTraversal_canceled()) {
                    data.setTraversal_in_progress(false);
                    return;
                }
            }

            if (path_found) {
                List<Node> path = reconstruct_path(previous, start, end);
                Node last_node = null;
                for(Node n: path) {
                    if (n.equals(start)) {
                        Gdx.app.postRunnable(() -> n.setColour(Color.GREEN));
                    } else if (n.equals(end)) {
                        Gdx.app.postRunnable(() -> n.setColour(Color.RED));
                    } else {
                        Gdx.app.postRunnable(() -> n.setColour(Color.SKY));
                    }

                    if (last_node != null) {
                        Node final_last_node = last_node;
                        Gdx.app.postRunnable(() -> highlight_edge(data, n, final_last_node, Color.SKY));
                    }

                    last_node = n;
                }
            } else {
                Gdx.app.postRunnable(() -> {
                    data.getError_popup_label().setText("No path found");
                    data.getError_popup().setVisible(true);
                });
            }

            data.setTraversal_in_progress(false);
            data.setTraversal_canceled(false);
        }).start();
    }

    public static void Bellman_Ford(Runtime_Data data) {
        data.setTraversal_in_progress(true);

        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();

        Node start = data.getGraph().get_node_from_id(data.getStart_node());
        Node end = data.getGraph().get_node_from_id(data.getEnd_node());

        AtomicBoolean negative_cycle_detected = new AtomicBoolean(false);
        List<Node> cycle_nodes = new ArrayList<>();

        for (Node n: data.getGraph().get_nodes()) {
            distances.put(n, Integer.MAX_VALUE);
            previous.put(n, null);
        }

        distances.put(start, 0);
        int num_nodes = data.getGraph().get_nodes().size();

        new Thread(() -> {
            Gdx.app.postRunnable(() -> {
                start.setColour(Color.GREEN);
                end.setColour(Color.RED);
            });

            for (int i = 0; i < num_nodes - 1; i++) {
                if (data.isTraversal_canceled()) {
                    data.setTraversal_in_progress(false);
                    return;
                }

                for (Node n: data.getGraph().get_nodes()) {
                    if (distances.get(n) == Integer.MAX_VALUE) {
                        continue;
                    }

                    for (Edge e: data.getGraph().get_edges(n)) {
                        Node neighbour = e.getTarget();
                        int weight = e.getWeight();
                        int alt = distances.get(n) + weight;

                        if (alt < distances.get(neighbour)) {
                            distances.put(neighbour, alt);
                            previous.put(neighbour, n);
                        }
                    }
                }
            }

            Node cycle_start = null;
            outer:
            for (Node n: data.getGraph().get_nodes()) {
                if (distances.get(n) == Integer.MAX_VALUE) {
                    continue;
                }

                for (Edge e: data.getGraph().get_edges(n)) {
                    Node neighbour = e.getTarget();
                    int weight = e.getWeight();

                    if (distances.get(n) + weight < distances.get(neighbour)) {
                        negative_cycle_detected.set(true);
                        cycle_start = neighbour;
                        break outer;
                    }
                }
            }

            if (negative_cycle_detected.get()) {
                Node current = cycle_start;

                for (int i = 0; i < num_nodes; i++) {
                    current = previous.get(current);
                    if (current == null) break;
                }

                if (current != null) {
                    Node cycle_node = current;
                    do {
                        cycle_nodes.add(current);
                        current = previous.get(current);
                    } while (current != null && !current.equals(cycle_node) && cycle_nodes.size() < num_nodes);
                }

                Gdx.app.postRunnable(() -> {
                    for (int i = 0; i < cycle_nodes.size(); i++) {
                        Node node = cycle_nodes.get(i);
                        if (!node.equals(start) && !node.equals(end)) {
                            node.setColour(Color.MAGENTA);
                        }

                        if (i > 0) {
                            Node prev = cycle_nodes.get(i - 1);
                            highlight_edge(data, prev, node, Color.MAGENTA);
                        }
                    }

                    if (cycle_nodes.size() > 1) {
                        highlight_edge(data, cycle_nodes.get(cycle_nodes.size() - 1),
                            cycle_nodes.get(0), Color.MAGENTA);
                    }

                    data.getError_popup_label().setText("Negative cycle detected!");
                    data.getError_popup().setVisible(true);
                });

            } else {
                List<Node> path = reconstruct_path(previous, start, end);

                if (path.isEmpty() || path.size() == 1) {
                    Gdx.app.postRunnable(() -> {
                        data.getError_popup_label().setText("No path found");
                        data.getError_popup().setVisible(true);
                    });
                } else {
                    Node last_node = null;
                    for(Node n: path) {
                        if (n.equals(start)) {
                            Gdx.app.postRunnable(() -> n.setColour(Color.GREEN));
                        } else if (n.equals(end)) {
                            Gdx.app.postRunnable(() -> n.setColour(Color.RED));
                        } else {
                            Gdx.app.postRunnable(() -> n.setColour(Color.SKY));
                        }

                        if (last_node != null) {
                            Node final_last_node = last_node;
                            Gdx.app.postRunnable(() -> highlight_edge(data, n, final_last_node, Color.SKY));
                        }

                        last_node = n;
                    }
                }
            }

            data.setTraversal_in_progress(false);
            data.setTraversal_canceled(false);
        }).start();
    }

    private static void element_highlight(Runtime_Data data, ArrayList<Node> visited, ArrayList<Node> discovered, Node current_node, Node start, Node end){
        for (Node node: visited){
            if (node != start && node != end && !data.isTraversal_canceled()){
                boolean all_neighbours_visited = true;
                for (Node neighbour: node.getNeighbours()){
                    if (!visited.contains(neighbour)){
                        all_neighbours_visited = false; // Check to see if each neighbour of every visited node has also been visited
                        break;
                    }
                }

                if (all_neighbours_visited){ // Highlight edge and reverse node purple if all neighbours visited
                    Gdx.app.postRunnable(() -> node.setColour(Color.PURPLE));
                    if (visited.contains(node) && discovered.contains(node)){
                        for (Node neighbour: node.getNeighbours()){
                            Gdx.app.postRunnable(() -> highlight_edge(data, node, neighbour, Color.PURPLE));
                        }
                    }
                } else{
                    if (node == current_node){ // Else, highlight the reverse node orange if it has been visited
                        Gdx.app.postRunnable(() -> node.setColour(Color.ORANGE));           // but its neighbours haven't
                    }
                }
            }
        }
    }

    // Euclidean distance calculation for the A* heuristic
    private static double euclidean_heuristic(int source_x, int source_y, int target_x, int target_y){
        int dx = target_x - source_x;
        int dy = target_y - source_y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static List<Node> reconstruct_path(Map<Node, Node> previous, Node start, Node end) {
        List<Node> path = new ArrayList<>();
        Node current = end;

        while (current != null) {
            path.add(0, current); // Add the current node to the front of the list
            current = previous.get(current); // Set current to the node that discovered the original current node
        }

        if (path.isEmpty() || !path.get(0).equals(start)) {
            return new ArrayList<>(); // Return a blank path if an invalid or no path is found
        }

        return path;
    }

    private static void highlight_edge(Runtime_Data data, Node node, Node neighbour, Color colour){
        List<Edge> edge = data.getGraph().get_edges(node);
        for (Edge e: edge){ // Get forward edge and set its colour to the inputted colour
            if (e.getTarget().equals(neighbour)){
                e.setColour(colour);
                break;
            }
        }
        edge = data.getGraph().get_edges(neighbour);
        for (Edge e: edge){ // Get reverse edge and sets its colour to the inputted colour
            if (e.getTarget().equals(node)){
                e.setColour(colour);
                break;
            }
        }
    }

    private static void sleep(long operation_speed){
        try{
            Thread.sleep(operation_speed); // Wait the set time between each step so the user can follow what is happening
        } catch (InterruptedException e){
            System.out.println("Sleep time of " +  operation_speed + "ms failed or was interrupted");
        }
    }
}
