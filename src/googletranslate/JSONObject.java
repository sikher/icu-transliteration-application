package googletranslate;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class JSONObject
{
  private Map map;
  public static final Object NULL = new Null(null);
  
  public JSONObject()
  {
    this.map = new HashMap();
  }
  
  public JSONObject(JSONObject paramJSONObject, String[] paramArrayOfString)
    throws JSONException
  {
    this();
    for (int i = 0; i < paramArrayOfString.length; i++) {
      putOnce(paramArrayOfString[i], paramJSONObject.opt(paramArrayOfString[i]));
    }
  }
  
  public JSONObject(JSONTokener paramJSONTokener)
    throws JSONException
  {
    this();
    if (paramJSONTokener.nextClean() != '{') {
      throw paramJSONTokener.syntaxError("A JSONObject text must begin with '{'");
    }
    for (;;)
    {
      int i = paramJSONTokener.nextClean();
      switch (i)
      {
      case 0: 
        throw paramJSONTokener.syntaxError("A JSONObject text must end with '}'");
      case 125: 
        return;
      }
      paramJSONTokener.back();
      String str = paramJSONTokener.nextValue().toString();
      i = paramJSONTokener.nextClean();
      if (i == 61)
      {
        if (paramJSONTokener.next() != '>') {
          paramJSONTokener.back();
        }
      }
      else if (i != 58) {
        throw paramJSONTokener.syntaxError("Expected a ':' after a key");
      }
      putOnce(str, paramJSONTokener.nextValue());
      switch (paramJSONTokener.nextClean())
      {
      case ',': 
      case ';': 
        if (paramJSONTokener.nextClean() == '}') {
          return;
        }
        paramJSONTokener.back();
      }
    }
    return;
    throw paramJSONTokener.syntaxError("Expected a ',' or '}'");
  }
  
  public JSONObject(Map paramMap)
  {
    this.map = (paramMap == null ? new HashMap() : paramMap);
  }
  
  public JSONObject(Map paramMap, boolean paramBoolean)
  {
    this.map = new HashMap();
    if (paramMap != null)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (isStandardProperty(localEntry.getValue().getClass())) {
          this.map.put(localEntry.getKey(), localEntry.getValue());
        } else {
          this.map.put(localEntry.getKey(), new JSONObject(localEntry.getValue(), paramBoolean));
        }
      }
    }
  }
  
  public JSONObject(Object paramObject)
  {
    this();
    populateInternalMap(paramObject, false);
  }
  
  public JSONObject(Object paramObject, boolean paramBoolean)
  {
    this();
    populateInternalMap(paramObject, paramBoolean);
  }
  
  private void populateInternalMap(Object paramObject, boolean paramBoolean)
  {
    Class localClass = paramObject.getClass();
    if (localClass.getClassLoader() == null) {
      paramBoolean = false;
    }
    Method[] arrayOfMethod = paramBoolean ? localClass.getMethods() : localClass.getDeclaredMethods();
    for (int i = 0; i < arrayOfMethod.length; i++) {
      try
      {
        Method localMethod = arrayOfMethod[i];
        if (Modifier.isPublic(localMethod.getModifiers()))
        {
          String str1 = localMethod.getName();
          String str2 = "";
          if (str1.startsWith("get")) {
            str2 = str1.substring(3);
          } else if (str1.startsWith("is")) {
            str2 = str1.substring(2);
          }
          if ((str2.length() > 0) && (Character.isUpperCase(str2.charAt(0))) && (localMethod.getParameterTypes().length == 0))
          {
            if (str2.length() == 1) {
              str2 = str2.toLowerCase();
            } else if (!Character.isUpperCase(str2.charAt(1))) {
              str2 = str2.substring(0, 1).toLowerCase() + str2.substring(1);
            }
            Object localObject = localMethod.invoke(paramObject, (Object[])null);
            if (localObject == null) {
              this.map.put(str2, NULL);
            } else if (localObject.getClass().isArray()) {
              this.map.put(str2, new JSONArray(localObject, paramBoolean));
            } else if ((localObject instanceof Collection)) {
              this.map.put(str2, new JSONArray((Collection)localObject, paramBoolean));
            } else if ((localObject instanceof Map)) {
              this.map.put(str2, new JSONObject((Map)localObject, paramBoolean));
            } else if (isStandardProperty(localObject.getClass())) {
              this.map.put(str2, localObject);
            } else if ((localObject.getClass().getPackage().getName().startsWith("java")) || (localObject.getClass().getClassLoader() == null)) {
              this.map.put(str2, localObject.toString());
            } else {
              this.map.put(str2, new JSONObject(localObject, paramBoolean));
            }
          }
        }
      }
      catch (Exception localException)
      {
        throw new RuntimeException(localException);
      }
    }
  }
  
  static boolean isStandardProperty(Class paramClass)
  {
    return (paramClass.isPrimitive()) || (paramClass.isAssignableFrom(Byte.class)) || (paramClass.isAssignableFrom(Short.class)) || (paramClass.isAssignableFrom(Integer.class)) || (paramClass.isAssignableFrom(Long.class)) || (paramClass.isAssignableFrom(Float.class)) || (paramClass.isAssignableFrom(Double.class)) || (paramClass.isAssignableFrom(Character.class)) || (paramClass.isAssignableFrom(String.class)) || (paramClass.isAssignableFrom(Boolean.class));
  }
  
  public JSONObject(Object paramObject, String[] paramArrayOfString)
  {
    this();
    Class localClass = paramObject.getClass();
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      String str = paramArrayOfString[i];
      try
      {
        putOpt(str, localClass.getField(str).get(paramObject));
      }
      catch (Exception localException) {}
    }
  }
  
  public JSONObject(String paramString)
    throws JSONException
  {
    this(new JSONTokener(paramString));
  }
  
  public JSONObject accumulate(String paramString, Object paramObject)
    throws JSONException
  {
    testValidity(paramObject);
    Object localObject = opt(paramString);
    if (localObject == null) {
      put(paramString, (paramObject instanceof JSONArray) ? new JSONArray().put(paramObject) : paramObject);
    } else if ((localObject instanceof JSONArray)) {
      ((JSONArray)localObject).put(paramObject);
    } else {
      put(paramString, new JSONArray().put(localObject).put(paramObject));
    }
    return this;
  }
  
  public JSONObject append(String paramString, Object paramObject)
    throws JSONException
  {
    testValidity(paramObject);
    Object localObject = opt(paramString);
    if (localObject == null) {
      put(paramString, new JSONArray().put(paramObject));
    } else if ((localObject instanceof JSONArray)) {
      put(paramString, ((JSONArray)localObject).put(paramObject));
    } else {
      throw new JSONException("JSONObject[" + paramString + "] is not a JSONArray.");
    }
    return this;
  }
  
  public static String doubleToString(double paramDouble)
  {
    if ((Double.isInfinite(paramDouble)) || (Double.isNaN(paramDouble))) {
      return "null";
    }
    String str = Double.toString(paramDouble);
    if ((str.indexOf('.') > 0) && (str.indexOf('e') < 0) && (str.indexOf('E') < 0))
    {
      while (str.endsWith("0")) {
        str = str.substring(0, str.length() - 1);
      }
      if (str.endsWith(".")) {
        str = str.substring(0, str.length() - 1);
      }
    }
    return str;
  }
  
  public Object get(String paramString)
    throws JSONException
  {
    Object localObject = opt(paramString);
    if (localObject == null) {
      throw new JSONException("JSONObject[" + quote(paramString) + "] not found.");
    }
    return localObject;
  }
  
  public boolean getBoolean(String paramString)
    throws JSONException
  {
    Object localObject = get(paramString);
    if ((localObject.equals(Boolean.FALSE)) || (((localObject instanceof String)) && (((String)localObject).equalsIgnoreCase("false")))) {
      return false;
    }
    if ((localObject.equals(Boolean.TRUE)) || (((localObject instanceof String)) && (((String)localObject).equalsIgnoreCase("true")))) {
      return true;
    }
    throw new JSONException("JSONObject[" + quote(paramString) + "] is not a Boolean.");
  }
  
  public double getDouble(String paramString)
    throws JSONException
  {
    Object localObject = get(paramString);
    try
    {
      return (localObject instanceof Number) ? ((Number)localObject).doubleValue() : Double.valueOf((String)localObject).doubleValue();
    }
    catch (Exception localException)
    {
      throw new JSONException("JSONObject[" + quote(paramString) + "] is not a number.");
    }
  }
  
  public int getInt(String paramString)
    throws JSONException
  {
    Object localObject = get(paramString);
    return (localObject instanceof Number) ? ((Number)localObject).intValue() : (int)getDouble(paramString);
  }
  
  public JSONArray getJSONArray(String paramString)
    throws JSONException
  {
    Object localObject = get(paramString);
    if ((localObject instanceof JSONArray)) {
      return (JSONArray)localObject;
    }
    throw new JSONException("JSONObject[" + quote(paramString) + "] is not a JSONArray.");
  }
  
  public JSONObject getJSONObject(String paramString)
    throws JSONException
  {
    Object localObject = get(paramString);
    if ((localObject instanceof JSONObject)) {
      return (JSONObject)localObject;
    }
    throw new JSONException("JSONObject[" + quote(paramString) + "] is not a JSONObject.");
  }
  
  public long getLong(String paramString)
    throws JSONException
  {
    Object localObject = get(paramString);
    return (localObject instanceof Number) ? ((Number)localObject).longValue() : getDouble(paramString);
  }
  
  public static String[] getNames(JSONObject paramJSONObject)
  {
    int i = paramJSONObject.length();
    if (i == 0) {
      return null;
    }
    Iterator localIterator = paramJSONObject.keys();
    String[] arrayOfString = new String[i];
    for (int j = 0; localIterator.hasNext(); j++) {
      arrayOfString[j] = ((String)localIterator.next());
    }
    return arrayOfString;
  }
  
  public static String[] getNames(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    Class localClass = paramObject.getClass();
    Field[] arrayOfField = localClass.getFields();
    int i = arrayOfField.length;
    if (i == 0) {
      return null;
    }
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++) {
      arrayOfString[j] = arrayOfField[j].getName();
    }
    return arrayOfString;
  }
  
  public String getString(String paramString)
    throws JSONException
  {
    return get(paramString).toString();
  }
  
  public boolean has(String paramString)
  {
    return this.map.containsKey(paramString);
  }
  
  public boolean isNull(String paramString)
  {
    return NULL.equals(opt(paramString));
  }
  
  public Iterator keys()
  {
    return this.map.keySet().iterator();
  }
  
  public int length()
  {
    return this.map.size();
  }
  
  public JSONArray names()
  {
    JSONArray localJSONArray = new JSONArray();
    Iterator localIterator = keys();
    while (localIterator.hasNext()) {
      localJSONArray.put(localIterator.next());
    }
    return localJSONArray.length() == 0 ? null : localJSONArray;
  }
  
  public static String numberToString(Number paramNumber)
    throws JSONException
  {
    if (paramNumber == null) {
      throw new JSONException("Null pointer");
    }
    testValidity(paramNumber);
    String str = paramNumber.toString();
    if ((str.indexOf('.') > 0) && (str.indexOf('e') < 0) && (str.indexOf('E') < 0))
    {
      while (str.endsWith("0")) {
        str = str.substring(0, str.length() - 1);
      }
      if (str.endsWith(".")) {
        str = str.substring(0, str.length() - 1);
      }
    }
    return str;
  }
  
  public Object opt(String paramString)
  {
    return paramString == null ? null : this.map.get(paramString);
  }
  
  public boolean optBoolean(String paramString)
  {
    return optBoolean(paramString, false);
  }
  
  public boolean optBoolean(String paramString, boolean paramBoolean)
  {
    try
    {
      return getBoolean(paramString);
    }
    catch (Exception localException) {}
    return paramBoolean;
  }
  
  public JSONObject put(String paramString, Collection paramCollection)
    throws JSONException
  {
    put(paramString, new JSONArray(paramCollection));
    return this;
  }
  
  public double optDouble(String paramString)
  {
    return optDouble(paramString, NaN.0D);
  }
  
  public double optDouble(String paramString, double paramDouble)
  {
    try
    {
      Object localObject = opt(paramString);
      return (localObject instanceof Number) ? ((Number)localObject).doubleValue() : new Double((String)localObject).doubleValue();
    }
    catch (Exception localException) {}
    return paramDouble;
  }
  
  public int optInt(String paramString)
  {
    return optInt(paramString, 0);
  }
  
  public int optInt(String paramString, int paramInt)
  {
    try
    {
      return getInt(paramString);
    }
    catch (Exception localException) {}
    return paramInt;
  }
  
  public JSONArray optJSONArray(String paramString)
  {
    Object localObject = opt(paramString);
    return (localObject instanceof JSONArray) ? (JSONArray)localObject : null;
  }
  
  public JSONObject optJSONObject(String paramString)
  {
    Object localObject = opt(paramString);
    return (localObject instanceof JSONObject) ? (JSONObject)localObject : null;
  }
  
  public long optLong(String paramString)
  {
    return optLong(paramString, 0L);
  }
  
  public long optLong(String paramString, long paramLong)
  {
    try
    {
      return getLong(paramString);
    }
    catch (Exception localException) {}
    return paramLong;
  }
  
  public String optString(String paramString)
  {
    return optString(paramString, "");
  }
  
  public String optString(String paramString1, String paramString2)
  {
    Object localObject = opt(paramString1);
    return localObject != null ? localObject.toString() : paramString2;
  }
  
  public JSONObject put(String paramString, boolean paramBoolean)
    throws JSONException
  {
    put(paramString, paramBoolean ? Boolean.TRUE : Boolean.FALSE);
    return this;
  }
  
  public JSONObject put(String paramString, double paramDouble)
    throws JSONException
  {
    put(paramString, new Double(paramDouble));
    return this;
  }
  
  public JSONObject put(String paramString, int paramInt)
    throws JSONException
  {
    put(paramString, new Integer(paramInt));
    return this;
  }
  
  public JSONObject put(String paramString, long paramLong)
    throws JSONException
  {
    put(paramString, new Long(paramLong));
    return this;
  }
  
  public JSONObject put(String paramString, Map paramMap)
    throws JSONException
  {
    put(paramString, new JSONObject(paramMap));
    return this;
  }
  
  public JSONObject put(String paramString, Object paramObject)
    throws JSONException
  {
    if (paramString == null) {
      throw new JSONException("Null key.");
    }
    if (paramObject != null)
    {
      testValidity(paramObject);
      this.map.put(paramString, paramObject);
    }
    else
    {
      remove(paramString);
    }
    return this;
  }
  
  public JSONObject putOnce(String paramString, Object paramObject)
    throws JSONException
  {
    if ((paramString != null) && (paramObject != null))
    {
      if (opt(paramString) != null) {
        throw new JSONException("Duplicate key \"" + paramString + "\"");
      }
      put(paramString, paramObject);
    }
    return this;
  }
  
  public JSONObject putOpt(String paramString, Object paramObject)
    throws JSONException
  {
    if ((paramString != null) && (paramObject != null)) {
      put(paramString, paramObject);
    }
    return this;
  }
  
  public static String quote(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0)) {
      return "\"\"";
    }
    char c2 = '\000';
    int j = paramString.length();
    StringBuffer localStringBuffer = new StringBuffer(j + 4);
    localStringBuffer.append('"');
    for (int i = 0; i < j; i++)
    {
      char c1 = c2;
      c2 = paramString.charAt(i);
      switch (c2)
      {
      case '"': 
      case '\\': 
        localStringBuffer.append('\\');
        localStringBuffer.append(c2);
        break;
      case '/': 
        if (c1 == '<') {
          localStringBuffer.append('\\');
        }
        localStringBuffer.append(c2);
        break;
      case '\b': 
        localStringBuffer.append("\\b");
        break;
      case '\t': 
        localStringBuffer.append("\\t");
        break;
      case '\n': 
        localStringBuffer.append("\\n");
        break;
      case '\f': 
        localStringBuffer.append("\\f");
        break;
      case '\r': 
        localStringBuffer.append("\\r");
        break;
      default: 
        if ((c2 < ' ') || ((c2 >= '') && (c2 < ' ')) || ((c2 >= ' ') && (c2 < '℀')))
        {
          String str = "000" + Integer.toHexString(c2);
          localStringBuffer.append("\\u" + str.substring(str.length() - 4));
        }
        else
        {
          localStringBuffer.append(c2);
        }
        break;
      }
    }
    localStringBuffer.append('"');
    return localStringBuffer.toString();
  }
  
  public Object remove(String paramString)
  {
    return this.map.remove(paramString);
  }
  
  public Iterator sortedKeys()
  {
    return new TreeSet(this.map.keySet()).iterator();
  }
  
  public static Object stringToValue(String paramString)
  {
    if (paramString.equals("")) {
      return paramString;
    }
    if (paramString.equalsIgnoreCase("true")) {
      return Boolean.TRUE;
    }
    if (paramString.equalsIgnoreCase("false")) {
      return Boolean.FALSE;
    }
    if (paramString.equalsIgnoreCase("null")) {
      return NULL;
    }
    int i = paramString.charAt(0);
    if (((i >= 48) && (i <= 57)) || (i == 46) || (i == 45) || (i == 43))
    {
      if (i == 48) {
        if ((paramString.length() > 2) && ((paramString.charAt(1) == 'x') || (paramString.charAt(1) == 'X'))) {
          try
          {
            return new Integer(Integer.parseInt(paramString.substring(2), 16));
          }
          catch (Exception localException1) {}
        } else {
          try
          {
            return new Integer(Integer.parseInt(paramString, 8));
          }
          catch (Exception localException2) {}
        }
      }
      try
      {
        if ((paramString.indexOf('.') > -1) || (paramString.indexOf('e') > -1) || (paramString.indexOf('E') > -1)) {
          return Double.valueOf(paramString);
        }
        Long localLong = new Long(paramString);
        if (localLong.longValue() == localLong.intValue()) {
          return new Integer(localLong.intValue());
        }
        return localLong;
      }
      catch (Exception localException3) {}
    }
    return paramString;
  }
  
  static void testValidity(Object paramObject)
    throws JSONException
  {
    if (paramObject != null) {
      if ((paramObject instanceof Double))
      {
        if ((((Double)paramObject).isInfinite()) || (((Double)paramObject).isNaN())) {
          throw new JSONException("JSON does not allow non-finite numbers.");
        }
      }
      else if (((paramObject instanceof Float)) && ((((Float)paramObject).isInfinite()) || (((Float)paramObject).isNaN()))) {
        throw new JSONException("JSON does not allow non-finite numbers.");
      }
    }
  }
  
  public JSONArray toJSONArray(JSONArray paramJSONArray)
    throws JSONException
  {
    if ((paramJSONArray == null) || (paramJSONArray.length() == 0)) {
      return null;
    }
    JSONArray localJSONArray = new JSONArray();
    for (int i = 0; i < paramJSONArray.length(); i++) {
      localJSONArray.put(opt(paramJSONArray.getString(i)));
    }
    return localJSONArray;
  }
  
  public String toString()
  {
    try
    {
      Iterator localIterator = keys();
      StringBuffer localStringBuffer = new StringBuffer("{");
      while (localIterator.hasNext())
      {
        if (localStringBuffer.length() > 1) {
          localStringBuffer.append(',');
        }
        Object localObject = localIterator.next();
        localStringBuffer.append(quote(localObject.toString()));
        localStringBuffer.append(':');
        localStringBuffer.append(valueToString(this.map.get(localObject)));
      }
      localStringBuffer.append('}');
      return localStringBuffer.toString();
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
    int j = length();
    if (j == 0) {
      return "{}";
    }
    Iterator localIterator = sortedKeys();
    StringBuffer localStringBuffer = new StringBuffer("{");
    int k = paramInt2 + paramInt1;
    Object localObject;
    if (j == 1)
    {
      localObject = localIterator.next();
      localStringBuffer.append(quote(localObject.toString()));
      localStringBuffer.append(": ");
      localStringBuffer.append(valueToString(this.map.get(localObject), paramInt1, paramInt2));
    }
    else
    {
      int i;
      while (localIterator.hasNext())
      {
        localObject = localIterator.next();
        if (localStringBuffer.length() > 1) {
          localStringBuffer.append(",\n");
        } else {
          localStringBuffer.append('\n');
        }
        for (i = 0; i < k; i++) {
          localStringBuffer.append(' ');
        }
        localStringBuffer.append(quote(localObject.toString()));
        localStringBuffer.append(": ");
        localStringBuffer.append(valueToString(this.map.get(localObject), paramInt1, k));
      }
      if (localStringBuffer.length() > 1)
      {
        localStringBuffer.append('\n');
        for (i = 0; i < paramInt2; i++) {
          localStringBuffer.append(' ');
        }
      }
    }
    localStringBuffer.append('}');
    return localStringBuffer.toString();
  }
  
  static String valueToString(Object paramObject)
    throws JSONException
  {
    if ((paramObject == null) || (paramObject.equals(null))) {
      return "null";
    }
    if ((paramObject instanceof JSONString))
    {
      String str;
      try
      {
        str = ((JSONString)paramObject).toJSONString();
      }
      catch (Exception localException)
      {
        throw new JSONException(localException);
      }
      if ((str instanceof String)) {
        return (String)str;
      }
      throw new JSONException("Bad value from toJSONString: " + str);
    }
    if ((paramObject instanceof Number)) {
      return numberToString((Number)paramObject);
    }
    if (((paramObject instanceof Boolean)) || ((paramObject instanceof JSONObject)) || ((paramObject instanceof JSONArray))) {
      return paramObject.toString();
    }
    if ((paramObject instanceof Map)) {
      return new JSONObject((Map)paramObject).toString();
    }
    if ((paramObject instanceof Collection)) {
      return new JSONArray((Collection)paramObject).toString();
    }
    if (paramObject.getClass().isArray()) {
      return new JSONArray(paramObject).toString();
    }
    return quote(paramObject.toString());
  }
  
  static String valueToString(Object paramObject, int paramInt1, int paramInt2)
    throws JSONException
  {
    if ((paramObject == null) || (paramObject.equals(null))) {
      return "null";
    }
    try
    {
      if ((paramObject instanceof JSONString))
      {
        String str = ((JSONString)paramObject).toJSONString();
        if ((str instanceof String)) {
          return (String)str;
        }
      }
    }
    catch (Exception localException) {}
    if ((paramObject instanceof Number)) {
      return numberToString((Number)paramObject);
    }
    if ((paramObject instanceof Boolean)) {
      return paramObject.toString();
    }
    if ((paramObject instanceof JSONObject)) {
      return ((JSONObject)paramObject).toString(paramInt1, paramInt2);
    }
    if ((paramObject instanceof JSONArray)) {
      return ((JSONArray)paramObject).toString(paramInt1, paramInt2);
    }
    if ((paramObject instanceof Map)) {
      return new JSONObject((Map)paramObject).toString(paramInt1, paramInt2);
    }
    if ((paramObject instanceof Collection)) {
      return new JSONArray((Collection)paramObject).toString(paramInt1, paramInt2);
    }
    if (paramObject.getClass().isArray()) {
      return new JSONArray(paramObject).toString(paramInt1, paramInt2);
    }
    return quote(paramObject.toString());
  }
  
  public Writer write(Writer paramWriter)
    throws JSONException
  {
    try
    {
      int i = 0;
      Iterator localIterator = keys();
      paramWriter.write(123);
      while (localIterator.hasNext())
      {
        if (i != 0) {
          paramWriter.write(44);
        }
        Object localObject1 = localIterator.next();
        paramWriter.write(quote(localObject1.toString()));
        paramWriter.write(58);
        Object localObject2 = this.map.get(localObject1);
        if ((localObject2 instanceof JSONObject)) {
          ((JSONObject)localObject2).write(paramWriter);
        } else if ((localObject2 instanceof JSONArray)) {
          ((JSONArray)localObject2).write(paramWriter);
        } else {
          paramWriter.write(valueToString(localObject2));
        }
        i = 1;
      }
      paramWriter.write(125);
      return paramWriter;
    }
    catch (IOException localIOException)
    {
      throw new JSONException(localIOException);
    }
  }
  
  private static final class Null
  {
    protected final Object clone()
    {
      return this;
    }
    
    public boolean equals(Object paramObject)
    {
      return (paramObject == null) || (paramObject == this);
    }
    
    public String toString()
    {
      return "null";
    }
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/JSONObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */