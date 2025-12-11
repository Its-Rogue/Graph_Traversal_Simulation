package structural_classes;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Priority_Queue {
    private final ArrayList<Integer> heap;

    public Priority_Queue() {
        heap = new ArrayList<>();
    }

    public void add(int value) {
        heap.add(value);
        bubble_up(heap.size() - 1);
    }

    public int poll() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        int min = heap.get(0);
        int last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            bubble_down();
        }

        return min;
    }

    public int peek() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }

    public boolean remove(int value) {
        int index = find_index(value);
        if (index == -1) {
            return false;
        }

        if (index == heap.size() - 1) {
            heap.remove(index);
            return true;
        }

        int last_element = heap.remove(heap.size() - 1);
        heap.set(index, last_element);

        bubble_up(index);
        bubble_down_from(index);

        return true;
    }

    private int find_index(int value) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i) == value) {
                return i;
            }
        }
        return -1;
    }

    private void bubble_down_from(int index) {
        int current = index;
        int size = heap.size();

        while (true) {
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int smallest = current;

            if (left < size && heap.get(left) < heap.get(smallest)) {
                smallest = left;
            }
            if (right < size && heap.get(right) < heap.get(smallest)) {
                smallest = right;
            }
            if (smallest == current) {
                break;
            }
            swap(current, smallest);
            current = smallest;
        }
    }

    private void bubble_up(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index) >= heap.get(parent)) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void bubble_down() {
        bubble_down_from(0);
    }

    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}