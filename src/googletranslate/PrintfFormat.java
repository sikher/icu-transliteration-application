package googletranslate;

import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

public class PrintfFormat
{
  private Vector vFmt = new Vector();
  private int cPos = 0;
  private DecimalFormatSymbols dfs = null;
  
  public PrintfFormat(String paramString)
    throws IllegalArgumentException
  {
    this(Locale.getDefault(), paramString);
  }
  
  public PrintfFormat(Locale paramLocale, String paramString)
    throws IllegalArgumentException
  {
    this.dfs = new DecimalFormatSymbols(paramLocale);
    int i = 0;
    ConversionSpecification localConversionSpecification = null;
    String str = nonControl(paramString, 0);
    if (str != null)
    {
      localConversionSpecification = new ConversionSpecification();
      localConversionSpecification.setLiteral(str);
      this.vFmt.addElement(localConversionSpecification);
    }
    while ((this.cPos != -1) && (this.cPos < paramString.length()))
    {
      for (i = this.cPos + 1; i < paramString.length(); i++)
      {
        int j = 0;
        j = paramString.charAt(i);
        if ((j == 105) || (j == 100) || (j == 102) || (j == 103) || (j == 71) || (j == 111) || (j == 120) || (j == 88) || (j == 101) || (j == 69) || (j == 99) || (j == 115) || (j == 37)) {
          break;
        }
      }
      i = Math.min(i + 1, paramString.length());
      localConversionSpecification = new ConversionSpecification(paramString.substring(this.cPos, i));
      this.vFmt.addElement(localConversionSpecification);
      str = nonControl(paramString, i);
      if (str != null)
      {
        localConversionSpecification = new ConversionSpecification();
        localConversionSpecification.setLiteral(str);
        this.vFmt.addElement(localConversionSpecification);
      }
    }
  }
  
  private String nonControl(String paramString, int paramInt)
  {
    String str = "";
    this.cPos = paramString.indexOf("%", paramInt);
    if (this.cPos == -1) {
      this.cPos = paramString.length();
    }
    return paramString.substring(paramInt, this.cPos);
  }
  
  public String sprintf(Object[] paramArrayOfObject)
  {
    Enumeration localEnumeration = this.vFmt.elements();
    ConversionSpecification localConversionSpecification = null;
    int i = 0;
    int j = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    while (localEnumeration.hasMoreElements())
    {
      localConversionSpecification = (ConversionSpecification)localEnumeration.nextElement();
      i = localConversionSpecification.getConversionCharacter();
      if (i == 0)
      {
        localStringBuffer.append(localConversionSpecification.getLiteral());
      }
      else if (i == 37)
      {
        localStringBuffer.append("%");
      }
      else
      {
        if (localConversionSpecification.isPositionalSpecification())
        {
          j = localConversionSpecification.getArgumentPosition() - 1;
          int k;
          if (localConversionSpecification.isPositionalFieldWidth())
          {
            k = localConversionSpecification.getArgumentPositionForFieldWidth() - 1;
            localConversionSpecification.setFieldWidthWithArg(((Integer)paramArrayOfObject[k]).intValue());
          }
          if (localConversionSpecification.isPositionalPrecision())
          {
            k = localConversionSpecification.getArgumentPositionForPrecision() - 1;
            localConversionSpecification.setPrecisionWithArg(((Integer)paramArrayOfObject[k]).intValue());
          }
        }
        else
        {
          if (localConversionSpecification.isVariableFieldWidth())
          {
            localConversionSpecification.setFieldWidthWithArg(((Integer)paramArrayOfObject[j]).intValue());
            j++;
          }
          if (localConversionSpecification.isVariablePrecision())
          {
            localConversionSpecification.setPrecisionWithArg(((Integer)paramArrayOfObject[j]).intValue());
            j++;
          }
        }
        if ((paramArrayOfObject[j] instanceof Byte)) {
          localStringBuffer.append(localConversionSpecification.internalsprintf(((Byte)paramArrayOfObject[j]).byteValue()));
        } else if ((paramArrayOfObject[j] instanceof Short)) {
          localStringBuffer.append(localConversionSpecification.internalsprintf(((Short)paramArrayOfObject[j]).shortValue()));
        } else if ((paramArrayOfObject[j] instanceof Integer)) {
          localStringBuffer.append(localConversionSpecification.internalsprintf(((Integer)paramArrayOfObject[j]).intValue()));
        } else if ((paramArrayOfObject[j] instanceof Long)) {
          localStringBuffer.append(localConversionSpecification.internalsprintf(((Long)paramArrayOfObject[j]).longValue()));
        } else if ((paramArrayOfObject[j] instanceof Float)) {
          localStringBuffer.append(localConversionSpecification.internalsprintf(((Float)paramArrayOfObject[j]).floatValue()));
        } else if ((paramArrayOfObject[j] instanceof Double)) {
          localStringBuffer.append(localConversionSpecification.internalsprintf(((Double)paramArrayOfObject[j]).doubleValue()));
        } else if ((paramArrayOfObject[j] instanceof Character)) {
          localStringBuffer.append(localConversionSpecification.internalsprintf(((Character)paramArrayOfObject[j]).charValue()));
        } else if ((paramArrayOfObject[j] instanceof String)) {
          localStringBuffer.append(localConversionSpecification.internalsprintf((String)paramArrayOfObject[j]));
        } else {
          localStringBuffer.append(localConversionSpecification.internalsprintf(paramArrayOfObject[j]));
        }
        if (!localConversionSpecification.isPositionalSpecification()) {
          j++;
        }
      }
    }
    return localStringBuffer.toString();
  }
  
  public String sprintf()
  {
    Enumeration localEnumeration = this.vFmt.elements();
    ConversionSpecification localConversionSpecification = null;
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    while (localEnumeration.hasMoreElements())
    {
      localConversionSpecification = (ConversionSpecification)localEnumeration.nextElement();
      i = localConversionSpecification.getConversionCharacter();
      if (i == 0) {
        localStringBuffer.append(localConversionSpecification.getLiteral());
      } else if (i == 37) {
        localStringBuffer.append("%");
      }
    }
    return localStringBuffer.toString();
  }
  
  public String sprintf(int paramInt)
    throws IllegalArgumentException
  {
    Enumeration localEnumeration = this.vFmt.elements();
    ConversionSpecification localConversionSpecification = null;
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    while (localEnumeration.hasMoreElements())
    {
      localConversionSpecification = (ConversionSpecification)localEnumeration.nextElement();
      i = localConversionSpecification.getConversionCharacter();
      if (i == 0) {
        localStringBuffer.append(localConversionSpecification.getLiteral());
      } else if (i == 37) {
        localStringBuffer.append("%");
      } else {
        localStringBuffer.append(localConversionSpecification.internalsprintf(paramInt));
      }
    }
    return localStringBuffer.toString();
  }
  
  public String sprintf(long paramLong)
    throws IllegalArgumentException
  {
    Enumeration localEnumeration = this.vFmt.elements();
    ConversionSpecification localConversionSpecification = null;
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    while (localEnumeration.hasMoreElements())
    {
      localConversionSpecification = (ConversionSpecification)localEnumeration.nextElement();
      i = localConversionSpecification.getConversionCharacter();
      if (i == 0) {
        localStringBuffer.append(localConversionSpecification.getLiteral());
      } else if (i == 37) {
        localStringBuffer.append("%");
      } else {
        localStringBuffer.append(localConversionSpecification.internalsprintf(paramLong));
      }
    }
    return localStringBuffer.toString();
  }
  
  public String sprintf(double paramDouble)
    throws IllegalArgumentException
  {
    Enumeration localEnumeration = this.vFmt.elements();
    ConversionSpecification localConversionSpecification = null;
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    while (localEnumeration.hasMoreElements())
    {
      localConversionSpecification = (ConversionSpecification)localEnumeration.nextElement();
      i = localConversionSpecification.getConversionCharacter();
      if (i == 0) {
        localStringBuffer.append(localConversionSpecification.getLiteral());
      } else if (i == 37) {
        localStringBuffer.append("%");
      } else {
        localStringBuffer.append(localConversionSpecification.internalsprintf(paramDouble));
      }
    }
    return localStringBuffer.toString();
  }
  
  public String sprintf(String paramString)
    throws IllegalArgumentException
  {
    Enumeration localEnumeration = this.vFmt.elements();
    ConversionSpecification localConversionSpecification = null;
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    while (localEnumeration.hasMoreElements())
    {
      localConversionSpecification = (ConversionSpecification)localEnumeration.nextElement();
      i = localConversionSpecification.getConversionCharacter();
      if (i == 0) {
        localStringBuffer.append(localConversionSpecification.getLiteral());
      } else if (i == 37) {
        localStringBuffer.append("%");
      } else {
        localStringBuffer.append(localConversionSpecification.internalsprintf(paramString));
      }
    }
    return localStringBuffer.toString();
  }
  
  public String sprintf(Object paramObject)
    throws IllegalArgumentException
  {
    Enumeration localEnumeration = this.vFmt.elements();
    ConversionSpecification localConversionSpecification = null;
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    while (localEnumeration.hasMoreElements())
    {
      localConversionSpecification = (ConversionSpecification)localEnumeration.nextElement();
      i = localConversionSpecification.getConversionCharacter();
      if (i == 0) {
        localStringBuffer.append(localConversionSpecification.getLiteral());
      } else if (i == 37) {
        localStringBuffer.append("%");
      } else if ((paramObject instanceof Byte)) {
        localStringBuffer.append(localConversionSpecification.internalsprintf(((Byte)paramObject).byteValue()));
      } else if ((paramObject instanceof Short)) {
        localStringBuffer.append(localConversionSpecification.internalsprintf(((Short)paramObject).shortValue()));
      } else if ((paramObject instanceof Integer)) {
        localStringBuffer.append(localConversionSpecification.internalsprintf(((Integer)paramObject).intValue()));
      } else if ((paramObject instanceof Long)) {
        localStringBuffer.append(localConversionSpecification.internalsprintf(((Long)paramObject).longValue()));
      } else if ((paramObject instanceof Float)) {
        localStringBuffer.append(localConversionSpecification.internalsprintf(((Float)paramObject).floatValue()));
      } else if ((paramObject instanceof Double)) {
        localStringBuffer.append(localConversionSpecification.internalsprintf(((Double)paramObject).doubleValue()));
      } else if ((paramObject instanceof Character)) {
        localStringBuffer.append(localConversionSpecification.internalsprintf(((Character)paramObject).charValue()));
      } else if ((paramObject instanceof String)) {
        localStringBuffer.append(localConversionSpecification.internalsprintf((String)paramObject));
      } else {
        localStringBuffer.append(localConversionSpecification.internalsprintf(paramObject));
      }
    }
    return localStringBuffer.toString();
  }
  
  private class ConversionSpecification
  {
    private boolean thousands = false;
    private boolean leftJustify = false;
    private boolean leadingSign = false;
    private boolean leadingSpace = false;
    private boolean alternateForm = false;
    private boolean leadingZeros = false;
    private boolean variableFieldWidth = false;
    private int fieldWidth = 0;
    private boolean fieldWidthSet = false;
    private int precision = 0;
    private static final int defaultDigits = 6;
    private boolean variablePrecision = false;
    private boolean precisionSet = false;
    private boolean positionalSpecification = false;
    private int argumentPosition = 0;
    private boolean positionalFieldWidth = false;
    private int argumentPositionForFieldWidth = 0;
    private boolean positionalPrecision = false;
    private int argumentPositionForPrecision = 0;
    private boolean optionalh = false;
    private boolean optionall = false;
    private boolean optionalL = false;
    private char conversionCharacter = '\000';
    private int pos = 0;
    private String fmt;
    
    ConversionSpecification() {}
    
    ConversionSpecification(String paramString)
      throws IllegalArgumentException
    {
      if (paramString == null) {
        throw new NullPointerException();
      }
      if (paramString.length() == 0) {
        throw new IllegalArgumentException("Control strings must have positive lengths.");
      }
      if (paramString.charAt(0) == '%')
      {
        this.fmt = paramString;
        this.pos = 1;
        setArgPosition();
        setFlagCharacters();
        setFieldWidth();
        setPrecision();
        setOptionalHL();
        if (setConversionCharacter())
        {
          if (this.pos == paramString.length())
          {
            if ((this.leadingZeros) && (this.leftJustify)) {
              this.leadingZeros = false;
            }
            if ((this.precisionSet) && (this.leadingZeros) && ((this.conversionCharacter == 'd') || (this.conversionCharacter == 'i') || (this.conversionCharacter == 'o') || (this.conversionCharacter == 'x'))) {
              this.leadingZeros = false;
            }
          }
          else
          {
            throw new IllegalArgumentException("Malformed conversion specification=" + paramString);
          }
        }
        else {
          throw new IllegalArgumentException("Malformed conversion specification=" + paramString);
        }
      }
      else
      {
        throw new IllegalArgumentException("Control strings must begin with %.");
      }
    }
    
    void setLiteral(String paramString)
    {
      this.fmt = paramString;
    }
    
    String getLiteral()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int i = 0;
      while (i < this.fmt.length()) {
        if (this.fmt.charAt(i) == '\\')
        {
          i++;
          if (i < this.fmt.length())
          {
            int j = this.fmt.charAt(i);
            switch (j)
            {
            case 97: 
              localStringBuffer.append('\007');
              break;
            case 98: 
              localStringBuffer.append('\b');
              break;
            case 102: 
              localStringBuffer.append('\f');
              break;
            case 110: 
              localStringBuffer.append(System.getProperty("line.separator"));
              break;
            case 114: 
              localStringBuffer.append('\r');
              break;
            case 116: 
              localStringBuffer.append('\t');
              break;
            case 118: 
              localStringBuffer.append('\013');
              break;
            case 92: 
              localStringBuffer.append('\\');
            }
            i++;
          }
          else
          {
            localStringBuffer.append('\\');
          }
        }
        else
        {
          i++;
        }
      }
      return this.fmt;
    }
    
    char getConversionCharacter()
    {
      return this.conversionCharacter;
    }
    
    boolean isVariableFieldWidth()
    {
      return this.variableFieldWidth;
    }
    
    void setFieldWidthWithArg(int paramInt)
    {
      if (paramInt < 0) {
        this.leftJustify = true;
      }
      this.fieldWidthSet = true;
      this.fieldWidth = Math.abs(paramInt);
    }
    
    boolean isVariablePrecision()
    {
      return this.variablePrecision;
    }
    
    void setPrecisionWithArg(int paramInt)
    {
      this.precisionSet = true;
      this.precision = Math.max(paramInt, 0);
    }
    
    String internalsprintf(int paramInt)
      throws IllegalArgumentException
    {
      String str = "";
      switch (this.conversionCharacter)
      {
      case 'd': 
      case 'i': 
        if (this.optionalh) {
          str = printDFormat((short)paramInt);
        } else if (this.optionall) {
          str = printDFormat(paramInt);
        } else {
          str = printDFormat(paramInt);
        }
        break;
      case 'X': 
      case 'x': 
        if (this.optionalh) {
          str = printXFormat((short)paramInt);
        } else if (this.optionall) {
          str = printXFormat(paramInt);
        } else {
          str = printXFormat(paramInt);
        }
        break;
      case 'o': 
        if (this.optionalh) {
          str = printOFormat((short)paramInt);
        } else if (this.optionall) {
          str = printOFormat(paramInt);
        } else {
          str = printOFormat(paramInt);
        }
        break;
      case 'C': 
      case 'c': 
        str = printCFormat((char)paramInt);
        break;
      default: 
        throw new IllegalArgumentException("Cannot format a int with a format using a " + this.conversionCharacter + " conversion character.");
      }
      return str;
    }
    
    String internalsprintf(long paramLong)
      throws IllegalArgumentException
    {
      String str = "";
      switch (this.conversionCharacter)
      {
      case 'd': 
      case 'i': 
        if (this.optionalh) {
          str = printDFormat((short)(int)paramLong);
        } else if (this.optionall) {
          str = printDFormat(paramLong);
        } else {
          str = printDFormat((int)paramLong);
        }
        break;
      case 'X': 
      case 'x': 
        if (this.optionalh) {
          str = printXFormat((short)(int)paramLong);
        } else if (this.optionall) {
          str = printXFormat(paramLong);
        } else {
          str = printXFormat((int)paramLong);
        }
        break;
      case 'o': 
        if (this.optionalh) {
          str = printOFormat((short)(int)paramLong);
        } else if (this.optionall) {
          str = printOFormat(paramLong);
        } else {
          str = printOFormat((int)paramLong);
        }
        break;
      case 'C': 
      case 'c': 
        str = printCFormat((char)(int)paramLong);
        break;
      default: 
        throw new IllegalArgumentException("Cannot format a long with a format using a " + this.conversionCharacter + " conversion character.");
      }
      return str;
    }
    
    String internalsprintf(double paramDouble)
      throws IllegalArgumentException
    {
      String str = "";
      switch (this.conversionCharacter)
      {
      case 'f': 
        str = printFFormat(paramDouble);
        break;
      case 'E': 
      case 'e': 
        str = printEFormat(paramDouble);
        break;
      case 'G': 
      case 'g': 
        str = printGFormat(paramDouble);
        break;
      default: 
        throw new IllegalArgumentException("Cannot format a double with a format using a " + this.conversionCharacter + " conversion character.");
      }
      return str;
    }
    
    String internalsprintf(String paramString)
      throws IllegalArgumentException
    {
      String str = "";
      if ((this.conversionCharacter == 's') || (this.conversionCharacter == 'S')) {
        str = printSFormat(paramString);
      } else {
        throw new IllegalArgumentException("Cannot format a String with a format using a " + this.conversionCharacter + " conversion character.");
      }
      return str;
    }
    
    String internalsprintf(Object paramObject)
    {
      String str = "";
      if ((this.conversionCharacter == 's') || (this.conversionCharacter == 'S')) {
        str = printSFormat(paramObject.toString());
      } else {
        throw new IllegalArgumentException("Cannot format a String with a format using a " + this.conversionCharacter + " conversion character.");
      }
      return str;
    }
    
    private char[] fFormatDigits(double paramDouble)
    {
      int i1 = 0;
      int i2 = 0;
      String str;
      if (paramDouble > 0.0D)
      {
        str = Double.toString(paramDouble);
      }
      else if (paramDouble < 0.0D)
      {
        str = Double.toString(-paramDouble);
        i2 = 1;
      }
      else
      {
        str = Double.toString(paramDouble);
        if (str.charAt(0) == '-')
        {
          i2 = 1;
          str = str.substring(1);
        }
      }
      int i3 = str.indexOf('E');
      int i4 = str.indexOf('.');
      int m;
      if (i4 != -1) {
        m = i4;
      } else if (i3 != -1) {
        m = i3;
      } else {
        m = str.length();
      }
      int n;
      if (i4 != -1)
      {
        if (i3 != -1) {
          n = i3 - i4 - 1;
        } else {
          n = str.length() - i4 - 1;
        }
      }
      else {
        n = 0;
      }
      int i5;
      if (i3 != -1)
      {
        i5 = i3 + 1;
        i1 = 0;
        if (str.charAt(i5) == '-')
        {
          i5++;
          while ((i5 < str.length()) && (str.charAt(i5) == '0')) {
            i5++;
          }
          if (i5 < str.length()) {
            i1 = -Integer.parseInt(str.substring(i5));
          }
        }
        else
        {
          if (str.charAt(i5) == '+') {
            i5++;
          }
          while ((i5 < str.length()) && (str.charAt(i5) == '0')) {
            i5++;
          }
          if (i5 < str.length()) {
            i1 = Integer.parseInt(str.substring(i5));
          }
        }
      }
      if (this.precisionSet) {
        i5 = this.precision;
      } else {
        i5 = 5;
      }
      char[] arrayOfChar1 = str.toCharArray();
      char[] arrayOfChar2 = new char[m + n];
      for (int j = 0; j < m; j++) {
        arrayOfChar2[j] = arrayOfChar1[j];
      }
      int i = j + 1;
      for (int k = 0; k < n; k++)
      {
        arrayOfChar2[j] = arrayOfChar1[i];
        j++;
        i++;
      }
      if (m + i1 <= 0)
      {
        arrayOfChar3 = new char[-i1 + n];
        j = 0;
        k = 0;
        while (k < -m - i1)
        {
          arrayOfChar3[j] = '0';
          k++;
          j++;
        }
        i = 0;
        while (i < m + n)
        {
          arrayOfChar3[j] = arrayOfChar2[i];
          i++;
          j++;
        }
      }
      char[] arrayOfChar3 = arrayOfChar2;
      boolean bool = false;
      if (i5 < -i1 + n)
      {
        if (i1 < 0) {
          i = i5;
        } else {
          i = i5 + m;
        }
        bool = checkForCarry(arrayOfChar3, i);
        if (bool) {
          bool = startSymbolicCarry(arrayOfChar3, i - 1, 0);
        }
      }
      char[] arrayOfChar4;
      if (m + i1 <= 0)
      {
        arrayOfChar4 = new char[2 + i5];
        if (!bool) {
          arrayOfChar4[0] = '0';
        } else {
          arrayOfChar4[0] = '1';
        }
        if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
        {
          arrayOfChar4[1] = '.';
          i = 0;
          for (j = 2; i < Math.min(i5, arrayOfChar3.length); j++)
          {
            arrayOfChar4[j] = arrayOfChar3[i];
            i++;
          }
          while (j < arrayOfChar4.length)
          {
            arrayOfChar4[j] = '0';
            j++;
          }
        }
      }
      else
      {
        if (!bool)
        {
          if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0)) {
            arrayOfChar4 = new char[m + i1 + i5 + 1];
          } else {
            arrayOfChar4 = new char[m + i1];
          }
          j = 0;
        }
        else
        {
          if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0)) {
            arrayOfChar4 = new char[m + i1 + i5 + 2];
          } else {
            arrayOfChar4 = new char[m + i1 + 1];
          }
          arrayOfChar4[0] = '1';
          j = 1;
        }
        i = 0;
        while (i < Math.min(m + i1, arrayOfChar3.length))
        {
          arrayOfChar4[j] = arrayOfChar3[i];
          i++;
          j++;
        }
        while (i < m + i1)
        {
          arrayOfChar4[j] = '0';
          i++;
          j++;
        }
        if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
        {
          arrayOfChar4[j] = '.';
          j++;
          for (k = 0; (i < arrayOfChar3.length) && (k < i5); k++)
          {
            arrayOfChar4[j] = arrayOfChar3[i];
            i++;
            j++;
          }
          while (j < arrayOfChar4.length)
          {
            arrayOfChar4[j] = '0';
            j++;
          }
        }
      }
      int i6 = 0;
      if ((!this.leftJustify) && (this.leadingZeros))
      {
        i7 = 0;
        if (this.thousands)
        {
          i8 = 0;
          if ((arrayOfChar4[0] == '+') || (arrayOfChar4[0] == '-') || (arrayOfChar4[0] == ' ')) {
            i8 = 1;
          }
          for (i9 = i8; (i9 < arrayOfChar4.length) && (arrayOfChar4[i9] != '.'); i9++) {}
          i7 = (i9 - i8) / 3;
        }
        if (this.fieldWidthSet) {
          i6 = this.fieldWidth - arrayOfChar4.length;
        }
        if (((i2 == 0) && ((this.leadingSign) || (this.leadingSpace))) || (i2 != 0)) {
          i6--;
        }
        i6 -= i7;
        if (i6 < 0) {
          i6 = 0;
        }
      }
      j = 0;
      char[] arrayOfChar5;
      if (((i2 == 0) && ((this.leadingSign) || (this.leadingSpace))) || (i2 != 0))
      {
        arrayOfChar5 = new char[arrayOfChar4.length + i6 + 1];
        j++;
      }
      else
      {
        arrayOfChar5 = new char[arrayOfChar4.length + i6];
      }
      if (i2 == 0)
      {
        if (this.leadingSign) {
          arrayOfChar5[0] = '+';
        }
        if (this.leadingSpace) {
          arrayOfChar5[0] = ' ';
        }
      }
      else
      {
        arrayOfChar5[0] = '-';
      }
      i = 0;
      while (i < i6)
      {
        arrayOfChar5[j] = '0';
        i++;
        j++;
      }
      i = 0;
      while (i < arrayOfChar4.length)
      {
        arrayOfChar5[j] = arrayOfChar4[i];
        i++;
        j++;
      }
      int i7 = 0;
      if ((arrayOfChar5[0] == '+') || (arrayOfChar5[0] == '-') || (arrayOfChar5[0] == ' ')) {
        i7 = 1;
      }
      for (int i8 = i7; (i8 < arrayOfChar5.length) && (arrayOfChar5[i8] != '.'); i8++) {}
      int i9 = (i8 - i7) / 3;
      if (i8 < arrayOfChar5.length) {
        arrayOfChar5[i8] = PrintfFormat.this.dfs.getDecimalSeparator();
      }
      char[] arrayOfChar6 = arrayOfChar5;
      if ((this.thousands) && (i9 > 0))
      {
        arrayOfChar6 = new char[arrayOfChar5.length + i9 + i7];
        arrayOfChar6[0] = arrayOfChar5[0];
        i = i7;
        k = i7;
        while (i < i8)
        {
          if ((i > 0) && ((i8 - i) % 3 == 0))
          {
            arrayOfChar6[k] = PrintfFormat.this.dfs.getGroupingSeparator();
            arrayOfChar6[(k + 1)] = arrayOfChar5[i];
            k += 2;
          }
          else
          {
            arrayOfChar6[k] = arrayOfChar5[i];
            k++;
          }
          i++;
        }
        while (i < arrayOfChar5.length)
        {
          arrayOfChar6[k] = arrayOfChar5[i];
          i++;
          k++;
        }
      }
      return arrayOfChar6;
    }
    
    private String fFormatString(double paramDouble)
    {
      int i = 0;
      char[] arrayOfChar1;
      if (Double.isInfinite(paramDouble))
      {
        if (paramDouble == Double.POSITIVE_INFINITY)
        {
          if (this.leadingSign) {
            arrayOfChar1 = "+Inf".toCharArray();
          } else if (this.leadingSpace) {
            arrayOfChar1 = " Inf".toCharArray();
          } else {
            arrayOfChar1 = "Inf".toCharArray();
          }
        }
        else {
          arrayOfChar1 = "-Inf".toCharArray();
        }
        i = 1;
      }
      else if (Double.isNaN(paramDouble))
      {
        if (this.leadingSign) {
          arrayOfChar1 = "+NaN".toCharArray();
        } else if (this.leadingSpace) {
          arrayOfChar1 = " NaN".toCharArray();
        } else {
          arrayOfChar1 = "NaN".toCharArray();
        }
        i = 1;
      }
      else
      {
        arrayOfChar1 = fFormatDigits(paramDouble);
      }
      char[] arrayOfChar2 = applyFloatPadding(arrayOfChar1, false);
      return new String(arrayOfChar2);
    }
    
    private char[] eFormatDigits(double paramDouble, char paramChar)
    {
      int i2 = 0;
      int i6 = 0;
      String str;
      if (paramDouble > 0.0D)
      {
        str = Double.toString(paramDouble);
      }
      else if (paramDouble < 0.0D)
      {
        str = Double.toString(-paramDouble);
        i6 = 1;
      }
      else
      {
        str = Double.toString(paramDouble);
        if (str.charAt(0) == '-')
        {
          i6 = 1;
          str = str.substring(1);
        }
      }
      int i3 = str.indexOf('E');
      if (i3 == -1) {
        i3 = str.indexOf('e');
      }
      int i4 = str.indexOf('.');
      int n;
      if (i4 != -1) {
        n = i4;
      } else if (i3 != -1) {
        n = i3;
      } else {
        n = str.length();
      }
      int i1;
      if (i4 != -1)
      {
        if (i3 != -1) {
          i1 = i3 - i4 - 1;
        } else {
          i1 = str.length() - i4 - 1;
        }
      }
      else {
        i1 = 0;
      }
      if (i3 != -1)
      {
        i7 = i3 + 1;
        i2 = 0;
        if (str.charAt(i7) == '-')
        {
          i7++;
          while ((i7 < str.length()) && (str.charAt(i7) == '0')) {
            i7++;
          }
          if (i7 < str.length()) {
            i2 = -Integer.parseInt(str.substring(i7));
          }
        }
        else
        {
          if (str.charAt(i7) == '+') {
            i7++;
          }
          while ((i7 < str.length()) && (str.charAt(i7) == '0')) {
            i7++;
          }
          if (i7 < str.length()) {
            i2 = Integer.parseInt(str.substring(i7));
          }
        }
      }
      if (i4 != -1) {
        i2 += i4 - 1;
      }
      int m;
      if (this.precisionSet) {
        m = this.precision;
      } else {
        m = 5;
      }
      Object localObject;
      if ((i4 != -1) && (i3 != -1)) {
        localObject = (str.substring(0, i4) + str.substring(i4 + 1, i3)).toCharArray();
      } else if (i4 != -1) {
        localObject = (str.substring(0, i4) + str.substring(i4 + 1)).toCharArray();
      } else if (i3 != -1) {
        localObject = str.substring(0, i3).toCharArray();
      } else {
        localObject = str.toCharArray();
      }
      int i7 = 0;
      int i8 = 0;
      if (localObject[0] != '0') {
        i8 = 0;
      } else {
        for (i8 = 0; (i8 < localObject.length) && (localObject[i8] == '0'); i8++) {}
      }
      char[] arrayOfChar1;
      if (i8 + m < localObject.length - 1)
      {
        boolean bool = checkForCarry((char[])localObject, i8 + m + 1);
        if (bool) {
          bool = startSymbolicCarry((char[])localObject, i8 + m, i8);
        }
        if (bool)
        {
          arrayOfChar1 = new char[i8 + m + 1];
          arrayOfChar1[i8] = '1';
          for (j = 0; j < i8; j++) {
            arrayOfChar1[j] = '0';
          }
          i = i8;
          for (j = i8 + 1; j < m + 1; j++)
          {
            arrayOfChar1[j] = localObject[i];
            i++;
          }
          i2++;
          localObject = arrayOfChar1;
        }
      }
      int i5;
      if ((Math.abs(i2) < 100) && (!this.optionalL)) {
        i5 = 4;
      } else {
        i5 = 5;
      }
      if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0)) {
        arrayOfChar1 = new char[2 + m + i5];
      } else {
        arrayOfChar1 = new char[1 + i5];
      }
      if (localObject[0] != '0')
      {
        arrayOfChar1[0] = localObject[0];
        j = 1;
      }
      else
      {
        for (j = 1; j < (i3 == -1 ? localObject.length : i3); j++) {
          if (localObject[j] != '0') {
            break;
          }
        }
        if (((i3 != -1) && (j < i3)) || ((i3 == -1) && (j < localObject.length)))
        {
          arrayOfChar1[0] = localObject[j];
          i2 -= j;
          j++;
        }
        else
        {
          arrayOfChar1[0] = '0';
          j = 2;
        }
      }
      if ((this.alternateForm) || (!this.precisionSet) || (this.precision != 0))
      {
        arrayOfChar1[1] = '.';
        i = 2;
      }
      else
      {
        i = 1;
      }
      for (int k = 0; (k < m) && (j < localObject.length); k++)
      {
        arrayOfChar1[i] = localObject[j];
        j++;
        i++;
      }
      while (i < arrayOfChar1.length - i5)
      {
        arrayOfChar1[i] = '0';
        i++;
      }
      arrayOfChar1[(i++)] = paramChar;
      if (i2 < 0) {
        arrayOfChar1[(i++)] = '-';
      } else {
        arrayOfChar1[(i++)] = '+';
      }
      i2 = Math.abs(i2);
      if (i2 >= 100)
      {
        switch (i2 / 100)
        {
        case 1: 
          arrayOfChar1[i] = '1';
          break;
        case 2: 
          arrayOfChar1[i] = '2';
          break;
        case 3: 
          arrayOfChar1[i] = '3';
          break;
        case 4: 
          arrayOfChar1[i] = '4';
          break;
        case 5: 
          arrayOfChar1[i] = '5';
          break;
        case 6: 
          arrayOfChar1[i] = '6';
          break;
        case 7: 
          arrayOfChar1[i] = '7';
          break;
        case 8: 
          arrayOfChar1[i] = '8';
          break;
        case 9: 
          arrayOfChar1[i] = '9';
        }
        i++;
      }
      switch (i2 % 100 / 10)
      {
      case 0: 
        arrayOfChar1[i] = '0';
        break;
      case 1: 
        arrayOfChar1[i] = '1';
        break;
      case 2: 
        arrayOfChar1[i] = '2';
        break;
      case 3: 
        arrayOfChar1[i] = '3';
        break;
      case 4: 
        arrayOfChar1[i] = '4';
        break;
      case 5: 
        arrayOfChar1[i] = '5';
        break;
      case 6: 
        arrayOfChar1[i] = '6';
        break;
      case 7: 
        arrayOfChar1[i] = '7';
        break;
      case 8: 
        arrayOfChar1[i] = '8';
        break;
      case 9: 
        arrayOfChar1[i] = '9';
      }
      i++;
      switch (i2 % 10)
      {
      case 0: 
        arrayOfChar1[i] = '0';
        break;
      case 1: 
        arrayOfChar1[i] = '1';
        break;
      case 2: 
        arrayOfChar1[i] = '2';
        break;
      case 3: 
        arrayOfChar1[i] = '3';
        break;
      case 4: 
        arrayOfChar1[i] = '4';
        break;
      case 5: 
        arrayOfChar1[i] = '5';
        break;
      case 6: 
        arrayOfChar1[i] = '6';
        break;
      case 7: 
        arrayOfChar1[i] = '7';
        break;
      case 8: 
        arrayOfChar1[i] = '8';
        break;
      case 9: 
        arrayOfChar1[i] = '9';
      }
      int i9 = 0;
      if ((!this.leftJustify) && (this.leadingZeros))
      {
        i10 = 0;
        if (this.thousands)
        {
          i11 = 0;
          if ((arrayOfChar1[0] == '+') || (arrayOfChar1[0] == '-') || (arrayOfChar1[0] == ' ')) {
            i11 = 1;
          }
          for (i12 = i11; (i12 < arrayOfChar1.length) && (arrayOfChar1[i12] != '.'); i12++) {}
          i10 = (i12 - i11) / 3;
        }
        if (this.fieldWidthSet) {
          i9 = this.fieldWidth - arrayOfChar1.length;
        }
        if (((i6 == 0) && ((this.leadingSign) || (this.leadingSpace))) || (i6 != 0)) {
          i9--;
        }
        i9 -= i10;
        if (i9 < 0) {
          i9 = 0;
        }
      }
      int j = 0;
      char[] arrayOfChar2;
      if (((i6 == 0) && ((this.leadingSign) || (this.leadingSpace))) || (i6 != 0))
      {
        arrayOfChar2 = new char[arrayOfChar1.length + i9 + 1];
        j++;
      }
      else
      {
        arrayOfChar2 = new char[arrayOfChar1.length + i9];
      }
      if (i6 == 0)
      {
        if (this.leadingSign) {
          arrayOfChar2[0] = '+';
        }
        if (this.leadingSpace) {
          arrayOfChar2[0] = ' ';
        }
      }
      else
      {
        arrayOfChar2[0] = '-';
      }
      for (k = 0; k < i9; k++)
      {
        arrayOfChar2[j] = '0';
        j++;
      }
      int i = 0;
      while ((i < arrayOfChar1.length) && (j < arrayOfChar2.length))
      {
        arrayOfChar2[j] = arrayOfChar1[i];
        i++;
        j++;
      }
      int i10 = 0;
      if ((arrayOfChar2[0] == '+') || (arrayOfChar2[0] == '-') || (arrayOfChar2[0] == ' ')) {
        i10 = 1;
      }
      for (int i11 = i10; (i11 < arrayOfChar2.length) && (arrayOfChar2[i11] != '.'); i11++) {}
      int i12 = i11 / 3;
      if (i11 < arrayOfChar2.length) {
        arrayOfChar2[i11] = PrintfFormat.this.dfs.getDecimalSeparator();
      }
      char[] arrayOfChar3 = arrayOfChar2;
      if ((this.thousands) && (i12 > 0))
      {
        arrayOfChar3 = new char[arrayOfChar2.length + i12 + i10];
        arrayOfChar3[0] = arrayOfChar2[0];
        i = i10;
        k = i10;
        while (i < i11)
        {
          if ((i > 0) && ((i11 - i) % 3 == 0))
          {
            arrayOfChar3[k] = PrintfFormat.this.dfs.getGroupingSeparator();
            arrayOfChar3[(k + 1)] = arrayOfChar2[i];
            k += 2;
          }
          else
          {
            arrayOfChar3[k] = arrayOfChar2[i];
            k++;
          }
          i++;
        }
        while (i < arrayOfChar2.length)
        {
          arrayOfChar3[k] = arrayOfChar2[i];
          i++;
          k++;
        }
      }
      return arrayOfChar3;
    }
    
    private boolean checkForCarry(char[] paramArrayOfChar, int paramInt)
    {
      boolean bool = false;
      if (paramInt < paramArrayOfChar.length) {
        if ((paramArrayOfChar[paramInt] == '6') || (paramArrayOfChar[paramInt] == '7') || (paramArrayOfChar[paramInt] == '8') || (paramArrayOfChar[paramInt] == '9'))
        {
          bool = true;
        }
        else if (paramArrayOfChar[paramInt] == '5')
        {
          for (int i = paramInt + 1; (i < paramArrayOfChar.length) && (paramArrayOfChar[i] == '0'); i++) {}
          bool = i < paramArrayOfChar.length;
          if ((!bool) && (paramInt > 0)) {
            bool = (paramArrayOfChar[(paramInt - 1)] == '1') || (paramArrayOfChar[(paramInt - 1)] == '3') || (paramArrayOfChar[(paramInt - 1)] == '5') || (paramArrayOfChar[(paramInt - 1)] == '7') || (paramArrayOfChar[(paramInt - 1)] == '9');
          }
        }
      }
      return bool;
    }
    
    private boolean startSymbolicCarry(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      boolean bool = true;
      for (int i = paramInt1; (bool) && (i >= paramInt2); i--)
      {
        bool = false;
        switch (paramArrayOfChar[i])
        {
        case '0': 
          paramArrayOfChar[i] = '1';
          break;
        case '1': 
          paramArrayOfChar[i] = '2';
          break;
        case '2': 
          paramArrayOfChar[i] = '3';
          break;
        case '3': 
          paramArrayOfChar[i] = '4';
          break;
        case '4': 
          paramArrayOfChar[i] = '5';
          break;
        case '5': 
          paramArrayOfChar[i] = '6';
          break;
        case '6': 
          paramArrayOfChar[i] = '7';
          break;
        case '7': 
          paramArrayOfChar[i] = '8';
          break;
        case '8': 
          paramArrayOfChar[i] = '9';
          break;
        case '9': 
          paramArrayOfChar[i] = '0';
          bool = true;
        }
      }
      return bool;
    }
    
    private String eFormatString(double paramDouble, char paramChar)
    {
      int i = 0;
      char[] arrayOfChar1;
      if (Double.isInfinite(paramDouble))
      {
        if (paramDouble == Double.POSITIVE_INFINITY)
        {
          if (this.leadingSign) {
            arrayOfChar1 = "+Inf".toCharArray();
          } else if (this.leadingSpace) {
            arrayOfChar1 = " Inf".toCharArray();
          } else {
            arrayOfChar1 = "Inf".toCharArray();
          }
        }
        else {
          arrayOfChar1 = "-Inf".toCharArray();
        }
        i = 1;
      }
      else if (Double.isNaN(paramDouble))
      {
        if (this.leadingSign) {
          arrayOfChar1 = "+NaN".toCharArray();
        } else if (this.leadingSpace) {
          arrayOfChar1 = " NaN".toCharArray();
        } else {
          arrayOfChar1 = "NaN".toCharArray();
        }
        i = 1;
      }
      else
      {
        arrayOfChar1 = eFormatDigits(paramDouble, paramChar);
      }
      char[] arrayOfChar2 = applyFloatPadding(arrayOfChar1, false);
      return new String(arrayOfChar2);
    }
    
    private char[] applyFloatPadding(char[] paramArrayOfChar, boolean paramBoolean)
    {
      char[] arrayOfChar = paramArrayOfChar;
      if (this.fieldWidthSet)
      {
        int k;
        int i;
        int j;
        if (this.leftJustify)
        {
          k = this.fieldWidth - paramArrayOfChar.length;
          if (k > 0)
          {
            arrayOfChar = new char[paramArrayOfChar.length + k];
            for (i = 0; i < paramArrayOfChar.length; i++) {
              arrayOfChar[i] = paramArrayOfChar[i];
            }
            j = 0;
            while (j < k)
            {
              arrayOfChar[i] = ' ';
              j++;
              i++;
            }
          }
        }
        else if ((!this.leadingZeros) || (paramBoolean))
        {
          k = this.fieldWidth - paramArrayOfChar.length;
          if (k > 0)
          {
            arrayOfChar = new char[paramArrayOfChar.length + k];
            for (i = 0; i < k; i++) {
              arrayOfChar[i] = ' ';
            }
            j = 0;
          }
        }
        else
        {
          while (j < paramArrayOfChar.length)
          {
            arrayOfChar[i] = paramArrayOfChar[j];
            i++;
            j++;
            continue;
            if (this.leadingZeros)
            {
              k = this.fieldWidth - paramArrayOfChar.length;
              if (k > 0)
              {
                arrayOfChar = new char[paramArrayOfChar.length + k];
                i = 0;
                j = 0;
                if (paramArrayOfChar[0] == '-')
                {
                  arrayOfChar[0] = '-';
                  i++;
                  j++;
                }
                for (int m = 0; m < k; m++)
                {
                  arrayOfChar[i] = '0';
                  i++;
                }
                while (j < paramArrayOfChar.length)
                {
                  arrayOfChar[i] = paramArrayOfChar[j];
                  i++;
                  j++;
                }
              }
            }
          }
        }
      }
      return arrayOfChar;
    }
    
    private String printFFormat(double paramDouble)
    {
      return fFormatString(paramDouble);
    }
    
    private String printEFormat(double paramDouble)
    {
      if (this.conversionCharacter == 'e') {
        return eFormatString(paramDouble, 'e');
      }
      return eFormatString(paramDouble, 'E');
    }
    
    private String printGFormat(double paramDouble)
    {
      int i = this.precision;
      int k = 0;
      char[] arrayOfChar1;
      if (Double.isInfinite(paramDouble))
      {
        if (paramDouble == Double.POSITIVE_INFINITY)
        {
          if (this.leadingSign) {
            arrayOfChar1 = "+Inf".toCharArray();
          } else if (this.leadingSpace) {
            arrayOfChar1 = " Inf".toCharArray();
          } else {
            arrayOfChar1 = "Inf".toCharArray();
          }
        }
        else {
          arrayOfChar1 = "-Inf".toCharArray();
        }
        k = 1;
      }
      else if (Double.isNaN(paramDouble))
      {
        if (this.leadingSign) {
          arrayOfChar1 = "+NaN".toCharArray();
        } else if (this.leadingSpace) {
          arrayOfChar1 = " NaN".toCharArray();
        } else {
          arrayOfChar1 = "NaN".toCharArray();
        }
        k = 1;
      }
      else
      {
        if (!this.precisionSet) {
          this.precision = 6;
        }
        if (this.precision == 0) {
          this.precision = 1;
        }
        int m = -1;
        String str1;
        if (this.conversionCharacter == 'g')
        {
          str1 = eFormatString(paramDouble, 'e').trim();
          m = str1.indexOf('e');
        }
        else
        {
          str1 = eFormatString(paramDouble, 'E').trim();
          m = str1.indexOf('E');
        }
        int j = m + 1;
        int n = 0;
        if (str1.charAt(j) == '-')
        {
          j++;
          while ((j < str1.length()) && (str1.charAt(j) == '0')) {
            j++;
          }
          if (j < str1.length()) {
            n = -Integer.parseInt(str1.substring(j));
          }
        }
        else
        {
          if (str1.charAt(j) == '+') {
            j++;
          }
          while ((j < str1.length()) && (str1.charAt(j) == '0')) {
            j++;
          }
          if (j < str1.length()) {
            n = Integer.parseInt(str1.substring(j));
          }
        }
        String str4;
        if (!this.alternateForm)
        {
          String str2;
          if ((n >= -4) && (n < this.precision)) {
            str2 = fFormatString(paramDouble).trim();
          } else {
            str2 = str1.substring(0, m);
          }
          for (j = str2.length() - 1; (j >= 0) && (str2.charAt(j) == '0'); j--) {}
          if ((j >= 0) && (str2.charAt(j) == '.')) {
            j--;
          }
          String str3;
          if (j == -1) {
            str3 = "0";
          } else if (!Character.isDigit(str2.charAt(j))) {
            str3 = str2.substring(0, j + 1) + "0";
          } else {
            str3 = str2.substring(0, j + 1);
          }
          if ((n >= -4) && (n < this.precision)) {
            str4 = str3;
          } else {
            str4 = str3 + str1.substring(m);
          }
        }
        else if ((n >= -4) && (n < this.precision))
        {
          str4 = fFormatString(paramDouble).trim();
        }
        else
        {
          str4 = str1;
        }
        if ((this.leadingSpace) && (paramDouble >= 0.0D)) {
          str4 = " " + str4;
        }
        arrayOfChar1 = str4.toCharArray();
      }
      char[] arrayOfChar2 = applyFloatPadding(arrayOfChar1, false);
      this.precision = i;
      return new String(arrayOfChar2);
    }
    
    private String printDFormat(short paramShort)
    {
      return printDFormat(Short.toString(paramShort));
    }
    
    private String printDFormat(long paramLong)
    {
      return printDFormat(Long.toString(paramLong));
    }
    
    private String printDFormat(int paramInt)
    {
      return printDFormat(Integer.toString(paramInt));
    }
    
    private String printDFormat(String paramString)
    {
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      int n = 0;
      int i1 = paramString.charAt(0) == '-' ? 1 : 0;
      if ((paramString.equals("0")) && (this.precisionSet) && (this.precision == 0)) {
        paramString = "";
      }
      if (i1 == 0)
      {
        if ((this.precisionSet) && (paramString.length() < this.precision)) {
          i = this.precision - paramString.length();
        }
      }
      else if ((this.precisionSet) && (paramString.length() - 1 < this.precision)) {
        i = this.precision - paramString.length() + 1;
      }
      if (i < 0) {
        i = 0;
      }
      if (this.fieldWidthSet)
      {
        j = this.fieldWidth - i - paramString.length();
        if ((i1 == 0) && ((this.leadingSign) || (this.leadingSpace))) {
          j--;
        }
      }
      if (j < 0) {
        j = 0;
      }
      if (this.leadingSign) {
        k++;
      } else if (this.leadingSpace) {
        k++;
      }
      k += j;
      k += i;
      k += paramString.length();
      char[] arrayOfChar1 = new char[k];
      int i3;
      if (this.leftJustify)
      {
        if (i1 != 0) {
          arrayOfChar1[(m++)] = '-';
        } else if (this.leadingSign) {
          arrayOfChar1[(m++)] = '+';
        } else if (this.leadingSpace) {
          arrayOfChar1[(m++)] = ' ';
        }
        char[] arrayOfChar2 = paramString.toCharArray();
        n = i1 != 0 ? 1 : 0;
        for (i3 = 0; i3 < i; i3++)
        {
          arrayOfChar1[m] = '0';
          m++;
        }
        i3 = n;
        while (i3 < arrayOfChar2.length)
        {
          arrayOfChar1[m] = arrayOfChar2[i3];
          i3++;
          m++;
        }
        for (i3 = 0; i3 < j; i3++)
        {
          arrayOfChar1[m] = ' ';
          m++;
        }
      }
      else
      {
        if (!this.leadingZeros)
        {
          for (m = 0; m < j; m++) {
            arrayOfChar1[m] = ' ';
          }
          if (i1 != 0) {
            arrayOfChar1[(m++)] = '-';
          } else if (this.leadingSign) {
            arrayOfChar1[(m++)] = '+';
          } else if (this.leadingSpace) {
            arrayOfChar1[(m++)] = ' ';
          }
        }
        else
        {
          if (i1 != 0) {
            arrayOfChar1[(m++)] = '-';
          } else if (this.leadingSign) {
            arrayOfChar1[(m++)] = '+';
          } else if (this.leadingSpace) {
            arrayOfChar1[(m++)] = ' ';
          }
          i2 = 0;
          while (i2 < j)
          {
            arrayOfChar1[m] = '0';
            i2++;
            m++;
          }
        }
        int i2 = 0;
        while (i2 < i)
        {
          arrayOfChar1[m] = '0';
          i2++;
          m++;
        }
        char[] arrayOfChar3 = paramString.toCharArray();
        n = i1 != 0 ? 1 : 0;
        i3 = n;
        while (i3 < arrayOfChar3.length)
        {
          arrayOfChar1[m] = arrayOfChar3[i3];
          i3++;
          m++;
        }
      }
      return new String(arrayOfChar1);
    }
    
    private String printXFormat(short paramShort)
    {
      String str1 = null;
      if (paramShort == Short.MIN_VALUE)
      {
        str1 = "8000";
      }
      else if (paramShort < 0)
      {
        String str2;
        if (paramShort == Short.MIN_VALUE)
        {
          str2 = "0";
        }
        else
        {
          str2 = Integer.toString(-paramShort - 1 ^ 0xFFFFFFFF ^ 0x8000, 16);
          if ((str2.charAt(0) == 'F') || (str2.charAt(0) == 'f')) {
            str2 = str2.substring(16, 32);
          }
        }
        switch (str2.length())
        {
        case 1: 
          str1 = "800" + str2;
          break;
        case 2: 
          str1 = "80" + str2;
          break;
        case 3: 
          str1 = "8" + str2;
          break;
        case 4: 
          switch (str2.charAt(0))
          {
          case '1': 
            str1 = "9" + str2.substring(1, 4);
            break;
          case '2': 
            str1 = "a" + str2.substring(1, 4);
            break;
          case '3': 
            str1 = "b" + str2.substring(1, 4);
            break;
          case '4': 
            str1 = "c" + str2.substring(1, 4);
            break;
          case '5': 
            str1 = "d" + str2.substring(1, 4);
            break;
          case '6': 
            str1 = "e" + str2.substring(1, 4);
            break;
          case '7': 
            str1 = "f" + str2.substring(1, 4);
          }
          break;
        }
      }
      else
      {
        str1 = Integer.toString(paramShort, 16);
      }
      return printXFormat(str1);
    }
    
    private String printXFormat(long paramLong)
    {
      String str1 = null;
      if (paramLong == Long.MIN_VALUE)
      {
        str1 = "8000000000000000";
      }
      else if (paramLong < 0L)
      {
        String str2 = Long.toString(-paramLong - 1L ^ 0xFFFFFFFFFFFFFFFF ^ 0x8000000000000000, 16);
        switch (str2.length())
        {
        case 1: 
          str1 = "800000000000000" + str2;
          break;
        case 2: 
          str1 = "80000000000000" + str2;
          break;
        case 3: 
          str1 = "8000000000000" + str2;
          break;
        case 4: 
          str1 = "800000000000" + str2;
          break;
        case 5: 
          str1 = "80000000000" + str2;
          break;
        case 6: 
          str1 = "8000000000" + str2;
          break;
        case 7: 
          str1 = "800000000" + str2;
          break;
        case 8: 
          str1 = "80000000" + str2;
          break;
        case 9: 
          str1 = "8000000" + str2;
          break;
        case 10: 
          str1 = "800000" + str2;
          break;
        case 11: 
          str1 = "80000" + str2;
          break;
        case 12: 
          str1 = "8000" + str2;
          break;
        case 13: 
          str1 = "800" + str2;
          break;
        case 14: 
          str1 = "80" + str2;
          break;
        case 15: 
          str1 = "8" + str2;
          break;
        case 16: 
          switch (str2.charAt(0))
          {
          case '1': 
            str1 = "9" + str2.substring(1, 16);
            break;
          case '2': 
            str1 = "a" + str2.substring(1, 16);
            break;
          case '3': 
            str1 = "b" + str2.substring(1, 16);
            break;
          case '4': 
            str1 = "c" + str2.substring(1, 16);
            break;
          case '5': 
            str1 = "d" + str2.substring(1, 16);
            break;
          case '6': 
            str1 = "e" + str2.substring(1, 16);
            break;
          case '7': 
            str1 = "f" + str2.substring(1, 16);
          }
          break;
        }
      }
      else
      {
        str1 = Long.toString(paramLong, 16);
      }
      return printXFormat(str1);
    }
    
    private String printXFormat(int paramInt)
    {
      String str1 = null;
      if (paramInt == Integer.MIN_VALUE)
      {
        str1 = "80000000";
      }
      else if (paramInt < 0)
      {
        String str2 = Integer.toString(-paramInt - 1 ^ 0xFFFFFFFF ^ 0x80000000, 16);
        switch (str2.length())
        {
        case 1: 
          str1 = "8000000" + str2;
          break;
        case 2: 
          str1 = "800000" + str2;
          break;
        case 3: 
          str1 = "80000" + str2;
          break;
        case 4: 
          str1 = "8000" + str2;
          break;
        case 5: 
          str1 = "800" + str2;
          break;
        case 6: 
          str1 = "80" + str2;
          break;
        case 7: 
          str1 = "8" + str2;
          break;
        case 8: 
          switch (str2.charAt(0))
          {
          case '1': 
            str1 = "9" + str2.substring(1, 8);
            break;
          case '2': 
            str1 = "a" + str2.substring(1, 8);
            break;
          case '3': 
            str1 = "b" + str2.substring(1, 8);
            break;
          case '4': 
            str1 = "c" + str2.substring(1, 8);
            break;
          case '5': 
            str1 = "d" + str2.substring(1, 8);
            break;
          case '6': 
            str1 = "e" + str2.substring(1, 8);
            break;
          case '7': 
            str1 = "f" + str2.substring(1, 8);
          }
          break;
        }
      }
      else
      {
        str1 = Integer.toString(paramInt, 16);
      }
      return printXFormat(str1);
    }
    
    private String printXFormat(String paramString)
    {
      int i = 0;
      int j = 0;
      if ((paramString.equals("0")) && (this.precisionSet) && (this.precision == 0)) {
        paramString = "";
      }
      if (this.precisionSet) {
        i = this.precision - paramString.length();
      }
      if (i < 0) {
        i = 0;
      }
      if (this.fieldWidthSet)
      {
        j = this.fieldWidth - i - paramString.length();
        if (this.alternateForm) {
          j -= 2;
        }
      }
      if (j < 0) {
        j = 0;
      }
      int k = 0;
      if (this.alternateForm) {
        k += 2;
      }
      k += i;
      k += paramString.length();
      k += j;
      char[] arrayOfChar1 = new char[k];
      int m = 0;
      int i2;
      if (this.leftJustify)
      {
        if (this.alternateForm)
        {
          arrayOfChar1[(m++)] = '0';
          arrayOfChar1[(m++)] = 'x';
        }
        int n = 0;
        while (n < i)
        {
          arrayOfChar1[m] = '0';
          n++;
          m++;
        }
        char[] arrayOfChar2 = paramString.toCharArray();
        i2 = 0;
        while (i2 < arrayOfChar2.length)
        {
          arrayOfChar1[m] = arrayOfChar2[i2];
          i2++;
          m++;
        }
        i2 = 0;
        while (i2 < j)
        {
          arrayOfChar1[m] = ' ';
          i2++;
          m++;
        }
      }
      else
      {
        if (!this.leadingZeros)
        {
          i1 = 0;
          while (i1 < j)
          {
            arrayOfChar1[m] = ' ';
            i1++;
            m++;
          }
        }
        if (this.alternateForm)
        {
          arrayOfChar1[(m++)] = '0';
          arrayOfChar1[(m++)] = 'x';
        }
        if (this.leadingZeros)
        {
          i1 = 0;
          while (i1 < j)
          {
            arrayOfChar1[m] = '0';
            i1++;
            m++;
          }
        }
        int i1 = 0;
        while (i1 < i)
        {
          arrayOfChar1[m] = '0';
          i1++;
          m++;
        }
        localObject = paramString.toCharArray();
        i2 = 0;
        while (i2 < localObject.length)
        {
          arrayOfChar1[m] = localObject[i2];
          i2++;
          m++;
        }
      }
      Object localObject = new String(arrayOfChar1);
      if (this.conversionCharacter == 'X') {
        localObject = ((String)localObject).toUpperCase();
      }
      return (String)localObject;
    }
    
    private String printOFormat(short paramShort)
    {
      String str1 = null;
      if (paramShort == Short.MIN_VALUE)
      {
        str1 = "100000";
      }
      else if (paramShort < 0)
      {
        String str2 = Integer.toString(-paramShort - 1 ^ 0xFFFFFFFF ^ 0x8000, 8);
        switch (str2.length())
        {
        case 1: 
          str1 = "10000" + str2;
          break;
        case 2: 
          str1 = "1000" + str2;
          break;
        case 3: 
          str1 = "100" + str2;
          break;
        case 4: 
          str1 = "10" + str2;
          break;
        case 5: 
          str1 = "1" + str2;
        }
      }
      else
      {
        str1 = Integer.toString(paramShort, 8);
      }
      return printOFormat(str1);
    }
    
    private String printOFormat(long paramLong)
    {
      String str1 = null;
      if (paramLong == Long.MIN_VALUE)
      {
        str1 = "1000000000000000000000";
      }
      else if (paramLong < 0L)
      {
        String str2 = Long.toString(-paramLong - 1L ^ 0xFFFFFFFFFFFFFFFF ^ 0x8000000000000000, 8);
        switch (str2.length())
        {
        case 1: 
          str1 = "100000000000000000000" + str2;
          break;
        case 2: 
          str1 = "10000000000000000000" + str2;
          break;
        case 3: 
          str1 = "1000000000000000000" + str2;
          break;
        case 4: 
          str1 = "100000000000000000" + str2;
          break;
        case 5: 
          str1 = "10000000000000000" + str2;
          break;
        case 6: 
          str1 = "1000000000000000" + str2;
          break;
        case 7: 
          str1 = "100000000000000" + str2;
          break;
        case 8: 
          str1 = "10000000000000" + str2;
          break;
        case 9: 
          str1 = "1000000000000" + str2;
          break;
        case 10: 
          str1 = "100000000000" + str2;
          break;
        case 11: 
          str1 = "10000000000" + str2;
          break;
        case 12: 
          str1 = "1000000000" + str2;
          break;
        case 13: 
          str1 = "100000000" + str2;
          break;
        case 14: 
          str1 = "10000000" + str2;
          break;
        case 15: 
          str1 = "1000000" + str2;
          break;
        case 16: 
          str1 = "100000" + str2;
          break;
        case 17: 
          str1 = "10000" + str2;
          break;
        case 18: 
          str1 = "1000" + str2;
          break;
        case 19: 
          str1 = "100" + str2;
          break;
        case 20: 
          str1 = "10" + str2;
          break;
        case 21: 
          str1 = "1" + str2;
        }
      }
      else
      {
        str1 = Long.toString(paramLong, 8);
      }
      return printOFormat(str1);
    }
    
    private String printOFormat(int paramInt)
    {
      String str1 = null;
      if (paramInt == Integer.MIN_VALUE)
      {
        str1 = "20000000000";
      }
      else if (paramInt < 0)
      {
        String str2 = Integer.toString(-paramInt - 1 ^ 0xFFFFFFFF ^ 0x80000000, 8);
        switch (str2.length())
        {
        case 1: 
          str1 = "2000000000" + str2;
          break;
        case 2: 
          str1 = "200000000" + str2;
          break;
        case 3: 
          str1 = "20000000" + str2;
          break;
        case 4: 
          str1 = "2000000" + str2;
          break;
        case 5: 
          str1 = "200000" + str2;
          break;
        case 6: 
          str1 = "20000" + str2;
          break;
        case 7: 
          str1 = "2000" + str2;
          break;
        case 8: 
          str1 = "200" + str2;
          break;
        case 9: 
          str1 = "20" + str2;
          break;
        case 10: 
          str1 = "2" + str2;
          break;
        case 11: 
          str1 = "3" + str2.substring(1);
        }
      }
      else
      {
        str1 = Integer.toString(paramInt, 8);
      }
      return printOFormat(str1);
    }
    
    private String printOFormat(String paramString)
    {
      int i = 0;
      int j = 0;
      if ((paramString.equals("0")) && (this.precisionSet) && (this.precision == 0)) {
        paramString = "";
      }
      if (this.precisionSet) {
        i = this.precision - paramString.length();
      }
      if (this.alternateForm) {
        i++;
      }
      if (i < 0) {
        i = 0;
      }
      if (this.fieldWidthSet) {
        j = this.fieldWidth - i - paramString.length();
      }
      if (j < 0) {
        j = 0;
      }
      int k = i + paramString.length() + j;
      char[] arrayOfChar1 = new char[k];
      int m;
      int i1;
      if (this.leftJustify)
      {
        for (m = 0; m < i; m++) {
          arrayOfChar1[m] = '0';
        }
        char[] arrayOfChar2 = paramString.toCharArray();
        i1 = 0;
        while (i1 < arrayOfChar2.length)
        {
          arrayOfChar1[m] = arrayOfChar2[i1];
          i1++;
          m++;
        }
        i1 = 0;
        while (i1 < j)
        {
          arrayOfChar1[m] = ' ';
          i1++;
          m++;
        }
      }
      else
      {
        if (this.leadingZeros) {
          for (m = 0; m < j; m++) {
            arrayOfChar1[m] = '0';
          }
        }
        for (m = 0; m < j; m++) {
          arrayOfChar1[m] = ' ';
        }
        int n = 0;
        while (n < i)
        {
          arrayOfChar1[m] = '0';
          n++;
          m++;
        }
        char[] arrayOfChar3 = paramString.toCharArray();
        i1 = 0;
        while (i1 < arrayOfChar3.length)
        {
          arrayOfChar1[m] = arrayOfChar3[i1];
          i1++;
          m++;
        }
      }
      return new String(arrayOfChar1);
    }
    
    private String printCFormat(char paramChar)
    {
      int i = 1;
      int j = this.fieldWidth;
      if (!this.fieldWidthSet) {
        j = i;
      }
      char[] arrayOfChar = new char[j];
      int k = 0;
      if (this.leftJustify)
      {
        arrayOfChar[0] = paramChar;
        for (k = 1; k <= j - i; k++) {
          arrayOfChar[k] = ' ';
        }
      }
      for (k = 0; k < j - i; k++) {
        arrayOfChar[k] = ' ';
      }
      arrayOfChar[k] = paramChar;
      return new String(arrayOfChar);
    }
    
    private String printSFormat(String paramString)
    {
      int i = paramString.length();
      int j = this.fieldWidth;
      if ((this.precisionSet) && (i > this.precision)) {
        i = this.precision;
      }
      if (!this.fieldWidthSet) {
        j = i;
      }
      int k = 0;
      if (j > i) {
        k += j - i;
      }
      if (i >= paramString.length()) {
        k += paramString.length();
      } else {
        k += i;
      }
      char[] arrayOfChar1 = new char[k];
      int m = 0;
      if (this.leftJustify)
      {
        char[] arrayOfChar2;
        if (i >= paramString.length())
        {
          arrayOfChar2 = paramString.toCharArray();
          for (m = 0; m < paramString.length(); m++) {
            arrayOfChar1[m] = arrayOfChar2[m];
          }
        }
        else
        {
          arrayOfChar2 = paramString.substring(0, i).toCharArray();
          for (m = 0; m < i; m++) {
            arrayOfChar1[m] = arrayOfChar2[m];
          }
        }
        int n = 0;
        while (n < j - i)
        {
          arrayOfChar1[m] = ' ';
          n++;
          m++;
        }
      }
      else
      {
        for (m = 0; m < j - i; m++) {
          arrayOfChar1[m] = ' ';
        }
        char[] arrayOfChar3;
        int i1;
        if (i >= paramString.length())
        {
          arrayOfChar3 = paramString.toCharArray();
          for (i1 = 0; i1 < paramString.length(); i1++)
          {
            arrayOfChar1[m] = arrayOfChar3[i1];
            m++;
          }
        }
        else
        {
          arrayOfChar3 = paramString.substring(0, i).toCharArray();
          for (i1 = 0; i1 < i; i1++)
          {
            arrayOfChar1[m] = arrayOfChar3[i1];
            m++;
          }
        }
      }
      return new String(arrayOfChar1);
    }
    
    private boolean setConversionCharacter()
    {
      boolean bool = false;
      this.conversionCharacter = '\000';
      if (this.pos < this.fmt.length())
      {
        char c = this.fmt.charAt(this.pos);
        if ((c == 'i') || (c == 'd') || (c == 'f') || (c == 'g') || (c == 'G') || (c == 'o') || (c == 'x') || (c == 'X') || (c == 'e') || (c == 'E') || (c == 'c') || (c == 's') || (c == '%'))
        {
          this.conversionCharacter = c;
          this.pos += 1;
          bool = true;
        }
      }
      return bool;
    }
    
    private void setOptionalHL()
    {
      this.optionalh = false;
      this.optionall = false;
      this.optionalL = false;
      if (this.pos < this.fmt.length())
      {
        int i = this.fmt.charAt(this.pos);
        if (i == 104)
        {
          this.optionalh = true;
          this.pos += 1;
        }
        else if (i == 108)
        {
          this.optionall = true;
          this.pos += 1;
        }
        else if (i == 76)
        {
          this.optionalL = true;
          this.pos += 1;
        }
      }
    }
    
    private void setPrecision()
    {
      int i = this.pos;
      this.precisionSet = false;
      if ((this.pos < this.fmt.length()) && (this.fmt.charAt(this.pos) == '.'))
      {
        this.pos += 1;
        if ((this.pos < this.fmt.length()) && (this.fmt.charAt(this.pos) == '*'))
        {
          this.pos += 1;
          if (!setPrecisionArgPosition())
          {
            this.variablePrecision = true;
            this.precisionSet = true;
          }
          return;
        }
        while (this.pos < this.fmt.length())
        {
          char c = this.fmt.charAt(this.pos);
          if (!Character.isDigit(c)) {
            break;
          }
          this.pos += 1;
        }
        if (this.pos > i + 1)
        {
          String str = this.fmt.substring(i + 1, this.pos);
          this.precision = Integer.parseInt(str);
          this.precisionSet = true;
        }
      }
    }
    
    private void setFieldWidth()
    {
      int i = this.pos;
      this.fieldWidth = 0;
      this.fieldWidthSet = false;
      if ((this.pos < this.fmt.length()) && (this.fmt.charAt(this.pos) == '*'))
      {
        this.pos += 1;
        if (!setFieldWidthArgPosition())
        {
          this.variableFieldWidth = true;
          this.fieldWidthSet = true;
        }
      }
      else
      {
        while (this.pos < this.fmt.length())
        {
          char c = this.fmt.charAt(this.pos);
          if (!Character.isDigit(c)) {
            break;
          }
          this.pos += 1;
        }
        if ((i < this.pos) && (i < this.fmt.length()))
        {
          String str = this.fmt.substring(i, this.pos);
          this.fieldWidth = Integer.parseInt(str);
          this.fieldWidthSet = true;
        }
      }
    }
    
    private void setArgPosition()
    {
      for (int i = this.pos; (i < this.fmt.length()) && (Character.isDigit(this.fmt.charAt(i))); i++) {}
      if ((i > this.pos) && (i < this.fmt.length()) && (this.fmt.charAt(i) == '$'))
      {
        this.positionalSpecification = true;
        this.argumentPosition = Integer.parseInt(this.fmt.substring(this.pos, i));
        this.pos = (i + 1);
      }
    }
    
    private boolean setFieldWidthArgPosition()
    {
      boolean bool = false;
      for (int i = this.pos; (i < this.fmt.length()) && (Character.isDigit(this.fmt.charAt(i))); i++) {}
      if ((i > this.pos) && (i < this.fmt.length()) && (this.fmt.charAt(i) == '$'))
      {
        this.positionalFieldWidth = true;
        this.argumentPositionForFieldWidth = Integer.parseInt(this.fmt.substring(this.pos, i));
        this.pos = (i + 1);
        bool = true;
      }
      return bool;
    }
    
    private boolean setPrecisionArgPosition()
    {
      boolean bool = false;
      for (int i = this.pos; (i < this.fmt.length()) && (Character.isDigit(this.fmt.charAt(i))); i++) {}
      if ((i > this.pos) && (i < this.fmt.length()) && (this.fmt.charAt(i) == '$'))
      {
        this.positionalPrecision = true;
        this.argumentPositionForPrecision = Integer.parseInt(this.fmt.substring(this.pos, i));
        this.pos = (i + 1);
        bool = true;
      }
      return bool;
    }
    
    boolean isPositionalSpecification()
    {
      return this.positionalSpecification;
    }
    
    int getArgumentPosition()
    {
      return this.argumentPosition;
    }
    
    boolean isPositionalFieldWidth()
    {
      return this.positionalFieldWidth;
    }
    
    int getArgumentPositionForFieldWidth()
    {
      return this.argumentPositionForFieldWidth;
    }
    
    boolean isPositionalPrecision()
    {
      return this.positionalPrecision;
    }
    
    int getArgumentPositionForPrecision()
    {
      return this.argumentPositionForPrecision;
    }
    
    private void setFlagCharacters()
    {
      this.thousands = false;
      this.leftJustify = false;
      this.leadingSign = false;
      this.leadingSpace = false;
      this.alternateForm = false;
      this.leadingZeros = false;
      while (this.pos < this.fmt.length())
      {
        int i = this.fmt.charAt(this.pos);
        if (i == 39)
        {
          this.thousands = true;
        }
        else if (i == 45)
        {
          this.leftJustify = true;
          this.leadingZeros = false;
        }
        else if (i == 43)
        {
          this.leadingSign = true;
          this.leadingSpace = false;
        }
        else if (i == 32)
        {
          if (!this.leadingSign) {
            this.leadingSpace = true;
          }
        }
        else if (i == 35)
        {
          this.alternateForm = true;
        }
        else
        {
          if (i != 48) {
            break;
          }
          if (!this.leftJustify) {
            this.leadingZeros = true;
          }
        }
        this.pos += 1;
      }
    }
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/PrintfFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */