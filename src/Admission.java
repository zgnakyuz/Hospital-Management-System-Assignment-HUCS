import java.util.ArrayList;


public class Admission {
    private int ID;
    private int patientID;
    private ArrayList<Examination> examinations = new ArrayList<Examination>();
    private int totalCost;

    // Constructor for no given examinations
    public Admission(int ID, int patientID) {
        this.ID = ID;
        this.patientID = patientID;
    }

    public Admission(int ID, int patientID, ArrayList<Examination> examinations, int totalCost) {
        this.ID = ID;
        this.patientID = patientID;
        this.examinations = examinations;
        this.totalCost = totalCost;
    }

    public int getID() {
        return ID;
    }

    public ArrayList<Examination> getExaminations() {
        return examinations;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString(){
        String string = String.format("%d\t%s", this.ID, this.patientID);

        for (int i = 0; i < this.examinations.size(); i++) {
            Examination examination = this.examinations.get(i);
            String examinationType = examination.getClass().getName();
            String operations = examination.operationsToString();

            string += System.getProperty("line.separator") + String.format("%s\t%s", examinationType, operations);
        }

        return string;
    }
}
