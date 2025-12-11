package helper_classes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import fundamental_classes.Runtime_Data;
import structural_classes.Edge;
import structural_classes.Graph;
import structural_classes.Node;
import structural_classes.vec2;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Save_Handling {
    private static final String save_path = System.getProperty("user.home") + "\\Documents\\Graph Traversal Simulator\\Saved Layouts\\";
    private static final File folder_file = new File(save_path);

    public static void save_current_layout(Runtime_Data data) {
        try {
            if (!folder_file.exists()) {
                folder_file.mkdirs(); // Create the default file directory if it does not already exist
            }

            DateTimeFormatter time_format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String time  = LocalDateTime.now().format(time_format);
            File save_file = new File(folder_file, "Saved Layout " + time + ".txt"); // Get the date and time, format it to time_format and append it to the name of the save file

            StringBuilder sb = new StringBuilder(); // Template to add the required elements to the file
            Graph graph = data.getGraph(); // Get the current layout from the graph in Runtime_Data

            sb.append("Nodes\n"); // Add a nodes header to separate the file, to make it easier to read from when loading it in the future

            for (Node node: graph.get_nodes()) {
                StringBuilder neighbour_ids = new StringBuilder();
                for (Node neighbour: node.getNeighbours()) { // Get id of each neighbour to the current node and append it to the neighbour_ids string builder
                    neighbour_ids.append(neighbour.getId()).append(",");
                }
                neighbour_ids.deleteCharAt(neighbour_ids.length()-1); // Remove the trailing comma from the neighbour_ids string builder

                sb.append(node.getId()).append(",").append(node.getPosition().getX()).append(",").append(node.getPosition().getY()).append(",").append(neighbour_ids).append("\n"); // Append the ID, X pos, Y pos, neighbour ids and a newline per node
            }

            sb.append("\nEdges\n"); // Add an edges header to separate the file, indicating when the split needs to occur between creating nodes and edges

            for (Node node: graph.get_nodes()) {
                for (Edge edge: graph.get_edges(node)) {
                    if (edge.getDirection().equals("forward")) { // Only get one edge to save file space, the reverse edge can then be created from the source and target being inverted
                        sb.append(edge.getSource().getId()).append(",").append(edge.getTarget().getId()).append(",").append(edge.getWeight()).append("\n"); // Append the source ID, target ID, weight and a newline per edge
                    }
                }
            }
            sb.append("~~~");
            Files.writeString(save_file.toPath(), sb.toString()); // Write the string to the save file

        } catch (Exception e) {
            System.err.println("Failed to save layout file: " + e.getMessage()); // Catch any errors that may occur
        }
    }

    public static void load_saved_layout(Runtime_Data data) {
        if (!folder_file.exists()) {
            folder_file.mkdirs(); // Create the folder if it doesn't already exist
        }

        Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()); // Set the display mode to windowed to allow the popup to show on the screen above the simulator
        File selected_file = choose_saved_layout_file(); // Choose the file
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode()); // Set the display mode to fullscreen to allow for better user experience

        if (selected_file == null || !selected_file.getName().split("\\.")[1].equals("txt")) { // Make sure a file has been chosen, and that the file is of the correct type
            data.getError_popup_label().setText("Invalid file type or none selected");
            data.getError_popup().setVisible(true);
            return;
        }

        try (Scanner sc = new Scanner(selected_file)) {
            Graph.clear(); // Clear the graph to allow for a new one to be loaded
            Graph temp_graph = new Graph(); // Create a temp graph that will then update the graph in Runtime_Data
            String header_line;

            if (sc.hasNextLine()) {
                header_line = sc.nextLine();
                if (!header_line.equals("Nodes")) { // Check to see if the first line is the header Nodes to ensure the file layout is correct
                    data.getError_popup_label().setText("Invalid text file");
                    data.getError_popup().setVisible(true);
                    return;
                }
            }

            List<Node> created_nodes = new ArrayList<>(); // Containers for the nodes and edges read from the chosen file
            Map<Integer, Node> node_map = new HashMap<>();
            Map<Integer, List<Integer>> neighbour_map = new HashMap<>();

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) { // Break if the gap between the nodes and edges is detected
                    break;
                }

                String[] node_data = line.split(","); // Split the line into its components (id, position, neighbours) and assign the former 2 to variables
                int id = Integer.parseInt(node_data[0]);
                vec2 pos = new vec2(Integer.parseInt(node_data[1]), Integer.parseInt(node_data[2]));

                Node node = new Node(data.getNode_radius(), id, pos, new ArrayList<>(), Color.WHITE); // Create a new node and add it to the containers
                created_nodes.add(node);
                node_map.put(id, node);

                List<Integer> neighbour_ids = new ArrayList<>(); // Add the neighbours to a list before adding them to the map second map container
                for (int i = 3; i < node_data.length; i++) {
                    neighbour_ids.add(Integer.parseInt(node_data[i]));
                }
                neighbour_map.put(id, neighbour_ids);
            }

            for (Node node: created_nodes) { // Add the neighbours to each of the nodes read from the file
                List<Integer> neighbour_ids = neighbour_map.get(node.getId());
                for (int neighbour_id: neighbour_ids) {
                    Node neighbour = node_map.get(neighbour_id);
                    if (neighbour != null) {
                        node.add_neighbour(neighbour);
                    }
                    temp_graph.add_node(node); // Add the fully informed node to the temporary graph
                }
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.isEmpty()) { // Skip a line if it is empty, such as the gap between the nodes and edges
                    continue;
                } else if (line.equals("~~~")) { // Break when the end of the file is reached, indicated by the ~~~ line
                    break;
                }

                String[] edge_data = line.split(","); // Split the read line into its components
                if (edge_data.length == 3) {
                    int source_id = Integer.parseInt(edge_data[0]); // Assign the components to variables
                    int target_id = Integer.parseInt(edge_data[1]);
                    int weight = Integer.parseInt(edge_data[2]);

                    Node source = node_map.get(source_id); // Get the actual node structure for the source and target of the read edge
                    Node target = node_map.get(target_id);

                    if (source != null && target != null) {
                        Edge forward_edge = new Edge(source, target, weight, "forward", Color.WHITE); // Create the forward and reverse edges
                        Edge reverse_edge = new Edge(target, source, weight, "reverse", Color.WHITE);

                        temp_graph.add_edge(forward_edge); // Add the edges to the graph's adj list
                        temp_graph.add_edge(reverse_edge);
                    }
                } else {
                    System.err.println("Invalid edge format in text file");
                }
            }

            data.setGraph(temp_graph); // Update the graph in Runtime_Data with the temporary one created at the beginning

        } catch (Exception e) {
            System.err.println("Failed to load saved layout file: " + e.getMessage()); // Catch any exceptions that may occur
        }
    }

    private static File choose_saved_layout_file() {
        JFileChooser chooser = new JFileChooser(); // Create a file chooser entity
        chooser.setCurrentDirectory(folder_file); // Set the default directory to the default save directory
        chooser.setFileFilter(new FileNameExtensionFilter("Txt Files","txt")); // Set the default filter to show text files by default

        int result = chooser.showSaveDialog(null); // Show the chooser popup
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile(); // Return the selected file if one is chosen
        }

        return null; // Return null if no file is chosen
    }
}
