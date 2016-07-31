package googletranslate;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.filechooser.FileFilter;

class ExampleFileFilter
  extends FileFilter
{
  private static String TYPE_UNKNOWN = "Type Unknown";
  private static String HIDDEN_FILE = "Hidden File";
  private Hashtable filters = null;
  private String description = null;
  private String fullDescription = null;
  private boolean useExtensionsInDescription = true;
  
  public ExampleFileFilter() {}
  
  public ExampleFileFilter(String paramString)
  {
    this(paramString, null);
  }
  
  public ExampleFileFilter(String paramString1, String paramString2)
  {
    this();
    if (paramString1 != null) {
      addExtension(paramString1);
    }
    if (paramString2 != null) {
      setDescription(paramString2);
    }
  }
  
  public ExampleFileFilter(String[] paramArrayOfString)
  {
    this(paramArrayOfString, null);
  }
  
  public ExampleFileFilter(String[] paramArrayOfString, String paramString)
  {
    this();
    for (int i = 0; i < paramArrayOfString.length; i++) {
      addExtension(paramArrayOfString[i]);
    }
    if (paramString != null) {
      setDescription(paramString);
    }
  }
  
  public boolean accept(File paramFile)
  {
    if (paramFile != null)
    {
      if (paramFile.isDirectory()) {
        return true;
      }
      String str = getExtension(paramFile);
      if ((str != null) && (this.filters.get(getExtension(paramFile)) != null)) {
        return true;
      }
    }
    return false;
  }
  
  public String getExtension(File paramFile)
  {
    if (paramFile != null)
    {
      String str = paramFile.getName();
      int i = str.lastIndexOf('.');
      if ((i > 0) && (i < str.length() - 1)) {
        return str.substring(i + 1).toLowerCase();
      }
    }
    return null;
  }
  
  public void addExtension(String paramString)
  {
    if (this.filters == null) {
      this.filters = new Hashtable(5);
    }
    this.filters.put(paramString.toLowerCase(), this);
    this.fullDescription = null;
  }
  
  public String getDescription()
  {
    if (this.fullDescription == null) {
      if ((this.description == null) || (isExtensionListInDescription()))
      {
        this.fullDescription = (this.description + " (");
        Enumeration localEnumeration = this.filters.keys();
        if (localEnumeration != null) {
          for (this.fullDescription = (this.fullDescription + "." + (String)localEnumeration.nextElement()); localEnumeration.hasMoreElements(); this.fullDescription = (this.fullDescription + ", " + (String)localEnumeration.nextElement())) {}
        }
        this.fullDescription += ")";
      }
      else
      {
        this.fullDescription = this.description;
      }
    }
    return this.fullDescription;
  }
  
  public void setDescription(String paramString)
  {
    this.description = paramString;
    this.fullDescription = null;
  }
  
  public void setExtensionListInDescription(boolean paramBoolean)
  {
    this.useExtensionsInDescription = paramBoolean;
    this.fullDescription = null;
  }
  
  public boolean isExtensionListInDescription()
  {
    return this.useExtensionsInDescription;
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/ExampleFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */