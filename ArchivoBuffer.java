import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class ArchivoBuffer {
    private BufferedReader reader;
    private String cache;

    public ArchivoBuffer(File f) throws IOException {
        this.reader = new BufferedReader(new FileReader(f));
        actualizarCache();
    }
    public boolean isEmpty() {
        return cache == null;
    }
    public String peek() {
        return cache;
    }
    public String pop() throws IOException {
        String res = cache;
        actualizarCache();
        return res;
    }
    private void actualizarCache() throws IOException {
        this.cache = reader.readLine();
    }
    public void close() throws IOException {
        reader.close();
    }
}
