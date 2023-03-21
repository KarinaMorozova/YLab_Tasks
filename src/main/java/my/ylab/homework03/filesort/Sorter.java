package my.ylab.homework03.filesort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Sorter {
    int chunkSize = 25_000_000; // число записей = 1 Гб примерно
    private List<File> outputs = new ArrayList<>();
    private Path tempDir;

    public void setTempDirectory(File datafile) throws IOException {
        tempDir = Files.createTempDirectory(Path.of(datafile.getParent()), "tempOuts");

        File directory = new File(tempDir.toString());
        if ( !directory.exists() || !directory.isDirectory() ){
            throw new IllegalArgumentException("Такой папки не существует");
        }
    }

    private void writeToFile(List<Long> list,File file) throws IOException{
        try (PrintWriter pw = new PrintWriter(file)) {
            for (int i = 0; i < list.size(); i++) {
                pw.println(list.get(i));
            }
            pw.flush();
        }
    }
    public void splitFile(File file) {
        outputs.clear();
        List<Long> longs = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(file);
            Scanner scanner = new Scanner(fileInputStream)) {

            int currPosition = 0;
            while (scanner.hasNextLine()) {
                String stringFromFile = scanner.nextLine();
                longs.add(Long.valueOf(stringFromFile));

                currPosition++;
                if ( currPosition >= chunkSize ){
                    currPosition = 0;
                    Collections.sort(longs);

                    File tempFile = new File(tempDir.toString() + "\\temp" + System.currentTimeMillis());
                    outputs.add(tempFile);

                    writeToFile(longs, tempFile);
                    longs.clear();
                }
            }

            // хвост
            Collections.sort(longs);
            File tempFile = new File(tempDir.toString() + "\\temp" + System.currentTimeMillis());
            outputs.add(tempFile);

            writeToFile(longs, tempFile);
            longs.clear();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void merge(File file) {
        Map<Long, Scanner> map = new HashMap<>();
        List<Scanner> scanners = new ArrayList<>();

        try (PrintWriter writer = new PrintWriter(file) ) {

            for ( int i = 0; i < outputs.size(); i++ ){
                Scanner scanner = new Scanner(outputs.get(i));
                scanners.add(scanner);
                if (scanner.hasNextLong()) {
                    Long number = scanner.nextLong();
                    map.put(number, scanners.get(i));
                }
            }

            List<Long> sorted = new LinkedList<>(map.keySet());
            while ( map.size() > 0 ){
                Collections.sort(sorted);
                Long num = sorted.remove(0);
                writer.println(num);

                Scanner scanner = map.remove(num);
                if (scanner.hasNextLong()) {
                    Long number = scanner.nextLong();
                    map.put(number, scanner);
                    sorted.add(number);
                 }
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        finally {
            for (Scanner scanner: scanners) {
                scanner.close();
            }

            for (File f: outputs){
                f.delete();
            }
        }

    }

    public File sortFile(File dataFile) throws IOException {
        setTempDirectory(dataFile);
        splitFile(dataFile);

        File sortedFile = new File(dataFile.getParent() + "\\sorted.txt");
        merge(sortedFile);
        tempDir.toFile().deleteOnExit();

        return sortedFile;
    }
}

