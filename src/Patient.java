public class Patient {
    private int ID;
    private String name;
    private String surname;
    private String phoneNumber;
    private String address;

    public Patient(int ID, String name, String surname, String phoneNumber, String address) {
        this.ID = ID;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%d\t%s %s\t%s\t%s", this.ID, this.name, this.surname, this.phoneNumber, this.address);
    }
}
