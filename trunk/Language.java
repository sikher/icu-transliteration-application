/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package googletranslate;

/**
 *
 * @author koper
 */
public enum Language {
        AUTO_DETECT(""),
        ALBANIAN("sq"),
        ARABIC("ar"),
        BULGARIAN("bg"),
        CATALAN("ca"),
        CHINESE("zh"),
        CHINESE_SIMPLIFIED("zh-CN"),
        CHINESE_TRADITIONAL("zh-TW"),
        CROATIAN("hr"),
        CZECH("cs"),
        DANISH("da"),
        DUTCH("nl"),
        ENGLISH("en"),
        ESTONIAN("et"),
        FILIPINO("tl"),
        FINNISH("fi"),
        FRENCH("fr"),
        GALACIAN("gl"),
        GERMAN("de"),
        GREEK("el"),
        HEBREW("iw"),
        HINDI("hi"),
        HUNGARIAN("hu"),
        INDONESIAN("id"),
        ITALIAN("it"),
        JAPANESE("ja"),
        KOREAN("ko"),
        LATVIAN("lv"),
        LITHUANIAN("lt"),
        MALTESE("mt"),
        NORWEGIAN("no"),
        PERSIAN("fa"),
        POLISH("pl"),
        PORTUGUESE("pt"),
        ROMANIAN("ro"),
        RUSSIAN("ru"),
        SERBIAN("sr"),
        SLOVAK("sk"),
        SLOVENIAN("sl"),
        SPANISH("es"),
        SWEDISH("sv"),
        THAI("th"),
        TURKISH("tr"),
        UKRANIAN("uk"),
        VIETNAMESE("vi");

        /**
         * Google's String representation of this language.
         */
        private final String language;

        /**
         * Enum constructor.
         * @param pLanguage The language identifier.
         */
        private Language(final String pLanguage) {
                language = pLanguage;
        }

        /**
         * Returns the String representation of this language.
         * @return The String representation of this language.
         */
        @Override
        public String toString() {
                return language;
        }
}