class TicketCounter {

    int seatsLeft = 5;
    int successfulBookings = 0;

    // WITHOUT synchronization
    void book(String name) {

        if (seatsLeft > 0) {

            // Small delay to make race condition visible
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            seatsLeft--;
            successfulBookings++;

            System.out.println(
                name + " booked a seat"
            );
        }
        else {
            System.out.println(
                name + " failed - No seat"
            );
        }
    }
}

class BookingThread extends Thread {

    TicketCounter counter;

    BookingThread(TicketCounter counter) {
        this.counter = counter;
    }

    public void run() {
        counter.book(
            Thread.currentThread().getName()
        );
    }
}

public class SeatBookingRace {

    public static void main(String[] args)
            throws InterruptedException {

        TicketCounter counter = new TicketCounter();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {

            threads[i] =
                new BookingThread(counter);

            threads[i].setName("User-" + (i + 1));

            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println();
        System.out.println(
            "Successful bookings = " +
            counter.successfulBookings
        );

        System.out.println(
            "Seats left = " +
            counter.seatsLeft
        );
    }
}