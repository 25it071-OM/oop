interface Switchable {
    void on();
    void off();

    default void toggle() {
        on();
    }
}

class Fan implements Switchable {
    public void on() {
        System.out.println("Fan ON");
    }

    public void off() {
        System.out.println("Fan OFF");
    }
}

class Light implements Switchable {
    public void on() {
        System.out.println("Light ON");
    }

    public void off() {
        System.out.println("Light OFF");
    }
}

// Functional Interface
interface SwitchPolicy {
    boolean maySwitchOn(Switchable device, int hour);
}

public class RemoteControlDemo {
    public static void main(String[] args) {

        Switchable[] devices = {
            new Fan(),
            new Light()
        };

        // Toggle each device
        for (Switchable device : devices) {
            device.toggle();
        }

        // Anonymous class
        SwitchPolicy policy1 = new SwitchPolicy() {
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour <= 22;
            }
        };

        // Lambda
        SwitchPolicy policy2 = (device, hour) ->
                hour >= 8 && hour <= 20;

        System.out.println("Anonymous: "
                + policy1.maySwitchOn(devices[0], 10));

        System.out.println("Lambda: "
                + policy2.maySwitchOn(devices[1], 23));
    }
}