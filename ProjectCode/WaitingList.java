import java.util.*;
import java.io.*;
public class WaitingList implements Serializable {
  private static final long serialVersionUID = 1L;
  private List waitings = new LinkedList();
  private static WaitingList waitingList;
  private WaitingList() {
  }
  public static WaitingList instance() {
    if (waitingList == null) {
      return (waitingList = new WaitingList());
    } else {
      return waitingList;
    }
  }
  
  public boolean insertWaiting(Waiting waiting) {
    waitings.add(waiting);
    return true;
  }

  public Iterator getWaitings(){
     return waitings.iterator();
  }
  
  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(waitingList);
    } catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      if (waitingList != null) {
        return;
      } else {
        input.defaultReadObject();
        if (waitingList == null) {
          waitingList = (WaitingList) input.readObject();
        } else {
          input.readObject();
        }
      }
    } catch(IOException ioe) {
      ioe.printStackTrace();
    } catch(ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
  }
  public String toString() {
    return waitings.toString();
  }
}