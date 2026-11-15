package struktur;

public class DynamicArray<T> implements StrukturData<T> {
    private Object[] data;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    public DynamicArray() {
        data = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public void add(T value) {
        if (size == data.length) resize();
        data[size++] = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int position) {
        if (position < 0 || position >= size) throw new IndexOutOfBoundsException();
        return (T) data[position];
    }

    @Override
    public T remove(int position) {
        if (position < 0 || position >= size) throw new IndexOutOfBoundsException();
        T removed = get(position);
        for (int i = position; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        return removed;
    }

    @Override public int size() { return size; }
    @Override public boolean isEmpty() { return size == 0; }
    @Override public void clear() { data = new Object[INITIAL_CAPACITY]; size = 0; }
    
    @Override
    public boolean contains(T value) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(value)) return true;
        }
        return false;
    }

    private void resize() {
        Object[] newData = new Object[data.length * 2];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    @Override
    public boolean validateInvariant() {
        return size <= data.length;
    }
}