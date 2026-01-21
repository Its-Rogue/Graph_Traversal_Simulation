package structural_classes;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Priority_Queue {
    private static class Entry {
        int value;
        int priority;

        Entry(int value, int priority) {
            this.value = value;
            this.priority = priority;
        }
    }

    private final ArrayList<Entry> heap;

    public Priority_Queue() {
        heap = new ArrayList<>();
    }

    // Add a value to the pq when no specific priority has been given
    public void add(int value) {
        add(value, value);
    }

    // Add a value to the pq and bubble up to maintain heap priority
    public void add(int value, int priority) {
        heap.add(new Entry(value, priority));
        bubble_up(heap.size() - 1);
    }

    // Remove and return minimum value from pq
    public int poll() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        int min = heap.get(0).value;
        Entry last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            bubble_down();
        }

        return min;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }

    // Remove specific value from the pq and re-heap
    public boolean remove(int value) {
        int index = find_index(value);
        if (index == -1) {
            return false;
        }

        if (index == heap.size() - 1) {
            heap.remove(index);
            return true;
        }

        Entry last_element = heap.remove(heap.size() - 1);
        heap.set(index, last_element);

        bubble_up(index); // Re-heap / restore heap to proper form
        bubble_down_from(index);

        return true;
    }

    private int find_index(int value) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).value == value) {
                return i;
            }
        }
        return -1;
    }

    // Bubble down to maintain min-heap priority, used for priority queue
    private void bubble_down_from(int index) {
        int current = index;
        int size = heap.size();

        while (true) {
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int smallest = current;

            if (left < size && heap.get(left).priority < heap.get(smallest).priority) {
                smallest = left;
            }
            if (right < size && heap.get(right).priority < heap.get(smallest).priority) {
                smallest = right;
            }
            if (smallest == current) {
                break;
            }
            swap(current, smallest);
            current = smallest;
        }
    }

    // Bubble up to maintain min-heap priority, used for priority queue
    private void bubble_up(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).priority >= heap.get(parent).priority) break;
            swap(index, parent);
            index = parent;
        }
    }

    // Bubble down from root of the heap
    private void bubble_down() {
        bubble_down_from(0);
    }

    // Swap 2 elements in the heap based on their values
    private void swap(int i, int j) {
        Entry temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}