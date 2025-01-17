import java.sql.Date;

/**
 * Used for displaying information created by user to display in log
 * with a unique serialNumber.
 *
 * @author Chris Frank
 */
public class ProductionRecord {

    private int productionNumber;
    private int productID;
    private String serialNumber;
    private java.sql.Date dateProduced;

    private static int countAU = 0;
    private static int countVI = 0;
    private static int countAM = 0;
    private static int countVM = 0;

    // Default constructor called to update / store production record
    ProductionRecord(int productionNumber, int productID, String serialNumber, Date dateProduced) {
        this.productionNumber = productionNumber;
        this.productID = productID;
        this.serialNumber = serialNumber;
        this.dateProduced = dateProduced;
    }

    // Constructor to be called in Controller class
    ProductionRecord(Product product, int itemCount) {
        productID = product.id;
        this.productionNumber = itemCount;
        serialNumber = serialNumFormat(product);
        java.util.Date utilDate = new java.util.Date();
        dateProduced = new java.sql.Date(utilDate.getTime());
    }


    // Getters
    public int getProductionNumber() {
        return productionNumber;
    }

    public int getProductID() {
        return productID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Date getDateProduced() {
        return dateProduced;
    }


    // Setters
    public void setProductionNumber(int productionNumber) {
        this.productionNumber = productionNumber;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setDateProduced(Date dateProduced) {
        this.dateProduced = dateProduced;
    }

    public String toString() {
        return "Prod. Num: " + getProductionNumber() +
                " Product ID: " + getProductID() +
                " Serial Num: " + getSerialNumber() +
                " Date: " + getDateProduced();
    }

    /**
     * Uses individual Product object's information to create
     * unique serialNumber.  Uses first 3 elements from manufacturer
     * string, the Product's 2 letter ItemType code, and its count of
     * similar item types that exist, padded with < 5 leading zeros.
     *
     * @param product object passed to generate a serial number for
     *                that specific product
     * @return serialNumber
     */
    String serialNumFormat(Product product) {
        String manufacturer;

        // Grab first 3 letters of manufacturer
        if (product.manufacturer.length() >= 3)
            manufacturer = product.manufacturer.substring(0, 3);
        else
            manufacturer = product.manufacturer;

        String itemTypeCode = product.type.code;

        String serialNumber = manufacturer + itemTypeCode;

        int typeCount = 0;

        switch (product.type) {
            case AUDIO:
                typeCount = countAU;
                countAU++;
                break;
            case VISUAL:
                typeCount = countVI;
                countVI++;
                break;
            case AUDIO_MOBILE:
                typeCount = countAM;
                countAM++;
                break;
            case VISUAL_MOBILE:
                typeCount = countVM;
                countVM++;
                break;
        }

        serialNumber += String.format("%05d", typeCount);

        return serialNumber;
    }
}
