package yc.com.calendar.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import yc.com.calendar.bean.HuangLiDbInfo;
import yc.com.calendar.bean.IndexDate;

/**
 * Created by wanglin  on 2018/1/15 11:03.
 * 数据库操作类
 */

public class DbManager {


    private static String dbName = "saa.db";


    /**
     * This is a Assets Database Manager
     * Use it, you can use a assets database file in you application
     * It will copy the database file to "/data/data/[your application package name]/database" when you first time you use it
     * Then you can get a SQLiteDatabase object by the assets database file
     *
     * @author smartTop
     * @time 2012-09-20
     * <p>
     * <p>
     * How to use:
     * 1. Initialize AssetsDatabaseManager
     * 2. Get AssetsDatabaseManager
     * 3. Get a SQLiteDatabase object through database file
     * 4. Use this database object
     * <p>
     * Using example:
     * AssetsDatabaseManager.initManager(getApplication()); // this method is only need call one time
     * AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  // get a AssetsDatabaseManager object
     * SQLiteDatabase db1 = mg.getDatabase("db1.db");  // get SQLiteDatabase object, db1.db is a file in assets folder
     * db1.??? // every operate by you want
     * Of cause, you can use AssetsDatabaseManager.getManager().getDatabase("xx") to get a database when you need use a database
     */

    private static String tag = "AssetsDatabase"; // for LogCat
    private static String databasepath; // %s is packageName
    // A mapping from assets database file to SQLiteDatabase object
    private Map<String, SQLiteDatabase> databases = new HashMap<String, SQLiteDatabase>();
    // Context of application
    private Context context = null;
    // Singleton Pattern
    private static DbManager mInstance = null;

    /**
     * Initialize AssetsDatabaseManager
     *
     * @param context, context of application
     */
    public static void initManager(Context context) {
        if (mInstance == null) {
            mInstance = new DbManager(context);
            String sdPath = SDCardUtils.getSDCardPath();
            if (sdPath == null) {
                sdPath = context.getCacheDir().getPath();
            }

            databasepath = sdPath + "/calendar/%s/db";
        }
    }

    /**
     * Get a AssetsDatabaseManager object
     *
     * @return, if success return a AssetsDatabaseManager object, else return null
     */
    public static DbManager getManager() {
        return mInstance;
    }

    private DbManager(Context context) {
        this.context = context;
    }

    /**
     * Get a assets database, if this database is opened this method is only return a copy of the opened database
     *
     * @param , the assets file which will be opened for a database
     * @return, if success it return a SQLiteDatabase object else return null
     */
    public SQLiteDatabase getDatabase() {
        if (databases.get(dbName) != null) {
            LogUtils.i(tag, String.format("Return a database copy of %s", dbName));
            return (SQLiteDatabase) databases.get(dbName);
        }
        if (context == null)
            return null;
        LogUtils.i(tag, String.format("Create database %s", dbName));
        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbName);
        File file = new File(sfile);
        SharedPreferences dbs = context.getSharedPreferences(DbManager.class.toString(), 0);
        boolean flag = dbs.getBoolean(dbName, false); // Get Database file flag, if true means this database file was copied and valid
        if (!flag || !file.exists()) {
            file = new File(spath);
            if (!file.exists() && !file.mkdirs()) {
                LogUtils.i(tag, "Create \"" + spath + "\" fail!");
                return null;
            }
            if (!copyAssetsToFilesystem(dbName, sfile)) {
                LogUtils.i(tag, String.format("Copy %s to %s fail!", dbName, sfile));
                return null;
            }
            dbs.edit().putBoolean(dbName, true).commit();
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null, SQLiteDatabase.OPEN_READONLY);
        if (db != null) {
            databases.put(dbName, db);
        }
        return db;
    }

    private String getDatabaseFilepath() {
        return String.format(databasepath, context.getApplicationInfo().packageName);
    }

    private String getDatabaseFile(String dbfile) {
        return getDatabaseFilepath() + "/" + dbfile;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des) {
        LogUtils.i(tag, "Copy " + assetsSrc + " to " + des);
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            AssetManager am = context.getAssets();
            istream = am.open(assetsSrc);
            ostream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer)) > 0) {
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (istream != null)
                    istream.close();
                if (ostream != null)
                    ostream.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * Close assets database
     *
     * @param dbfile, the assets file which will be closed soon
     * @return, the status of this operating
     */
    public boolean closeDatabase(String dbfile) {
        if (databases.get(dbfile) != null) {
            SQLiteDatabase db = (SQLiteDatabase) databases.get(dbfile);
            db.close();
            databases.remove(dbfile);
            return true;
        }
        return false;
    }

    /**
     * Close all assets database
     */
    public static void closeAllDatabase() {
        LogUtils.i(tag, "closeAllDatabase");
        if (mInstance != null) {
            for (int i = 0; i < mInstance.databases.size(); ++i) {
                if (mInstance.databases.get(i) != null) {
                    mInstance.databases.get(i).close();
                }
            }
            mInstance.databases.clear();
        }
    }

    private SQLiteDatabase sqliteDB;

    private IndexDate getJxInfo(String[] columns, String selection, String[] selectionArgs) {

        sqliteDB = getDatabase();
        try {
            String table = "IndexTable";
            Cursor cursor = sqliteDB.query(table, columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {

                String jx = cursor.getString(cursor.getColumnIndex("jx"));
                String gz = cursor.getString(cursor.getColumnIndex("gz"));

                IndexDate indexDate = new IndexDate(jx, gz);

                cursor.moveToNext();
                cursor.close();
                return indexDate;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private HuangLiDbInfo getHuangliInfo(String[] columns, String selection, String[] selectionArgs) {

        try {
            String table = "YJData";

            Cursor cursor = sqliteDB.query(table, columns, selection, selectionArgs, null, null, null);
            if (cursor.moveToFirst()) {
                String ji = cursor.getString(cursor.getColumnIndex("ji"));
                String yi = cursor.getString(cursor.getColumnIndex("yi"));

                HuangLiDbInfo huangLiDbInfo = new HuangLiDbInfo(ji, yi);

                cursor.moveToNext();
                cursor.close();
//                sqliteDB.close();
                return huangLiDbInfo;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public HuangLiDbInfo getYiJiInfo(String date) {
        if (TextUtils.isEmpty(date)) return null;
        StringBuilder sb = new StringBuilder();

        String[] dates = date.split("-");
        sb.append(dates[0]).append("-");
        if (!dates[1].startsWith("0") && Integer.parseInt(dates[1]) < 10) {
            sb.append("0").append(dates[1]);
        } else {
            sb.append(dates[1]);
        }
        sb.append("-");
        if (!dates[2].startsWith("0") && Integer.parseInt(dates[2]) < 10) {
            sb.append("0").append(dates[2]);

        } else {
            sb.append(dates[2]);
        }

        String[] coloums = new String[]{"jx", "gz"};
        String selections = "_Date = '" + sb.toString() + "'";
        IndexDate jxInfo = getJxInfo(coloums, selections, null);
        String[] coloums1 = new String[]{"ji", "yi"};
        if (jxInfo != null) {
            String selections1 = "jx = " + jxInfo.getJx() + " and gz = " + jxInfo.getGz();
            return getHuangliInfo(coloums1, selections1, null);
        }

        return null;
    }
}
