import java.util.*;
import java.lang.*;
import java.io.*;
public class Product implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  //private String manufacturers;
  private String id;
  private double price;
  private int quantity;
  private double total;
  private List suppliers = new LinkedList();
  private List orders = new LinkedList();
  
  public Product(Product product){
   this.name = product.name;
   this.id = product.id;
   this.price = product.price;
   this.quantity = product.quantity;
  }
  public Product(String name, String id, double price, int quantity) {
    this.name = name;
    this.id = id;
	this.price = price;
	this.quantity = quantity;
  }
  
  public boolean insertOrderS(OrderS order){
    orders.add(order);
	return true;
  }
  public Iterator getOrderS(){
    return orders.iterator();
  }
  public boolean insertSupplier(Supplier supplier){
	 suppliers.add(supplier);
	 return true;
  }
  public Iterator getSupplier(){
     return suppliers.iterator();
  }
  public String getname() {
    return name;
  }
  public String getId() {
    return id;
  }
  
  public void setPrice(double price){
    this.price = price;
  }
  
  public double getPrice(){
    return price;
  }
  public int getQuantity(){
    return quantity;
  }
  public void setQuantity(int q){
     this.quantity = q;
  }
  public double getTotal(){
   total = quantity*price;
   return total;
  }
  public void updateQuantity(int quantity){
     this.quantity += quantity;
  }
  public boolean equals(String id) {
    return this.id.equals(id);
  }
  public String toString() {
      return "Product name: " + name + "  |  ID: " + id + "  |  SalePrice: " + price + "  |  Quantity: " + quantity;
  }
}
