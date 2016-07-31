package googletranslate;

public enum Language
{
  AUTO_DETECT(""),  ALBANIAN("sq"),  ARABIC("ar"),  BULGARIAN("bg"),  CATALAN("ca"),  CHINESE("zh"),  CHINESE_SIMPLIFIED("zh-CN"),  CHINESE_TRADITIONAL("zh-TW"),  CROATIAN("hr"),  CZECH("cs"),  DANISH("da"),  DUTCH("nl"),  ENGLISH("en"),  ESTONIAN("et"),  FILIPINO("tl"),  FINNISH("fi"),  FRENCH("fr"),  GALACIAN("gl"),  GERMAN("de"),  GREEK("el"),  HEBREW("iw"),  HINDI("hi"),  HUNGARIAN("hu"),  INDONESIAN("id"),  ITALIAN("it"),  JAPANESE("ja"),  KOREAN("ko"),  LATVIAN("lv"),  LITHUANIAN("lt"),  MALTESE("mt"),  NORWEGIAN("no"),  PERSIAN("fa"),  POLISH("pl"),  PORTUGUESE("pt"),  ROMANIAN("ro"),  RUSSIAN("ru"),  SERBIAN("sr"),  SLOVAK("sk"),  SLOVENIAN("sl"),  SPANISH("es"),  SWEDISH("sv"),  THAI("th"),  TURKISH("tr"),  UKRANIAN("uk"),  VIETNAMESE("vi");
  
  private final String language;
  
  private Language(String paramString)
  {
    this.language = paramString;
  }
  
  public String toString()
  {
    return this.language;
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/Language.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */