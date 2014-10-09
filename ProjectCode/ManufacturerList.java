import java.util.*;
import java.lang.*;
import java.io.*;
public class ManufacturerList implements Serializable {
  private static final long serialVersionUID = 1L;
  private List manufacturers = new LinkedList();
  private static ManufacturerList manufacturerlist; 
  private ManufacturerList() {
  }
  
  public static ManufacturerList instance() {
    if (manufacturerlist == null) {
      return (manufacturerlist = new ManufacturerList());
    } else {
      return manufacturerlist;
    }
  }
  public boolean insertManufacturer(Manufacturer manufacturer) {
    manufacturers.add(manufacturer);
    return true;
  }
  
  public Iterator getManufacturers() {
    return manufacturers.iterator();
  }
  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(manufacturerlist);
    } catch(IOException ioe) {
      System.out.println(ioe);
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      if (manufacturerlist != null) {
        return;
      } else {
        input.defaultReadObject();
        if (manufacturerlist == null) {
          manufacturerlist = (ManufacturerList) input.readObject();
        } else {
          input.readObject();
        }
      }
    } catch(IOException ioe) {
      System.out.println("in ManufacturerList readObject \n" + ioe);
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
  }
  public String toString() {
    return manufacturers.toString();
  }
}
