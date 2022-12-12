package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistAccDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistTransDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteHelper;

public class PersistentExpenseManager extends ExpenseManager{

    public PersistentExpenseManager(Context context){
        setup(context);
    }
    @Override
    public void setup(Context context) {

        SQLiteHelper sqLiteHelper = new SQLiteHelper(context);

        AccountDAO persistAccDAO = new PersistAccDAO(sqLiteHelper);
        setAccountsDAO(persistAccDAO);

        TransactionDAO persistTransDAO = new PersistTransDAO(sqLiteHelper);
        setTransactionsDAO(persistTransDAO);

    }
}
