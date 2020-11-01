/**
 * Holds methods used by any object that has a screen.
 *
 * @author Chris Frank
 */
public interface ScreenSpec {

    public String getResolution();

    public int getRefreshRate();

    public int getResponseTime();
}
