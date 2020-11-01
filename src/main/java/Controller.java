/**
 * Control interactions between user interface,
 * database manipulation, and processes.
 *
 * @author Chris Frank
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class Controller {


    // ***** Product Tab *****
    // ObservableList to hold Products for use in the TableView in Product Line tab
    ObservableList<Product> productLine = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> productLineTableView;

    @FXML
    private TableColumn<Product, String> productColumn;

    @FXML
    private TableColumn<Product, String> manufacturerColumn;

    @FXML
    private TableColumn<Product, ItemType> typeColumn;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtManufacturer;

    @FXML
    private ChoiceBox<ItemType> choicebxItemType;

    @FXML
    private Button btnAddProduct;

    @FXML
    void AddProduct(ActionEvent event) {                        // Product Line Tab Button
        addProduct();
    }
    // ***** End Product Tab *****

    // ***** Produce Tab *****
    // ObservableList to hold the Strings for use in the ListView in Produce tab
    ObservableList<String> produceObservableList = FXCollections.observableArrayList();

    @FXML
    private ListView<Product> listViewChooseProduct;

    @FXML
    private ComboBox<String> cmbobxChooseQuantity;

    @FXML
    private Button RecordProductionButton;

    @FXML
    void RecordProduction(ActionEvent event) {                  // Produce Tab Button
        // Create a ProductionRecord object: logEntry and pass into it the Product selected by user
        // iterating however many times the user selects from the ComboBox
        for (int i = 0; i < Integer.parseInt(cmbobxChooseQuantity.getValue()); i++) {
            ProductionRecord logEntry = new ProductionRecord(listViewChooseProduct.getSelectionModel().getSelectedItem(), totalCount++);
            prodLogTextArea.appendText(logEntry.toString() + "\n");
        }

    }
    // ***** End Produce Tab  *****

    // ***** Production Log Tab *****
    @FXML
    private TextArea prodLogTextArea;
    // ***** End Production Log Tab *****


    // Initialize Interface with information from database
    public void initialize() {

        // Product Line Tab Item Type Choice Box
        populateProductLineChoiceBox();
        populateProdLineTableView(false);

        // Produce Tab Choose Quantity Combo Box
        populateProduceComboBox();

        // Production Log

    }

    static int totalCount = 1; // Count total number of products

    // Initialize Database Driver information used in all methods connecting to DB
    // JDBC Driver name and database URL
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/ProdDB";

    // Database Credentials
    final String USER = "";
    final String PASS = "";

    // ***** Product Tab Methods *****

    /**
     * User fills in data for product and manufacturer to be added to database
     */
    public void addProduct() {
        Connection conn = null;
        PreparedStatement pStmt = null;

        try {
            // Step 1: Register JDBC Driver
            Class.forName(JDBC_DRIVER);

            // Step 2: Open a Connection
            System.out.println("Connecting to ProDDB...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database successfully...");

            // Step 3: Execute a Query
            System.out.println("Inserting records into the table...");

            // Variables to hold values user inputs
            String product = txtProductName.getText();
            String manufacturer = txtManufacturer.getText();
            ItemType itemType = choicebxItemType.getValue();

            String sql = "INSERT INTO PRODUCT (NAME, MANUFACTURER, TYPE) VALUES(?, ?, ?)";

            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, product);
            pStmt.setString(2, manufacturer);
            pStmt.setString(3, itemType.toString());

            pStmt.executeUpdate();        // executeUpdate() executes a string to be as SQL code
            System.out.println("Inserted records into the table...");

            productLineTableView.setAccessibleText("");
            populateProdLineTableView(true);


        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if (pStmt != null)
                    conn.close();
            } catch (SQLException se) {
            }// do nothing

            try {
                if (conn != null)
                    conn.close();

            } catch (SQLException se) {
                se.printStackTrace();
            }// end try

        }// end finally try

    }// end addProduct()


    /**
     * Take either one or every row of data from Product table
     * in database and fill TableView will corresponding data.
     *
     * @param isForUpdate
     */
    public void populateProdLineTableView(boolean isForUpdate) {
        Connection conn = null;
        Statement stmt = null;

        try {
            // Step 1: Register JDBC Driver
            Class.forName(JDBC_DRIVER);

            // Step 2: Open a Connection
            System.out.println("Connecting to ProDDB...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database successfully...");

            // Step 3: Execute a Query
            System.out.println("Populating data from the table...");

            stmt = conn.createStatement();
            String sql;

            // Select whether populate entire database or most recent element
            if (isForUpdate)
                sql = "SELECT TOP 1 * FROM PRODUCT ORDER BY ID DESC";
            else
                sql = "SELECT * FROM PRODUCT";

            ResultSet rs = stmt.executeQuery(sql);

            productColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            productLineTableView.setItems(productLine);

            while (rs.next()) {
                productLine.add(new Widget(rs.getString("name"), rs.getString("manufacturer"), ItemType.valueOf(rs.getString("type"))));
                produceObservableList.add(new Widget(rs.getString("name"), rs.getString("manufacturer"), ItemType.valueOf(rs.getString("type"))).toString());
                listViewChooseProduct.getItems().add(new Widget(rs.getString("name"), rs.getString("manufacturer"), ItemType.valueOf(rs.getString("type"))));
            }

            System.out.println("Records Populated from table...");

        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            // finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }// do nothing

            try {
                if (conn != null)
                    conn.close();

            } catch (SQLException se) {
                se.printStackTrace();
            }// end try

        }// end finally try
    }


    /**
     * Populate ChoiceBox in the Product Line tab with
     * each element of the Product Table
     */
    public void populateProductLineChoiceBox() {
        for (ItemType item : ItemType.values()) {
            choicebxItemType.getItems().add(item);
        }
    }


    /**
     * Populate ComboBox in the Produce tab with numbers
     * (1 - 10) to be selected by user
     */
    public void populateProduceComboBox() {
        for (int i = 0; i <= 9; i++) {
            String j = String.valueOf(i + 1);
            cmbobxChooseQuantity.getItems().add(i, j);
        }
        cmbobxChooseQuantity.getSelectionModel().selectFirst();
        cmbobxChooseQuantity.setEditable(true);
    }

} // end controller