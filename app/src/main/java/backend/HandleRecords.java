package backend;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is a generic class,its list accepts an interface,classes that represents the columnsObject will impletement this interface
 * class to checks records when application starts,collect all the data changes available for table like inserts,delete updates etc
 */
public class HandleRecords {

    List<DbColumns> insertedColumnObject=new ArrayList<>();
    List<String> deletedColumnObject=new ArrayList<>();
    List<DbColumns> UpdatedColumnObject=new ArrayList<>();

    public List<String> getDeletedColoumnObjects() {
        return deletedColumnObject;
    }

    public void setDeletedColoumnObject(String DbColumns) {
        deletedColumnObject.add(DbColumns) ;
    }

    public List<DbColumns> getInsertedColoumnObjects() {
        return insertedColumnObject;
    }

    public void setInsertedColoumnObject(DbColumns DbColumns) {
        insertedColumnObject.add(DbColumns);

    }

    public List<DbColumns> getUpdatedColoumnObjects() {
        return UpdatedColumnObject;
    }

    public void setUpdatedColoumnObject(DbColumns DbColumns) {
        UpdatedColumnObject.add(DbColumns);
    }
}
