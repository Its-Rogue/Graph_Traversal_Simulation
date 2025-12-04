package structural_classes;

public class Queue {
    private int front_of_stack = 0;
    private int rear_of_stack = 0;
    private int length = 0;
    private Node[] q;

    public Queue(int size){
        q = new Node[size];
    }

    public Node[] getQ() {
        return q;
    }

    public void setQ(Node[] q) {
        this.q = q;
    }

    public int getFront_of_stack() {
        return front_of_stack;
    }

    public void setFront_of_stack(int front_of_stack) {
        this.front_of_stack = front_of_stack;
    }

    public int getRear_of_stack() {
        return rear_of_stack;
    }

    public void enQueue(Node node){
        q[rear_of_stack] = node;
        rear_of_stack++;
        length++;
    }

    public Node deQueue(){
        length--;
        return q[front_of_stack--];
    }

    public boolean isEmpty(){
        return length == 0;
    }

    public boolean isFull(){
        return length == q.length;
    }
}