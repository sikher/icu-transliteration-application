package googletranslate;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReadConfig
{
  Config conf = new Config();
  
  public void readXml(String paramString)
  {
    System.out.println("path = " + new File("."));
    try
    {
      DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
      Document localDocument = localDocumentBuilder.parse(new File(paramString));
      localDocument.getDocumentElement().normalize();
      NodeList localNodeList1 = localDocument.getElementsByTagName("languages");
      for (int i = 0; i < localNodeList1.getLength(); i++)
      {
        Node localNode1 = localNodeList1.item(i);
        if (localNode1.getNodeType() == 1)
        {
          Element localElement1 = (Element)localNode1;
          NodeList localNodeList2 = localElement1.getElementsByTagName("l");
          for (int j = 0; j < localNodeList2.getLength(); j++)
          {
            Node localNode2 = localNodeList2.item(j);
            if (localNode2.getNodeType() == 1)
            {
              Element localElement2 = (Element)localNode2;
              NodeList localNodeList3 = localElement2.getChildNodes();
              String str = localNodeList3.item(0).getNodeValue().trim();
              this.conf.languages.add(str);
            }
          }
        }
      }
    }
    catch (SAXParseException localSAXParseException)
    {
      System.out.println("** Parsing error, line " + localSAXParseException.getLineNumber() + ", uri " + localSAXParseException.getSystemId());
      System.out.println(" " + localSAXParseException.getMessage());
    }
    catch (SAXException localSAXException)
    {
      localSAXException.printStackTrace();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private String getString(Element paramElement, String paramString)
  {
    NodeList localNodeList1 = paramElement.getElementsByTagName(paramString);
    Element localElement = (Element)localNodeList1.item(0);
    NodeList localNodeList2 = localElement.getChildNodes();
    String str = localNodeList2.item(0).getNodeValue().trim();
    return str;
  }
  
  private int getInt(Element paramElement, String paramString)
  {
    return Integer.parseInt(getString(paramElement, paramString));
  }
  
  public Config getConfig()
  {
    return this.conf;
  }
  
  public static void main(String[] paramArrayOfString)
  {
    ReadConfig localReadConfig = new ReadConfig();
    System.out.println("path = " + new File("").getAbsolutePath());
    localReadConfig.readXml("config.xml");
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/ReadConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */