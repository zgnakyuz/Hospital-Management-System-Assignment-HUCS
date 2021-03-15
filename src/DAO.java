import java.util.ArrayList;

interface DAO {
    abstract Object getByID(int wantedID);
    abstract void deleteByID(int wantedID);
    abstract void add(Object object);
    abstract Object[] getAll();
    abstract ArrayList<String> findDataByID(int wantedID);
}
