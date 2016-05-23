package server_commmunication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by johnanderson1 on 3/1/16.
 */
public class CheckForUpdates  {
    boolean updateLocal;
    boolean updateOther;
    boolean addtolocal;
    boolean addtoOther;



    String uniquekeyForOthers;
    String uniquekeyForLocal;
    String keyfornewRecordForLocal;

    String keyForNewRecordForOther;

    public void setUpdateLocal(boolean val){


    updateLocal=val;
    }

    public  boolean getUpdateLocal(){


        return updateLocal;
    }

    public void setUpdateOther(boolean val){


        updateOther=val;
    }

    public  boolean getUpdateOther(){


        return updateOther;
    }



    /*@param call this method to set if records are on the local system and not on the server
    * */
    public void setAddToOther(boolean val){


        addtoOther=val;
    }
        /*@param call this method to know if record are on local system and not on the server
    * */

    public boolean isAddToOther(){


      return addtoOther;
    }

    /*@param get ServerKeys to be added to the server
    * */
    public String getKeyForNewRecordForOther() {
        return keyForNewRecordForOther;
    }

    /*@param set ServerKeys to be added to the server
 * */
    public void setKeyForNewRecordForOther(String keyForNewRecordForOther) {
        this.keyForNewRecordForOther = keyForNewRecordForOther;
    }


    public boolean isAddtolocal()
    {

      return addtolocal  ;
    }

    /**@param called the set to true when new ServerKeys are available for records to be added to local machine */
    public void setAddtolocal(boolean addtlocal)
    {

        addtolocal=addtlocal;
    }

    /**@param called the set ServerKeys that would be added to local system */
    public void setKeystoAddlocal(String key){


        keyfornewRecordForLocal=key;
    }


    /**@param called the get ServerKeys that would be added to local system */
    public String getKeysToAddToLocal(){

        return keyfornewRecordForLocal;
    }
    public String getUniquekeyForLocal() {
        return uniquekeyForLocal;
    }

    public void setUniquekeyForLocal(String uniquekeyForLocal) {
        this.uniquekeyForLocal = uniquekeyForLocal;
    }

    public String getUniquekeyForOthers() {

        return uniquekeyForOthers;
    }

    public void setUniquekeyForOthers(String uniquekeyForOthers) {
        this.uniquekeyForOthers = uniquekeyForOthers;
    }
}
