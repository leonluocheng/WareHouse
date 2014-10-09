/*
   Cheng Luo
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class Loginstate extends WareHouseState implements ActionListener{
  private static final int MANAGER_LOGIN = 0;
  private static final int SALESPERSON_LOGIN = 1;
  private static final int CLIENT_LOGIN =2;
  private static final int EXIT = 3;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
  private WareHouseContext context;
  private JFrame frame;
  private AbstractButton clientButton, salespersonButton, managerButton, logoutButton;
  private static Loginstate instance;
  private String M_Pass = "manager";

  private static int x =400 , y = 400;
  
  private Loginstate() {
      super();
	  clientButton = new JButton("Client");
	  clientButton.setPreferredSize(new Dimension(100,30));
      salespersonButton = SalespersonButton.instance(); 
	  salespersonButton.setPreferredSize(new Dimension(100,30));
	  managerButton = ManagerButton.instance(); 
	  managerButton.setPreferredSize(new Dimension(100,30));
      logoutButton = new JButton("Logout");
	  logoutButton.setPreferredSize(new Dimension(100,30));
      clientButton.addActionListener(this);
      logoutButton.addActionListener(this);
      SalespersonButton.instance().setListener();
	  ManagerButton.instance().setListener();
      // context = WareHouseContext.instance();
  }

  public static Loginstate instance() {
    if (instance == null) {
      instance = new Loginstate();
    }
    return instance;
  }

  public void actionPerformed(ActionEvent event) {
	if (event.getSource().equals(this.clientButton)) { 
       this.Client();
	 }
    else if (event.getSource().equals(this.logoutButton)) 
       (WareHouseContext.instance()).changeState(3);
  } 
  
  public void clear(){ 
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());   
  }  
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" ));
        if (value <= EXIT && value >= MANAGER_LOGIN) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
 
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  private void Client(){
    String clientID = JOptionPane.showInputDialog(
                     frame,"Please input the client id: ");
    if (Warehouse.instance().searchClient(clientID)){
      (WareHouseContext.instance()).setLogin(WareHouseContext.IsClient);
      (WareHouseContext.instance()).setClient(clientID);  
       clear();
      (WareHouseContext.instance()).changeState(2);
    }
    else 
      JOptionPane.showMessageDialog(frame,"Invalid client id.");
  } 
/**
  public void process() {
    int command;
	System.out.println("Welcome to use WareHouse system!");
    System.out.println("0.login as manager\n"+ 
                        "1.login as salesperson\n" +
                        "2.login as client\n"+
						"3.exit the system\n");     
    while ((command = getCommand()) != EXIT) {

      switch (command) {
        case MANAGER_LOGIN:     Manager();
                                break;
        case SALESPERSON_LOGIN: Salesperson();
                                break;
		case CLIENT_LOGIN:      Client();
		                        break;
        default:                System.out.println("Invalid choice");
                                
      }
	System.out.println("Welcome to use WareHouse system!");
    System.out.println("0.login as manager\n"+ 
                        "1.login as salesperson\n" +
                        "2.login as client\n"+
						"3.exit the system\n"); 
    }
    (WareHouseContext.instance()).changeState(3);
  }
**/

  public void run() {
   //process();
   frame = WareHouseContext.instance().getFrame();
   frame.getContentPane().removeAll();
   frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER,100,50));
   frame.getContentPane().add(this.managerButton);
   frame.getContentPane().add(this.salespersonButton);
   frame.getContentPane().add(this.clientButton);
   frame.getContentPane().add(this.logoutButton);
   frame.setVisible(true);
   frame.paint(frame.getGraphics()); 
  }
}
