/**
@author Cheng Luo
*/
import java.util.*;
import java.text.*;
import java.io.*;
public class UserInterface {
  private static UserInterface userInterface;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCTS = 2;
  private static final int ADD_MANUFACTURER = 3;
  private static final int ADD_SUPPLIER = 4;
  private static final int DELETE_SUPPLIER = 5;
  private static final int RECEIVE_SHIPMENT = 6;
  private static final int ACCEPT_PAYMENT = 7;
  private static final int ACCEPT_ORDER = 8;
  private static final int PROCESS_ORDER = 9;
  private static final int GET_TRANSACTIONS = 10;
  private static final int LIST_UNBALANCE=11;
  private static final int RECORD_ORDER =12;
  private static final int SHOW_CLIENTS = 13;
  private static final int SHOW_PRODUCTS = 14;
  private static final int SHOW_MANUFACTURERS = 15;
  private static final int SHOW_ORDERLIST = 16;
  private static final int SHOW_INVOICELIST = 17;
  private static final int SHOW_WAITINGLIST= 18;
  private static final int LIST_OUTSTANDING_MANUFACTURER =19;
  private static final int LIST_MANUFACTURER_PRICE =20;
  private static final int SAVE = 21;
  private static final int RETRIEVE = 22;
  private static final int HELP = 23;
  private UserInterface() {
    if (yesOrNo("Look for saved data and  use it?")) {
      retrieve();
    } else {
      warehouse = Warehouse.instance();
    }
  }
  public static UserInterface instance() {
    if (userInterface == null) {
      return userInterface = new UserInterface();
    } else {
      return userInterface;
    }
  }
  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }
  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  public double getDouble(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Double num = Double.valueOf(item);
        return num.doubleValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  public Calendar getDate(String prompt) {
    do {
      try {
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe) {
        System.out.println("Please input a date as mm/dd/yy");
      }
    } while (true);
  }
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 23 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(ADD_PRODUCTS + " to  add products");
    System.out.println(ADD_MANUFACTURER + " to  Add manufacturer");
	System.out.println(ADD_SUPPLIER+ " to add manufacturer to supplierlist");
	System.out.println(DELETE_SUPPLIER+ " to delete manufacturer from supplierlist");
    System.out.println(RECEIVE_SHIPMENT + " to  receive a shipment ");
    System.out.println(ACCEPT_PAYMENT + " to  receive a payment ");
    System.out.println(ACCEPT_ORDER + " to accept a order");
    System.out.println(PROCESS_ORDER + " to  process orders");
    System.out.println(GET_TRANSACTIONS + " to  print all transactions of client");
	System.out.println(LIST_UNBALANCE + " to  print all clients who have an outstanding balance");
	System.out.println(RECORD_ORDER + " to record an order placed with a manufacturer");
    System.out.println(SHOW_CLIENTS + " to  print clients");
    System.out.println(SHOW_PRODUCTS + " to  print products");
	System.out.println(SHOW_MANUFACTURERS + " to  print manufacturers");
	System.out.println(SHOW_ORDERLIST + " to print orders");
    System.out.println(SHOW_INVOICELIST + " to print inovices");
	System.out.println(SHOW_WAITINGLIST + " to print waitings");
	System.out.println(LIST_OUTSTANDING_MANUFACTURER + " to print all outstanding manufacturer orders for a given product");
    System.out.println(LIST_MANUFACTURER_PRICE + " to print all manufacturers and their prices for a given product");
    System.out.println(SAVE + " to  save data");
    System.out.println(RETRIEVE + " to  retrieve");
    System.out.println(HELP + " for help");
  }

  public void addClient() {
    String name = getToken("Enter Client name");
    String address = getToken("Enter address");
    String phone = getToken("Enter phone");
    Client result;
    result = warehouse.addClient(name, address, phone);
    if (result == null) {
      System.out.println("Could not add Client");
    }
    System.out.println(result);
  }

  public void addProducts() {
    Product result;
    do {
      String name = getToken("Enter  name");
      //String manufacturers = getToken("Enter manufacturers");
      String id = getToken("Enter id");
	  double price = getDouble("Enter price");
	  int qunatity = getNumber("Enter number of products");
	  
      result = warehouse.addProduct(name,id,price,qunatity);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Product could not be added");
      }
      if (!yesOrNo("Add more products?")) {
        break;
      }
    } while (true);
  }

  public void addManufacturers() {
    Manufacturer result;
    do {
      String name = getToken("Enter  name");
      String address = getToken("Enter address");
      String id = getToken("Enter id");
	  
      result = warehouse.addManufacturer(name,address,id);
      if (result != null) {
        System.out.println(result);
      } else {
        System.out.println("Manufacturer could not be added");
      }
      if (!yesOrNo("Add more Manufacturers?")) {
        break;
      }
    } while (true);
  }
  
  public void addSupplier(){
	do {
	    String Mid = getToken("Please enter manufacturer ID you want to add into supplierList");
		Manufacturer Mresult = warehouse.searchManufacturer(Mid);
	    if(Mresult != null){
		   String Pid = getToken("Please enter the product ID");
		   Product Presult = warehouse.searchProduct(Pid);
		   if(Presult != null){   
			  double supplierPrice = getDouble("Please enter the supplier price!");
			  if(warehouse.makeSupplier(Mresult,Presult,supplierPrice))
			     System.out.println("Adding successful!");
	       }
		   else
			  System.out.println("Cannot find this product ID in invetory!");
	   }
		else
		  System.out.println("Cannot find this ID in manufacturer List!");
	 
	 if (!yesOrNo("Add more Manufacturers?")) {
        break;
	 }
	}while(true);
  }
  
  public void deleteSupplier(){
    do{
		String Mid = getToken("Please enter manufacturer ID you want to delete from supplierList"); 
		Manufacturer Mresult = warehouse.searchManufacturer(Mid);
		if(Mresult != null){
			String Pid = getToken("Please enter product ID for this supplier");
			Product Presult = warehouse.searchProduct(Pid);
			if(Presult != null){
			   if(warehouse.deleteManufacturer(Mresult,Presult)){
			      System.out.println("Delete successful!");
			   }
			}
			else
			  System.out.println("this manufacturer is not in supplierList!");
		}
		else
		 System.out.println("Cannot find this ID in Manufacturer List!");
		if (!yesOrNo("delete more Manufacturers?")) {
        break;
	 }
	}while(true);

  }
  
  public void receiveShipment(){
    String OSid = getToken("Please enter the order ID you placed with supplier!");
	warehouse.receivedOrder(OSid);
	String Pid = getToken("Please enter product ID you received");
	Product Presult = warehouse.searchProduct(Pid);
	boolean filled = false;
	int quantity = 0;
	if(Presult != null){   
	  for(Iterator waitOrders = warehouse.getWaitings();waitOrders.hasNext();){
	     Waiting waiting = (Waiting)(waitOrders.next());
		 waiting.showWaiting();
		 if (yesOrNo("Do you want fill this order?(Note:if you want to fill waitlist, receive amount must bigger than following quantity!)")) {
		    quantity += warehouse.getWaitquantity(waiting,Pid);	
			//wait order transfer into invoice and ship it.
            warehouse.WaitToInvoice(waiting,Pid);			
		 }
		 else
		    System.out.println("Next waiting Order!");
	   }
	  int ProductQuantity = getNumber("Please enter the quantity for this product in this shipment!");
	  //update inventory
      if(warehouse. compareQuantity(ProductQuantity,quantity)){
	     int a = ProductQuantity - quantity;
	     if (yesOrNo("Do you want update inventory?")){
		    Presult.updateQuantity(a);
			System.out.println("Inventory update successfully!");
		 } 
		 else
			System.out.println("shipment processed!");
	  }
	}
	else
	  System.out.println("Cannot find this product in Inventory!");
  }
  
  /***************************************************************
    extra creadit for the project
  */
  public void recordOrder(){
	String ProductID = getToken("Please intput the productID");
	Product result = warehouse.searchProduct(ProductID);
	if(result != null){
	  	String name = getToken("Please input the manufacturer name");
	    String orderID = getToken("Please input orderID placed with manufacturer");
	    int quantity = getNumber("Please input the quantity of the product");
		warehouse.makeOrderS(name,ProductID,orderID,quantity);
	}
	else
	  System.out.println("Cannot find the product in inventory");
	
  }
  public void listOManufacturer(){
    String ProductID = getToken("Please intput the productID you want to see the outstanding manufacturer orders");
	Product result = warehouse.searchProduct(ProductID);
	if(result != null){
	   Iterator allList = warehouse.listOutManufacturerOrders(result);
	   for(;allList.hasNext();){
	      OrderS orderS = (OrderS)(allList.next());
		  if(!orderS.IsReceived())
			System.out.println(orderS.toString()); 
	   }
	}
	else 
	  System.out.println("Can not find this product!");
    System.out.println("Display end!"); 
 }
  
  /***************************************************************
    extra creadit for the project
  */
  public void listManufacturer_Price(){
    String Pid = getToken("Please enter product ID for this supplier");
    Product Presult = warehouse.searchProduct(Pid);
	if(Presult != null){
		Iterator allsuppliers = warehouse.listManufacturerAndPrice(Presult);
        while (allsuppliers.hasNext()){
			Supplier supplier = (Supplier)(allsuppliers.next());
			System.out.println(supplier.toString());
      }
	}
	else
	  System.out.println("Cannot find this product");
  }
  
  public void acceptOrder(){
	Order result;
	String clientID = getToken("Please enter the Client ID");
	if(warehouse.searchClient(clientID)){
		result = warehouse.makeOrder(clientID);
			if(result != null){
				do{
				//String name = getToken("Enter Product name");
				String id = getToken("Enter Product id");
				//double price = getDouble("Enter price");
				int qunatity = getNumber("Enter number of products");
				Product product = new Product(warehouse.searchProduct(id));
			    product.setQuantity(qunatity);
				//Product product = new Product(name,id,price,qunatity);
				warehouse.addToOrder(product,result);
				if (!yesOrNo("want order another product?"))
                    break;
				}while(true);
				warehouse.caculateOrderCost(result);
				warehouse.orderIssued(result,clientID);
				warehouse.updateBalance(result, clientID);
				result.showOrder();
			}
			else{
			System.out.println("Order could not be added");
			}
	}
	else
		System.out.println("This Client is not exsits");
 }
  
  public void acceptPayment() {
	  double result = 0.0;
	  String clientID = getToken("Please enter the Client ID");
	  if(warehouse.searchClient(clientID)){
	     double payment = getDouble("Please enter the client's payment!");
	     result = warehouse.addPayment(payment,clientID);
	  	  System.out.println("The balance of client is " + result);
	  }
	  else
		System.out.println("Cannot find this client in clientList! Please try again!");

  }

  public void showProducts() {
      Iterator allProducts = warehouse.getProducts();
      while (allProducts.hasNext()){
	  Product product = (Product)(allProducts.next());
          System.out.println(product.toString());
      }
  }

  public void showClients() {
      Iterator allClients = warehouse.getClients();
      while (allClients.hasNext()){
	  Client client = (Client)(allClients.next());
          System.out.println(client.toString());
      }
  }
  
  public void showManufacturers(){
      Iterator allManufacturers = warehouse.getManufacturers();
      while (allManufacturers.hasNext()){
		  Manufacturer manufacturer = (Manufacturer)(allManufacturers.next());
          System.out.println(manufacturer.toString());
      }
  }
  
  public void showOrders(){
      System.out.println("Order List in database: ");
      Iterator allOrders = warehouse.getOrder();
	  while (allOrders.hasNext()){
		  Order order = (Order)(allOrders.next());
		  order.showOrder();
	  }
  }
  
  public void showInvoices(){
      System.out.println("Invoice List in database: ");
      Iterator allInvoices = warehouse.getInvoices();
	  while(allInvoices.hasNext()){
		Invoice invoice = (Invoice)(allInvoices.next());
		invoice.showInvoice();
	  }
  }
  public void showWaitings(){
      System.out.println("Waiting List in database: ");
      Iterator allWaitings = warehouse.getWaitings();
	  while(allWaitings.hasNext()){
		Waiting waiting = (Waiting)(allWaitings.next());
		waiting.showWaiting();
	  }

  }
  
  public void processOrder(){
      Order result;
	  Invoice invoice;
      String orderID = getToken("Please enter the Order ID");
      result = warehouse.searchOrder(orderID);
	  if(result != null){
		invoice = warehouse.createInvoice(result);
	    if(invoice != null){
		   invoice.showInvoice();
		   boolean shipped = yesOrNo("Do you want shipped this order?");
		   if(shipped){
		      if(warehouse.setShipped(invoice)){
                 if(warehouse.updateInventory(invoice))
					System.out.println("The invertory is updated!");
			  }
			System.out.println("This order is already shipped");  
		   }
		   else{
		    System.out.println("This order is no shipped.");
		   }
		}
		else
	       System.out.println("These product is not avaiable at invertory");
	  }
      else
		System.out.println("Cannot find this order");

  }
  
  public void getTransactions(){
   do{
	 String clientID = getToken("Please enter the Client ID you want to see his/her transactions");
	 if(warehouse.searchClient(clientID)){
	   Iterator allTransactions = warehouse.getTransaction(clientID);
	   if(allTransactions != null){
	   System.out.println("There are orders for this client:");
	   System.out.println("");
	   for(;allTransactions.hasNext();){
		  Order order = (Order)(allTransactions.next());
		  order.showOrder();
	     }
	   }
	   else{
	      System.out.println("This client do not have any order yet!");
	    }
	 }
	 else
	   System.out.println("Cannot find this client");
     
	 if (!yesOrNo("More client's transaction want to see?")) 
         break;	 
   }while(true);   
  }
  
  public void listUnbalance(){
      Iterator allUnbalanceClients = warehouse.getClients();
	  System.out.println("List of clients who have an outstanding balance:");
	  System.out.println("");
      while (allUnbalanceClients.hasNext()){
		Client client = (Client)(allUnbalanceClients.next());
		if(client.getBalance()<0)
			System.out.println(client.toString());
      }
  }
  
  private void save() {
    if (warehouse.save()) {
      System.out.println(" The warehouse has been successfully saved in the file warehouseData \n" );
    } else {
      System.out.println(" There has been an error in saving \n" );
    }
  }
  private void retrieve() {
    try {
      Warehouse tempwarehouse = Warehouse.retrieve();
      if (tempwarehouse != null) {
        System.out.println(" The warehouse has been successfully retrieved from the file warehouseData \n" );
        warehouse = tempwarehouse;
      } else {
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }
  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT:        addClient();
                                break;
        case ADD_PRODUCTS:      addProducts();
                                break;
        case ADD_MANUFACTURER:  addManufacturers();
                                break;
		case ADD_SUPPLIER:      addSupplier();
								break;
        case DELETE_SUPPLIER:   deleteSupplier();
								break;
        case RECEIVE_SHIPMENT:  receiveShipment();
                                break;
        case ACCEPT_PAYMENT:    acceptPayment();
                                break;
        case ACCEPT_ORDER:      acceptOrder();
                                break;
        case PROCESS_ORDER:     processOrder();
                                break;
        case GET_TRANSACTIONS:  getTransactions();
                                break;
        case LIST_UNBALANCE:    listUnbalance();
                                break;		
		case RECORD_ORDER:      recordOrder();
								break;
		case SHOW_MANUFACTURERS:showManufacturers();
		                        break;
        case SHOW_CLIENTS:	    showClients();
                                break; 		
        case SHOW_PRODUCTS:	    showProducts();
                                break;
        case SHOW_ORDERLIST:	showOrders();
								break;
		case SHOW_INVOICELIST:  showInvoices();
								break;
		case SHOW_WAITINGLIST:  showWaitings();
								break;
        case LIST_OUTSTANDING_MANUFACTURER: listOManufacturer();
		                        break;
        case LIST_MANUFACTURER_PRICE: listManufacturer_Price();
								break;
        case SAVE:              save();
                                break;
        case RETRIEVE:          retrieve();
                                break;
        case HELP:              help();
                                break;
      }
    }
  }
  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}
