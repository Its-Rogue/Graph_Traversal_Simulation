package helper_classes;

import fundamental_classes.Runtime_Data;
import structural_classes.Edge;
import structural_classes.Graph;
import structural_classes.Node;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Save_Handling {
    private static final String save_path = System.getProperty("user.home") + "\\Documents\\Graph Traversal Simulator\\Saved Layouts\\";
    private static final File folder_temp = new File(save_path);

    public static void save_current_layout(Runtime_Data data){
        try {
            if (!folder_temp.exists()) {
                folder_temp.mkdirs();
            }

            DateTimeFormatter time_format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            String time  = LocalDateTime.now().format(time_format);
            File save_file = new File(folder_temp, "Saved Layout " + time + ".txt");

            StringBuilder sb = new StringBuilder();
            Graph graph = data.getGraph();

            sb.append("Nodes\n");
            sb.append("id, x, y\n");

            for (Node node: graph.get_nodes()) {
                sb.append(node.getId()).append(",").append(node.getPosition().getX()).append(",").append(node.getPosition().getY()).append("\n");
            }

            sb.append("\nEdges\n");
            sb.append("source, target, weight\n");

            for (Node node: graph.get_nodes()) {
                for (Edge edge: graph.get_edges(node)) {
                    if (edge.getDirection().equals("forward")) {
                        sb.append(edge.getSource().getId()).append(",").append(edge.getTarget().getId()).append(",").append(edge.getWeight()).append("\n");
                    }
                }
            }

            Files.writeString(save_file.toPath(), sb.toString());

        } catch (Exception e){
            System.err.println("Failed to save layout file: " + e.getMessage());
        }
    }

    public void load_saved_layout(Runtime_Data data){
        if (!folder_temp.exists()) {
            folder_temp.mkdirs();
        }

        File selected_file = choose_saved_layout_file(folder_temp);

        if (selected_file == null) {
            return;
        }

        try {
            Graph.clear();

        } catch (Exception e) {
            System.err.println("Failed to load saved layout file: " + e.getMessage());
        }
    }

    private File choose_saved_layout_file(File folder){
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(folder);
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files", ".txt"));
        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }
}
