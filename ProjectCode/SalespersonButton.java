import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class SalespersonButton extends JButton implements ActionListener{ 
  private static SalespersonButton instance;
  private String S_Pass = "salesclerk";
  private String M_Pass = "manager";
  private JFrame frame;
  
  private SalespersonButton() {
      super("SalesClerk");
  }

  public static SalespersonButton instance() {
    if (instance == null) {
      instance = new SalespersonButton();
    }
    return instance;
  }

  public void setListener(){
    this.addActionListener(this);
  }

  public void actionPerformed(ActionEvent event) {
  
    String Password = JOptionPane.showInputDialog(
                     frame,"Please input the password for salesclerk operations: ");
    if (S_Pass.equals(Password)){
      (WareHouseContext.instance()).setLogin(WareHouseContext.IsSalesperson);
       Loginstate.instance().clear();
      (WareHouseContext.instance()).changeState(1);
    }
    else if(M_Pass.equals(Password)){
         Loginstate.instance().clear();
        (WareHouseContext.instance()).changeState(1);
    }
    else 
      JOptionPane.showMessageDialog(frame,"Invalid password!");
  }

}
