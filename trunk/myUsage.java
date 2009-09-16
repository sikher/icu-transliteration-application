/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package googletranslate;

import info.monitorenter.cpdetector.io.*;
import java.io.File;
import java.io.IOException;

public class myUsage{
  // Create the proxy:
  CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance(); // A singleton.

  // constructor:
  public myUsage(){
    // Add the implementations of info.monitorenter.cpdetector.io.ICodepageDetector:
    // This one is quick if we deal with unicode codepages:
    detector.add(new ByteOrderMarkDetector());
    // The first instance delegated to tries to detect the meta charset attribut in html pages.
    detector.add(new ParsingDetector(true)); // be verbose about parsing.
    // This one does the tricks of exclusion and frequency detection, if first implementation is
    // unsuccessful:
    detector.add(JChardetFacade.getInstance()); // Another singleton.
    detector.add(ASCIIDetector.getInstance()); // Fallback, see javadoc.
  }
  public boolean someMethod(File document) throws IOException{
    boolean ret = false;
    // Work with the configured proxy:
    java.nio.charset.Charset charset = null;
    charset = detector.detectCodepage(document.toURL());
    if(charset == null){
      //project.forName("cpdetector").report("bogus document",document.toUrl());
    }
    else{
      // Open the document in the given code page:
      java.io.Reader reader = new java.io.InputStreamReader(new java.io.FileInputStream(document),charset);
      // Read from it, do sth., whatever you desire. The character are now - hopefully - correct..
      ret = true;
    }
    return ret;
  }

  public static void main(String[] args){
      myUsage m = new myUsage() ;
   //   m.someMethod(null);
  }
}