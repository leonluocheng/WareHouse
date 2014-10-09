import java.util.*;
import java.lang.*;
import java.io.*;
public class Manufacturer implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String address;
  private String id;
  private List suppliers = new LinkedList();

  public Manufacturer(String name, String address, String id) {
    this.name = name;
    this.address = address;
    this.id = id;
  }
  public boolean insertSupplier(Supplier supplier){
	 suppliers.add(supplier);
	 return true;
  }
  public Iterator getSupplier(){
     return suppliers.iterator();
  }
  public String getaddress() {
    return address;
  }
  public String getname() {
    return name;
  }
  public String getId() {
    return id;
  }
  public boolean equals(String id) {
    return this.id.equals(id);
  }
  public String toString() {
      return "name " + name + " address " + address + " id " + id;
  }
}
