import java.util.*;

class OutOfStockException extends Exception {

    private int shortfall;

    public OutOfStockException(String message, int shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception {

    public InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {

    private Map<String, Integer> stock = new HashMap<>();

    public Warehouse() {
        stock.put("Pen", 10);
        stock.put("Book", 5);
        stock.put("Laptop", 2);
    }

    public void issue(String item, int qty)
            throws OutOfStockException,
                   InvalidQuantityException {

        if (qty <= 0) {
            throw new InvalidQuantityException(
                "Quantity must be greater than 0"
            );
        }

        int available = stock.getOrDefault(item, 0);

        if (qty > available) {

            int shortfall = qty - available;

            throw new OutOfStockException(
                "Not enough stock for " + item,
                shortfall
            );
        }

        stock.put(item, available - qty);

        System.out.println(
            "Issued " + qty + " " + item
        );
    }
}

class Request {

    String item;
    int qty;

    Request(String item, int qty) {
        this.item = item;
        this.qty = qty;
    }
}

public class StockIssueDemo {

    public static void main(String[] args) {

        Warehouse warehouse = new Warehouse();

        List<Request> requests = Arrays.asList(
            new Request("Pen", 3),
            new Request("Book", 10),
            new Request("Laptop", 1),
            new Request("Pen", 0),
            new Request("Mobile", 2)
        );

        for (Request request : requests) {

            try {

                warehouse.issue(
                    request.item,
                    request.qty
                );

            }
            catch (OutOfStockException e) {

                System.out.println(
                    "Out of stock: " +
                    e.getMessage()
                );

                System.out.println(
                    "Shortfall = " +
                    e.getShortfall()
                );
            }

            catch (InvalidQuantityException e) {

                System.out.println(
                    "Invalid quantity: " +
                    e.getMessage()
                );
            }
        }

        System.out.println("All requests processed.");
    }
}