package googletranslate;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class JSONArray
{
  private ArrayList myArrayList;
  
  public JSONArray()
  {
    this.myArrayList = new ArrayList();
  }
  
  public JSONArray(JSONTokener paramJSONTokener)
    throws JSONException
  {
    this();
    int i = paramJSONTokener.nextClean();
    char c;
    if (i == 91) {
      c = ']';
    } else if (i == 40) {
      c = ')';
    } else {
      throw paramJSONTokener.syntaxError("A JSONArray text must start with '['");
    }
    if (paramJSONTokener.nextClean() == ']') {
      return;
    }
    paramJSONTokener.back();
    for (;;)
    {
      if (paramJSONTokener.nextClean() == ',')
      {
        paramJSONTokener.back();
        this.myArrayList.add(null);
      }
      else
      {
        paramJSONTokener.back();
        this.myArrayList.add(paramJSONTokener.nextValue());
      }
      i = paramJSONTokener.nextClean();
      switch (i)
      {
      case 44: 
      case 59: 
        if (paramJSONTokener.nextClean() == ']') {
          return;
        }
        paramJSONTokener.back();
      }
    }
    if (c != i) {
      throw paramJSONTokener.syntaxError("Expected a '" + new Character(c) + "'");
    }
    return;
    throw paramJSONTokener.syntaxError("Expected a ',' or ']'");
  }
  
  public JSONArray(String paramString)
    throws JSONException
  {
    this(new JSONTokener(paramString));
  }
  
  public JSONArray(Collection paramCollection)
  {
    this.myArrayList = (paramCollection == null ? new ArrayList() : new ArrayList(paramCollection));
  }
  
  public JSONArray(Collection paramCollection, boolean paramBoolean)
  {
    this.myArrayList = new ArrayList();
    if (paramCollection != null)
    {
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if ((localObject instanceof Map)) {
          this.myArrayList.add(new JSONObject((Map)localObject, paramBoolean));
        } else if (!JSONObject.isStandardProperty(localObject.getClass())) {
          this.myArrayList.add(new JSONObject(localObject, paramBoolean));
        } else {
          this.myArrayList.add(localObject);
        }
      }
    }
  }
  
  public JSONArray(Object paramObject)
    throws JSONException
  {
    this();
    if (paramObject.getClass().isArray())
    {
      int i = Array.getLength(paramObject);
      for (int j = 0; j < i; j++) {
        put(Array.get(paramObject, j));
      }
    }
    else
    {
      throw new JSONException("JSONArray initial value should be a string or collection or array.");
    }
  }
  
  public JSONArray(Object paramObject, boolean paramBoolean)
    throws JSONException
  {
    this();
    if (paramObject.getClass().isArray())
    {
      int i = Array.getLength(paramObject);
      for (int j = 0; j < i; j++)
      {
        Object localObject = Array.get(paramObject, j);
        if (JSONObject.isStandardProperty(localObject.getClass())) {
          this.myArrayList.add(localObject);
        } else {
          this.myArrayList.add(new JSONObject(localObject, paramBoolean));
        }
      }
    }
    else
    {
      throw new JSONException("JSONArray initial value should be a string or collection or array.");
    }
  }
  
  public Object get(int paramInt)
    throws JSONException
  {
    Object localObject = opt(paramInt);
    if (localObject == null) {
      throw new JSONException("JSONArray[" + paramInt + "] not found.");
    }
    return localObject;
  }
  
  public boolean getBoolean(int paramInt)
    throws JSONException
  {
    Object localObject = get(paramInt);
    if ((localObject.equals(Boolean.FALSE)) || (((localObject instanceof String)) && (((String)localObject).equalsIgnoreCase("false")))) {
      return false;
    }
    if ((localObject.equals(Boolean.TRUE)) || (((localObject instanceof String)) && (((String)localObject).equalsIgnoreCase("true")))) {
      return true;
    }
    throw new JSONException("JSONArray[" + paramInt + "] is not a Boolean.");
  }
  
  public double getDouble(int paramInt)
    throws JSONException
  {
    Object localObject = get(paramInt);
    try
    {
      return (localObject instanceof Number) ? ((Number)localObject).doubleValue() : Double.valueOf((String)localObject).doubleValue();
    }
    catch (Exception localException)
    {
      throw new JSONException("JSONArray[" + paramInt + "] is not a number.");
    }
  }
  
  public int getInt(int paramInt)
    throws JSONException
  {
    Object localObject = get(paramInt);
    return (localObject instanceof Number) ? ((Number)localObject).intValue() : (int)getDouble(paramInt);
  }
  
  public JSONArray getJSONArray(int paramInt)
    throws JSONException
  {
    Object localObject = get(paramInt);
    if ((localObject instanceof JSONArray)) {
      return (JSONArray)localObject;
    }
    throw new JSONException("JSONArray[" + paramInt + "] is not a JSONArray.");
  }
  
  public JSONObject getJSONObject(int paramInt)
    throws JSONException
  {
    Object localObject = get(paramInt);
    if ((localObject instanceof JSONObject)) {
      return (JSONObject)localObject;
    }
    throw new JSONException("JSONArray[" + paramInt + "] is not a JSONObject.");
  }
  
  public long getLong(int paramInt)
    throws JSONException
  {
    Object localObject = get(paramInt);
    return (localObject instanceof Number) ? ((Number)localObject).longValue() : getDouble(paramInt);
  }
  
  public String getString(int paramInt)
    throws JSONException
  {
    return get(paramInt).toString();
  }
  
  public boolean isNull(int paramInt)
  {
    return JSONObject.NULL.equals(opt(paramInt));
  }
  
  public String join(String paramString)
    throws JSONException
  {
    int i = length();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int j = 0; j < i; j++)
    {
      if (j > 0) {
        localStringBuffer.append(paramString);
      }
      localStringBuffer.append(JSONObject.valueToString(this.myArrayList.get(j)));
    }
    return localStringBuffer.toString();
  }
  
  public int length()
  {
    return this.myArrayList.size();
  }
  
  public Object opt(int paramInt)
  {
    return (paramInt < 0) || (paramInt >= length()) ? null : this.myArrayList.get(paramInt);
  }
  
  public boolean optBoolean(int paramInt)
  {
    return optBoolean(paramInt, false);
  }
  
  public boolean optBoolean(int paramInt, boolean paramBoolean)
  {
    try
    {
      return getBoolean(paramInt);
    }
    catch (Exception localException) {}
    return paramBoolean;
  }
  
  public double optDouble(int paramInt)
  {
    return optDouble(paramInt, NaN.0D);
  }
  
  public double optDouble(int paramInt, double paramDouble)
  {
    try
    {
      return getDouble(paramInt);
    }
    catch (Exception localException) {}
    return paramDouble;
  }
  
  public int optInt(int paramInt)
  {
    return optInt(paramInt, 0);
  }
  
  public int optInt(int paramInt1, int paramInt2)
  {
    try
    {
      return getInt(paramInt1);
    }
    catch (Exception localException) {}
    return paramInt2;
  }
  
  public JSONArray optJSONArray(int paramInt)
  {
    Object localObject = opt(paramInt);
    return (localObject instanceof JSONArray) ? (JSONArray)localObject : null;
  }
  
  public JSONObject optJSONObject(int paramInt)
  {
    Object localObject = opt(paramInt);
    return (localObject instanceof JSONObject) ? (JSONObject)localObject : null;
  }
  
  public long optLong(int paramInt)
  {
    return optLong(paramInt, 0L);
  }
  
  public long optLong(int paramInt, long paramLong)
  {
    try
    {
      return getLong(paramInt);
    }
    catch (Exception localException) {}
    return paramLong;
  }
  
  public String optString(int paramInt)
  {
    return optString(paramInt, "");
  }
  
  public String optString(int paramInt, String paramString)
  {
    Object localObject = opt(paramInt);
    return localObject != null ? localObject.toString() : paramString;
  }
  
  public JSONArray put(boolean paramBoolean)
  {
    put(paramBoolean ? Boolean.TRUE : Boolean.FALSE);
    return this;
  }
  
  public JSONArray put(Collection paramCollection)
  {
    put(new JSONArray(paramCollection));
    return this;
  }
  
  public JSONArray put(double paramDouble)
    throws JSONException
  {
    Double localDouble = new Double(paramDouble);
    JSONObject.testValidity(localDouble);
    put(localDouble);
    return this;
  }
  
  public JSONArray put(int paramInt)
  {
    put(new Integer(paramInt));
    return this;
  }
  
  public JSONArray put(long paramLong)
  {
    put(new Long(paramLong));
    return this;
  }
  
  public JSONArray put(Map paramMap)
  {
    put(new JSONObject(paramMap));
    return this;
  }
  
  public JSONArray put(Object paramObject)
  {
    this.myArrayList.add(paramObject);
    return this;
  }
  
  public JSONArray put(int paramInt, boolean paramBoolean)
    throws JSONException
  {
    put(paramInt, paramBoolean ? Boolean.TRUE : Boolean.FALSE);
    return this;
  }
  
  public JSONArray put(int paramInt, Collection paramCollection)
    throws JSONException
  {
    put(paramInt, new JSONArray(paramCollection));
    return this;
  }
  
  public JSONArray put(int paramInt, double paramDouble)
    throws JSONException
  {
    put(paramInt, new Double(paramDouble));
    return this;
  }
  
  public JSONArray put(int paramInt1, int paramInt2)
    throws JSONException
  {
    put(paramInt1, new Integer(paramInt2));
    return this;
  }
  
  public JSONArray put(int paramInt, long paramLong)
    throws JSONException
  {
    put(paramInt, new Long(paramLong));
    return this;
  }
  
  public JSONArray put(int paramInt, Map paramMap)
    throws JSONException
  {
    put(paramInt, new JSONObject(paramMap));
    return this;
  }
  
  public JSONArray put(int paramInt, Object paramObject)
    throws JSONException
  {
    JSONObject.testValidity(paramObject);
    if (paramInt < 0) {
      throw new JSONException("JSONArray[" + paramInt + "] not found.");
    }
    if (paramInt < length())
    {
      this.myArrayList.set(paramInt, paramObject);
    }
    else
    {
      while (paramInt != length()) {
        put(JSONObject.NULL);
      }
      put(paramObject);
    }
    return this;
  }
  
  public Object remove(int paramInt)
  {
    Object localObject = opt(paramInt);
    this.myArrayList.remove(paramInt);
    return localObject;
  }
  
  public JSONObject toJSONObject(JSONArray paramJSONArray)
    throws JSONException
  {
    if ((paramJSONArray == null) || (paramJSONArray.length() == 0) || (length() == 0)) {
      return null;
    }
    JSONObject localJSONObject = new JSONObject();
    for (int i = 0; i < paramJSONArray.length(); i++) {
      localJSONObject.put(paramJSONArray.getString(i), opt(i));
    }
    return localJSONObject;
  }
  
  public String toString()
  {
    try
    {
      return '[' + join(",") + ']';
    }
    catch (Exception localException) {}
    return null;
  }
  
  public String toString(int paramInt)
    throws JSONException
  {
    return toString(paramInt, 0);
  }
  
  String toString(int paramInt1, int paramInt2)
    throws JSONException
  {
    int i = length();
    if (i == 0) {
      return "[]";
    }
    StringBuffer localStringBuffer = new StringBuffer("[");
    if (i == 1)
    {
      localStringBuffer.append(JSONObject.valueToString(this.myArrayList.get(0), paramInt1, paramInt2));
    }
    else
    {
      int k = paramInt2 + paramInt1;
      localStringBuffer.append('\n');
      for (int j = 0; j < i; j++)
      {
        if (j > 0) {
          localStringBuffer.append(",\n");
        }
        for (int m = 0; m < k; m++) {
          localStringBuffer.append(' ');
        }
        localStringBuffer.append(JSONObject.valueToString(this.myArrayList.get(j), paramInt1, k));
      }
      localStringBuffer.append('\n');
      for (j = 0; j < paramInt2; j++) {
        localStringBuffer.append(' ');
      }
    }
    localStringBuffer.append(']');
    return localStringBuffer.toString();
  }
  
  public Writer write(Writer paramWriter)
    throws JSONException
  {
    try
    {
      int i = 0;
      int j = length();
      paramWriter.write(91);
      for (int k = 0; k < j; k++)
      {
        if (i != 0) {
          paramWriter.write(44);
        }
        Object localObject = this.myArrayList.get(k);
        if ((localObject instanceof JSONObject)) {
          ((JSONObject)localObject).write(paramWriter);
        } else if ((localObject instanceof JSONArray)) {
          ((JSONArray)localObject).write(paramWriter);
        } else {
          paramWriter.write(JSONObject.valueToString(localObject));
        }
        i = 1;
      }
      paramWriter.write(93);
      return paramWriter;
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/JSONArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */