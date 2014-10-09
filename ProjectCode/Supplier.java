import java.util.*;
import java.lang.*;
import java.io.*;
public class Supplier implements Serializable {
  private static final long serialVersionUID = 1L;
  private Manufacturer manufacturer;
  private Product product;
  private double price;

  public Supplier(Manufacturer manufacturer, Product product, double price) {
    this.manufacturer = manufacturer;
    this.price = price;
	this.product = product;
  }

  public double getprice() {
    return price;
  }
  public Manufacturer getmanufacturer() {
    return manufacturer;
  }
  public Product getProduct(){
    return product;
  }
  public boolean equals(Manufacturer manufacturer, Product product){
    return this.manufacturer.equals(manufacturer.getId()) && this.product.equals(product.getId());
  }
  public String toString(){
      return "manufacturer: " + manufacturer.toString() + "\nprice: " + price + "\nproduct: "+ product.toString();
  }
}
