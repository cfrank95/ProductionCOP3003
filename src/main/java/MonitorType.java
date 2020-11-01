/**
 * Defines type for each Product containing a Monitor
 *
 * @author Chris Frank
 */
public enum MonitorType {

    LCD("LCD"),
    LED("LED");

    public final String type;

    MonitorType(String type) {
        this.type = type;
    }
}
