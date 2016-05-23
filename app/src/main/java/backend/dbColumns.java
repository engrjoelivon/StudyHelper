package backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.List;


/**
 * Serve as a layer over the database,and as an interface between the data structure sent to the server and the data recieved from the server
 * DbColumns interface should be implemented by a class that represents the db column  database class,the class will have setters
 * and getters method for all the columns present in the db.And must override the getObject as list method,this is appropriate since
 * it can be easily passed to the server as a list of string,in the case where json would be used.
   it should be used for communication between server and client and also for json serialized data.
 */
public interface DbColumns {

     String getObjectAsJsonArrayString();
     String acceptRecordsFromServer(JsonArray recordArray);
     List<String> deletedRecordsKeys(JsonArray deleteArray);
     List<String> acceptupdatedRecordsFromServer(JsonArray recordArray);
     /**
      *<h3>
      *
      * called the first time an application runs on a device to verify if application is new,returns a true or false
      *</h3>
      * */
     boolean isApplicationNew();
}
