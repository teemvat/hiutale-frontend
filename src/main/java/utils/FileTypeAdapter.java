package utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;

/**
 * The FileTypeAdapter class is a custom Gson TypeAdapter for serializing and deserializing
 * File objects to and from JSON. It converts a File object to its absolute path as a string
 * during serialization and reconstructs a File object from its path during deserialization.
 */
public class FileTypeAdapter extends TypeAdapter<File> {

    /**
     * Serializes a File object to its JSON representation.
     *
     * @param out   The JsonWriter to write the JSON output to.
     * @param value The File object to serialize. If null, writes a JSON null value.
     * @throws IOException If an I/O error occurs during writing.
     */
    @Override
    public void write(JsonWriter out, File value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getAbsolutePath());
        }
    }

    /**
     * Deserializes a JSON string to a File object.
     *
     * @param in The JsonReader to read the JSON input from.
     * @return A File object constructed from the JSON string, or null if the string is null.
     * @throws IOException If an I/O error occurs during reading.
     */
    @Override
    public File read(JsonReader in) throws IOException {
        String path = in.nextString();
        return path == null ? null : new File(path);
    }
}