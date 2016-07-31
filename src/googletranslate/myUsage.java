package googletranslate;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class myUsage
{
  CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
  
  public myUsage()
  {
    this.detector.add(new ByteOrderMarkDetector());
    this.detector.add(new ParsingDetector(true));
    this.detector.add(JChardetFacade.getInstance());
    this.detector.add(ASCIIDetector.getInstance());
  }
  
  public boolean someMethod(File paramFile)
    throws IOException
  {
    boolean bool = false;
    Charset localCharset = null;
    localCharset = this.detector.detectCodepage(paramFile.toURL());
    if (localCharset != null)
    {
      InputStreamReader localInputStreamReader = new InputStreamReader(new FileInputStream(paramFile), localCharset);
      bool = true;
    }
    return bool;
  }
  
  public static void main(String[] paramArrayOfString)
  {
    myUsage localmyUsage = new myUsage();
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/myUsage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */