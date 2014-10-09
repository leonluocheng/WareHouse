import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class Clientstate extends WareHouseState implements ActionListener{
  private static Clientstate clientstate;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int VIEW_ACCOUNT =1;
  private static final int ACCEPT_ORDER = 2;
  private static final int SHOW_PRODUCTS = 3;
  
  private static final int GET_TRANSACTIONS = 1;
  private static final int SHOW_INVOICE = 2;

  private JFrame frame;
  private AbstractButton logoutButton,backButton,menuButton;
  
  private Clientstate() {
    warehouse = Warehouse.instance();
    logoutButton = new JButton("Logout");
	logoutButton.setPreferredSize(new Dimension(100,30));
    logoutButton.addActionListener(this);
	backButton = new JButton("Back");
	backButton.setPreferredSize(new Dimension(100,30));
    backButton.addActionListener(this);
    menuButton = new JButton("Client Menu");
    menuButton.addActionListener(this);		  

  }
 
  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.logoutButton)) 
       (WareHouseContext.instance()).changeState(3);
	else if (event.getSource().equals(this.backButton)){
	   if((WareHouseContext.instance()).getLogin() == WareHouseContext.IsManager)
	       (WareHouseContext.instance()).changeState(1);	   
	   if((WareHouseContext.instance()).getLogin() == WareHouseContext.IsSalesperson)
	       (WareHouseContext.instance()).changeState(1);
	   if((WareHouseContext.instance()).getLogin() == WareHouseContext.IsClient)
	       (WareHouseContext.instance()).changeState(2);
	}
	else if (event.getSource().equals(this.menuButton)){
	    String option = JOptionPane.showInputDialog(frame,help(),"Client Menu",JOptionPane.PLAIN_MESSAGE);
		if(option == null || option.equals("")){
		}
		else{
		int command = Integer.parseInt(option);
	    switch (command) {
	           case VIEW_ACCOUNT:      viewAccount();
									   break;
               case ACCEPT_ORDER:      acceptOrder();
                                       break;
               case SHOW_PRODUCTS:	   showProducts();
                                       break;
			   default:   JOptionPane.showMessageDialog(frame,"Invaild Choice","Error",JOptionPane.ERROR_MESSAGE);
									   break;
		  }
      } 
	}
  } 
  
  public static Clientstate instance() {
    if (clientstate == null) {
      return clientstate = new Clientstate();
    } else {
      return clientstate;
    }
  }

  private boolean yesOrNo(String prompt) {
 	int n = JOptionPane.showConfirmDialog(frame,prompt,"Dear Client",JOptionPane.YES_NO_OPTION);
	if(n == 0)return true;
	else return false;
	
  }

  public String help() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Welcome to client menu! Client: " + WareHouseContext.instance().getClient()+"\n");
    buffer.append("Please enter a number as explained below:\n");
	buffer.append(VIEW_ACCOUNT + " to view account information\n");
    buffer.append(ACCEPT_ORDER + " to place a order\n");
	buffer.append(SHOW_PRODUCTS + " to  print products and their sale price\n");
    return buffer.toString();
  }

  public String help1() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Please enter a number as explained below:\n");
	buffer.append(GET_TRANSACTIONS + " to  print orders list\n");
	buffer.append(SHOW_INVOICE + " to  print invoice list\n");
    return buffer.toString();
  }
  
  public void acceptOrder(){
	Order result;
	String clientID = WareHouseContext.instance().getClient();
	result = warehouse.makeOrder(clientID);

		if(result != null){
			do{ 
			    JMultiInput input = new JMultiInput("Product ID", "Product Quantity");
				int result1 = input.result("Please enter the product you want to ordered");
				String id = input.getFirstText();
				String num = input.getSecondText();
				
				if(id == null || id.equals("")|| num == null || num.equals("")){
					JOptionPane.showMessageDialog(frame,"ID and Quantity Cannot be empty","Error",JOptionPane.ERROR_MESSAGE);
				    
				}
				else{
				    int quantity = Integer.parseInt(num);
					Product product = new Product(warehouse.searchProduct(id));
					product.setQuantity(quantity);
					warehouse.addToOrder(product,result);
                }					
				if (yesOrNo("want order another product?"))
                    break;
			  }while(true);
				
				warehouse.caculateOrderCost(result);
				warehouse.orderIssued(result,clientID);
				warehouse.updateBalance(result, clientID);
				JOptionPane.showMessageDialog(frame,result.showOrder(),"Order Detail",JOptionPane.PLAIN_MESSAGE);
			}
		else{
		  JOptionPane.showMessageDialog(frame,"Order could not be added","Error",JOptionPane.ERROR_MESSAGE);
		}
 }

public void getTransactions(){
     String message = "";
     String clientID = WareHouseContext.instance().getClient();
	 Iterator allTransactions = warehouse.getTransaction(clientID);
	   if(allTransactions != null){
	   for(;allTransactions.hasNext();){
		  Order order = (Order)(allTransactions.next());
		  message = message + order.showOrder() + "\n";
	     }
		 if(message.equals(""))
		    JOptionPane.showMessageDialog(frame,"No orders","Warning",JOptionPane.WARNING_MESSAGE);
		 else
			JOptionPane.showMessageDialog(frame,message,"Transaction List", JOptionPane.PLAIN_MESSAGE);
	   }
	   else
	     JOptionPane.showMessageDialog(frame,"This client do not have any order yet!","Error",JOptionPane.ERROR_MESSAGE);
}

  public void showProducts() {
	  String message ="";
      Iterator allProducts = warehouse.getProducts();
      while (allProducts.hasNext()){
	  Product product = (Product)(allProducts.next());
          message = message + product.toString() + "\n";
      }
	  JOptionPane.showMessageDialog(frame,message,"Product List", JOptionPane.PLAIN_MESSAGE);
  }

  public void getInvoice(){
     String clientID = WareHouseContext.instance().getClient();
	 String strInvoices = warehouse.showInvoicesClient(clientID);

	 if(strInvoices.equals("")){
	   JOptionPane.showMessageDialog(frame,"No invoice","Warning",JOptionPane.WARNING_MESSAGE);
	 }
	 else{
	   JOptionPane.showMessageDialog(frame,strInvoices,"Invoice List",JOptionPane.PLAIN_MESSAGE);
	 }
  }

  public void viewAccount(){
        String clientID = WareHouseContext.instance().getClient();
		Client client = warehouse.searchClientObj(clientID);
        //JOptionPane.showMessageDialog(frame,client,"Account Information",JOptionPane.PLAIN_MESSAGE);

        String option = JOptionPane.showInputDialog(frame,client.toString() + "\n" + help1(),"Account Information",JOptionPane.PLAIN_MESSAGE);
		if(option == null || option.equals("")){
		}
		else{
		int command = Integer.parseInt(option);
	    switch (command) {
		       case SHOW_INVOICE:  	   getInvoice();
                                       break;
               case GET_TRANSACTIONS:  getTransactions();
                                       break;
			   default:   JOptionPane.showMessageDialog(frame,"Invaild Choice","Error",JOptionPane.ERROR_MESSAGE);
									   break;
		  }
      } 
  }

  public void run() { 
   frame = WareHouseContext.instance().getFrame();
   frame.getContentPane().removeAll();
   frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER,100,50));
   frame.getContentPane().add(this.menuButton);
   frame.getContentPane().add(this.backButton);
   frame.getContentPane().add(this.logoutButton);
   frame.setVisible(true);
   frame.paint(frame.getGraphics());
  }

}
