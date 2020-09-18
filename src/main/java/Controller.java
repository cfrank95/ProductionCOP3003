import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {


    // Product Tab
    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtManufacturer;

    @FXML
    private ChoiceBox<?> choicebxItemType;

    @FXML
    private Button btnAddProduct;

    @FXML
    void AddProduct(ActionEvent event) {                        // Product Tab Button
        ProductionConnectDatabase();
    }
    // End Product Tab

    // Produce Tab
    @FXML
    private Button RecordProductionButton;

    @FXML
    private ListView<?> lstvwChooseProduct;

    @FXML
    private ComboBox<String> cmbobxChooseQuantity;

    @FXML
    void RecordProduction(ActionEvent event) {                  // Produce Tab Button

    }
    // ***** End Produce Tab  ******

    // Initialize drop-down boxes
    public void initialize(){

        // Produce Tab Choose Quantity Combo Box
        for(int i = 0; i <= 9; i++){
            String j = String.valueOf(i + 1);
            cmbobxChooseQuantity.getItems().add(i,j);
        }
        cmbobxChooseQuantity.setEditable(true);

    }


    public void ProductionConnectDatabase() {

        // JDBC Driver name and database URL
        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:./res/ProdDB";

        // Database Credentials
        final String USER = "";
        final String PASS = "";

        Connection conn = null;
        Statement stmt = null;


        // The following code is to connect and interact with database tables from the GUI
        // It is almost all completely useless in its current form, but is being used as test models
        // Exception handling used in current form for testing
        try {

            // Step 1: Register JDBC Driver
            Class.forName(JDBC_DRIVER);

            // Step 2: Open a Connection
            System.out.println("Connecting to ProDDB...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database successfully...");

            // Step 3: Execute a Query
            System.out.println("Inserting records into the table...");
            stmt = conn.createStatement();  // object for sending SQL statements to DB

            // **** The following will most likely be in its own method ****

            // text box to String
            String product = txtProductName.getText();
            String manufacturer = txtManufacturer.getText();

            // String sql = "INSERT INTO PRODUCT (NAME, MANUFACTURER, TYPE)" +                  [TO BE UPDATED]
            //        "VALUES (" + product + "," + manufacturer + "," + "test" + ")";
            // stmt.executeUpdate(sql);        // executeUpdate() executes a string to be as SQL code
            System.out.println("Inserted records into the table...");

            // **** method would end here ****

        }catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            // finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            }catch(SQLException se) {
            }// do nothing

            try {
                if (conn != null)
                    conn.close();

            }catch(SQLException se) {
                se.printStackTrace();
            }// end try

        }// end finally try

    }// end main()

} // end controller