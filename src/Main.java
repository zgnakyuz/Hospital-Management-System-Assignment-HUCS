import java.io.File;


public class Main {

    public static void main(String[] args) {
        File outputFile = new File("output.txt");
        outputFile.delete();  // To clear the output text every starting of the program

        File inputFile = new File(args[0]);
        String[] inputs = FileIO.readFile(inputFile);

        for (String input : inputs) {
            Utility.applyCommand(input);
        }
    }
}
