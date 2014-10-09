/**
@author Cheng Luo
*/
import java.util.*;
import java.io.*;
public class OrderS implements Serializable {
  private static final long serialVersionUID = 1L;
  private String productID;
  private String id;
  private String manufacturerName;
  private int quantity;
  private boolean received = false;
  
  public OrderS(String manufacturerName, String productID, String orderID, int quantity) {
	this.manufacturerName = manufacturerName;
    this.productID = productID;
	this.id = orderID;
	this.quantity = quantity;
  }
  
  public boolean IsReceived(){
     return received;
  }  
  public void setReceived(){
     received = true;
  }
  public String getID() {
    return id;
  }
  public String getName(){
    return manufacturerName;
  }
  public String getProductID(){
    return productID;
  }
  public int getQuantity(){
    return quantity;
  }

  public boolean equals(String id) {
    return this.id.equals(id);
  }
  
  public String toString() {
    String string = "Manufacturer_Name: " + manufacturerName + " Order Id: " + id + " Product ID "+ productID + " Quantity "+ quantity + " Is received? " + received;
    return string;
  }
}