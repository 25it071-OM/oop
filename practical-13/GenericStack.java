
class Stack<T> {
    private Object[] data;
    private int top = -1;

    Stack(int size) {
        data = new Object[size];
    }

    public void push(T value) {
        if (top == data.length - 1) {
            System.out.println("Stack Overflow");
            return;
        }
        data[++top] = value;
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        if (top == -1) {
            System.out.println("Stack is Empty");
            return null;
        }

        T value = (T) data[top];
        data[top--] = null;
        return value;
    }

    public boolean isEmpty() {
        return top == -1;
    }
}

public class GenericStack {
    public static void main(String[] args) {
        Stack<Integer> s1 = new Stack<>(5);
        s1.push(10);
        s1.push(20);
        System.out.println("Integer Stack: " + s1.pop());

        Stack<String> s2 = new Stack<>(5);
        s2.push("Java");
        s2.push("Hello");
        System.out.println("String Stack: " + s2.pop());

        System.out.println("Empty Stack: " + s2.pop());
    }
}
