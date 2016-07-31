package googletranslate;

import java.io.IOException;
import java.io.Writer;

public class JSONWriter
{
  private static final int maxdepth = 20;
  private boolean comma = false;
  protected char mode = 'i';
  private JSONObject[] stack = new JSONObject[20];
  private int top = 0;
  protected Writer writer;
  
  public JSONWriter(Writer paramWriter)
  {
    this.writer = paramWriter;
  }
  
  private JSONWriter append(String paramString)
    throws JSONException
  {
    if (paramString == null) {
      throw new JSONException("Null pointer");
    }
    if ((this.mode == 'o') || (this.mode == 'a'))
    {
      try
      {
        if ((this.comma) && (this.mode == 'a')) {
          this.writer.write(44);
        }
        this.writer.write(paramString);
      }
      catch (IOException localIOException)
      {
        throw new JSONException(localIOException);
      }
      if (this.mode == 'o') {
        this.mode = 'k';
      }
      this.comma = true;
      return this;
    }
    throw new JSONException("Value out of sequence.");
  }
  
  public JSONWriter array()
    throws JSONException
  {
    if ((this.mode == 'i') || (this.mode == 'o') || (this.mode == 'a'))
    {
      push(null);
      append("[");
      this.comma = false;
      return this;
    }
    throw new JSONException("Misplaced array.");
  }
  
  private JSONWriter end(char paramChar1, char paramChar2)
    throws JSONException
  {
    if (this.mode != paramChar1) {
      throw new JSONException(paramChar1 == 'o' ? "Misplaced endObject." : "Misplaced endArray.");
    }
    pop(paramChar1);
    try
    {
      this.writer.write(paramChar2);
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
    this.comma = true;
    return this;
  }
  
  public JSONWriter endArray()
    throws JSONException
  {
    return end('a', ']');
  }
  
  public JSONWriter endObject()
    throws JSONException
  {
    return end('k', '}');
  }
  
  public JSONWriter key(String paramString)
    throws JSONException
  {
    if (paramString == null) {
      throw new JSONException("Null key.");
    }
    if (this.mode == 'k') {
      try
      {
        this.stack[(this.top - 1)].putOnce(paramString, Boolean.TRUE);
        if (this.comma) {
          this.writer.write(44);
        }
        this.writer.write(JSONObject.quote(paramString));
        this.writer.write(58);
        this.comma = false;
        this.mode = 'o';
        return this;
      }
      catch (IOException localIOException)
      {
        throw new JSONException(localIOException);
      }
    }
    throw new JSONException("Misplaced key.");
  }
  
  public JSONWriter object()
    throws JSONException
  {
    if (this.mode == 'i') {
      this.mode = 'o';
    }
    if ((this.mode == 'o') || (this.mode == 'a'))
    {
      append("{");
      push(new JSONObject());
      this.comma = false;
      return this;
    }
    throw new JSONException("Misplaced object.");
  }
  
  private void pop(char paramChar)
    throws JSONException
  {
    if (this.top <= 0) {
      throw new JSONException("Nesting error.");
    }
    char c = this.stack[(this.top - 1)] == null ? 'a' : 'k';
    if (c != paramChar) {
      throw new JSONException("Nesting error.");
    }
    this.top -= 1;
    this.mode = (this.stack[(this.top - 1)] == null ? 'a' : this.top == 0 ? 'd' : 'k');
  }
  
  private void push(JSONObject paramJSONObject)
    throws JSONException
  {
    if (this.top >= 20) {
      throw new JSONException("Nesting too deep.");
    }
    this.stack[this.top] = paramJSONObject;
    this.mode = (paramJSONObject == null ? 'a' : 'k');
    this.top += 1;
  }
  
  public JSONWriter value(boolean paramBoolean)
    throws JSONException
  {
    return append(paramBoolean ? "true" : "false");
  }
  
  public JSONWriter value(double paramDouble)
    throws JSONException
  {
    return value(new Double(paramDouble));
  }
  
  public JSONWriter value(long paramLong)
    throws JSONException
  {
    return append(Long.toString(paramLong));
  }
  
  public JSONWriter value(Object paramObject)
    throws JSONException
  {
    return append(JSONObject.valueToString(paramObject));
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/JSONWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */