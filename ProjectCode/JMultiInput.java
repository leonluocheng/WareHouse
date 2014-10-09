import javax.swing.*;

public class JMultiInput {
      private JTextField xField = new JTextField(10);
	  private JTextField yField = new JTextField(20);
	  private JTextField zField = new JTextField(10);
	  private JTextField wField = new JTextField(10);
	  private JPanel myPanel = new JPanel();
	  public JMultiInput(String a,String b){
		   myPanel.add(new JLabel(a));
		   myPanel.add( xField);
		   myPanel.add(Box.createHorizontalStrut(15));
		   myPanel.add(new JLabel(b));
		   myPanel.add( yField);
	  }
	  public JMultiInput(String a,String b,String c){
		   myPanel.add(new JLabel(a));
		   myPanel.add( xField);
		   myPanel.add(Box.createHorizontalStrut(15));
		   myPanel.add(new JLabel(b));
		   myPanel.add( yField);
		   myPanel.add(Box.createHorizontalStrut(15));
		   myPanel.add(new JLabel(c));
		   myPanel.add( zField);
	  }
	  public JMultiInput(String a,String b,String c,String d){
		   myPanel.add(new JLabel(a));
		   myPanel.add( xField);
		   myPanel.add(Box.createHorizontalStrut(15));
		   myPanel.add(new JLabel(b));
		   myPanel.add( yField);
		   myPanel.add(Box.createHorizontalStrut(15));
		   myPanel.add(new JLabel(c));
		   myPanel.add( zField);
		   myPanel.add(Box.createHorizontalStrut(15));
		   myPanel.add(new JLabel(d));
		   myPanel.add( wField);
	  }
	  public int result(String title){
		   return JOptionPane.showConfirmDialog(null, myPanel, 
               title, JOptionPane.OK_CANCEL_OPTION);
	  }
	  public String getFirstText(){return xField.getText();}
	  public String getSecondText(){return yField.getText();}
	  public String getThirdText(){return zField.getText();}
	  public String getFourthText(){return wField.getText();}
}