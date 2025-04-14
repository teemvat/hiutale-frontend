package utils;

import java.util.Locale;

/**
 * Utility class for determining if a given locale uses a right-to-left (RTL) language.
 */
public class RtlLanguageUtil {

  /**
   * Checks if the given locale corresponds to a right-to-left (RTL) language.
   *
   * @param locale the {@link Locale} to check. If null, the method will return false.
   * @return {@code true} if the locale's language is written in a right-to-left direction,
   *         {@code false} otherwise.
   */
  public static boolean isRtl(Locale locale) {
    if (locale == null) {
      return false;
    }

    // Get the first character of the display language
    char firstChar = locale.getDisplayLanguage(locale).charAt(0);

    // Check if it belongs to RTL character ranges
    byte directionality = Character.getDirectionality(firstChar);
    return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
            || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
  }
}
