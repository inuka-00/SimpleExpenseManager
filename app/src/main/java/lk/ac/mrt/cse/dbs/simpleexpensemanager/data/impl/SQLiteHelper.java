package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    // My database name and version
    private static final String SQL_Database_Name = "200036T.db";
    private static int VERSION = 1;

    private static String ACCOUNT_TABLE = "account_table";
    private static String ACCOUNT_NUMBER = "accountNumber";
    private static String ACCOUNT_BANK = "bankName";
    private static String ACCOUNT_HOLDER = "accountHolder";
    private static String ACCOUNT_BALANCE = "balance";

    private static String TRANSACTION_TABLE = "transaction_table";
    private static String TRANSACTION_ID = "id";
    private static String TRANSACTION_DATE = "date";
    private static String TRANSACTION_ACCOUNT_NUMBER = "accountNumber";
    private static String TRANSACTION_EXPENSE_TYPE = "expenseType";
    private static String TRANSACTION_AMOUNT= "amount";

    public SQLiteHelper(@Nullable Context context) {
        super(context, SQL_Database_Name, null, VERSION);
    }

    public static String getAccountTable() {
        return ACCOUNT_TABLE;
    }

    public static String getAccountNumber() {
        return ACCOUNT_NUMBER;
    }

    public static String getAccountBank() {
        return ACCOUNT_BANK;
    }

    public static String getAccountHolder() {
        return ACCOUNT_HOLDER;
    }

    public static String getAccountBalance() {
        return ACCOUNT_BALANCE;
    }

    public static String getTransactionTable() {
        return TRANSACTION_TABLE;
    }

    public static String getTransactionId() {
        return TRANSACTION_ID;
    }

    public static String getTransactionDate() {
        return TRANSACTION_DATE;
    }

    public static String getTransactionAccountNumber() {
        return TRANSACTION_ACCOUNT_NUMBER;
    }

    public static String getTransactionExpenseType() {
        return TRANSACTION_EXPENSE_TYPE;
    }

    public static String getTransactionAmount() {
        return TRANSACTION_AMOUNT;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String queryForCreateAccountTable =
                "CREATE TABLE " +
                        ACCOUNT_TABLE + " ( " +
                        ACCOUNT_NUMBER + " TEXT PRIMARY KEY, " +
                        ACCOUNT_BANK + " TEXT NOT NULL, " +
                        ACCOUNT_HOLDER + " TEXT NOT NULL, "+
                        ACCOUNT_BALANCE + " REAL NOT NULL);";


        String queryForCreateTransactionTable =
                "CREATE TABLE " +
                        TRANSACTION_TABLE + " ( " +
                        TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TRANSACTION_DATE + " TEXT NOT NULL, " +
                        TRANSACTION_ACCOUNT_NUMBER + " TEXT NOT NULL, " +
                        TRANSACTION_EXPENSE_TYPE + " TEXT NOT NULL, " +
                        TRANSACTION_AMOUNT + " REAL NOT NULL, " +
                        "FOREIGN KEY (" + TRANSACTION_ACCOUNT_NUMBER + ") REFERENCES " + ACCOUNT_TABLE + "(" + ACCOUNT_NUMBER + "));";

        sqLiteDatabase.execSQL(queryForCreateAccountTable);
        sqLiteDatabase.execSQL(queryForCreateTransactionTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;
        String DROP_TRANSACTION_TABLE = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;

        sqLiteDatabase.execSQL(DROP_ACCOUNT_TABLE);
        sqLiteDatabase.execSQL(DROP_TRANSACTION_TABLE);

        onCreate(sqLiteDatabase);

    }
}
