package server_commmunication;

import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by johnanderson1 on 2/22/16.
 */
public interface HandleFirebaseInterface {




    void login(int code);
    void reg(int code,Map<String, Object> res);
    void errorCode(FirebaseError fe);



}
