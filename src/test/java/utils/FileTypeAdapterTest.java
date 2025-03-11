package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileTypeAdapterTest {
    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(File.class, new FileTypeAdapter())
                .create();
    }

    @Test
    void write() throws IOException {
        File file = new File("/file.txt");

        String json = gson.toJson(file);

        assertNotNull(json);
        assertEquals("\"C:\\\\file.txt\"", json);
    }

    @Test
    void read() {
        String json = "\"/file.txt\"";

        File file = gson.fromJson(json, File.class);

        assertNotNull(file);
        assertEquals("C:\\file.txt", file.getAbsolutePath());
    }
}