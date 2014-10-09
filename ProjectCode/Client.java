import java.util.*;
import java.io.*;
public class Client implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String address;
  private String phone;
  private String id;
  private double balance;
  private static final String Client_STRING = "C";
 
  private List transactions = new LinkedList();
  
  public  Client (String name, String address, String phone) {
    this.name = name;
    this.address = address;
    this.phone = phone;
    id = Client_STRING + (ClientIdServer.instance()).getId();
    balance = 0.0;
  }

  public String getName() {
    return name;
  }
  public String getPhone() {
    return phone;
  }
  public String getAddress() {
    return address;
  }
  public String getID() {
    return id;
  }
  public double getBalance(){
    return balance;
  }
  public Iterator getTransactions(){
     return transactions.iterator();
  }
  public boolean addPayment(double payment){
     balance += payment;
	 return true;
  }
  public void updateBalance(double cost){
	 balance -=cost;
  }
  public boolean addOrder(Order order){
    transactions.add(order);
	return true;
  }
  public void setName(String newName) {
    name = newName;
  }
  public void setAddress(String newAddress) {
    address = newAddress;
  }
  public void setPhone(String newPhone) {
    phone = newPhone;
  }
  public boolean equals(String id) {
    return this.id.equals(id);
  }
  public String toString() {
    String string = "Client name: " + name + " id: " + id +"\naddress: " + address + " phone: " + phone + "\nBalance: " + balance;
    return string;
  }
}
