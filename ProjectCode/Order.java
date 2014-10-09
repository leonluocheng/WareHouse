import java.util.*;
import java.io.*;
public class Order implements Serializable {
  private static final long serialVersionUID = 1L;
  private String clientID;
  private String invoiceID;
  private List products = new LinkedList();
  private double totalCost;
  private String id;
  private static final String Order_STRING = "O";
  
  public Order(String clientID) {
    this.clientID = clientID;
	this.totalCost =0.0;
    id = Order_STRING + (OrderIdServer.instance()).getId();
  }
  public String getclientID() {
    return clientID;
  }
  public String getinvoiceID() {
    return invoiceID;
  }
  public String getID() {
    return id;
  }
  public double getTotalCost(){
    return totalCost;
  }
  public Iterator getProductlist()
  {  
     return products.iterator();
  }

  public boolean addProduct(Product product)
  {
    //Product product1 = new Product(product);
    products.add(product);
	return true;
  }
  public void setTotalCost() {
      Iterator productlist = products.iterator();
      while (productlist.hasNext()){
	   Product product = (Product)(productlist.next());
       totalCost += product.getTotal();
      }
  }
  public boolean equals(String id) {
    return this.id.equals(id);
  }
  
  public String showOrder(){
	 String message = this.toString() + "\n"+"The products list of the Order are:\n";
     Iterator productlist = products.iterator();
	 while (productlist.hasNext()){
	   Product product = (Product)(productlist.next());
	   message = message + product.toString() + "\n";
     } 
	 return message;
  }
  public String toString() {
    String string = "Client ID: " + clientID + " Order Id: " + id + "\ntotalcost: " + totalCost;
    return string;
  }
}