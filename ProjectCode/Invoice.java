import java.util.*;
import java.io.*;
import java.lang.*;
public class Invoice implements Serializable {
  private static final long serialVersionUID = 1L;
  private String id;
  private List products = new LinkedList();
  private double totalCost;
  private boolean shipped;
  private static final String INVOICE_STRING = "I";
  
  public Invoice(Waiting waiting){
    totalCost = 0.0;
    id = INVOICE_STRING + waiting.getId();
	shipped = true;   
  }
  
  public int getSize(){
    return products.size();
  }
  
  public Invoice(Order order){
    totalCost = 0.0;
    id = INVOICE_STRING + order.getID();
	shipped = false;
  }
  
  public String getId() {
    return id;
  }
  
  public Iterator getProductlist()
  {
     return products.iterator();
  }
  
  public double getTotalCost() {
   totalCost = 0.0;
   Iterator productlist = products.iterator();
   while (productlist.hasNext()){
	  Product product = (Product)(productlist.next());
      totalCost += product.getTotal();
   }
   return totalCost;
  }
  
  public boolean equals(String id) {
    return this.id.equals(id);
  }
  
  public boolean insertProduct(Product product){
      Product product1 = new Product(product);
   	  products.add(product1);
	  return true;
  }
  
  public void setshipped() {
    shipped = true;
  }
  
 public String showInvoice(){
	 String message = this.toString() + "\n"+"The products list of the Order are:\n";
     Iterator productlist = products.iterator();
	 while (productlist.hasNext()){
	   Product product = (Product)(productlist.next());
	   message = message + product.toString() + "\n";
     } 
	 return message;
 }

 public String toString() {
    String string = "Invoice ID: " + id + " TotalCost: "+ getTotalCost() +" Is shipped? " + shipped;
    return string;
 }
}