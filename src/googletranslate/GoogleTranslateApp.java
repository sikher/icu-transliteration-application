package googletranslate;

import java.awt.Window;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

public class GoogleTranslateApp
  extends SingleFrameApplication
{
  protected void startup()
  {
    show(new GoogleTranslateView(this));
  }
  
  protected void configureWindow(Window paramWindow) {}
  
  public static GoogleTranslateApp getApplication()
  {
    return (GoogleTranslateApp)Application.getInstance(GoogleTranslateApp.class);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    launch(GoogleTranslateApp.class, paramArrayOfString);
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/GoogleTranslateApp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */