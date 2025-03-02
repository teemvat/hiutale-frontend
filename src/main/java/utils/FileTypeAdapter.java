package utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;

public class FileTypeAdapter extends TypeAdapter<File> {
    @Override
    public void write(JsonWriter out, File value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getAbsolutePath());
        }
    }

    @Override
    public File read(JsonReader in) throws IOException {
        String path = in.nextString();
        return path == null ? null : new File(path);
    }
}
