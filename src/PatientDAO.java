import java.io.File;
import java.util.ArrayList;


public class PatientDAO implements DAO {
    private File patientDB = new File("patient.txt");

    @Override
    public Patient getByID(int wantedID) {
        ArrayList<String> foundData = findDataByID(wantedID);

        for (String data : foundData) {
            String[] dataSplit = data.split("\\t");
            String[] nameAndSurname = dataSplit[1].split("\\s");
            int ID = Integer.parseInt(dataSplit[0]);
            String name = nameAndSurname[0];
            String surname = nameAndSurname[1];
            String phoneNumber = dataSplit[2];
            String address = dataSplit[3];

            return new Patient(ID, name, surname, phoneNumber, address);
        }

        return null;
    }

    @Override
    public void deleteByID(int wantedID) {
        ArrayList<String> data = findDataByID(wantedID);

        FileIO.deleteLinesByID(patientDB, data);
    }

    @Override
    public void add(Object object) {
        Patient patient = (Patient) object;

        FileIO.addLineToFileByID(patientDB, patient.toString(), patient.getID());
    }

    @Override
    public Patient[] getAll() {
        String[] data = FileIO.readFile(patientDB);
        ArrayList<Patient> patients = new ArrayList<Patient>();

        for (String line : data) {
            int patientID = Integer.parseInt(line.split("\\t")[0]);
            Patient patient = getByID(patientID);

            patients.add(patient);
        }

        return patients.toArray(new Patient[0]);
    }

    @Override
    public ArrayList<String> findDataByID(int wantedID) {
        ArrayList<String> foundData = new ArrayList<String>();
        String[] data = FileIO.readFile(patientDB);

        for (String line : data) {
            String[] lineSplit = line.split("\\s+");
            int ID = Integer.parseInt(lineSplit[0]);

            if (ID == wantedID) {
                foundData.add(line);
                return foundData;
            }
        }

        return null;
    }
}
