package googletranslate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class JSONTokener
{
  private int index;
  private Reader reader;
  private char lastChar;
  private boolean useLastChar;
  
  public JSONTokener(Reader paramReader)
  {
    this.reader = (paramReader.markSupported() ? paramReader : new BufferedReader(paramReader));
    this.useLastChar = false;
    this.index = 0;
  }
  
  public JSONTokener(String paramString)
  {
    this(new StringReader(paramString));
  }
  
  public void back()
    throws JSONException
  {
    if ((this.useLastChar) || (this.index <= 0)) {
      throw new JSONException("Stepping back two steps is not supported");
    }
    this.index -= 1;
    this.useLastChar = true;
  }
  
  public static int dehexchar(char paramChar)
  {
    if ((paramChar >= '0') && (paramChar <= '9')) {
      return paramChar - '0';
    }
    if ((paramChar >= 'A') && (paramChar <= 'F')) {
      return paramChar - '7';
    }
    if ((paramChar >= 'a') && (paramChar <= 'f')) {
      return paramChar - 'W';
    }
    return -1;
  }
  
  public boolean more()
    throws JSONException
  {
    int i = next();
    if (i == 0) {
      return false;
    }
    back();
    return true;
  }
  
  public char next()
    throws JSONException
  {
    if (this.useLastChar)
    {
      this.useLastChar = false;
      if (this.lastChar != 0) {
        this.index += 1;
      }
      return this.lastChar;
    }
    int i;
    try
    {
      i = this.reader.read();
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
    if (i <= 0)
    {
      this.lastChar = '\000';
      return '\000';
    }
    this.index += 1;
    this.lastChar = ((char)i);
    return this.lastChar;
  }
  
  public char next(char paramChar)
    throws JSONException
  {
    char c = next();
    if (c != paramChar) {
      throw syntaxError("Expected '" + paramChar + "' and instead saw '" + c + "'");
    }
    return c;
  }
  
  public String next(int paramInt)
    throws JSONException
  {
    if (paramInt == 0) {
      return "";
    }
    char[] arrayOfChar = new char[paramInt];
    int i = 0;
    if (this.useLastChar)
    {
      this.useLastChar = false;
      arrayOfChar[0] = this.lastChar;
      i = 1;
    }
    try
    {
      int j;
      while ((i < paramInt) && ((j = this.reader.read(arrayOfChar, i, paramInt - i)) != -1)) {
        i += j;
      }
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
    this.index += i;
    if (i < paramInt) {
      throw syntaxError("Substring bounds error");
    }
    this.lastChar = arrayOfChar[(paramInt - 1)];
    return new String(arrayOfChar);
  }
  
  public char nextClean()
    throws JSONException
  {
    for (;;)
    {
      char c = next();
      if ((c == 0) || (c > ' ')) {
        return c;
      }
    }
  }
  
  public String nextString(char paramChar)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      char c = next();
      switch (c)
      {
      case '\000': 
      case '\n': 
      case '\r': 
        throw syntaxError("Unterminated string");
      case '\\': 
        c = next();
        switch (c)
        {
        case 'b': 
          localStringBuffer.append('\b');
          break;
        case 't': 
          localStringBuffer.append('\t');
          break;
        case 'n': 
          localStringBuffer.append('\n');
          break;
        case 'f': 
          localStringBuffer.append('\f');
          break;
        case 'r': 
          localStringBuffer.append('\r');
          break;
        case 'u': 
          localStringBuffer.append((char)Integer.parseInt(next(4), 16));
          break;
        case '"': 
        case '\'': 
        case '/': 
        case '\\': 
          localStringBuffer.append(c);
          break;
        default: 
          throw syntaxError("Illegal escape.");
        }
        break;
      default: 
        if (c == paramChar) {
          return localStringBuffer.toString();
        }
        localStringBuffer.append(c);
      }
    }
  }
  
  public String nextTo(char paramChar)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      char c = next();
      if ((c == paramChar) || (c == 0) || (c == '\n') || (c == '\r'))
      {
        if (c != 0) {
          back();
        }
        return localStringBuffer.toString().trim();
      }
      localStringBuffer.append(c);
    }
  }
  
  public String nextTo(String paramString)
    throws JSONException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      char c = next();
      if ((paramString.indexOf(c) >= 0) || (c == 0) || (c == '\n') || (c == '\r'))
      {
        if (c != 0) {
          back();
        }
        return localStringBuffer.toString().trim();
      }
      localStringBuffer.append(c);
    }
  }
  
  public Object nextValue()
    throws JSONException
  {
    char c = nextClean();
    switch (c)
    {
    case '"': 
    case '\'': 
      return nextString(c);
    case '{': 
      back();
      return new JSONObject(this);
    case '(': 
    case '[': 
      back();
      return new JSONArray(this);
    }
    StringBuffer localStringBuffer = new StringBuffer();
    while ((c >= ' ') && (",:]}/\\\"[{;=#".indexOf(c) < 0))
    {
      localStringBuffer.append(c);
      c = next();
    }
    back();
    String str = localStringBuffer.toString().trim();
    if (str.equals("")) {
      throw syntaxError("Missing value");
    }
    return JSONObject.stringToValue(str);
  }
  
  public char skipTo(char paramChar)
    throws JSONException
  {
    char c;
    try
    {
      int i = this.index;
      this.reader.mark(Integer.MAX_VALUE);
      do
      {
        c = next();
        if (c == 0)
        {
          this.reader.reset();
          this.index = i;
          return c;
        }
      } while (c != paramChar);
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
    back();
    return c;
  }
  
  public JSONException syntaxError(String paramString)
  {
    return new JSONException(paramString + toString());
  }
  
  public String toString()
  {
    return " at character " + this.index;
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/JSONTokener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */