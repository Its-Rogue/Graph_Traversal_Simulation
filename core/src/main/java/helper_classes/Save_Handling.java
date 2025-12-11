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
import java.util.ArrayList;
import java.util.Scanner;

public class Save_Handling {
    private static final String save_path = System.getProperty("user.home") + "\\Documents\\Graph Traversal Simulator\\Saved Layouts\\";
    private static final File folder_file = new File(save_path);

    public static void save_current_layout(Runtime_Data data){
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
                sb.append(node.getId()).append(",").append(node.getPosition().getX()).append(",").append(node.getPosition().getY()).append("\n"); // Append the ID, X pos, Y pos and a newline per node
            }

            sb.append("\nEdges\n"); // Add an edges header to separate the file, indicating when the split needs to occur between creating nodes and edges

            for (Node node: graph.get_nodes()) {
                for (Edge edge: graph.get_edges(node)) {
                    if (edge.getDirection().equals("forward")) { // Only get one edge to save file space, the reverse edge can then be created from the source and target being inverted
                        sb.append(edge.getSource().getId()).append(",").append(edge.getTarget().getId()).append(",").append(edge.getWeight()).append("\n"); // Append the source ID, target ID, weight and a newline per edge
                    }
                }
            }

            Files.writeString(save_file.toPath(), sb.toString()); // Write the string to the save file

        } catch (Exception e){
            System.err.println("Failed to save layout file: " + e.getMessage()); // Catch any errors that may occur
        }
    }

    public static void load_saved_layout(Runtime_Data data){
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

        try (Scanner sc = new Scanner(selected_file)){
            Graph.clear(); // Clear the graph to allow for a new one to be loaded
            Graph temp_graph = new Graph(); // Create a temp graph that will then update the graph in Runtime_Data
            String header_line;

            if (sc.hasNextLine()){
                header_line = sc.nextLine();
                if (!header_line.equals("Nodes")){
                    data.getError_popup_label().setText("Invalid text file");
                    data.getError_popup().setVisible(true);
                    return;
                }
            }

            while (sc.hasNextLine()){
                String line = sc.nextLine();
                if (!line.isEmpty()){
                    String[] temp_string = line.split(",");
                    int[] elements = new int[temp_string.length];
                    for (int i = 0; i < temp_string.length; i++) {
                        elements[i] = Integer.parseInt(temp_string[i]);
                    }
                    Node read_node = new Node(30, elements[0], new vec2(elements[1], elements[2]), new ArrayList<>(), Color.WHITE);
                }
            }
            //
            // TODO: add neighbours to save file, read them from save file, use a character other than "" for gap, maybe a ~~~
            //
        } catch (Exception e) {
            System.err.println("Failed to load saved layout file: " + e.getMessage()); // Catch any exceptions that may occur
        }
    }

    private static File choose_saved_layout_file(){
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
