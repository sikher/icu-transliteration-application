package googletranslate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class Translate
{
  private static final String ENCODING = "UTF-8";
  private static final String URL_STRING = "http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&langpair=";
  private static final String TEXT_VAR = "&q=";
  private static String referrer = "http://code.google.com/p/google-api-translate-java/";
  
  public static void setHttpReferrer(String paramString)
  {
    referrer = paramString;
  }
  
  public static String translate(String paramString, Language paramLanguage1, Language paramLanguage2)
    throws Exception
  {
    return retrieveTranslation(paramString, paramLanguage1, paramLanguage2);
  }
  
  /* Error */
  private static String retrieveTranslation(String paramString, Language paramLanguage1, Language paramLanguage2)
    throws Exception
  {
    // Byte code:
    //   0: new 4	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 5	java/lang/StringBuilder:<init>	()V
    //   7: astore_3
    //   8: aload_3
    //   9: ldc 6
    //   11: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   14: aload_1
    //   15: invokevirtual 8	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   18: ldc 9
    //   20: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: aload_2
    //   24: invokevirtual 8	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload_3
    //   29: ldc 10
    //   31: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: aload_0
    //   35: ldc 11
    //   37: invokestatic 12	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   40: invokevirtual 7	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   43: pop
    //   44: new 13	java/net/URL
    //   47: dup
    //   48: aload_3
    //   49: invokevirtual 14	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   52: invokespecial 15	java/net/URL:<init>	(Ljava/lang/String;)V
    //   55: invokevirtual 16	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   58: checkcast 17	java/net/HttpURLConnection
    //   61: astore 4
    //   63: aload 4
    //   65: ldc 18
    //   67: getstatic 2	googletranslate/Translate:referrer	Ljava/lang/String;
    //   70: invokevirtual 19	java/net/HttpURLConnection:setRequestProperty	(Ljava/lang/String;Ljava/lang/String;)V
    //   73: aload 4
    //   75: invokevirtual 20	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   78: invokestatic 21	googletranslate/Translate:toString	(Ljava/io/InputStream;)Ljava/lang/String;
    //   81: astore 5
    //   83: new 22	googletranslate/JSONObject
    //   86: dup
    //   87: aload 5
    //   89: invokespecial 23	googletranslate/JSONObject:<init>	(Ljava/lang/String;)V
    //   92: astore 6
    //   94: aload 6
    //   96: ldc 24
    //   98: invokevirtual 25	googletranslate/JSONObject:get	(Ljava/lang/String;)Ljava/lang/Object;
    //   101: checkcast 22	googletranslate/JSONObject
    //   104: ldc 26
    //   106: invokevirtual 27	googletranslate/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   109: astore 7
    //   111: aload 7
    //   113: invokestatic 28	com/tecnick/htmlutils/htmlentities/HTMLEntities:unhtmlentities	(Ljava/lang/String;)Ljava/lang/String;
    //   116: astore 8
    //   118: aload 4
    //   120: invokevirtual 20	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   123: invokevirtual 29	java/io/InputStream:close	()V
    //   126: aload 4
    //   128: invokevirtual 30	java/net/HttpURLConnection:getErrorStream	()Ljava/io/InputStream;
    //   131: ifnull +11 -> 142
    //   134: aload 4
    //   136: invokevirtual 30	java/net/HttpURLConnection:getErrorStream	()Ljava/io/InputStream;
    //   139: invokevirtual 29	java/io/InputStream:close	()V
    //   142: aload 8
    //   144: areturn
    //   145: astore 9
    //   147: aload 4
    //   149: invokevirtual 20	java/net/HttpURLConnection:getInputStream	()Ljava/io/InputStream;
    //   152: invokevirtual 29	java/io/InputStream:close	()V
    //   155: aload 4
    //   157: invokevirtual 30	java/net/HttpURLConnection:getErrorStream	()Ljava/io/InputStream;
    //   160: ifnull +11 -> 171
    //   163: aload 4
    //   165: invokevirtual 30	java/net/HttpURLConnection:getErrorStream	()Ljava/io/InputStream;
    //   168: invokevirtual 29	java/io/InputStream:close	()V
    //   171: aload 9
    //   173: athrow
    //   174: astore_3
    //   175: new 31	java/lang/Exception
    //   178: dup
    //   179: ldc 32
    //   181: aload_3
    //   182: invokespecial 33	java/lang/Exception:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   185: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	186	0	paramString	String
    //   0	186	1	paramLanguage1	Language
    //   0	186	2	paramLanguage2	Language
    //   7	42	3	localStringBuilder	StringBuilder
    //   174	8	3	localException	Exception
    //   61	103	4	localHttpURLConnection	java.net.HttpURLConnection
    //   81	7	5	str1	String
    //   92	3	6	localJSONObject	JSONObject
    //   109	3	7	str2	String
    //   116	27	8	str3	String
    //   145	27	9	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   73	118	145	finally
    //   145	147	145	finally
    //   0	142	174	java/lang/Exception
    //   145	174	174	java/lang/Exception
  }
  
  private static String toString(InputStream paramInputStream)
    throws Exception
  {
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      if (paramInputStream != null)
      {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream, "UTF-8"));
        String str;
        while (null != (str = localBufferedReader.readLine())) {
          localStringBuilder.append(str).append('\n');
        }
      }
    }
    catch (Exception localException)
    {
      throw new Exception("[google-api-translate-java] Error reading translation stream.", localException);
    }
    return localStringBuilder.toString();
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/Translate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */