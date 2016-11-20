package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by sudaraka on 11/19/16.
 */
public class PersistantTransactionDAO implements TransactionDAO{

    @Override
    public void logTransaction(Context con, Date date, String accountNo, ExpenseType expenseType, double amount) {
        dbHelper dbh=new dbHelper(con);
        dbh.logTransaction(date,accountNo,expenseType,amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs(Context con) {
        dbHelper dbh=new dbHelper(con);
        return dbh.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(Context con,int limit) {
        List<Transaction> listA=getAllTransactionLogs(con);

        if (listA == null){
            return null;
        }
        if(listA.size()<=limit){
            return  listA;
        }

        return listA.subList(listA.size() - limit, listA.size());
    }
}
