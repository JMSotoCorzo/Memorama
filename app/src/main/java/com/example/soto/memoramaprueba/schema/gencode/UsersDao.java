package com.example.soto.memoramaprueba.schema;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.example.soto.memoramaprueba.schema.Users;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USERS".
*/
public class UsersDao extends AbstractDao<Users, Long> {

    public static final String TABLENAME = "USERS";

    /**
     * Properties of entity Users.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserName = new Property(1, String.class, "userName", false, "USER_NAME");
        public final static Property NickName = new Property(2, String.class, "nickName", false, "NICK_NAME");
        public final static Property Password = new Property(3, String.class, "password", false, "PASSWORD");
        public final static Property ImageURL = new Property(4, String.class, "imageURL", false, "IMAGE_URL");
        public final static Property BestScore = new Property(5, long.class, "bestScore", false, "BEST_SCORE");
    };


    public UsersDao(DaoConfig config) {
        super(config);
    }
    
    public UsersDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USERS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_NAME\" TEXT NOT NULL ," + // 1: userName
                "\"NICK_NAME\" TEXT NOT NULL ," + // 2: nickName
                "\"PASSWORD\" TEXT NOT NULL ," + // 3: password
                "\"IMAGE_URL\" TEXT NOT NULL ," + // 4: imageURL
                "\"BEST_SCORE\" INTEGER NOT NULL );"); // 5: bestScore
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USERS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Users entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserName());
        stmt.bindString(3, entity.getNickName());
        stmt.bindString(4, entity.getPassword());
        stmt.bindString(5, entity.getImageURL());
        stmt.bindLong(6, entity.getBestScore());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Users readEntity(Cursor cursor, int offset) {
        Users entity = new Users( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // userName
            cursor.getString(offset + 2), // nickName
            cursor.getString(offset + 3), // password
            cursor.getString(offset + 4), // imageURL
            cursor.getLong(offset + 5) // bestScore
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Users entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserName(cursor.getString(offset + 1));
        entity.setNickName(cursor.getString(offset + 2));
        entity.setPassword(cursor.getString(offset + 3));
        entity.setImageURL(cursor.getString(offset + 4));
        entity.setBestScore(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Users entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Users entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
