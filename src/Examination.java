import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public abstract class Examination {
    private int cost;
    private ArrayList<String> operations = new ArrayList<String>();

    public void addOperation(OperationDecorator operation) {
        String[] array = operation.toString().split("\\s");
        String[] operations = Arrays.copyOfRange(array, 1, array.length);

        Collections.reverse(Arrays.asList(operations));  // To keep the order the same order as input's

        this.operations = new ArrayList<String>(Arrays.asList(operations));
        this.cost = operation.getCost();
    }

    public String operationsToString() {
        String operations = "";

        for (String operation : this.operations) {
            operations += operation;
            if (!operation.equals(this.operations.get(this.operations.size() - 1))) {
                operations += " ";
            }
        }

        return operations;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public static Examination create(String examinationType) {
        switch (examinationType) {
            case "Inpatient":
                return new Inpatient();

            case "Outpatient":
                return new Outpatient();

            default:
                return null;
        }
    }
}


class Inpatient extends Examination {

    public Inpatient() {
        setCost(10);
    }

    @Override
    public String toString() {
        return "Inpatient";
    }
}


class Outpatient extends Examination {

    public Outpatient() {
        setCost(15);
    }

    @Override
    public String toString() {
        return "Outpatient";
    }
}


abstract class OperationDecorator extends Examination {
    public static OperationDecorator apply(ArrayList<String> operations, Examination examination) {
        String operation = operations.get(0);
        operations.remove(0);

        // If statements to stop the recursion
        switch (operation) {
            case "doctorvisit":
                if (operations.size() == 0) {
                    return new DoctorVisit(examination);
                }
                return new DoctorVisit(OperationDecorator.apply(operations, examination));

            case "imaging":
                if (operations.size() == 0) {
                    return new Imaging(examination);
                }
                return new Imaging(OperationDecorator.apply(operations, examination));

            case "tests":
                if (operations.size() == 0) {
                    return new Tests(examination);
                }
                return new Tests(OperationDecorator.apply(operations, examination));

            case "measurements":
                if (operations.size() == 0) {
                    return new Measurements(examination);
                }
                return new Measurements(OperationDecorator.apply(operations, examination));

            default:
                return null;
        }
    }
}


class DoctorVisit extends OperationDecorator {
    Examination examination;

    public DoctorVisit(Examination examination) {
        this.examination = examination;
    }

    @Override
    public int getCost() {
        return 15 + examination.getCost();
    }

    @Override
    public String toString() {
        return examination.toString() + " doctorvisit";
    }
}


class Imaging extends OperationDecorator {
    Examination examination;

    public Imaging(Examination examination) {
        this.examination = examination;
    }

    @Override
    public int getCost() {
        return 10 + examination.getCost();
    }

    @Override
    public String toString() {
        return examination.toString() + " imaging";
    }
}


class Tests extends OperationDecorator {
    Examination examination;

    public Tests(Examination examination) {
        this.examination = examination;
    }

    @Override
    public int getCost() {
        return 7 + examination.getCost();
    }

    @Override
    public String toString() {
        return examination.toString() + " tests";
    }
}


class Measurements extends OperationDecorator {
    Examination examination;

    public Measurements(Examination examination) {
        this.examination = examination;
    }

    @Override
    public int getCost() {
        return 5 + examination.getCost();
    }

    @Override
    public String toString() {
        return examination.toString() + " measurements";
    }
}
