package com.example.soto.memoramaprueba.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.soto.memoramaprueba.schema.DaoMaster;
import com.example.soto.memoramaprueba.schema.DaoSession;
import com.example.soto.memoramaprueba.schema.Users;
import com.example.soto.memoramaprueba.schema.UsersDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a singleton implementation for a helper class that contains methods to execute commands related with user entities from the database.
 *
 * @author Jose Soto
 *
 */
public class UserHelper {

    public static final String TAG = UserHelper.class.getName();
    public static final String NO_ERROR = "Process completed";
    public static final String USER_NOT_AVAILABLE = "The specified user name is not available";
    public static final String PASSWORDS_NOT_AVAILABLE = "The specified password is not available";
    public static final String WRONG_PASSWORD_CONFIRMATION = "Wrong password confirmation";
    public static final String MISSING_DATA = "All data are required";
    public static final String INVALID_ACCOUNT = "The specified user name and/or password are invalid";
    public static final String DB_ERROR = "Something fails, try again please";

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private UsersDao usersDao;

    private static UserHelper instance = null;

    /**
     * Method to obtain the current instance of the class.
     * @param context Context that request the instance
     * @return
     */
    public static UserHelper getInstance(Context context){
        if(instance == null) {
            instance = new UserHelper(context);
        }
        return instance;
    }

    /**
     * Protected construct that initializes and creates database session.
     * @param context Context that requested the instance
     */
    protected UserHelper(Context context) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context, "Users", null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        usersDao = daoSession.getUsersDao();
    }

    /**
     * Creates a new user.
     * @param userName user's nickname
     * @param password user's password
     * @param confirmation password's confirmation
     * @return a map instance with the error code and the created user
     */
    public Map createUser(String userName, String password, String confirmation){
        Map response = new HashMap<String, Object>();
        response.put("USER", null);
        String errorCode = NO_ERROR;
        if(userName.trim().isEmpty() || password.trim().isEmpty() || confirmation.trim().isEmpty()){
            errorCode = MISSING_DATA;
        }else{
            List users = usersDao.queryBuilder()
                    .where(UsersDao.Properties.UserName.eq(userName))
                    .list();
            if(!users.isEmpty()){
                errorCode = USER_NOT_AVAILABLE;
            }else{
                users = usersDao.queryBuilder()
                        .where(UsersDao.Properties.Password.eq(password))
                        .list();
                if(!users.isEmpty()){
                    errorCode = PASSWORDS_NOT_AVAILABLE;
                }else{
                    if(!password.equals(confirmation)){
                        errorCode = WRONG_PASSWORD_CONFIRMATION;
                    }else{
                        //Create the user
                        Users newUser = new Users();
                        newUser.setUserName(userName);
                        newUser.setNickName("Null");
                        newUser.setPassword(password);
                        newUser.setImageURL("Null");
                        try {
                            long id = usersDao.insert(newUser);
                            newUser.setId(id);
                            Log.e("TAG", newUser.toString());
                            response.put("USER", newUser);
                        } catch (Exception e) {
                            errorCode = DB_ERROR;
                        }
                    }
                }
            }

        }
        response.put("ERROR", errorCode);
        return response;
    }

    /**
     * Validates user's credentials.
     * @param userName user's nickname
     * @param password user's password
     * @return a map instance with the error code and the created user
     */
    public Map loginUser(String userName, String password){
        Map response = new HashMap<String, Object>();
        response.put("USER", null);
        String errorCode = NO_ERROR;
        if(userName.trim().isEmpty() || password.trim().isEmpty()){
            errorCode = MISSING_DATA;
        }else{
            try{
                Users user = usersDao.queryBuilder()
                        .where(UsersDao.Properties.UserName.eq(userName), UsersDao.Properties.Password.eq(password)).unique();
                if(user == null){
                    errorCode = INVALID_ACCOUNT;
                }else{
                    Log.e("TAG", user.toString());
                    response.put("USER", user);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                errorCode = DB_ERROR;
            }
        }
        response.put("ERROR", errorCode);
        return response;
    }

    /**
     * Checks if the new score is the new best score for the user with the provided ID.
     * @param userId user's nickname
     * @param newScore user's new score
     * @return a map instance with the error code
     */
    public Map checkAndUpdateScore(long userId, long newScore){
        Map response = new HashMap<String, Object>();
        String errorCode = NO_ERROR;

            try{
                Users user = usersDao.load(userId);
                    if(user.getBestScore() > newScore){
                        user.setBestScore(newScore);
                        usersDao.insertOrReplace(user);
                        response.put("NEW_BEST_SCORE", Boolean.TRUE);
                    }else{
                        response.put("NEW_BEST_SCORE", Boolean.FALSE);
                    }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                errorCode = DB_ERROR;
            }
        response.put("ERROR", errorCode);
        return response;
    }

    /**
     * Deletes the user withe the provided ID
     * @param id user's ID
     * @return a map instance with the error code
     */
    public Map deleteUser(long id){
        Map response = new HashMap<String, Object>();
        String errorCode = NO_ERROR;
        try{
            usersDao.deleteByKey(id);
        } catch (Exception e) {
            errorCode = DB_ERROR;
        }
        response.put("ERROR", errorCode);
        return response;
    }

    /**
     * Releases the resources used by the database session.
     */
    public void release(){
        if(instance != null){
            instance = null;
        }
        if(db != null){
            db.close();
            db = null;
        }
        if(daoMaster != null){
            daoMaster = null;
            daoSession.clear();
        }
        if(daoSession != null) daoSession = null;
        if(usersDao != null) usersDao = null;
    }

}
