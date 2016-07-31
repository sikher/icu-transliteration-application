package com.glaforge.i18n.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.SortedMap;

public class CharsetToolkit
{
  private byte[] buffer;
  private Charset defaultCharset;
  private boolean enforce8Bit = false;
  
  public CharsetToolkit(byte[] paramArrayOfByte)
  {
    this.buffer = paramArrayOfByte;
    this.defaultCharset = getDefaultSystemCharset();
  }
  
  public CharsetToolkit(byte[] paramArrayOfByte, Charset paramCharset)
  {
    this.buffer = paramArrayOfByte;
    setDefaultCharset(paramCharset);
  }
  
  public void setDefaultCharset(Charset paramCharset)
  {
    if (paramCharset != null) {
      this.defaultCharset = paramCharset;
    } else {
      this.defaultCharset = getDefaultSystemCharset();
    }
  }
  
  public void setEnforce8Bit(boolean paramBoolean)
  {
    this.enforce8Bit = paramBoolean;
  }
  
  public boolean getEnforce8Bit()
  {
    return this.enforce8Bit;
  }
  
  public Charset getDefaultCharset()
  {
    return this.defaultCharset;
  }
  
  public Charset guessEncoding()
  {
    if (hasUTF8Bom(this.buffer)) {
      return Charset.forName("UTF-8");
    }
    if (hasUTF16LEBom(this.buffer)) {
      return Charset.forName("UTF-16LE");
    }
    if (hasUTF16BEBom(this.buffer)) {
      return Charset.forName("UTF-16BE");
    }
    int i = 0;
    int j = 1;
    int k = this.buffer.length;
    for (int m = 0; m < k - 6; m++)
    {
      byte b1 = this.buffer[m];
      byte b2 = this.buffer[(m + 1)];
      byte b3 = this.buffer[(m + 2)];
      byte b4 = this.buffer[(m + 3)];
      byte b5 = this.buffer[(m + 4)];
      byte b6 = this.buffer[(m + 5)];
      if (b1 < 0)
      {
        i = 1;
        if (isTwoBytesSequence(b1))
        {
          if (!isContinuationChar(b2)) {
            j = 0;
          } else {
            m++;
          }
        }
        else if (isThreeBytesSequence(b1))
        {
          if ((!isContinuationChar(b2)) || (!isContinuationChar(b3))) {
            j = 0;
          } else {
            m += 2;
          }
        }
        else if (isFourBytesSequence(b1))
        {
          if ((!isContinuationChar(b2)) || (!isContinuationChar(b3)) || (!isContinuationChar(b4))) {
            j = 0;
          } else {
            m += 3;
          }
        }
        else if (isFiveBytesSequence(b1))
        {
          if ((!isContinuationChar(b2)) || (!isContinuationChar(b3)) || (!isContinuationChar(b4)) || (!isContinuationChar(b5))) {
            j = 0;
          } else {
            m += 4;
          }
        }
        else if (isSixBytesSequence(b1))
        {
          if ((!isContinuationChar(b2)) || (!isContinuationChar(b3)) || (!isContinuationChar(b4)) || (!isContinuationChar(b5)) || (!isContinuationChar(b6))) {
            j = 0;
          } else {
            m += 5;
          }
        }
        else {
          j = 0;
        }
      }
      if (j == 0) {
        break;
      }
    }
    if (i == 0)
    {
      if (this.enforce8Bit) {
        return this.defaultCharset;
      }
      return Charset.forName("US-ASCII");
    }
    if (j != 0) {
      return Charset.forName("UTF-8");
    }
    return this.defaultCharset;
  }
  
  public static Charset guessEncoding(File paramFile, int paramInt)
    throws FileNotFoundException, IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(paramFile);
    byte[] arrayOfByte = new byte[paramInt];
    localFileInputStream.read(arrayOfByte);
    localFileInputStream.close();
    CharsetToolkit localCharsetToolkit = new CharsetToolkit(arrayOfByte);
    localCharsetToolkit.setDefaultCharset(getDefaultSystemCharset());
    return localCharsetToolkit.guessEncoding();
  }
  
  public static Charset guessEncoding(File paramFile, int paramInt, Charset paramCharset)
    throws FileNotFoundException, IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(paramFile);
    byte[] arrayOfByte = new byte[paramInt];
    localFileInputStream.read(arrayOfByte);
    localFileInputStream.close();
    CharsetToolkit localCharsetToolkit = new CharsetToolkit(arrayOfByte);
    localCharsetToolkit.setDefaultCharset(paramCharset);
    return localCharsetToolkit.guessEncoding();
  }
  
  private static boolean isContinuationChar(byte paramByte)
  {
    return (Byte.MIN_VALUE <= paramByte) && (paramByte <= -65);
  }
  
  private static boolean isTwoBytesSequence(byte paramByte)
  {
    return (-64 <= paramByte) && (paramByte <= -33);
  }
  
  private static boolean isThreeBytesSequence(byte paramByte)
  {
    return (-32 <= paramByte) && (paramByte <= -17);
  }
  
  private static boolean isFourBytesSequence(byte paramByte)
  {
    return (-16 <= paramByte) && (paramByte <= -9);
  }
  
  private static boolean isFiveBytesSequence(byte paramByte)
  {
    return (-8 <= paramByte) && (paramByte <= -5);
  }
  
  private static boolean isSixBytesSequence(byte paramByte)
  {
    return (-4 <= paramByte) && (paramByte <= -3);
  }
  
  public static Charset getDefaultSystemCharset()
  {
    return Charset.forName(System.getProperty("file.encoding"));
  }
  
  private static boolean hasUTF8Bom(byte[] paramArrayOfByte)
  {
    return (paramArrayOfByte[0] == -17) && (paramArrayOfByte[1] == -69) && (paramArrayOfByte[2] == -65);
  }
  
  private static boolean hasUTF16LEBom(byte[] paramArrayOfByte)
  {
    return (paramArrayOfByte[0] == -1) && (paramArrayOfByte[1] == -2);
  }
  
  private static boolean hasUTF16BEBom(byte[] paramArrayOfByte)
  {
    return (paramArrayOfByte[0] == -2) && (paramArrayOfByte[1] == -1);
  }
  
  public static Charset[] getAvailableCharsets()
  {
    Collection localCollection = Charset.availableCharsets().values();
    return (Charset[])localCollection.toArray(new Charset[localCollection.size()]);
  }
  
  public static void main(String[] paramArrayOfString)
    throws FileNotFoundException, IOException
  {
    File localFile = new File("windows-1252.txt");
    Charset localCharset = guessEncoding(localFile, 4096);
    System.err.println("Charset found: " + localCharset.displayName());
    FileInputStream localFileInputStream = new FileInputStream(localFile);
    InputStreamReader localInputStreamReader = new InputStreamReader(localFileInputStream, localCharset);
    BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
    String str;
    while ((str = localBufferedReader.readLine()) != null) {
      System.out.println(str);
    }
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/com/glaforge/i18n/io/CharsetToolkit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */