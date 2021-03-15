import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class Utility {

    public static void applyCommand(String input) {
        PatientDAO patientDAO = new PatientDAO();
        AdmissionDAO admissionDAO = new AdmissionDAO();
        ArrayList<String> inputSplit = new ArrayList<String>(Arrays.asList(input.split("\\s")));
        File outputFile = new File("output.txt");

        String command = inputSplit.get(0);

        switch (command) {
            case "AddPatient":
                int ID = Integer.parseInt(inputSplit.get(1));
                String name = inputSplit.get(2);
                String surname = inputSplit.get(3);
                String phoneNumber = inputSplit.get(4);

                String address = "Address: ";
                for (int i = 5; i < inputSplit.size(); i++) {
                    address += inputSplit.get(i);

                    if (i != inputSplit.size() - 1) {  // Whitespace adjusting to prevent extra space at the end
                        address += " ";
                    }
                }

                Patient patient = new Patient(ID, name, surname, phoneNumber, address);
                patientDAO.add(patient);

                // Writing the output message to the output.txt
                String[] lines = new String[]{String.format("Patient %d %s added", ID, name)};
                FileIO.writeLinesToFile(outputFile, lines);

                break;

            case "RemovePatient":
                int removingID = Integer.parseInt(inputSplit.get(1));
                int removingAdmissionID = admissionDAO.getByPatientID(removingID).getID();
                String removingName = patientDAO.getByID(removingID).getName();

                patientDAO.deleteByID(removingID);
                admissionDAO.deleteByID(removingAdmissionID);

                // Writing the output message to the output.txt
                lines = new String[]{String.format("Patient %d %s removed", removingID, removingName)};
                FileIO.writeLinesToFile(outputFile, lines);

                break;

            case "CreateAdmission":
                int admissionID = Integer.parseInt(inputSplit.get(1));
                int patientID = Integer.parseInt(inputSplit.get(2));

                admissionDAO.add(new Admission(admissionID, patientID));

                // Writing the output message to the output.txt
                lines = new String[]{String.format("Admission %d created", admissionID)};
                FileIO.writeLinesToFile(outputFile, lines);

                break;

            case "AddExamination":
                admissionID = Integer.parseInt(inputSplit.get(1));
                String examinationType = inputSplit.get(2);
                ArrayList<String> operations = new ArrayList<String>(inputSplit.subList(3, inputSplit.size()));

                Examination examination = Examination.create(examinationType);
                examination.addOperation(OperationDecorator.apply(operations, examination));


                Admission admission = admissionDAO.getByID(admissionID);
                admission.getExaminations().add(examination);
                admission.setTotalCost(admission.getTotalCost() + examination.getCost());

                // Deletes the existing admission in the txt and writes the updated
                admissionDAO.deleteByID(admissionID);
                admissionDAO.add(admission);

                // Writing the output message to the output.txt
                lines = new String[]{String.format("%s examination added to admission %d", examinationType, admissionID)};
                FileIO.writeLinesToFile(outputFile, lines);

                break;

            case "TotalCost":
                ArrayList<String> totalCostOutput = new ArrayList<String>();
                admissionID = Integer.parseInt(inputSplit.get(1));
                admission = admissionDAO.getByID(admissionID);

                totalCostOutput.add(String.format("TotalCost for admission %d", admissionID));

                for (Examination exam : admission.getExaminations()) {
                    examinationType = exam.getClass().getName();
                    String operationString = exam.operationsToString();

                    totalCostOutput.add(String.format("\t%s %s %d$", examinationType, operationString, exam.getCost()));
                }

                totalCostOutput.add(String.format("\tTotal: %d$", admission.getTotalCost()));

                // Writing the output message to the output.txt
                lines = totalCostOutput.toArray(new String[0]);
                FileIO.writeLinesToFile(outputFile, lines);

                break;

            case "ListPatients":
                Patient[] patients = patientDAO.getAll();
                ArrayList<String> patientsOutput = new ArrayList<String>();
                patientsOutput.add("Patient List:");

                // Sorting according to the patient names
                Comparator<Patient> nameComparator = Comparator.comparing(Patient::getName);
                Arrays.sort(patients, nameComparator);

                for (Patient pat : patients) {
                    String[] array = pat.toString().split("\\t");
                    String correctedToString = "";
                    for (int i = 0; i < array.length; i++) {
                        String data = array[i];
                        correctedToString += data ;

                        if (i != array.length - 1) {
                            correctedToString += " ";
                        }
                    }

                    patientsOutput.add(correctedToString);
                }

                // Writing the output message to the output.txt
                lines = patientsOutput.toArray(new String[0]);
                FileIO.writeLinesToFile(outputFile, lines);

                break;
        }
    }

    public static boolean isInteger(String string) {
        if (string == null) {
            return false;
        }

        try {
            int num = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
