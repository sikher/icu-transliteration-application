package googletranslate;

import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import org.jdesktop.application.Action;

public class GoogleTranslateAboutBox
  extends JDialog
{
  private JButton closeButton;
  
  public GoogleTranslateAboutBox(Frame paramFrame)
  {
    super(paramFrame);
    initComponents();
    getRootPane().setDefaultButton(this.closeButton);
  }
  
  @Action
  public void closeAboutBox()
  {
    dispose();
  }
  
  private void initComponents() {}
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/GoogleTranslateAboutBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */