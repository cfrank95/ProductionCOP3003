# ProductionCOP3003
Semester Java FX Project 

9/18/2020:
  **Sprint 1 (Alpha):**
  
  Added GUI for 3 tabs: Product Line, Produce, Production Log.
  * Product Line Tab:
    * 2 x 3 tab pane containing the following:           
      * Product Name label and text box for user entry.
      * Manufacturer label and text box for user entry.
      * Item Type label and combo box for user selection. (editable)
    * Existing products label
    * Table with C1 and C2 columns
  * Produce Tab:
    * Choose Product label
    * List View
    * Choose Quantity label
    * Record Production button
  * Production Log
    * Text Area
  
  Added H2 Database 
  Started database table layouts.
  
  Started main Controller methods that will fetch from the use input in the GUI and insert them into the database.
    The controller method has code currently in it that isn't being used to interact with data, but is being used to test different functions.
    
  Resources(sorted by greatness):
   * https://sites.google.com/site/profvanselow/course/cop-3003/
   * https://www.tutorialspoint.com/jdbc/jdbc-insert-records.htm
   * https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/doc-files/introduction_to_fxml.html#controllers
  
  
  
10/31/2020:
  **Sprint 2 (Beta):**
  
  Added Functionality for all 3 tabs.
* Users can now add a Product into the database using the fields in the first tab.
* The Product's information will then be displayed at the bottom of the Product Line tab in the TableView in sequential order.
* The Produce tab will display each Product in the database and its identifying information in the List View.
     * The user can select from this ListView and choose the quantity in the drop-down box to record the Production and be 
        displayed in the TextArea of the Production Log tab.
* The Production Log displays each Product being produced in the Produce tab with a unique serial number.

Resources:
   * https://sites.google.com/site/profvanselow/course/cop-3003/
   * http://tutorials.jenkov.com/javafx/tableview.html
   * http://tutorials.jenkov.com/javafx/listview.html
   
10/31/2020:
  **Sprint 3 (Release):**
  
  Added Employee tab
* Employees now have a place to create an account that is stored in the database.  An email and username is supplied, and if their password meets minimum requirements is set as well.  If their password doesn't meet minimum requirements, they are supplied a default password.
* A password is set to the database.
