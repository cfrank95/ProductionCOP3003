import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

/**
 * Control interactions between user interface,
 * database manipulation, and processes.
 *
 * @author Chris Frank
 */
public class Controller {


    public Button RecordProductionButton;

    // ***** Employee Tab *****
    @FXML
    private TextField txtfldName;

    @FXML
    private TextField txtfldPassword;

    @FXML
    private Label lblEmployeeError;

    @FXML
    void CreateEmployee() {
        createEmployee();
    }

    // ***** End Employee Tab *****

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
    private Label lblProdLineError;

    public Button btnAddProduct;

    public Controller() throws FileNotFoundException {
    }

    @FXML
    void AddProduct() {                        // Product Line Tab Button
        addProduct();
    }
    // ***** End Product Tab *****

    // ***** Produce Tab *****
    // ObservableList to hold the Strings for use in the ListView in Produce tab
    ObservableList<String> produceObservableList = FXCollections.observableArrayList();

    @FXML
    private Label lblRecordProductionError;

    @FXML
    private ListView<Product> listViewChooseProduct;

    @FXML
    private ComboBox<String> cmbobxChooseQuantity;

    public Button btnCreateEmployee;

    @FXML
    void RecordProduction() {                  // Produce Tab Button
        recordProduction(true);
    }
    // ***** End Produce Tab  *****

    // ***** Production Log Tab *****
    @FXML
    private TextArea prodLogTextArea;
    // ***** End Production Log Tab *****


    // Initialize Interface with information from database
    public void initialize() {

        // Product Line Tab
        populateProductLineChoiceBox();
        populateProdLineTableView(false);

        // Produce Tab
        populateProduceComboBox();

        // Production Log
        recordProduction(false);


    }

    static int totalCount = 1; // Count total number of products

    // Initialize Database Driver information used in all methods connecting to DB
    // JDBC Driver name and database URL
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./res/ProdDB";

    // Database Credentials
    final String USER = "";

    File password = new File("res/passFile");
    Scanner sc = new Scanner(password);
    final String PASS = sc.nextLine();


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
            String sql = "INSERT INTO PRODUCT (NAME, MANUFACTURER, TYPE) VALUES(?, ?, ?)";

            pStmt = conn.prepareStatement(sql);

            // Variables to hold values user inputs
            String product;
            if (txtProductName.getText().equals("")) {
                lblProdLineError.setText("Enter Product Name");
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(3)
                );
                visiblePause.setOnFinished(
                        event -> lblProdLineError.setText("")
                );
                visiblePause.play();
            } else {
                product = txtProductName.getText();
                pStmt.setString(1, product);
            }

            String manufacturer;
            if (txtManufacturer.getText().equals("")) {
                lblProdLineError.setText("Enter manufacturer");
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(3)
                );
                visiblePause.setOnFinished(
                        event -> lblProdLineError.setText("")
                );
                visiblePause.play();
            } else {
                manufacturer = txtManufacturer.getText();
                pStmt.setString(2, manufacturer);
            }

            ItemType itemType;
            if (choicebxItemType.getSelectionModel().isEmpty()) {
                lblProdLineError.setText("Select Item Type");
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(3)
                );
                visiblePause.setOnFinished(
                        event -> lblProdLineError.setText("")
                );
                visiblePause.play();
            } else {
                itemType = choicebxItemType.getValue();
                pStmt.setString(3, itemType.toString());
            }

            if (!choicebxItemType.getSelectionModel().isEmpty() &&
                    !txtProductName.getText().equals("") &&
                    !txtManufacturer.getText().equals("")) {
                pStmt.executeUpdate();        // executeUpdate() executes a string to be as SQL code
                System.out.println("Inserted records into the table...");

                productLineTableView.setAccessibleText("");
                populateProdLineTableView(true);
            }


        } catch (Exception se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }// Handle errors for Class.forName
        finally {
            // finally block used to close resources
            try {
                if (pStmt != null)
                    conn.close();
            } catch (SQLException ignored) {
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
     * in database and fill TableView with corresponding data.
     *
     * @param isForUpdate used in conditional statement to
     *                    either update table or create a
     *                    new one for user to view
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
            System.out.println("Populating data from the PRODUCT table...");

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
                productLine.add(new Widget(rs.getInt("id"), rs.getString("name"), rs.getString("manufacturer"), ItemType.valueOf(rs.getString("type"))));
                produceObservableList.add(new Widget(rs.getInt("id"), rs.getString("name"), rs.getString("manufacturer"), ItemType.valueOf(rs.getString("type"))).toString());
                listViewChooseProduct.getItems().add(new Widget(rs.getInt("id"), rs.getString("name"), rs.getString("manufacturer"), ItemType.valueOf(rs.getString("type"))));
            }

            System.out.println("Records Populated from table...");

        } catch (Exception se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }// Handle errors for Class.forName
        finally {
            // finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null)
                    conn.close();

            } catch (SQLException se) {
                se.printStackTrace();
            }// end try

        }// end finally try
    }

    /**
     * create entry in database for employee information
     * entered into the text fields
     */
    public void createEmployee() {
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
            System.out.println("Populating data from the EMPLOYEE table...");

            stmt = conn.createStatement();
            String sql;

            try {
                Employee employee = new Employee(txtfldName.getText(), txtfldPassword.getText());

                sql = "INSERT INTO EMPLOYEE(NAME, EMAIL, USERNAME, PASSWORD) VALUES (?,  ?, ?, ?)";

                PreparedStatement pStatement = conn.prepareStatement(sql);

                String NAME = "";
                if (employee.name.equals("")) {
                    lblEmployeeError.setText("Enter First & Last Name");
                    PauseTransition visiblePause = new PauseTransition(
                            Duration.seconds(3)
                    );
                    visiblePause.setOnFinished(
                            event -> lblEmployeeError.setText("")
                    );
                    visiblePause.play();
                } else {
                    NAME = employee.name;
                    pStatement.setString(1, NAME);
                }

                String EMAIL = employee.email;
                String USERNAME = employee.username;

                if (!NAME.equals("")) {
                    pStatement.setString(2, EMAIL);
                    pStatement.setString(3, USERNAME);

                    pStatement.executeUpdate();
                    System.out.println("Employee added to EMPLOYEE table...");
                }

                String PASSWORD = reverseString(employee.password);
                pStatement.setString(4, PASSWORD);

            } catch (NullPointerException e) {
                lblRecordProductionError.setText("Choose Quantity to Produce.");
                PauseTransition visiblePause = new PauseTransition(
                        Duration.seconds(3)
                );
                visiblePause.setOnFinished(
                        event -> lblRecordProductionError.setText("")
                );
                visiblePause.play();
            }

        } catch (Exception se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }// Handle errors for Class.forName
        finally {
            // finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException ignored) {
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

    public String reverseString(String pass) {
        if (pass.isEmpty()) {
            return pass;
        }

        return reverseString(pass.substring(1)) + pass.charAt(0);
    }

    /**
     * Take either one or every row of data from Product table
     * in database and fill Production Table with corresponding data.
     *
     * @param isUpdate    used in conditional statement to
     *                    either update table or create a
     *                    new one for user to view
     */
    public void recordProduction(boolean isUpdate) {
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
            System.out.println("Populating data from the PRODUCTIONRECORD table...");

            stmt = conn.createStatement();
            String sql;

            if (isUpdate) {
                // Create a ProductionRecord object: logEntry and pass into it the Product selected by user
                // iterating however many times the user selects from the ComboBox
                try {
                    for (int i = 0; i < Integer.parseInt(cmbobxChooseQuantity.getValue()); i++) {
                        sql = "INSERT INTO PRODUCTIONRECORD(PRODUCT_ID, SERIAL_NUM, DATE_PRODUCED)" +
                                "VALUES(?, ?, CURRENT_TIMESTAMP)";
                        ProductionRecord prodRecord = new ProductionRecord(listViewChooseProduct.getSelectionModel().getSelectedItem(), totalCount++);

                        PreparedStatement pStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        pStatement.setInt(1, prodRecord.getProductID());
                        pStatement.setString(2, prodRecord.getSerialNumber());

                        pStatement.executeUpdate();

                        System.out.println("Production Record added to Production Record table...");
                        prodLogTextArea.appendText(prodRecord.toString() + "\n");
                    }
                } catch (NullPointerException e) {
                    lblRecordProductionError.setText("Choose Quantity to Produce.");
                    PauseTransition visiblePause = new PauseTransition(
                            Duration.seconds(3)
                    );
                    visiblePause.setOnFinished(
                            event -> lblRecordProductionError.setText("")
                    );
                    visiblePause.play();
                }
            } else if (!isUpdate) {
                sql = "SELECT * FROM PRODUCTIONRECORD";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    totalCount++;
                    prodLogTextArea.appendText("Prod. Num: " + rs.getString(1) +
                            " Product ID: " + rs.getString(2) +
                            " Serial Num: " + rs.getString(3) +
                            " Date: " + rs.getString(4) + "\n");
                }

                System.out.println("Records Populated from table...");
            }

        } catch (Exception se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }// Handle errors for Class.forName
        finally {
            // finally block used to close resources
            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException ignored) {
            }// do nothing

            try {
                if (conn != null)
                    conn.close();

            } catch (SQLException se) {
                se.printStackTrace();
            }// end try

        }// end finally try
    }

} // end controller