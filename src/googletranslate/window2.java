package googletranslate;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import javax.swing.JFrame;

public class window2
  extends JFrame
{
  public window2()
  {
    initComponents();
  }
  
  private void initComponents()
  {
    setDefaultCloseOperation(3);
    setName("Form");
    getContentPane().setLayout(new GridBagLayout());
    pack();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        new window2().setVisible(true);
      }
    });
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/window2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */