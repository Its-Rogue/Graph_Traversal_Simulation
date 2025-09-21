package io.github.some_example_name;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Priority_Queue {
    private ArrayList<Integer> heap;

    public Priority_Queue(){
        heap = new ArrayList<>();
    }

    // Add an item to the back of the list
    public void add(int value) {
        heap.add(value);
        bubble_up(heap.size() - 1);
    }

    // Get first available item in list
    public int poll() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        int min = heap.get(0);
        int last = heap.remove(heap.size() - 1);
        if (heap.isEmpty()){
            heap.set(0, last);
            bubble_down(0);
        }
        return min;
    }

    // Get the first item in the heap
    public int peek() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return heap.get(0);
    }

    // Check if the list is empty, return true / false
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Clear the list when resetting the graph
    public void clear() {
        heap.clear();
    }

    public void bubble_up(int index){
        int parent = (index - 1) / 2;
        if (heap.get(index) >= heap.get(parent)) {
            return;
        }
        swap(index, parent);
        index = parent;
    }

    public void bubble_down(int index){
        int size = heap.size();
        if (index < 0 || index >= size) {
            return;
        }
        while (true){
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

    public void swap(int index_1, int index_2){
        int temp = heap.get(index_1);
        heap.set(index_1, heap.get(index_2));
        heap.set(index_2, temp);
    }
}
