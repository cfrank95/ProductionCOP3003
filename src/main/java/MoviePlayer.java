/**
 * Sublass of Product.  Implements Multimedia Control
 * Used for creating Products that are categorized by ItemType VISUAL.
 *
 * @author Chris Frank
 */
public class MoviePlayer extends Product implements MultimediaControl {
    Screen screen;
    MonitorType monitorType;

    MoviePlayer(String name, String manufacturer, Screen screen, MonitorType monitorType) {
        super(name, manufacturer, ItemType.VISUAL);
        this.screen = screen;
        this.monitorType = monitorType;
    }

    public void play() {
        System.out.println("Playing movie");
    }

    public void stop() {
        System.out.println("Stopping movie");
    }

    public void previous() {
        System.out.println("Previous movie");
    }

    public void next() {
        System.out.println("Next movie");
    }

    public String toString() {
        return "Name: " + name +
                "\nManufacturer: " + manufacturer +
                "\nType: " + type +
                "\nScreen: " +
                "\nResolution: " + screen.resolution +
                "\nRefresh rate " + screen.refreshRate +
                "\nResponse time: " + screen.responseTime +
                "\nMonitor Type: " + monitorType;
    }
}
