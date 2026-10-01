interface Notifier {
    void send(String message);
}

// Marker Interface
interface Urgent {
}

class EmailSender implements Notifier, Urgent {

    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SMSSender implements Notifier {

    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

public class NotificationDemo {
    public static void main(String[] args) {

        // Lambda senders
        Notifier email = message ->
                System.out.println("Email: " + message);

        Notifier sms = message ->
                System.out.println("SMS: " + message);

        Notifier[] senders = {
            email,
            sms
        };

        // Broadcast
        for (Notifier sender : senders) {
            sender.send("Exam tomorrow!");
        }

        // Urgent sender
        Notifier urgentEmail = new EmailSender();

        if (urgentEmail instanceof Urgent) {
            urgentEmail.send("URGENT: Exam tomorrow!");
            urgentEmail.send("URGENT: Exam tomorrow!");
        }
    }
}