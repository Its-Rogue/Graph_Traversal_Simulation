package structural_classes;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Map;

public class Priority_Queue {
    private final ArrayList<Integer> heap;
    private final Map<Integer, Integer> priorities;

    public Priority_Queue() {
        heap = new ArrayList<>();
        priorities = new HashMap<>();
    }

    public void add(int value) {
        add(value, value);
    }

    public void add(int value, int priority) {
        heap.add(value);
        priorities.put(value, priority);
        bubble_up(heap.size() - 1);
    }

    public int poll() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        int min = heap.get(0);
        int last = heap.remove(heap.size() - 1);

        priorities.remove(min);

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
        priorities.clear();
    }

    public boolean remove(int value) {
        int index = find_index(value);
        if (index == -1) {
            return false;
        }

        if (index == heap.size() - 1) {
            heap.remove(index);
            priorities.remove(value);
            return true;
        }

        int last_element = heap.remove(heap.size() - 1);
        heap.set(index, last_element);
        priorities.remove(value);

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

            if (left < size && priorities.get(heap.get(left)) < priorities.get(heap.get(smallest))) {
                smallest = left;
            }
            if (right < size && priorities.get(heap.get(right)) < priorities.get(heap.get(smallest))) {
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
            if (priorities.get(heap.get(index)) >= priorities.get(heap.get(parent))) break;
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