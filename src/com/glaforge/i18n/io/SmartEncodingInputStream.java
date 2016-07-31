package com.glaforge.i18n.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.Charset;

public class SmartEncodingInputStream
  extends InputStream
{
  private InputStream is;
  private int bufferLength;
  private boolean enforce8Bit;
  private Charset defaultCharset;
  private byte[] buffer;
  private int counter;
  private Charset charset;
  public static final int BUFFER_LENGTH_2KB = 2048;
  public static final int BUFFER_LENGTH_4KB = 4096;
  public static final int BUFFER_LENGTH_8KB = 8192;
  
  public SmartEncodingInputStream(InputStream paramInputStream, int paramInt, Charset paramCharset, boolean paramBoolean)
    throws IOException
  {
    this.is = paramInputStream;
    this.bufferLength = paramInt;
    this.enforce8Bit = paramBoolean;
    this.buffer = new byte[paramInt];
    this.counter = 0;
    this.bufferLength = paramInputStream.read(this.buffer);
    this.defaultCharset = paramCharset;
    CharsetToolkit localCharsetToolkit = new CharsetToolkit(this.buffer, paramCharset);
    localCharsetToolkit.setEnforce8Bit(paramBoolean);
    this.charset = localCharsetToolkit.guessEncoding();
  }
  
  public SmartEncodingInputStream(InputStream paramInputStream, int paramInt, Charset paramCharset)
    throws IOException
  {
    this(paramInputStream, paramInt, paramCharset, true);
  }
  
  public SmartEncodingInputStream(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    this(paramInputStream, paramInt, null, true);
  }
  
  public SmartEncodingInputStream(InputStream paramInputStream)
    throws IOException
  {
    this(paramInputStream, 4096, null, true);
  }
  
  public int read()
    throws IOException
  {
    if (this.counter < this.bufferLength) {
      return this.buffer[(this.counter++)];
    }
    return this.is.read();
  }
  
  public Reader getReader()
  {
    return new InputStreamReader(this, this.charset);
  }
  
  public Charset getEncoding()
  {
    return this.charset;
  }
  
  public static void main(String[] paramArrayOfString)
    throws IOException
  {
    FileInputStream localFileInputStream = new FileInputStream("us-ascii.txt");
    SmartEncodingInputStream localSmartEncodingInputStream = new SmartEncodingInputStream(localFileInputStream);
    System.err.println("The charset of this input stream is: " + localSmartEncodingInputStream.getEncoding().displayName());
    Reader localReader = localSmartEncodingInputStream.getReader();
    BufferedReader localBufferedReader = new BufferedReader(localReader);
    String str;
    while ((str = localBufferedReader.readLine()) != null) {
      System.out.println(str);
    }
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/com/glaforge/i18n/io/SmartEncodingInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */