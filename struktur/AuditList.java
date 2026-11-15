package struktur;

// Menggunakan Singly Linked List sederhana sebagai struktur sekunder
public class AuditList<T> {
    private Node<T> head;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    public void addLog(T log) {
        Node<T> newNode = new Node<>(log);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) current = current.next;
            current.next = newNode;
        }
        size++;
    }
    
    // Getter untuk view
    public T get(int index) {
        Node<T> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        return current.data;
    }
    
    public int size() { return size; }
}