package pe.saul.runapp.Modelos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import pe.saul.runapp.R;
import pe.saul.runapp.Modelos.Enum.ActivityTypes;

/**
 * Created by Saul on 14/02/2018.
 */

public class DBAccessHelper extends SQLiteOpenHelper {
    private static final String TAG = pe.saul.runapp.Modelos.DBAccessHelper.class
            .getSimpleName();

    // Name of the file, which contains the database
    private static final String DATABASE_NAME = "runapp.db";
    // If version changes, the database will be renewed
    private static final int DATABASE_VERSION = 7;

    private static String CREATE_TRACKS = "CREATE TABLE tracks(" +
            "tid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "tname TEXT NOT NULL UNIQUE " +
            "); ";
    private static String DROP_TRACKS = "DROP TABLE IF EXISTS tracks;";

    private static String CREATE_ACTIVITIES = "CREATE TABLE activities(" +
            "aid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "atype TEXT NOT NULL, " +
            "adate INTEGER NOT NULL, " +
            "aduration INTEGER NOT NULL, " +
            "tid INTEGER NOT NULL, " +
            "FOREIGN KEY (tid) REFERENCES tracks(tid) " +
            "ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";
    private static String DROP_ACTIVITIES = "DROP TABLE IF EXISTS activities;";

    private static String CREATE_COORDINATES = "CREATE TABLE coordinates(" +
            "cid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "clongitude REAL NOT NULL, " +
            "clatitude REAL NOT NULL, " +
            "cisstart INTEGER NOT NULL," +
            "cisend INTEGER NOT NULL," +
            "ctimefromstart INTEGER NOT NULL," +
            "cdistancefromprevious REAL NOT NULL," +
            "aid INTEGER NOT NULL, " +
            "cispause INTEGER NOT NULL," +
            "FOREIGN KEY (aid) REFERENCES activities(aid) " +
            "ON DELETE CASCADE ON UPDATE CASCADE" +
            "); ";
    private static String DROP_COORDINATES = "DROP TABLE IF EXISTS coordinates;";

    private static String INSERT_TRACK1 = "INSERT INTO tracks(tid, tname) "
            + "  VALUES(1, \"Ahornach-Rein\");";
    private static String INSERT_TRACK2 = "INSERT INTO tracks(tid, tname) "
            + "  VALUES(2, \"Ahornach-Knutten\");";

    private static String INSERT_ACTIVITY1 = "INSERT INTO activities(aid, atype, adate, " +
            "aduration, tid) "
            + "  VALUES(1, \"RUNNING\", 1447115888179, 660, 1);";
    private static String INSERT_ACTIVITY2 = "INSERT INTO activities(aid, atype, adate, " +
            "aduration, tid) "
            + "  VALUES(2, \"CYCLING\", 1451394599000, 5108, 2);";
    private static String INSERT_ACTIVITY3 = "INSERT INTO activities(aid, atype, adate, " +
            "aduration, tid) "
            + "  VALUES(3, \"CYCLING\", 1449929469000, 5301, 2);";

    private static String INSERT_COORDINATE1 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(1, 11.354850, 46.498012, 1, 0, 0, 0, 1, 0);";
    private static String INSERT_COORDINATE2 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(2, 11.354882, 46.497813, 0, 0, 5, 11, 1, 0);";
    private static String INSERT_COORDINATE3 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(3, 11.355027, 46.497625, 0, 0, 10, 14, 1, 0);";
    private static String INSERT_COORDINATE4 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(4, 11.355190, 46.497554, 0, 1, 15, 9, 1, 0);";
    private static String INSERT_COORDINATE5 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(5, -14.589844, 22.715390, 1, 0, 0, 12903, 2, 0);";
    private static String INSERT_COORDINATE6 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(6, 21.818848, 22.690052, 0, 0, 166543, 5322.1, 2, 0);";
    private static String INSERT_COORDINATE7 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(7, 31.047363, 30.916364, 0, 1, 211002, 976.8, 2, 0);";
    private static String INSERT_COORDINATE8 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(8, -14.589844, 22.715390, 1, 0, 0, 12903, 3, 0);";
    private static String INSERT_COORDINATE9 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(9, 21.818848, 22.690052, 0, 0, 166543, 5322.1, 3, 0);";
    private static String INSERT_COORDINATE10 = "INSERT INTO coordinates(cid, clongitude, " +
            "clatitude, cisstart, cisend, ctimefromstart, cdistancefromprevious, aid, cispause) "
            + "  VALUES(10, 31.047363, 30.916364, 0, 1, 211002, 976.8, 3, 0);";

    private static DBAccessHelper instance = null;

    /**
     * Private, because of Singleton-Pattern
     *
     * @param context
     */
    private DBAccessHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Returns an instance of this class
     *
     * @param context
     * @return
     */
    public static pe.saul.runapp.Modelos.DBAccessHelper getInstance(
            Context context) {
        if (instance == null)
            instance = new pe.saul.runapp.Modelos.DBAccessHelper(
                    context);
        return instance;
    }

    /**
     * Enable SQLite foreign key constrain because of ON DELETE CASCADE
     *
     * @param db
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    /**
     * Called if there is no database
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TRACKS);
        sqLiteDatabase.execSQL(CREATE_ACTIVITIES);
        sqLiteDatabase.execSQL(CREATE_COORDINATES);
        Log.d(TAG, "DB created");
        // Used for Testing
        /*insertTestData(sqLiteDatabase);
        Log.d(TAG, "Test data inserted");*/
    }

    /**
     * Called when the version of the database changes
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TRACKS);
        sqLiteDatabase.execSQL(DROP_ACTIVITIES);
        sqLiteDatabase.execSQL(DROP_COORDINATES);
        onCreate(sqLiteDatabase);
    }

    private void insertTestData(SQLiteDatabase sqlLiteDatabase) {
        sqlLiteDatabase.execSQL(INSERT_TRACK1);
        sqlLiteDatabase.execSQL(INSERT_TRACK2);
        sqlLiteDatabase.execSQL(INSERT_ACTIVITY1);
        sqlLiteDatabase.execSQL(INSERT_ACTIVITY2);
        sqlLiteDatabase.execSQL(INSERT_ACTIVITY3);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE1);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE2);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE3);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE4);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE5);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE6);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE7);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE8);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE9);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE10);
    }

    /**
     * Methods that select something from the database
     */

    /**
     * Selects all tracks from the database and orders it by the number of activities which
     * belongs to a track
     *
     * @return null if no tracks have been found
     */
    public ArrayList<Track> getTracks() {
        ArrayList<Track> ret = null;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            c = db.rawQuery("SELECT tid, tname, (SELECT COUNT(*) FROM activities WHERE tracks" +
                    ".tid=activities.tid)" + "  FROM tracks ORDER BY 3 DESC;", null);
            while (c.moveToNext()) {
                if (ret == null) {
                    ret = new ArrayList<Track>();
                }
                ret.add(new Track(c.getInt(0), c.getString(1)));
            }
            if (ret != null) {
                // If activities are found, then they are inserted
                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).setActivities(getActivities(ret.get(i)));
                }
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Error in getTracks(): " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        if (ret != null) {
            Log.d(TAG, "getTracks() was successful");
        }
        return ret;
    }

    /**
     * Selects all activities from a certain track and orders by date descending. The activity
     * gets a reference to the track
     *
     * @param t
     * @return null if no activities have been found
     */
    public ArrayList<Activity> getActivities(Track t) {
        ArrayList<Activity> ret = null;
        if (t != null) {
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = getWritableDatabase();
                c = db.rawQuery("SELECT a1.*, (SELECT COUNT(*) FROM activities a2 WHERE a2.atype " +
                                "= a1.atype AND a2.aid <> a1.aid AND a2.tid= ?) AS count" + "  " +
                                "FROM activities a1"
                                + "  WHERE tid = ? " + "ORDER BY count DESC, atype, adate DESC;",
                        new String[]{String.valueOf(t.getId()), String.valueOf(t.getId())});
                while (c.moveToNext()) {
                    if (ret == null) {
                        ret = new ArrayList<Activity>();
                    }
                    ret.add(new Activity(c.getInt(0), ActivityTypes.Type.valueOf(c.getString(1)),
                            c.getLong(2), c.getInt(3), t));
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in getActivities(): " + e.getMessage());
            } finally {
                try {
                    c.close();
                } catch (Exception e) {
                }
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
        if (ret != null) {
            Log.d(TAG, "getActivities() was successful");
        }
        return ret;
    }

    /**
     * Selects an activity with a certain id
     *
     * @param id
     * @return null if no activity has been found
     */
    public Activity getActivity(int id) {
        Activity ret = null;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            c = db.rawQuery("SELECT * " + "  FROM activities "
                            + "  WHERE aid = ?;",
                    new String[]{String.valueOf(id)});
            if (c.moveToFirst()) {
                ret = new Activity(c.getInt(0), ActivityTypes.Type.valueOf(c.getString(1)), c
                        .getLong(2), c.getInt(3));
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Error in getActivity(): " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        if (ret != null)
            Log.d(TAG, "getActivity() was successful");
        return ret;
    }

    /**
     * Selects all coordinates from a certain activity. The coordinate gets a reference to the
     * activity
     *
     * @param a
     * @return
     */
    public ArrayList<Coordinate> getCoordinates(Activity a) {
        ArrayList<Coordinate> ret = null;
        if (a != null) {
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = getWritableDatabase();
                c = db.rawQuery("SELECT * " + "  FROM coordinates "
                                + "  WHERE aid = ? " + ";",
                        new String[]{String.valueOf(a.getId())});
                while (c.moveToNext()) {
                    if (ret == null)
                        ret = new ArrayList<>();
                    ret.add(new Coordinate(c.getInt(0), c.getDouble(1), c.getDouble(2), c.getInt
                            (3) > 0, c.getInt(4) > 0, c.getInt(5), c.getDouble(6), a, c.getInt(8)
                            > 0));
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in getCoordinates(): " + e.getMessage());
            } finally {
                try {
                    c.close();
                } catch (Exception e) {
                }
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
        if (ret != null) {
            Log.d(TAG, "getCoordinates() was successful");
        }
        return ret;
    }

    /**
     * Selects a coordinate with a certain id
     *
     * @param id
     * @return
     */
    public Coordinate getCoordinate(int id) {
        Coordinate ret = null;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            c = db.rawQuery("SELECT * " + "  FROM coordinates "
                            + "  WHERE cid = ?;",
                    new String[]{String.valueOf(id)});
            if (c.moveToFirst()) {
                ret = new Coordinate(c.getInt(0), c.getDouble(1), c.getDouble(2), c.getInt(3) >
                        0, c.getInt(4) > 0, c.getInt(5), c.getDouble(6), c.getInt(8) > 0);
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Error in getCoordinate(): " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        if (ret != null) {
            Log.d(TAG, "getCoordinate() was successful");
        }
        return ret;
    }

    /**
     * @param longitude longitude of the actual position
     * @param latitude  latitude of the actual position
     * @param id        id of the activity
     * @return
     */
    public int getIDOfClosestCoordinateInActivity(double longitude, double latitude, int id) {
        int ret = -1;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            String[] selectionArgs = new String[5];
            selectionArgs[0] = String.valueOf(longitude);
            selectionArgs[1] = String.valueOf(longitude);
            selectionArgs[2] = String.valueOf(latitude);
            selectionArgs[3] = String.valueOf(latitude);
            selectionArgs[4] = String.valueOf(id);
            // a^2 + b^2 = c^2
            c = db.rawQuery("SELECT cid, ((?-clongitude)*(?-clongitude)+(?-clatitude)*" +
                            "(?-clatitude))" + "  FROM coordinates "
                            + "  WHERE aid = ? ORDER BY 2;",
                    selectionArgs);
            if (c.moveToFirst()) {
                ret = c.getInt(0);
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Error in getIDOfClosestCoordinateInActivity(): " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        if (ret != -1) {
            Log.d(TAG, "getIDOfClosestCoordinateInActivity() was successful");
        }
        return ret;
    }

    /**
     * Methods that insert something in the database
     */

    /**
     * Inserts a track into the database if the track is valid
     *
     * @param t
     * @return 0 if it was successful, otherwise -1
     */
    public int insertTrack(Track t) {
        int ret = 0;
        if (t == null) {
            ret = -1;
        } else {
            t.validate();
            if (t.getError() != null) {
                ret = -1;
            } else {
                SQLiteDatabase db = null;
                try {
                    db = getWritableDatabase();
                    ContentValues values = new ContentValues(1);
                    values.put("tname", t.getName());
                    ret = (int) db.insertOrThrow("tracks", null, values);
                    if (ret >= 0) {
                        t.setId(ret);
                        ret = 0;
                    } else {
                        ret = -1;
                    }
                } catch (SQLiteConstraintException e) {
                    t.setError(
                            "name",
                            Track.NAME_ALREADY_EXISTS);
                    ret = -1;
                } catch (SQLiteException e) {
                    Log.d(TAG, "Error in insertTrack(): " + e.getMessage());
                    ret = -1;
                } finally {
                    try {
                        db.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        if (ret == 0) {
            Log.d(TAG, "insertTrack() was successful");
        }
        return ret;
    }

    /**
     * Inserts an activity into the database if the activity has a track and the coordinates of the
     * activity are valid. If an error occurs, the activity and their coordinates will be deleted.
     * For example: If only one coordinate can`t be inserted every coordinate of the activity and
     * the activity itself will be deleted
     *
     * @param a
     * @return 0 if it was successful, otherwise -1
     */
    public int insertActivity(Activity a) {
        int ret = 0;
        if (a == null || a.getTrack() == null) {
            ret = -1;
        } else {
            SQLiteDatabase db = null;
            try {
                db = getWritableDatabase();
                db.beginTransaction();
                ContentValues values = new ContentValues(1);
                values.put("atype", a.getType().toString());
                values.put("adate", a.getDate());
                values.put("aduration", a.getDuration());
                values.put("tid", a.getTrack().getId());
                ret = (int) db.insert("activities", null, values);
                if (ret >= 0) {
                    a.setId(ret);
                    ret = 0;
                    // If coordinates are found, then they are inserted
                    if (a.getCoordinates() != null) {
                        for (int i = 0; i < a.getCoordinates().size(); i++) {
                            if (insertCoordinate(a.getCoordinates().get(i), db) == -1) {
                                ret = -1;
                            }
                        }
                    }
                } else {
                    ret = -1;
                }
                if (ret >= 0) {
                    db.setTransactionSuccessful();
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in insertActivity(): " + e.getMessage());
                ret = -1;
            } finally {
                try {
                    db.endTransaction();
                    db.close();
                } catch (Exception e) {
                }
            }
        }
        if (ret == 0) {
            Log.d(TAG, "insertActivity() was successful");
        }
        return ret;
    }

    /**
     * Inserts a coordinate into the database if the coordinate has a activity.
     * It needs a SQLiteDatabase object because the sql command in this method is part of a
     * transaction in the method 'insertActivity(Activity a)'
     *
     * @param c
     * @return 0 if it was successful, otherwise -1
     */
    private int insertCoordinate(Coordinate c, SQLiteDatabase db) {
        int ret = 0;
        if (c == null || c.getActivity() == null) {
            ret = -1;
        } else {
            try {
                ContentValues values = new ContentValues(1);
                values.put("clongitude", c.getLongitude());
                values.put("clatitude", c.getLatitude());
                int isStart = (c.isStart()) ? 1 : 0;
                int isEnd = (c.isEnd()) ? 1 : 0;
                int isPause = (c.isPause()) ? 1 : 0;
                values.put("cisstart", isStart);
                values.put("cisend", isEnd);
                values.put("ctimefromstart", c.getTimeFromStart());
                values.put("cdistancefromprevious", c.getDistanceFromPrevious());
                values.put("aid", c.getActivity().getId());
                values.put("cispause", isPause);
                ret = (int) db.insert("coordinates", null, values);
                if (ret >= 0) {
                    c.setId(ret);
                    ret = 0;
                } else {
                    ret = -1;
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in insertCoordinate(): " + e.getMessage());
                ret = -1;
            } finally {
                try {
                    //db.close();
                } catch (Exception e) {
                }
            }
        }
        if (ret == 0) {
            Log.d(TAG, "insertCoordinate() was successful");
        }
        return ret;
    }

    /**
     * Methods that update something in the database
     */

    /**
     * Updates a track in the database
     *
     * @param t
     * @return 0 if it was successful, otherwise -1
     */
    public int updateTrack(Track t) {
        int ret = 0;
        if (t == null) {
            ret = -1;
        } else {
            t.validate();
            if (t.getError() != null) {
                ret = -1;
            } else {
                SQLiteDatabase db = null;

                try {
                    db = getWritableDatabase();
                    ContentValues values = new ContentValues(2);
                    values.put("tname", t.getName());
                    if (db.update("tracks", values, "tid = ?",
                            new String[]{String.valueOf(t.getId())}) == 0)
                        ret = -1;
                } catch (SQLiteException e) {
                    Log.d(TAG, "Error in updateTrack(): " + e.getMessage());
                    ret = -1;
                } finally {
                    try {
                        db.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        if (ret == 0) {
            Log.d(TAG, "updateTrack() erfolgreich");
        }
        return ret;
    }


    /**
     * Methods that delete something from the database
     */

    /**
     * Delets a track from the database
     *
     * @param t
     * @return 0 if it was successful, otherwise -1
     */
    public int deleteTrack(Track t) {
        int ret = 0;
        if (t == null) {
            ret = -1;
        } else {
            SQLiteDatabase db = null;
            try {
                db = getWritableDatabase();
                if (db.delete("tracks", "tid = ?",
                        new String[]{String.valueOf(t.getId())}) != 1)
                    ret = -1;
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in deleteTrack(): " + e.getMessage());
                ret = -1;
            } finally {
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
        if (ret == 0) {
            Log.d(TAG, "deleteTrack() was successful");
        }
        return ret;
    }

    /**
     * Delets an activity from the database
     *
     * @param a
     * @return 0 if it was successful, otherwise -1
     */
    public int deleteActivity(Activity a) {
        int ret = 0;
        if (a == null) {
            ret = -1;
        } else {
            SQLiteDatabase db = null;
            try {
                db = getWritableDatabase();
                if (db.delete("activities", "aid = ?",
                        new String[]{String.valueOf(a.getId())}) != 1)
                    ret = -1;
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in deleteActivity(): " + e.getMessage());
                ret = -1;
            } finally {
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
        if (ret == 0) {
            Log.d(TAG, "deleteActivity() was successful");
        }
        return ret;
    }

    /**
     * Methods that set the Ranking in Activities
     */

    /**
     * Sets the ranking (best, avg, worst) in the Activities of a Track. This method is called
     * for every group of Activities of a Type.
     * This means that every Activity has the same Type (and Track).
     * If there is only one Activity of a specific Type then its the best Activity
     *
     * @param activities
     */
    public void setRankingForActivitiesInTrack(ArrayList<Activity> activities) {
        // Don't change the order of these methods otherwise it won't work,
        // because the methods setWorstActivitxy() and setBestActivity() sometimes have to
        // overwrite the Ranking of Activities marked by setAvgInActivity() as average Activity
        // If there is only one Activity in a group, setBestActivity() overwrites the ranking marked
        // by setAvgInActivity() and setWorstActivitxy()
        setAvgInActivity(activities);
        setWorstActivity(activities);
        setBestActivity(activities);
    }

    /**
     * Sets the best Activities in the group. The best Activities have the lowest duration
     *
     * @param activities
     * @return
     */
    private void setBestActivity(ArrayList<Activity> activities) {
        if (activities != null) {
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = getWritableDatabase();
                c = db.rawQuery("SELECT * " + "  FROM activities "
                                + "  WHERE tid = ? AND atype = ? AND aduration = (SELECT MIN" +
                                "(aduration) FROM activities WHERE tid = ? AND atype = ?);",
                        new String[]{String.valueOf(activities.get(0).getTrack().getId()),
                                activities.get(0).getType().toString(), String.valueOf(activities
                                .get(0).getTrack().getId()), activities.get(0).getType().toString
                                ()});
                ArrayList<Activity> best = new ArrayList<>();
                while (c.moveToNext()) {
                    best.add(new Activity(c.getInt(0), ActivityTypes.Type.valueOf(c.getString(1))
                            , c.getLong(2), c.getInt(3), activities.get(0).getTrack()));
                }
                for (int i = 0; i < activities.size(); i++) {
                    for (int j = 0; j < best.size(); j++) {
                        if (best.get(j).equals(activities.get(i))) {
                            best.get(j).setRanking(Activity.best);
                            activities.remove(activities.get(i));
                            activities.add(i, best.get(j));
                            Log.d(TAG, "setBestActivity() was successful");
                        }
                    }
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in setBestActivity(): " + e.getMessage());
            } finally {
                try {
                    c.close();
                } catch (Exception e) {
                }
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Sets the worst Activities in the group. The worst Activities have the highest duration
     *
     * @param activities
     * @activitiesurn
     */
    private void setWorstActivity(ArrayList<Activity> activities) {
        if (activities != null) {
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = getWritableDatabase();
                c = db.rawQuery("SELECT *  FROM activities "
                                + "  WHERE tid = ? AND atype = ? AND aduration = (SELECT MAX" +
                                "(aduration) FROM activities WHERE tid = ? AND atype = ?);",
                        new String[]{String.valueOf(activities.get(0).getTrack().getId()),
                                activities.get(0).getType().toString(), String.valueOf(activities
                                .get(0).getTrack().getId()), activities.get(0).getType().toString
                                ()});
                ArrayList<Activity> worst = new ArrayList<>();
                while (c.moveToNext()) {
                    worst.add(new Activity(c.getInt(0), ActivityTypes.Type.valueOf(c.getString(1)
                    ), c.getLong(2), c.getInt(3), activities.get(0).getTrack()));
                }
                for (int i = 0; i < activities.size(); i++) {
                    for (int j = 0; j < worst.size(); j++) {
                        if (worst.get(j).equals(activities.get(i))) {
                            worst.get(j).setRanking(Activity.worst);
                            activities.remove(activities.get(i));
                            activities.add(i, worst.get(j));
                            Log.d(TAG, "setWorstActivity() was successful");
                        }
                    }
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in setWorstActivity(): " + e.getMessage());
            } finally {
                try {
                    c.close();
                } catch (Exception e) {
                }
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Sets the most average Activities in the group. The most average Activities are the
     * Activities whose duration is closest to the average of all Activities in the group
     *
     * @param activities
     * @activitiesurn
     */
    private void setAvgInActivity(ArrayList<Activity> activities) {
        if (activities != null) {
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = getWritableDatabase();
                c = db.rawQuery("SELECT * FROM activities\n" +
                                "WHERE ABS((SELECT AVG(aduration) FROM activities WHERE tid = ? " +
                                "AND atype = ?) - aduration) = (SELECT ABS((SELECT AVG(aduration)" +
                                " FROM activities WHERE tid = ? AND atype = ?) - aduration) AS " +
                                "avg FROM activities WHERE tid = ? AND atype = ? ORDER BY avg " +
                                "LIMIT 1)\n" +
                                "AND tid = ? \n" +
                                "AND atype = ?;",
                        new String[]{String.valueOf(activities.get(0).getTrack().getId()),
                                activities.get(0).getType().toString(), String.valueOf(activities
                                .get(0).getTrack().getId()), activities.get(0).getType().toString
                                (), String.valueOf(activities.get(0).getTrack().getId()),
                                activities.get(0).getType().toString(), String.valueOf(activities
                                .get(0).getTrack().getId()), activities.get(0).getType().toString
                                ()});
                ArrayList<Activity> avg = new ArrayList<>();
                while (c.moveToNext()) {
                    avg.add(new Activity(c.getInt(0), ActivityTypes.Type.valueOf(c.getString(1)),
                            c.getLong(2), c.getInt(3), activities.get(0).getTrack()));
                }
                for (int i = 0; i < activities.size(); i++) {
                    for (int j = 0; j < avg.size(); j++)
                        if (avg.get(j).equals(activities.get(i))) {
                            avg.get(j).setRanking(Activity.avg);
                            activities.remove(activities.get(i));
                            activities.add(i, avg.get(j));
                            Log.d(TAG, "setAvgInActivity() was successful");
                        }
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in setAvgInActivity(): " + e.getMessage());
            } finally {
                try {
                    c.close();
                } catch (Exception e) {
                }
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
    }

}
