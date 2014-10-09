import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class Salespersonstate extends WareHouseState implements ActionListener{
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WareHouseContext context;
  private static Salespersonstate instance;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCTS = 2;
  private static final int RECEIVE_SHIPMENT = 3;
  private static final int ACCEPT_PAYMENT = 4;
  private static final int PROCESS_ORDER = 5;
  private static final int LIST_UNBALANCE=6;
  private static final int RECORD_ORDER =7;
  private static final int SHOW_CLIENTS = 8;
  private static final int SHOW_PRODUCTS = 9;
  private static final int SHOW_ORDERLIST = 10;
  private static final int SHOW_INVOICELIST = 11;
  private static final int SHOW_WAITINGLIST= 12;
 
  private JFrame frame;
  private AbstractButton logoutButton,backButton,clientButton,menuButton;

 
  
  private Salespersonstate() {
      super();
      warehouse = Warehouse.instance();
	  logoutButton = new JButton("Logout");
	  logoutButton.setPreferredSize(new Dimension(100,30));
      logoutButton.addActionListener(this);
	  backButton = new JButton("Back");
	  backButton.setPreferredSize(new Dimension(100,30));
      backButton.addActionListener(this);
	  clientButton = new JButton("ClientMenu");
	  clientButton.setPreferredSize(new Dimension(100,30));
	  clientButton.addActionListener(this);
	  menuButton = new JButton("SalesClerkMenu");
	  menuButton.addActionListener(this);
  }
  
  private void Client(){
    String clientID = JOptionPane.showInputDialog(
                     frame,"Please input the client id: ");
    if (Warehouse.instance().searchClient(clientID)){
      (WareHouseContext.instance()).setClient(clientID);  
       Loginstate.instance().clear();
      (WareHouseContext.instance()).changeState(2);
    }
    else 
      JOptionPane.showMessageDialog(frame,"Invalid client id.");
  } 
 
  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.logoutButton)) 
       (WareHouseContext.instance()).changeState(3);
	else if (event.getSource().equals(this.backButton)){
	   if((WareHouseContext.instance()).getLogin() == WareHouseContext.IsManager)
	       logout();
	   if((WareHouseContext.instance()).getLogin() == WareHouseContext.IsSalesperson)
	       (WareHouseContext.instance()).changeState(3);
	}
	else if (event.getSource().equals(this.clientButton)){
	   this.Client();
	}
	else if (event.getSource().equals(this.menuButton)){
	    String option = JOptionPane.showInputDialog(frame,help(),"SalesClerk Menu",JOptionPane.PLAIN_MESSAGE);
		if(option == null || option.equals("")){
		}
		else{
		   int command = Integer.parseInt(option);
		   switch (command) {
				    case ADD_CLIENT:        addClient();
											break;
					case ADD_PRODUCTS:      addProducts();
											break;
					case RECEIVE_SHIPMENT:  receiveShipment();
											break;
					case ACCEPT_PAYMENT:    acceptPayment();
											break;								
					case PROCESS_ORDER:     processOrder();
											break;
					case LIST_UNBALANCE:    listUnbalance();
											break;		
					case RECORD_ORDER:      recordOrder();
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
		  }
		}
	}
  } 
  
  public static Salespersonstate instance() {
    if (instance == null) {
      instance = new Salespersonstate();
    }
    return instance;
  }

  private boolean yesOrNo(String prompt) {
 	int n = JOptionPane.showConfirmDialog(frame,prompt,"Dear Salesclerk",JOptionPane.YES_NO_OPTION);
	if(n == 0)return true;
	else return false;
  }
  
  public String help() {
	StringBuffer buffer = new StringBuffer();
    buffer.append("Welcome to the SaleClerk menu!\n");
    buffer.append("Please enter a number as explained below:\n");
    buffer.append(ADD_CLIENT + " to add a client\n");
    buffer.append(ADD_PRODUCTS + " to add a product\n");
    buffer.append(RECEIVE_SHIPMENT + " to receive a shipment from supplier\n");
    buffer.append(ACCEPT_PAYMENT + " to accept a payment from client\n");
    buffer.append(PROCESS_ORDER + " to process order\n");
    buffer.append(LIST_UNBALANCE + " to  print all clients who have an outstanding balance \n");
    buffer.append(RECORD_ORDER + " to record an order placed with a manufacturer\n");
    buffer.append(SHOW_CLIENTS + " to  print clients\n");
    buffer.append(SHOW_PRODUCTS + " to  print products\n");
    buffer.append(SHOW_ORDERLIST + " to print orders\n");
    buffer.append(SHOW_INVOICELIST + " to print inovices\n"); 
    buffer.append(SHOW_WAITINGLIST + " to print waitings\n");
	return buffer.toString();
  }
  
  public void addClient() {
    JMultiInput input = new JMultiInput("Client name", "Address", "Phone Number");
	int result1 = input.result("Client Information");
    
	String name = input.getFirstText();
    String address = input.getSecondText();
    String phone = input.getThirdText();
	
	if(name.equals("")||address.equals("")|| address.equals("")){
	   JOptionPane.showMessageDialog(frame,"Unsuccessfully Adding Action!","Error",JOptionPane.ERROR_MESSAGE);
	}
	else{
		Client result;
		result = warehouse.addClient(name, address, phone);
		if (result == null) {
		JOptionPane.showMessageDialog(frame,"Could not add Client","Error",JOptionPane.ERROR_MESSAGE);
		}
		JOptionPane.showMessageDialog(frame,result.toString(),"Client Information",JOptionPane.PLAIN_MESSAGE);
	}
  }

  public void addProducts() {
    Product result;
    do {
	  JMultiInput input = new JMultiInput("Product name", "Product ID", "Product Price","Product Quantity");
	  int result1 = input.result("Product Information");
      String name = input.getFirstText();
      String id = input.getSecondText();
	  String cost = input.getThirdText();
	  String num = input.getFourthText();  
	  
	if(name == null || name.equals("") || id == null || id.equals("") || cost == null || cost.equals("")||num == null || num.equals("")){
	   JOptionPane.showMessageDialog(frame,"Unsuccessfully Adding Action!","Error",JOptionPane.ERROR_MESSAGE);
	}
	else{
	  Double price = Double.parseDouble(cost);
	  int quantity = Integer.parseInt(num);
      result = warehouse.addProduct(name,id,price,quantity);
      if (result != null) {
        JOptionPane.showMessageDialog(frame,result.toString(),"Product Information", JOptionPane.PLAIN_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(frame,"Product could not be added","Error",JOptionPane.ERROR_MESSAGE);
      }
	} 
      if (!yesOrNo("Add more products?"))
        break;
    } while (true);
  }
  
  public void receiveShipment(){
    String OSid = JOptionPane.showInputDialog("Please enter the order ID you placed with supplier!");
	if(OSid == null || OSid.equals("")){}
	else 
	   warehouse.receivedOrder(OSid);
	
	String Pid = JOptionPane.showInputDialog("Please enter product ID you received");
	if(Pid == null || Pid.equals("")){JOptionPane.showMessageDialog(frame,"Unsuccessful Action!");}
	else{
		Product Presult = warehouse.searchProduct(Pid);
		int quantity = 0;
		if(Presult != null){   
		    for(Iterator waitOrders = warehouse.getWaitings();waitOrders.hasNext();){
			Waiting waiting = (Waiting)(waitOrders.next());
			if (!yesOrNo("Do you want fill this order?\n" + waiting.showWaiting())) {
				quantity += warehouse.getWaitquantity(waiting,Pid);	
				//wait order transfer into invoice and ship it.
				warehouse.WaitToInvoice(waiting,Pid);			
			}
			else
				JOptionPane.showMessageDialog(frame,"Next waiting Order!");
			}
		String Productnum = JOptionPane.showInputDialog(frame,"Please enter the quantity for this product in this shipment!");
		if(Productnum == null || Productnum.equals("")){}
		else{
	     int ProductQuantity = Integer.parseInt(Productnum);
		 if(warehouse. compareQuantity(ProductQuantity,quantity)){
	        int a = ProductQuantity - quantity;
		    Presult.updateQuantity(a);
			JOptionPane.showMessageDialog(frame,"Inventory update successfully!");
		 }
		}
		}
		else
			JOptionPane.showMessageDialog(frame,"Cannot find this product in Inventory!","Error Message", JOptionPane.ERROR_MESSAGE);
		}
  }
  public void showProducts() {
      String message = "";
      Iterator allProducts = warehouse.getProducts();
      while (allProducts.hasNext()){
	  Product product = (Product)(allProducts.next());
          message = message + product.toString() + "\n";
      }
	 if(message.equals(""))
	    JOptionPane.showMessageDialog(frame,"No Product in the list");
     else 
	  JOptionPane.showMessageDialog(frame, message,"Product List", JOptionPane.PLAIN_MESSAGE);
  }

  public void showClients() {
      String message = "";
      Iterator allClients = warehouse.getClients();
      while (allClients.hasNext()){
	  Client client = (Client)(allClients.next());
          message = message + client.toString() + "\n";
      }
	 if(message.equals(""))
	    JOptionPane.showMessageDialog(frame,"No Client in the list");
     else 
	  JOptionPane.showMessageDialog(frame, message,"Client List", JOptionPane.PLAIN_MESSAGE);
  }
  
    public void showOrders(){
      String message = "";
      Iterator allOrders = warehouse.getOrder();
	  while (allOrders.hasNext()){
		  Order order = (Order)(allOrders.next());
		  message = message + order.showOrder() + "\n";
	  }
	 if(message.equals(""))
	    JOptionPane.showMessageDialog(frame,"No Orders");
     else 
	 JOptionPane.showMessageDialog(frame, message,"Order List", JOptionPane.PLAIN_MESSAGE);
  }
  
  public void showInvoices(){
      String message = "";
      Iterator allInvoices = warehouse.getInvoices();
	  while(allInvoices.hasNext()){
		Invoice invoice = (Invoice)(allInvoices.next());
		message = message + invoice.showInvoice() + "\n";
	  }
	 if(message.equals(""))
	    JOptionPane.showMessageDialog(frame,"No Invoices");
     else 	  
	 JOptionPane.showMessageDialog(frame, message,"Invoice List", JOptionPane.PLAIN_MESSAGE);
  }
  
  public void showWaitings(){
      String message = "";
      Iterator allWaitings = warehouse.getWaitings();
	  while(allWaitings.hasNext()){
		Waiting waiting = (Waiting)(allWaitings.next());
		message = message + waiting.showWaiting() + "\n";
	  }
	 if(message.equals(""))
	    JOptionPane.showMessageDialog(frame,"No Wait Orders");
     else 
		JOptionPane.showMessageDialog(frame, message,"Waiting List", JOptionPane.PLAIN_MESSAGE);
  }
    
  public void listUnbalance(){
      String message = "";
      Iterator allUnbalanceClients = warehouse.getClients();
      while (allUnbalanceClients.hasNext()){
		Client client = (Client)(allUnbalanceClients.next());
		if(client.getBalance()<0)
			message = message + client.toString() + "\n";
      }
	 if(message.equals(""))
	    JOptionPane.showMessageDialog(frame,"No Unbalnce client");
     else
		JOptionPane.showMessageDialog(frame, message,"Unbalance Client List", JOptionPane.PLAIN_MESSAGE);
  }
  
  public void processOrder(){
      Order result;
	  Invoice invoice;
      String orderID = JOptionPane.showInputDialog("Please enter the Order ID");
      result = warehouse.searchOrder(orderID);
	  if(result != null){
		invoice = warehouse.createInvoice(result);
	    if(invoice != null){
		   invoice.showInvoice();
		   boolean shipped = yesOrNo("Do you want shipped this order?");
		   if(shipped){
		      if(warehouse.setShipped(invoice)){
                 if(warehouse.updateInventory(invoice))
					JOptionPane.showMessageDialog(frame,"This order is already shipped and invertory is updated!");
			  }
		   }
		   else{
		    JOptionPane.showMessageDialog(frame,"This order is no shipped.");
		   }
		}
	  }
      else
		JOptionPane.showMessageDialog(frame,"Cannot find this order","Warning",JOptionPane.WARNING_MESSAGE);

  }
  
 public void recordOrder(){
	String ProductID = JOptionPane.showInputDialog("Please intput the productID");
	Product result = warehouse.searchProduct(ProductID);
	if(result != null){
	    JMultiInput input = new JMultiInput("manufacturer name", "Order ID", "Product Quantity");
		int result1 = input.result("Manufacturer order Information");
	  	String name = input.getFirstText();
	    String orderID = input.getSecondText();
	    String num = input.getThirdText();
		
		if(name == null || name.equals("") || orderID == null || orderID.equals("") || num == null || num.equals("")){
		  JOptionPane.showMessageDialog(frame,"Blank Cannot be empty!","Warning", JOptionPane.WARNING_MESSAGE);
		}
		
		else{
			int quantity = Integer.parseInt(num);
			warehouse.makeOrderS(name,ProductID,orderID,quantity);
		}
	}
	else
	  JOptionPane.showMessageDialog(frame,"Cannot find the product in inventory","Warning", JOptionPane.WARNING_MESSAGE);
	
  }
  
  public void acceptPayment() {
	  double result = 0.0;
	  String clientID = JOptionPane.showInputDialog("Please enter the Client ID");
	  if(clientID == null || clientID.equals("")){}
	  else{
		if(warehouse.searchClient(clientID)){
			String payment = JOptionPane.showInputDialog("Please enter the client's payment!");
			if(payment == null || payment.equals("")){
			  JOptionPane.showMessageDialog(frame,"Unsuccessfully Action!");
			}
			else{
				int pay = Integer.parseInt(payment);
				result = warehouse.addPayment(pay,clientID);
				JOptionPane.showMessageDialog(frame,"The balance of client is " + result);
			}
		}
		else
			JOptionPane.showMessageDialog(frame,"Cannot find this client in clientList!","Error",JOptionPane.ERROR_MESSAGE);
      }
  }

  public void logout()
  {  
      if (WareHouseContext.instance().getLogin() == WareHouseContext.IsManager)
       {  
        (WareHouseContext.instance()).changeState(0); // BACK with a code 2
       }
      else 
       (WareHouseContext.instance()).changeState(3); // BACK code 2, indicates error
  }
  
  public void run() {
   frame = WareHouseContext.instance().getFrame();
   frame.getContentPane().removeAll();
   frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER,100,50));
   frame.getContentPane().add(this.menuButton);
   frame.getContentPane().add(this.clientButton);
   frame.getContentPane().add(this.backButton);
   frame.getContentPane().add(this.logoutButton);
   frame.setVisible(true);
   frame.paint(frame.getGraphics());
  }

}
