import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.text.*;
public class Managerstate extends WareHouseState implements ActionListener{
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WareHouseContext context;
  private static Managerstate instance;
  private String Pass = "manager";
  private static final int CHANGE_SALES_PRICE= 1;
  private static final int ADD_MANUFACTURERS = 2;
  private static final int ADD_SUPPLIER = 3;
  private static final int DELETE_SUPPLIER = 4;
  private static final int SHOW_MANUFACTURERS =5;
  private static final int LIST_OUTSTANDING_MANUFACTURER = 6;
  private static final int LIST_MANUFACTURER_PRICE = 7;
  
  private JFrame frame;
  private AbstractButton logoutButton,salespersonButton,menuButton;

  private Managerstate() {
      super();
      warehouse = Warehouse.instance();
	  
	  logoutButton = new JButton("Logout");
	  logoutButton.setPreferredSize(new Dimension(100,30));
      logoutButton.addActionListener(this);	
	  
	  menuButton = new JButton("Manager Menu");
      menuButton.addActionListener(this);		  
      salespersonButton = SalespersonButton.instance(); 
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.logoutButton)) 
       (WareHouseContext.instance()).changeState(3);

	if (event.getSource().equals(this.menuButton)){
        String option = JOptionPane.showInputDialog(frame,help(),"Manager Menu",JOptionPane.PLAIN_MESSAGE);
		if(option == null || option.equals("")){
		}
		else{
		int command = Integer.parseInt(option);
	    switch (command) {
			case CHANGE_SALES_PRICE:            changeSalesPrice();
												break;
			case ADD_MANUFACTURERS:             addManufacturers();
												break;
			case ADD_SUPPLIER:                  addSupplier();
												break;
			case DELETE_SUPPLIER:               deleteSupplier();
												break;
			case SHOW_MANUFACTURERS:            showManufacturers();
												break;
			case LIST_OUTSTANDING_MANUFACTURER: listOManufacturer();
												break;
			case LIST_MANUFACTURER_PRICE: listManufacturer_Price();
												break;										
			default:   JOptionPane.showMessageDialog(frame,"Invaild Choice","Error",JOptionPane.ERROR_MESSAGE);
												break;
		  }
      } 
    }	  
  } 
 
  public static Managerstate instance() {
    if (instance == null) {
      instance = new Managerstate();
    }
    return instance;
  }

  private boolean yesOrNo(String prompt) {
    int n = JOptionPane.showConfirmDialog(frame,prompt,"",JOptionPane.YES_NO_OPTION);
	if(n == 0)return true;
	else return false;
  }

  public String help() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Welcome Manager!\n");
    buffer.append("Please enter a number as explained below:\n");
    buffer.append(CHANGE_SALES_PRICE + " to change sales price\n");
    buffer.append(ADD_MANUFACTURERS + " to  add manufacturers\n");
    buffer.append(ADD_SUPPLIER + " to  add supplier for an item \n");
    buffer.append(DELETE_SUPPLIER + " to delete supplier from supplierList\n");
    buffer.append(SHOW_MANUFACTURERS + " to  print manufacturers\n");
    buffer.append(LIST_OUTSTANDING_MANUFACTURER + " to print all outstanding manufacturer orders for a given product\n");
    buffer.append(LIST_MANUFACTURER_PRICE + "to print supplier price for a product!\n");
	return buffer.toString();
  }

  public void changeSalesPrice() {
     String PID = JOptionPane.showInputDialog(frame,"Please input the Product ID you want to modify its price!");
	 Product result;
	 result = warehouse.searchProduct(PID);
	 if(result != null){
	    String price = JOptionPane.showInputDialog(frame,"Please input the price you want to update");
		if(price == null || price.equals("")){
		}
		else{
		    double productPrice = Double.parseDouble(price);
			warehouse.modifyPrice(result,productPrice);
			JOptionPane.showMessageDialog(frame,result.toString(),"Update Product Information",JOptionPane.PLAIN_MESSAGE);
	    }
	 }
	 else
	   JOptionPane.showMessageDialog(frame,"Cannot find this prodcut in inventory!","Warning Message",JOptionPane.WARNING_MESSAGE);
  }
   
public void listManufacturer_Price(){
    String Pid = JOptionPane.showInputDialog(frame,"Please enter product ID for this supplier");
    Product Presult = warehouse.searchProduct(Pid);
	String message = "";
	if(Presult != null){
		Iterator allsuppliers = warehouse.listManufacturerAndPrice(Presult);
        while (allsuppliers.hasNext()){
			Supplier supplier = (Supplier)(allsuppliers.next());
			message = message + supplier.toString() + "\n";
      }
	  JOptionPane.showMessageDialog(frame,message,"Supplier List for Product: "+ Pid, JOptionPane.PLAIN_MESSAGE);
	}
	else
	  JOptionPane.showMessageDialog(frame,"Cannot find this product","Error",JOptionPane.ERROR_MESSAGE);
  }
  
  public void addManufacturers() {

    Manufacturer result;
    do {
	  JMultiInput input = new JMultiInput("Manufacturer name", "Address", "ID");
	  int result1 = input.result("Please enter Manufacturer Infromation");
	  String name = input.getFirstText();
	  String address = input.getSecondText();
      String id = input.getThirdText();
	  
	  if(name.equals("") || name == null || address.equals("") || address == null || id.equals("") || id == null){
	     JOptionPane.showMessageDialog(frame,"Could not add this Manufacturer","Error",JOptionPane.ERROR_MESSAGE);
	  }
	  else{
        result = warehouse.addManufacturer(name,address,id); 
        if (result != null) {
        JOptionPane.showMessageDialog(frame,result,"New Manufacturer",JOptionPane.PLAIN_MESSAGE);
        } else {
        JOptionPane.showMessageDialog(frame,"Manufacturer could not be added","Error", JOptionPane.WARNING_MESSAGE);
        }
	  }
      if (!yesOrNo("Add more Manufacturers?")) {
        break;
      }
    } while (true);
  }

  public void addSupplier(){
	do {
	    String Mid = JOptionPane.showInputDialog("Please enter manufacturer ID you want to add into supplierList");
		Manufacturer Mresult = warehouse.searchManufacturer(Mid);
	    if(Mresult != null){
		   String Pid = JOptionPane.showInputDialog("Please enter the product ID");
		   Product Presult = warehouse.searchProduct(Pid);
		   if(Presult != null){
			  String supplierPrice = JOptionPane.showInputDialog("Please enter the supplier price!");
			  if(supplierPrice == null || supplierPrice.equals("")){JOptionPane.showMessageDialog(frame,"price cannot be empty");}
			  else{
				Double price = Double.parseDouble(supplierPrice);
				if(warehouse.makeSupplier(Mresult,Presult,price))
					JOptionPane.showMessageDialog(frame,"Adding successful!");
			  }
	       }
		   else
			  JOptionPane.showMessageDialog(frame,"Cannot find this product ID in invetory!","Error",JOptionPane.ERROR_MESSAGE);
	   }
		else
		  JOptionPane.showMessageDialog(frame,"Cannot find this ID in manufacturer List!");

	 if (!yesOrNo("Add more Manufacturers?")) {
        break;
	 }
	}while(true);
  }

  public void deleteSupplier(){
    do{
		String Mid = JOptionPane.showInputDialog("Please enter manufacturer ID you want to delete from supplierList");
		if(Mid == null || Mid.equals("")){JOptionPane.showMessageDialog(frame,"Blank cannot be empty!","Error",JOptionPane.ERROR_MESSAGE);}
		else{
			Manufacturer Mresult = warehouse.searchManufacturer(Mid);
			if(Mresult != null){
				String Pid = JOptionPane.showInputDialog("Please enter product ID for this supplier");
				if(Pid == null || Pid.equals("")){JOptionPane.showMessageDialog(frame,"Blank cannot be empty!","Error",JOptionPane.ERROR_MESSAGE);}
				else{
					Product Presult = warehouse.searchProduct(Pid);
					if(Presult != null){
						if(warehouse.deleteManufacturer(Mresult,Presult))
							JOptionPane.showMessageDialog(frame,"Delete successful!");
					    else
							JOptionPane.showMessageDialog(frame,"this manufacturer is not in supplierList!","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		  else
			JOptionPane.showMessageDialog(frame,"Cannot find this ID in Manufacturer List!","Warning",JOptionPane.WARNING_MESSAGE);
		}		
		if (!yesOrNo("delete more Manufacturers?")) break;
	}while(true);

  }

  public void showManufacturers(){
      Iterator allManufacturers = warehouse.getManufacturers();
	  String message ="";
      while (allManufacturers.hasNext()){
		  Manufacturer manufacturer = (Manufacturer)(allManufacturers.next());
          message = message + manufacturer.toString()+"\n";
	  }
	  JOptionPane.showMessageDialog(frame,message,"Manufacturer List", JOptionPane.PLAIN_MESSAGE);
  }

  public void listOManufacturer(){
    String ProductID = JOptionPane.showInputDialog("Please intput the productID you want to see the outstanding manufacturer orders");
	Product result = warehouse.searchProduct(ProductID);
	String message="";
	if(result != null){
	   Iterator allList = warehouse.listOutManufacturerOrders(result);
	   for(;allList.hasNext();){
	      OrderS orderS = (OrderS)(allList.next());
		  if(!orderS.IsReceived())
			message = message + orderS.toString()+"\n";
	   }
	   JOptionPane.showMessageDialog(frame,message,"Outstanding Manufacturer List", JOptionPane.PLAIN_MESSAGE);
	}
	else
	JOptionPane.showMessageDialog(frame,"Can not find this product!","Error",JOptionPane.ERROR_MESSAGE);
 }
 
  public void run() {
   JLabel label = new JLabel();
   frame = WareHouseContext.instance().getFrame();
   frame.getContentPane().removeAll();
   frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER,100,50));
   frame.getContentPane().add(menuButton);
   frame.getContentPane().add(this.salespersonButton);
   frame.getContentPane().add(this.logoutButton);
   frame.setVisible(true);
   frame.paint(frame.getGraphics());	
  }
}
