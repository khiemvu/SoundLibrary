package com.oman.allinone.database.ormlite;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.oman.allinone.database.entity.Favourite;

import java.sql.SQLException;

/**
 * Created by Khiemvx on 5/13/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
// ------------------------------ FIELDS ------------------------------

    private static final int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "SoundLibrary.db";
    public static DatabaseHelper instance;
    private Dao<Favourite, String> FavouriteDAO;
    private Context context;


// --------------------------- CONSTRUCTORS ---------------------------

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static DatabaseHelper getInstance(Activity activity) {
        if (instance == null) {
            instance = new DatabaseHelper(activity);
        }
        return instance;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public Dao<Favourite, String> getFavouriteDAO() throws SQLException {
        if (FavouriteDAO == null) {
            FavouriteDAO = getDao(Favourite.class);
            ((BaseDaoImpl) FavouriteDAO).initialize();
        }
        return FavouriteDAO;
    }


// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Favourite.class);

        } catch (SQLException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Favourite.class, true);
            TableUtils.createTable(connectionSource, Favourite.class);


        } catch (SQLException e) {
        }
    }

    public void dropAllDatabase() throws SQLException {
        TableUtils.dropTable(connectionSource, Favourite.class, true);


    }

    public void createTables() throws SQLException {
        TableUtils.createTable(connectionSource, Favourite.class);

    }
}
