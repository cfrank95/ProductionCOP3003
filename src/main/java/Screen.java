/**
 * (Not used in current GUI program)
 * Categorizes any object that would have a screen.
 * Is used in MoviePlayer class
 *
 * @author Chris Frank
 */

public class Screen implements ScreenSpec {

    String resolution;
    int refreshRate;
    int responseTime;

    Screen(String resolution, int refreshRate, int responseTime) {
        this.resolution = resolution;
        this.refreshRate = refreshRate;
        this.responseTime = responseTime;
    }

    // Getters
    public String getResolution() {
        return resolution;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public String toString() {
        return "Screen:" +
                "\nResolution: " + resolution +
                "\nRefresh rate: " + refreshRate +
                "\nResponse time: " + responseTime;
    }
}
