package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistTransDAO implements TransactionDAO {

    private SQLiteHelper dbHelper;

    public PersistTransDAO(SQLiteHelper dbHelper) {
        this.dbHelper = dbHelper;

    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        // create transaction object
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // put it into the value container
        values.put(SQLiteHelper.getAccountNumber(), transaction.getAccountNo());
        values.put(SQLiteHelper.getTransactionDate(), transaction.getDate().toString());
        values.put(SQLiteHelper.getTransactionExpenseType(), transaction.getExpenseType().toString());
        values.put(SQLiteHelper.getTransactionAmount(), transaction.getAmount());

        // insert data to transaction table
        db.insert(SQLiteHelper.getTransactionTable(), null, values);
        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // get all transaction data query
        String query = "SELECT * FROM " + SQLiteHelper.getTransactionTable();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            // get data from each row
            String accNumber = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getTransactionAccountNumber()));
            ExpenseType expType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.getTransactionExpenseType())));
            double amount = cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.getTransactionAmount()));
            String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getTransactionDate()));
            // add to transactions list
            transactions.add(new Transaction(new Date(date), accNumber, expType, amount));
        }

        cursor.close();
        db.close();

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // get limited transaction data query
        String query = "SELECT * FROM " + SQLiteHelper.getTransactionTable() + " DESC LIMIT "+ limit;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            // get data from each row
            String accNumber = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getTransactionAccountNumber()));
            ExpenseType expType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.getTransactionExpenseType())));
            double amount = cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.getTransactionAmount()));
            String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getTransactionDate()));
            // add to transactions list
            transactions.add(new Transaction(new Date(date), accNumber, expType, amount));
        }

        cursor.close();
        db.close();

        return transactions;
    }

}