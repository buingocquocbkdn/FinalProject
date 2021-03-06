package cndd.finalproject;

/**
 * Created by Dark Wanderer on 08/05/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class MyDatabase {
    private static final String DATABASE_NAME = "4PICS_1WORD";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "LETTER";

    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_LETTER = "Letter";
    public static final String COLUMN_COIN = "Coin";
    public static final String COLUMN_CURRENTQUESTION = "CurrentQuestion";
    public static final String COLUMN_MAXQUESTION = "MaxQuestion";

    private static Context sContext;
    private static SQLiteDatabase sDatabase;
    private OpenHelper mOpenHelper;

    public MyDatabase(Context c){
        this.sContext = c;
    }

    public MyDatabase open() throws SQLException{
        mOpenHelper = new OpenHelper(sContext);
        sDatabase = mOpenHelper.getWritableDatabase();
        return this;
    }

    public List<Letter> getList() throws SQLException{
        List<Letter> letterList = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;

        Cursor kq = sDatabase.rawQuery(sql, null);
        if(kq.moveToFirst()){
            do{
                Letter letter = new Letter();
                letter.setId(kq.getInt(0));
                letter.setLetter(kq.getString(1));
                letter.setCoin(kq.getInt(2));
                letter.setCurrentQuestion(kq.getInt(3));
                letter.setMaxQuestion(kq.getInt(4));
                letterList.add(letter);
            }while(kq.moveToNext());
        }

        return letterList;
    }

    public List<Letter> getListByID(int id) throws SQLException{
        List<Letter> listletter = new ArrayList<Letter>();
        String sql = "SELECT * FROM "+TABLE_NAME+" where "+COLUMN_ID+"=?";

        Cursor kq = sDatabase.rawQuery(sql, new String[]{ String.valueOf(id)});
        if(kq.moveToFirst()){
            do{
                Letter letter = new Letter();
                letter.setId(kq.getInt(0));
                letter.setLetter(kq.getString(1));
                letter.setCoin(kq.getInt(2));
                letter.setCurrentQuestion(kq.getInt(3));
                letter.setMaxQuestion(kq.getInt(4));
                listletter.add(letter);
            }while(kq.moveToNext());
        }

        return listletter;
    }

    public int countRecords(){
        String count = "SELECT count(*) FROM "+TABLE_NAME;
        Cursor cur = sDatabase.rawQuery(count, null);
        cur.moveToFirst();
        return cur.getInt(0);
    }

    public long addItem(Letter obj){
        int id = obj.getId();
        String letter = obj.getLetter();
        int coin = obj.getCoin();
        int currentQuestion = obj.getCurrentQuestion();
        int maxQuestion = obj.getMaxQuestion();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_LETTER, letter);
        values.put(COLUMN_COIN, coin);
        values.put(COLUMN_CURRENTQUESTION, currentQuestion);
        values.put(COLUMN_MAXQUESTION, maxQuestion);

        return sDatabase.insert(TABLE_NAME, null, values);//(-1: insert failed, 1: insert successed)
    }

    public void close(){
        mOpenHelper.close();
    }

    public int deleteAll(){
        return sDatabase.delete(TABLE_NAME, null, null);
    }

    public int deleteItem(int id) {
        return sDatabase.delete(TABLE_NAME, COLUMN_ID + "='" + id + "'", null);
    }

    public boolean editData(int id ,String letter, int coin, int currentQuestion, int maxQuestion) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_LETTER, letter);
        values.put(COLUMN_COIN, coin);
        values.put(COLUMN_CURRENTQUESTION, currentQuestion);
        values.put(COLUMN_MAXQUESTION, maxQuestion);
        long kq = sDatabase.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return kq != 0;
    }

    public boolean updateCurrentQuestion(int id ,int currentQuestion) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, id);
        values.put(COLUMN_CURRENTQUESTION, currentQuestion);
        long kq = sDatabase.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return kq != 0;
    }



    public boolean updateCoin(int id ,int coin) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_COIN, coin);
        long kq = sDatabase.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return kq != 0;
    }

    static class OpenHelper extends SQLiteOpenHelper{
        public OpenHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+ TABLE_NAME + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_LETTER + " TEXT NOT NULL, "
                    + COLUMN_COIN + " INTEGER NOT NULL, "
                    + COLUMN_CURRENTQUESTION + " INTEGER NOT NULL, "
                    + COLUMN_MAXQUESTION + " INTEGER NOT NULL)"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            onCreate(db);
        }
    }
}


