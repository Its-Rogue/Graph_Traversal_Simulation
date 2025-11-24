package helper_classes;

import fundamental_classes.*;

public class Testing_Functions {
    public static void create(Runtime_Data data) {
        int spacing_x = 110;
        int spacing_y = 110;
        int starting_x = 300;
        int starting_y = 300;

        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
                data.getGraph().add_node(data.getNode_radius(), starting_x + (spacing_x * j), starting_y + (spacing_y * i), data);
            }
        }

        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++){
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
    }
}