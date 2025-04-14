package utils;

import java.util.function.Function;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * Utility class for working with JavaFX ComboBox components.
 * Provides methods to simplify the configuration and usage of ComboBox.
 */
public class ComboBoxUtils {

  /**
   * Sets a StringConverter for a ComboBox to handle conversion between
   * objects and their string representations.
   *
   * @param comboBox the ComboBox to set the converter for
   * @param toString a function to convert an object to its string representation
   * @param fromString a function to convert a string back to an object
   * @param <T> the type of the ComboBox items
   */
  public static <T> void setComboBoxConverter(ComboBox<T> comboBox, Function<T, String> toString, Function<String, T> fromString) {
    comboBox.setConverter(new StringConverter<T>() {
      @Override
      public String toString(T object) {
        return (object == null) ? "" : toString.apply(object);
      }

      @Override
      public T fromString(String string) {
        return comboBox.getItems().stream()
            .filter(item -> toString.apply(item).equals(string))
            .findFirst()
            .orElse(null);
      }
    });
  }
}
