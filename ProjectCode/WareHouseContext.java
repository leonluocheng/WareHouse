import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class WareHouseContext {
  
  private int currentState;
  private static Warehouse warehouse;
  private static WareHouseContext context;
  private int currentUser;
  private String ClientID;
  private static JFrame WareFrame; 
  private static JFrame frame;
  private static int x =300 , y = 400;
  private BufferedReader reader = new BufferedReader(new 
                                      InputStreamReader(System.in));
 
  public static final int IsManager = 0;
  public static final int IsSalesperson = 1;
  public static final int IsClient = 2;
  private static WareHouseState[] states;
  private int[][] nextState;

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
    /*String more = JOptionPane.showInputDialog(prompt + " (Y|y)[es] or anything else for no");
    if(more == null ) return false;
	if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;*/
	int n = JOptionPane.showConfirmDialog(frame,prompt,"Warehouse database",JOptionPane.YES_NO_OPTION);
	if(n == 0)return true;
	else return false;
  }

  private void retrieve() {
    try {
      Warehouse tempwarehouse = Warehouse.retrieve();
      if (tempwarehouse != null) {
        JOptionPane.showMessageDialog(frame, " The warehouse has been successfully retrieved from the file warehouseData" );
        warehouse = tempwarehouse;
      } else {
        JOptionPane.showMessageDialog(frame, "File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }

  public void setLogin(int code)
  {currentUser = code;}

  public void setClient(String cID)
  { ClientID = cID;}

  public int getLogin()
  { return currentUser;}

  public String getClient()
  { return ClientID;}
  
  public JFrame getFrame()
  { return WareFrame;}
  public void setFrame(String word)
  {
    WareFrame = new JFrame(word);
    WareFrame.setSize(x,y);
    WareFrame.setLocation(x, y);
  }

  private WareHouseContext() { //constructor
    if (yesOrNo("Look for saved data and  use it?")) {
      retrieve();
    } else {
      warehouse = Warehouse.instance();
    }
    // set up the FSM and transition table;
    states = new WareHouseState[4];
    states[0] = Managerstate.instance();
    states[1] = Salespersonstate.instance(); 
    states[2] = Clientstate.instance();
	states[3] = Loginstate.instance();
    nextState = new int[4][4];
	//********************************************************************************
    nextState[0][0] = 0;nextState[0][1] = 1;nextState[0][2] = -2; nextState[0][3] = 3;
    nextState[1][0] = 0;nextState[1][1] = 1;nextState[1][2] = 2; nextState[1][3] = 3;
    nextState[2][0] = -2;nextState[2][1] = 1;nextState[2][2] = 3; nextState[2][3] = 3;
	nextState[3][0] = 0;nextState[3][1] = 1;nextState[3][2] = 2; nextState[3][3] = -1;
	//***********************************************************************************/
    currentState = 3;
	WareFrame = new JFrame("WareHouse System");
    WareFrame.setSize(x,y);
    WareFrame.setLocation(x, y);

  }

  public void changeState(int transition)
  {
    //System.out.println("current state " + currentState + " \n \n ");
    currentState = nextState[currentState][transition];
    if (currentState == -2) 
      {System.out.println("Error has occurred"); terminate();}
    if (currentState == -1) 
      terminate();
    //System.out.println("current state " + currentState + " \n \n ");
    states[currentState].run();
  }

  private void terminate()
  {
   if (yesOrNo("Save data?")) {
      if (warehouse.save()) {
         JOptionPane.showMessageDialog(frame, " The library has been successfully saved in the file LibraryData!" );
       } else {
         JOptionPane.showMessageDialog(frame, " There has been an error in saving!" );
       }
     }
   //System.out.println(" Goodbye \n "); 
   System.exit(0);
  }

  public static WareHouseContext instance() {
    if (context == null) {
      context = new WareHouseContext();
    }
    return context;
  }

  public void process(){
    states[currentState].run();
  }
  
  public static void main (String[] args){
    WareHouseContext.instance().process(); 
	//states[currentState].run();
  }
}
