import java.util.*;
import java.io.*;
import java.lang.*;
public class Waiting implements Serializable {
  private static final long serialVersionUID = 1L;
  private String id;
  private List products = new LinkedList();
  private double totalCost;
  private boolean fill = false;
  private static final String WAIT_STRING = "W";

  public Waiting(Order order){
    totalCost = 0.0;
    id = WAIT_STRING + order.getID();
  }
  public String getId() {
    return id;
  }
  public int getSize(){
    return products.size();
  }
  public double getTotalCost() {
   totalCost =0.0;
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
  
  public Iterator getProducts(){
	 return products.iterator();
  }
  public boolean insertProduct(Product product){
      Product product1 = new Product(product);
	  products.add(product);
	  return true;
  }
  public void removeProduct(String productID){ 
	for(Iterator iterator1 = products.iterator();iterator1.hasNext();){ 
		Product p = (Product)iterator1.next(); 
		if(p.equals(productID)){ 
			iterator1.remove(); 
			break; 
		}	
	} 
  }

  public void setFill(){
      fill = true;
  }
  public boolean IsFilled(){
     return fill;
  }
 public String showWaiting(){
	 String message = this.toString() + "\n"+"The products list of the Order are:\n";
     Iterator productlist = products.iterator();
	 while (productlist.hasNext()){
	   Product product = (Product)(productlist.next());
	   message = message + product.toString() + "\n";
     } 
	 return message;
 }

 public String toString() {
    String string = "Waiting ID: " + id + " TotalCost: "+ getTotalCost();
    return string;
 }
 
 }