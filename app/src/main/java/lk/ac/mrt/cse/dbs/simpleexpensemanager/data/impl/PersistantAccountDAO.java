package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by sudaraka on 11/19/16.
 */
public class PersistantAccountDAO implements AccountDAO {


    @Override
    public List<String> getAccountNumbersList(Context con) {
        dbHelper dbh=new dbHelper(con);
        return dbh.getAccountNumbersList();
    }

    @Override
    public List<Account> getAccountsList(Context con) {
        dbHelper dbh=new dbHelper(con);
        return dbh.getAccountsList();
    }

    @Override
    public Account getAccount(Context con,String accountNo) throws InvalidAccountException {
        dbHelper dbh=new dbHelper(con);
        return dbh.getAccount(accountNo);
    }

    @Override
    public void addAccount(Context con,Account account) {
        dbHelper dbh=new dbHelper(con);
        dbh.addAccount(account);
    }

    @Override
    public void removeAccount(Context con,String accountNo) throws InvalidAccountException {
        dbHelper dbh=new dbHelper(con);
        dbh.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(Context con,String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!getAccountNumbersList(con).contains(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = getAccount(con,accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }

        //accounts.put(accountNo, account);
        dbHelper dbh=new dbHelper(con);
        dbh.updateBalance(account);
    }
}