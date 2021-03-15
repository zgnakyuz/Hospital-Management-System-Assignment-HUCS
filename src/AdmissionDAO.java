import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class AdmissionDAO implements DAO {
    private File admissionDB = new File("admission.txt");

    public Admission getByPatientID(int wantedID) {
        ArrayList<String> foundData = findDataByPatientID(wantedID);
        String firstLine = foundData.get(0);
        String[] firstLineSplit = firstLine.split("\\t");
        int admissionID = Integer.parseInt(firstLineSplit[0]);

        return getByID(admissionID);
    }

    @Override
    public Admission getByID(int wantedID) {
        ArrayList<String> foundData = findDataByID(wantedID);
        boolean isFirstLine = true;  // To get the admissionID and patientID
        int admissionID = 0;
        int patientID = 0;
        int totalCost = 0;
        ArrayList<Examination> examinations = new ArrayList<Examination>();

        for (String data : foundData) {
            String[] dataSplit = data.split("\\t");

            if (isFirstLine) {
                admissionID = Integer.parseInt(dataSplit[0]);
                patientID = Integer.parseInt(dataSplit[1]);
                isFirstLine = false;
                continue;
            }

            String examinationType = dataSplit[0];
            Examination examination = Examination.create(examinationType);
            ArrayList<String> operations = new ArrayList<String>(Arrays.asList(dataSplit[1].split("\\s")));
            examination.addOperation(OperationDecorator.apply(operations, examination));
            examinations.add(examination);

            totalCost += examination.getCost();
        }

        Admission admission = new Admission(admissionID, patientID, examinations, totalCost);
        return admission;
    }

    @Override
    public void deleteByID(int wantedID) {
        ArrayList<String> data = findDataByID(wantedID);

        FileIO.deleteLinesByID(admissionDB, data);
    }

    @Override
    public void add(Object object) {
        Admission admission = (Admission) object;

        FileIO.addLineToFileByID(admissionDB, admission.toString(), admission.getID());
    }

    @Override
    public Admission[] getAll() {
        return null;
    }

    public ArrayList<String> findDataByPatientID(int wantedID) {
        ArrayList<String> foundData = new ArrayList<String>();
        String[] data = FileIO.readFile(admissionDB);
        boolean isFound = false;

        for (String line : data) {
            String[] lineSplit = line.split("\\t");
            String IDCandidate = lineSplit[1];
            int patientID;

            // If another admission is found after our admission, loop will be stop
            if (Utility.isInteger(IDCandidate)) {
                if (isFound) {
                    break;
                }
            }

            if (isFound) {
                foundData.add(line);
            }

            if (Utility.isInteger(IDCandidate)) {
                patientID = Integer.parseInt(IDCandidate);

                if (patientID == wantedID) {
                    isFound = true;
                    foundData.add(line);
                }
            }
        }

        return foundData;
    }

    @Override
    public ArrayList<String> findDataByID(int wantedID) {
        ArrayList<String> foundData = new ArrayList<String>();
        String[] data = FileIO.readFile(admissionDB);
        boolean isFound = false;

        for (String line : data) {
            String[] lineSplit = line.split("\\t");
            String startOfLine = lineSplit[0];
            int admissionID;

            // If another admission is found after our admission, loop will be stop
            if (Utility.isInteger(startOfLine)) {
                if (isFound) {
                    break;
                }
            }

            if (isFound) {
                foundData.add(line);
            }

            if (Utility.isInteger(startOfLine)) {
                admissionID = Integer.parseInt(startOfLine);

                if (admissionID == wantedID) {
                    isFound = true;
                    foundData.add(line);
                }
            }
        }

        return foundData;
    }
}
