import java.io.*;
import java.util.*;

public class MergeSort {

    // Límite de 512 MB
    private static final long limite = 512 * 1024 * 1024;

    public static List<File> leerArchivo(File file) throws IOException {
        List<File> tempFiles = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            boolean endOfFile = false;
            int counter = 0;

            while (!endOfFile) {
                List<String> chunk = new ArrayList<>();
                long currentSize = 0;
                String line;
                while (currentSize < limite) {
                    line = reader.readLine();
                    if (line == null) {
                        endOfFile = true;
                        break;
                    }
                    chunk.add(line);
                    currentSize += line.length() * 2;
                }
                if (!chunk.isEmpty()) {
                    // Ordenamiento en RAM
                    Collections.sort(chunk);
                    // Guardar
                    File tempFile = new File(String.format("temp_%02d.dat", counter++));
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                        for (String s : chunk) {
                            writer.write(s);
                            writer.newLine();
                        }
                    }
                    tempFiles.add(tempFile);
                    System.out.println("Generado: " + tempFile.getName());
                }
            }
        }
        return tempFiles;
    }

    public static void mezcla(List<File> tempFiles, File output) throws IOException {
        PriorityQueue<ArchivoBuffer> pq = new PriorityQueue<>((a, b) -> a.peek().compareTo(b.peek()));
        // Abrir todos los archivos
        for (File f : tempFiles) {
            ArchivoBuffer ab = new ArchivoBuffer(f);
            if (!ab.isEmpty()) {
                pq.add(ab);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
            while (!pq.isEmpty()) {
                // Sacar el buffer que tiene el elemento más pequeño
                ArchivoBuffer masPequeno = pq.poll();

                writer.write(masPequeno.pop());
                writer.newLine();
                // Si al archivo todavía le quedan líneas, vuelve a entrar al Heap
                if (!masPequeno.isEmpty()) {
                    pq.add(masPequeno);
                } else {
                    masPequeno.close();
                }
            }
        }
    }
    public static void main(String[] args) {
        File inputFile = new File("datos_entrada.txt");
        File outputFile = new File("archivo_final_ordenado.txt");

        try {
            System.out.println("Inicio");
            List<File> tempFiles = leerArchivo(inputFile);

            System.out.println("Mezclar");
            mezcla(tempFiles, outputFile);

            System.out.println("Ordenados.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
