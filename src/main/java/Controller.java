import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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


    // ***** Employee Tab *****
    @FXML
    private TextField txtfldName;

    @FXML
    private TextField txtfldPassword;

    @FXML
    private Button btnCreateEmployee;

    @FXML
    void CreateEmployee(ActionEvent event) {
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
    private Button btnAddProduct;

    public Controller() throws FileNotFoundException {
    }

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

        // Product Line Tab
        populateProductLineChoiceBox();
        populateProdLineTableView(false);

        // Produce Tab
        populateProduceComboBox();

        // Production Log is included in populateProdLineTableView()

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
     * in database and fill TableView will corresponding data.
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
            }try {
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
            System.out.println("Populating data from the table...");

            stmt = conn.createStatement();
            String sql;

            Employee employee = new Employee(txtfldName.getText(), txtfldPassword.getText());

            String NAME = employee.name;
            String EMAIL = employee.email;
            String USERNAME = employee.username;
            String PASSWORD = reverseString(employee.password);

            sql = "INSERT INTO EMPLOYEE(NAME, EMAIL, USERNAME, PASSWORD) VALUES ( ?,  ?, ?, ?)";

            PreparedStatement pStatement = conn.prepareStatement(sql);

            pStatement.setString(1, NAME);
            pStatement.setString(2, EMAIL);
            pStatement.setString(3, USERNAME);
            pStatement.setString(4, PASSWORD);

            pStatement.executeUpdate();

            System.out.println("Employee added to Employee table...");

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

} // end controller