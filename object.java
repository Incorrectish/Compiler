import java.util.Arrays;

public class object<T, E, V, K> {
    private static int count;
    private T first;

    public T getFirst() {
        return first;
    }

    @Override
    public String toString() {
        return "object{" +
                "first=" + first +
                ", second=" + second +
                ", data=" + data +
                ", firstMatrix=" + Arrays.toString(firstMatrix) +
                ", friend=" + friend +
                '}';
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public E getSecond() {
        return second;
    }

    public void setSecond(E second) {
        this.second = second;
    }

    public V getData() {
        return data;
    }

    public void setData(V data) {
        this.data = data;
    }

    public K[][] getFirstMatrix() {
        return firstMatrix;
    }

    public void setFirstMatrix(K[][] firstMatrix) {
        this.firstMatrix = firstMatrix;
    }

    public object<T, E, V, K> getFriend() {
        return friend;
    }

    public void setFriend(object<T, E, V, K> friend) {
        this.friend = friend;
    }

    private E second;
    private V data;
    private K[][] firstMatrix;
    private object<T, E, V, K> friend;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        object.count = count;
    }
}
