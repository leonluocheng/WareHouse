/**
@author Cheng Luo
*/
import java.util.*;
import java.io.*;
public class Warehouse implements Serializable {
  private static final long serialVersionUID = 1L;
  private ProductList productList;
  private ClientList clientList;
  private ManufacturerList manufacturerList;
  private OrderList orderList;
  private WaitingList waitingList;
  private InvoiceList invoiceList;
  private OrderSList orderSList;
  private static Warehouse warehouse;
  
  private Warehouse() {
    productList = ProductList.instance();
    clientList = ClientList.instance();
	manufacturerList = ManufacturerList.instance();
	orderList = OrderList.instance();
	waitingList = WaitingList.instance();
	invoiceList = InvoiceList.instance();
	orderSList = OrderSList.instance();
  }
  
  public static Warehouse instance() {
    if (warehouse == null) {
      ClientIdServer.instance(); // instantiate all singletons
      OrderIdServer.instance();
	  return (warehouse = new Warehouse());
    } else {
      return warehouse;
    }
  }
  public Product addProduct(String name,String id, double price, int quantity) {
    Product product = new Product(name,id, price, quantity);
    if (productList.insertProduct(product)) {
      return (product);
    }
    return null;
  }
  public Client addClient(String name, String address, String phone) {
    Client client = new Client(name, address, phone);
    if (clientList.insertClient(client)) {
      return (client);
    }
    return null;
  }
  public Manufacturer addManufacturer(String name, String address, String id) {
    Manufacturer manufacturer = new Manufacturer(name, address, id);
    if (manufacturerList.insertManufacturer(manufacturer)) {
      return (manufacturer);
    }
    return null;
  }
  
  public Order makeOrder(String clientID){
	Order order = new Order(clientID);
	orderList.insertOrder(order);
	return order;
  }

  public boolean makeSupplier(Manufacturer Mresult,Product Presult,double supplierPrice){
	 Supplier supplier = new Supplier(Mresult, Presult, supplierPrice);
	 Mresult.insertSupplier(supplier);
	 Presult.insertSupplier(supplier);
	 return true;
  }
  
 public String showInvoicesClient(String clientID){
     Client client = searchClientObj(clientID);
	 String str = "";
	 String invoiceID="";
     Order order = null;
	 Invoice invoice = null;
	for(Iterator iter = client.getTransactions();iter.hasNext();){
	  order = (Order)iter.next();
	  invoiceID = "I"+order.getID();
	  for(Iterator it = invoiceList.getInvoices();it.hasNext();){
	    invoice = (Invoice)it.next();
		if(invoice.getId().equals(invoiceID))
		   str = str + invoice.showInvoice()+"\n";
	  }
	}
	return str;
  }
  
  public boolean deleteManufacturer(Manufacturer Mresult, Product Presult){
      Iterator suppliers1 = Mresult.getSupplier();
	  Iterator suppliers2 = Presult.getSupplier();  
	  boolean r1 = false;
	  boolean r2 = false;
	   for(;suppliers1.hasNext();){
	    Supplier supplier1 = (Supplier)(suppliers1.next());
		if(supplier1.equals(Mresult,Presult)){
		   suppliers1.remove();
		   r1 = true;
		}
	  }
	  
	  for(;suppliers2.hasNext();){
	    Supplier supplier2 = (Supplier)(suppliers2.next());
		if(supplier2.equals(Mresult,Presult)){
		   suppliers2.remove();
		   r2 = true;
		}
	  }
	  
	  if(r1||r2)return true;
	  else return false;
  }
  
  public Iterator listManufacturerAndPrice(Product Presult){
     return Presult.getSupplier();
  }

  public int getWaitquantity(Waiting waiting, String Pid){
       waiting.setFill();
	   Iterator products = waiting.getProducts();
	   int quantity = 0;
	   for(;products.hasNext();){
	     Product product = (Product)(products.next());
		 if(product.equals(Pid)){
			quantity = product.getQuantity();
			break;
		 }
	   }
	   return quantity;
  }
  
  public void WaitToInvoice(Waiting waiting, String Pid){
    Invoice invoice = new Invoice(waiting);
	for(Iterator products = waiting.getProducts(); products.hasNext();){
	   Product product = (Product)(products.next());
	   if(product.equals(Pid)){
	      invoice.insertProduct(product);
	      waiting.removeProduct(Pid);
	   }
	}
    invoiceList.insertInvoice(invoice);	
  }
  public boolean compareQuantity(int ProductQuantity, int quantity){
	if(ProductQuantity > quantity)
	  return true;
	else
	  return false;
  }
  
  public void makeOrderS(String name,String productID,String orderID,int quantity){
    OrderS orderS = new OrderS(name, productID, orderID, quantity);
	orderSList.insertOrderS(orderS);
    for(Iterator products = productList.getProducts(); products.hasNext();){
	   Product product = (Product)(products.next());
       if(product.equals(productID))
		  product.insertOrderS(orderS);
	}		
  }
  
  public Iterator listOutManufacturerOrders(Product product){
     return product.getOrderS();
  }
  
  public boolean addToOrder(Product product, Order order){
	order.addProduct(product);
	return true;
  }
  public void caculateOrderCost(Order order){
		order.setTotalCost();
  }
  public void orderIssued(Order order, String clientID){
	Iterator clients = clientList.getClients();
    while(clients.hasNext()){
       Client client = (Client)(clients.next());
	   if(client.equals(clientID)){
		   client.addOrder(order);
		   break;
	   }
    }
  }
  public double addPayment(double payment,String clientID){
	double result = 0.0;
	Iterator clients = clientList.getClients();
    while(clients.hasNext()){
       Client client = (Client)(clients.next());
	   if(client.equals(clientID)){
		  client.addPayment(payment);
		  result = client.getBalance();
		  break;
	   }
    } 
	return result;
  }
  public Invoice createInvoice(Order order){  	  
	  Invoice invoice = new Invoice(order);
	  Waiting waiting = new Waiting(order);  
	  for(Iterator productList1 = productList.getProducts(); productList1.hasNext();){
		Product product1 = (Product)(productList1.next());
		for(Iterator productList2 = order.getProductlist();productList2.hasNext();){
			Product product2 = (Product)(productList2.next());
			if(product1.equals(product2.getId())){
				if(product1.getQuantity() >= product2.getQuantity()){
                   //invoice = new Invoice(order);
				   invoice.insertProduct(product2);
				}				
				else{
                   if(product1.getQuantity()>0){
				      invoice.insertProduct(product1); 
				   }
				   //int quantity = product2.getQuantity() - product1.getQuantity();
				   Product product3 = new Product(product2);
				   product3.updateQuantity(-product1.getQuantity());
    			   //waiting = new Waiting(order);
				   waiting.insertProduct(product3);
				}
			}
		}
	  }
	  if(invoice.getSize() != 0)
		invoiceList.insertInvoice(invoice);
	  if(waiting.getSize() != 0)
		waitingList.insertWaiting(waiting);
	  
	  return invoice;
  }
  
  public boolean setShipped(Invoice invoice){
	 invoice.setshipped();
     return true;	
  }
  
  public boolean updateBalance(Order order, String clientID){
	Iterator clients = clientList.getClients();
    while(clients.hasNext()){
       Client client = (Client)(clients.next());
	   if(client.equals(clientID)){
		  client.updateBalance(order.getTotalCost());
		  break;
		}
	}
	return true;
  }
  
  public boolean updateInventory(Invoice invoice){
	 boolean result = false;
	 for(Iterator productList1 = invoice.getProductlist();productList1.hasNext();){
		Product product1 = (Product)(productList1.next());
		for( Iterator productList2 = productList.getProducts();productList2.hasNext();){
			Product product2 = (Product)(productList2.next());
			if(product1.equals(product2.getId())){
				product2.updateQuantity(-product1.getQuantity());
				result = true;
			}
		}
	  }
	  return result;
  }
 
  public Order searchOrder(String orderID){
    Iterator orders = orderList.getOrders();
	Order result = null;
    while(orders.hasNext()){
       Order order = (Order)(orders.next());
	   if(order.equals(orderID)){
	     return order;
		 }
    }
	return result;
  }
  
  public Product searchProduct(String id){
    Iterator products = productList.getProducts();
	Product result = null;
	while(products.hasNext()){
		Product product = (Product)(products.next());
		if(product.equals(id)){
			return product;
		}
	}
	return result;
  }
  
  public void receivedOrder(String OSid){
		Iterator allSupplierOrders = orderSList.getOrders();
		for(;allSupplierOrders.hasNext();){
			OrderS order = (OrderS)(allSupplierOrders.next());
			if(order.equals(OSid)){
				order.setReceived();
			}
		}
  }
  
  public Manufacturer searchManufacturer(String id){
     Iterator manufacturers = manufacturerList.getManufacturers();
	 Manufacturer result = null;
	 while(manufacturers.hasNext()){
     Manufacturer manufacturer = (Manufacturer)(manufacturers.next());
	   if(manufacturer.equals(id)){
		  return manufacturer;
		 }
    }
	return result;
  }
  
  public boolean searchClient(String clientID){
    Iterator clients = clientList.getClients();
	boolean result = false;
    while(clients.hasNext()){
       Client client = (Client)(clients.next());
	   if(client.equals(clientID)){
	     result = true;
		 break;
		 }
	   else
	     result = false;
    }
	return result;
  }
  public Client searchClientObj(String clientID){
    Iterator clients = clientList.getClients();
    while(clients.hasNext()){
       Client client = (Client)(clients.next());
	   if(client.equals(clientID))
			return client;
    }
	return null;
  }
  
  public void modifyPrice(Product product, double price){
      product.setPrice(price);
  }
  
  public Iterator getOrder(){
      return orderList.getOrders();
  }
  public Iterator getInvoices(){
      return invoiceList.getInvoices();
  }
  public Iterator getWaitings(){
      return waitingList.getWaitings();
  }
  public Iterator getProducts() {
      return productList.getProducts();
  }
  public Iterator getClients() {
      return clientList.getClients();
  }
  public Iterator getManufacturers() {
      return manufacturerList.getManufacturers();
  }  
  public Iterator getTransaction(String clientID){
    boolean flag = false;
	//Iterator transaction = null;//Initialized
	Iterator clients = clientList.getClients();
	Client client = null;
    while(clients.hasNext()){
       client = (Client)(clients.next());
	   if(client.equals(clientID)){
	   	   flag = true;
		   break;
		//   transaction = client.getTransactions();   
	   }
	}  
 	if(flag)
	  return client.getTransactions();//return transaction;
	else
	  return null;
  }

  public static Warehouse retrieve() {
    try {
      FileInputStream file = new FileInputStream("WarehouseData");
      ObjectInputStream input = new ObjectInputStream(file);
      input.readObject();
      ClientIdServer.retrieve(input);
	  OrderIdServer.retrieve(input);
      return warehouse;
    } catch(IOException ioe) {
      ioe.printStackTrace();
      return null;
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
      return null;
    }
  }
  public static  boolean save() {
    try {
      FileOutputStream file = new FileOutputStream("WarehouseData");
      ObjectOutputStream output = new ObjectOutputStream(file);
      output.writeObject(warehouse);
      output.writeObject(ClientIdServer.instance());
	  output.writeObject(OrderIdServer.instance());
      return true;
    } catch(IOException ioe) {
      ioe.printStackTrace();
      return false;
    }
  }
  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(warehouse);
    } catch(IOException ioe) {
      System.out.println(ioe);
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      input.defaultReadObject();
      if (warehouse == null) {
        warehouse = (Warehouse) input.readObject();
      } else {
        input.readObject();
      }
    } catch(IOException ioe) {
      ioe.printStackTrace();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  public String toString() {
    return productList + "\n" + clientList;
  }
}
