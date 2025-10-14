package io.github.some_example_name;

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

        if (!heap.isEmpty()) { // fix
            heap.set(0, last);
            bubble_down(0);
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

    private void bubble_up(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index) >= heap.get(parent)) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void bubble_down(int index) {
        int size = heap.size();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && heap.get(left) < heap.get(smallest)) {
                smallest = left;
            }
            if (right < size && heap.get(right) < heap.get(smallest)) {
                smallest = right;
            }
            if (smallest == index) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        int temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
