import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class FileIO {

    // Gets lines of a text file, creates an array with them and returns it
    public static String[] readFile(File file) {
        try {
            String path = file.getPath();
            int i = 0;
            int length = Files.readAllLines(Paths.get(path)).size();
            String[] results = new String[length];
            for (String line : Files.readAllLines(Paths.get(path))) {
                results[i++] = line;
            }
            return results;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Writes lines to a text file
    public static void writeLinesToFile(File file, String[] lines){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))){
            for (String line : lines) {
                bw.write(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inserts a line to a text file, by creating a new file and combining the lines and the old file
    public static void addLineToFileByID(File f, String line, int ID){
        try {
            File temp = new File("temp.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            String[] fileLines = readFile(f);

            String currentLine;
            int currentID = 0;
            int nextID = 0;
            boolean isInserted = false;

            for (int i = 0; i < fileLines.length; i++) {
                String fileLine = fileLines[i];
                String startOfLine = fileLine.split("\\t")[0];
                String startOfNextLine = "";

                try {
                    startOfNextLine = fileLines[i + 1].split("\\t")[0];
                } catch (ArrayIndexOutOfBoundsException e) {
                    bw.write(fileLine);

                    if (!isInserted) {
                        bw.write(System.getProperty("line.separator") + line);
                    }
                    break;
                }

                if (Utility.isInteger(startOfLine)) {
                    currentID = Integer.parseInt(startOfLine);
                }

                if (Utility.isInteger(startOfNextLine)) {
                    nextID = Integer.parseInt(startOfNextLine);
                }

                if (((i == 0) && (ID < currentID)) || ((currentID < ID) && (ID < nextID) && !isInserted)) {
                    bw.write(fileLine + System.getProperty("line.separator"));
                    bw.write(line + System.getProperty("line.separator"));
                    isInserted = true;
                    continue;
                }

                bw.write(fileLine + System.getProperty("line.separator"));
            }

            bw.close();
            boolean delete = f.delete();
            boolean b = temp.renameTo(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creates a new file and don't write the unwanted lines to it
    public static void deleteLinesByID(File f, ArrayList<String> lines){
        try {
            FileReader file = new FileReader(f.getName());
            BufferedReader br = new BufferedReader(file);
            File temp = new File("temp.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));

            int ID = Integer.parseInt(lines.get(0).split("\\t")[0]);
            int currentID = 0;
            int counter = 0;  // Counter of the lines that deleted (not written)
            String currentLine;
            boolean isFound = false;

            while ((currentLine = br.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                String startOfLine = trimmedLine.split("\\t")[0];

                if (Utility.isInteger(startOfLine)) {
                    currentID = Integer.parseInt(startOfLine);
                }

                if (ID == currentID || isFound) {
                    isFound = true;
                    counter++;

                    if (counter == lines.size()) {  // If all unwanted lines are deleted
                        isFound = false;
                    }
                } else {
                    bw.write(currentLine + System.getProperty("line.separator"));
                }
            }

            bw.close();
            br.close();
            boolean delete = f.delete();
            boolean b = temp.renameTo(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
