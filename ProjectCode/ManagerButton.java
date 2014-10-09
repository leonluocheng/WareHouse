import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class ManagerButton extends JButton implements ActionListener{ 
  private static ManagerButton instance;
  private String M_Pass = "manager";
  private JFrame frame;
  private ManagerButton() {
      super("Manager");
  }

  public static ManagerButton instance() {
    if (instance == null) {
      instance = new ManagerButton();
    }
    return instance;
  }

  public void setListener(){
    this.addActionListener(this);
  }

  public void actionPerformed(ActionEvent event) {
   String ManagerPassword = JOptionPane.showInputDialog(
                     frame,"Please input the password for manager: ");
    if (M_Pass.equals(ManagerPassword)){
      (WareHouseContext.instance()).setLogin(WareHouseContext.IsManager);
       Loginstate.instance().clear();
      (WareHouseContext.instance()).changeState(0);
	  
    }
	
    else 
      JOptionPane.showMessageDialog(frame,"Invalid password!");
   
  } 
}