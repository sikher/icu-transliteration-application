/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package googletranslate;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;

import org.w3c.dom.*;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class ReadConfig {
    Config conf = new Config() ;

    public void readXml (String path){
        System.out.println("path = "+new File(".")) ;
    	try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document gurmukhiXml = docBuilder.parse (new File(path));
            gurmukhiXml.getDocumentElement ().normalize ();
            //readng the config
            NodeList listOfViews = gurmukhiXml.getElementsByTagName("languages");
            for(int iView = 0 ; iView < listOfViews.getLength() ; iView++){
                Node keybNode = listOfViews.item(iView);
                if(  keybNode.getNodeType() == Node.ELEMENT_NODE){
                Element keybElement = (Element)keybNode;
                NodeList keybList = keybElement.getElementsByTagName("l");
              //  System.out.println(" keybList.getLength() = "+ keybList.getLength());
                for(int lView = 0 ; lView <  keybList.getLength() ; lView++){
                  //  System.out.println("lview = "+lView) ;
                    Node langNode = keybList.item(lView);
                     if(  langNode.getNodeType() == Node.ELEMENT_NODE){
                        Element langElement = (Element)langNode;
                        NodeList list = langElement.getChildNodes();
                        String ret = ((Node)list.item(0)).getNodeValue().trim() ;
                     //   System.out.println(ret) ;
                        conf.languages.add(ret);
                     }
                }
               // System.out.println(getString(keybElement, "textAreaFont"));

                }
            }

    	}catch (SAXParseException err) {
            System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
            System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
            e.printStackTrace ();

        }catch (Throwable t) {
            t.printStackTrace ();
        }

    }

    private String getString(Element el, String what){
    	NodeList nodeList = el.getElementsByTagName(what);
        Element element = (Element)nodeList.item(0);
        NodeList list = element.getChildNodes();
//      System.out.println("moveDown : " + ((Node)moveDownGEList.item(0)).getNodeValue().trim());
        String ret = ((Node)list.item(0)).getNodeValue().trim() ;
    //    System.out.println(what +" = " + ret);
    	return ret ;
    }
    private int getInt(Element el, String what){
    	return Integer.parseInt(getString(el,what));
    }
    public Config getConfig(){
        return conf ;
    }


	public static void main(String[] args){
		ReadConfig kcr = new ReadConfig() ;
                System.out.println("path = "+new File("").getAbsolutePath()) ;
                kcr.readXml("config.xml");

	}

}
