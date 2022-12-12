package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistAccDAO implements AccountDAO {
    private final SQLiteHelper dbHelper;

    public PersistAccDAO(SQLiteHelper sqLiteHelper) {

        this.dbHelper = sqLiteHelper;
    }
    @Override
    public List<String> getAccountNumbersList() {
        List<String> numbers = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + SQLiteHelper.getAccountNumber() + " FROM " + SQLiteHelper.getAccountTable();

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getAccountNumber()));
            System.out.println(number);
            numbers.add(number);
        }

        cursor.close();
        db.close();

        return numbers;

    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SQLiteHelper.getAccountTable();

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getAccountNumber()));
            String bank = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getAccountBank()));
            String owner = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getAccountHolder()));
            double balance = cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.getAccountBalance()));

            accounts.add(new Account(number, bank, owner, balance));
        }

        cursor.close();
        db.close();

        return accounts;

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String[] parameters = {accountNo};
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + SQLiteHelper.getAccountTable() + " WHERE accountNo = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, parameters);

        if (!cursor.moveToFirst()) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        String bankName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getAccountBank()));
        String accountHolderName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.getAccountHolder()));
        double balance = cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.getAccountBalance()));

        Account account = new Account(accountNo, bankName, accountHolderName, balance);


        cursor.close();
        sqLiteDatabase.close();

        return account;
    }


    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.getAccountNumber(), account.getAccountNo());
        values.put(SQLiteHelper.getAccountBank(), account.getBankName());
        values.put(SQLiteHelper.getAccountHolder(), account.getAccountHolderName());
        values.put(SQLiteHelper.getAccountBalance(), account.getBalance());

        db.insert(SQLiteHelper.getAccountTable(), null, values);
        db.close();
    }


    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] parameters = {accountNo};
        String whereClause = SQLiteHelper.getAccountNumber() + " = ?";
        int rowsAffected = db.delete(SQLiteHelper.getAccountTable(), whereClause, parameters);

        if (rowsAffected == 0) {
            throw new InvalidAccountException("Invalid Account");
        }
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String[] parameters = {accountNo};
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT " + SQLiteHelper.getAccountBalance() + " FROM " + SQLiteHelper.getAccountTable() + " WHERE " + SQLiteHelper.getAccountNumber() + "= ?";

        Cursor cursor = db.rawQuery(query, parameters);

        if (!cursor.moveToFirst()) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        double currBalance = cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.getAccountBalance()));
        cursor.close();

        switch (expenseType) {
            case EXPENSE:
                currBalance -= amount;
                break;
            case INCOME:
                currBalance += amount;
                break;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.getAccountBalance(), currBalance);

        String whereClause = SQLiteHelper.getAccountNumber() + " = ?";
        db.update(SQLiteHelper.getAccountTable(), contentValues, whereClause, parameters);
        db.close();
    }
}
