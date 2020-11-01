/**
 * Implements Item.
 * All Items that are to be produced.
 *
 * @author Chris Frank
 */
public abstract class Product implements Item {
    int id;
    ItemType type;
    String manufacturer;
    String name;

    Product(String name, String manufacturer, ItemType type) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.type = type;

    }

    public Product(String name, String manufacturer) {
    }

    public ItemType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String toString() {
        String string = "Name : " + name + "\nManufacturer: "
                + manufacturer + "\nType: " + type.code;

        return string;
    }
}

/**
 * Widget containing a Product for display to user.
 *
 * @author Chris Frank
 */
class Widget extends Product {

    Widget(String name, String manufacturer, ItemType type) {

        super(name, manufacturer, type);
    }
}