package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by sudaraka on 11/20/16.
 */
public class dbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "expenceManager";
    public static final String ACCOUNTS_TABLE_NAME = "accounts";
    public static final String ACCOUNTS_COLUMN_ACNUM = "ac_number";
    public static final String ACCOUNTS_COLUMN_bank = "bank";
    public static final String ACCOUNTS_COLUMN_holder = "holder";
    public static final String ACCOUNTS_COLUMN_balance = "balance";

    public static final String TRANSACTION_TABLE_NAME = "transaction1";
    public static final String TRANSACTION_COLUMN_acno = "acno";
    public static final String TRANSACTION_COLUMN_date = "date";
    public static final String TRANSACTION_COLUMN_expence_type = "expence_type";
    public static final String TRANSACTION_COLUMN_amount = "amount";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE "+ ACCOUNTS_TABLE_NAME + "("
                + ACCOUNTS_COLUMN_ACNUM + " TEXT PRIMARY KEY,"
                + ACCOUNTS_COLUMN_bank + " TEXT," + ACCOUNTS_COLUMN_holder +  " TEXT," +ACCOUNTS_COLUMN_balance+" REAL"+ ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
        Log.d("ok","ok1");
        String CREATE_TRANSACCTIONS_TABLE = "CREATE TABLE "+ TRANSACTION_TABLE_NAME + "("
                +"ID INTEGER PRIMARY KEY,FOREIGN KEY(" + TRANSACTION_COLUMN_acno + ") REFERENCES "+ACCOUNTS_TABLE_NAME+"("+ACCOUNTS_COLUMN_ACNUM+"),"
                + TRANSACTION_COLUMN_date + " TEXT," + TRANSACTION_COLUMN_expence_type +  " TEXT," +TRANSACTION_COLUMN_amount+" REAL"+ ")";
        db.execSQL(CREATE_TRANSACCTIONS_TABLE);
        Log.d("ok","ok2");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE_NAME);
        onCreate(db);
    }

    public List<String> getAccountNumbersList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ACCOUNTS_COLUMN_ACNUM+" FROM " + ACCOUNTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> accounts=new ArrayList();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                accounts.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return accounts;
    }

    public List<Account> getAccountsList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ACCOUNTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Account> accounts=new ArrayList();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String no=cursor.getString(1);
                String bnk=cursor.getString(2);
                String holder=cursor.getString(3);
                Double bal=cursor.getDouble(4);
                Account ac=new Account(no,bnk,holder,bal);
                accounts.add(ac);
            } while (cursor.moveToNext());
        }
        return accounts;
    }

    public Account getAccount(String accountNo){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ACCOUNTS_TABLE_NAME+" WHERE "+ACCOUNTS_COLUMN_ACNUM+"='"+accountNo+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Account> accounts=new ArrayList();
        // looping through all rows and adding to list
        cursor.moveToFirst();
        String no=cursor.getString(1);
        String bnk=cursor.getString(2);
        String holder=cursor.getString(3);
        Double bal=cursor.getDouble(4);
        Account ac=new Account(no,bnk,holder,bal);
        return ac;
    }

    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNTS_COLUMN_ACNUM, account.getAccountNo()); // Contact Name
        values.put(ACCOUNTS_COLUMN_bank, account.getBankName()); // Contact Phone Number
        values.put(ACCOUNTS_COLUMN_holder, account.getAccountHolderName());
        values.put(ACCOUNTS_COLUMN_holder, account.getAccountHolderName());
        values.put(ACCOUNTS_COLUMN_balance,account.getBalance());

        // Inserting Row
        db.insert(ACCOUNTS_TABLE_NAME, null, values);
        db.close();
    }

    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCOUNTS_TABLE_NAME, ACCOUNTS_COLUMN_ACNUM + " = ?",
                new String[] { accountNo });
        db.close();
    }

    public void updateBalance(Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACCOUNTS_COLUMN_ACNUM, account.getAccountNo()); // Contact Name
        values.put(ACCOUNTS_COLUMN_bank, account.getBankName()); // Contact Phone Number
        values.put(ACCOUNTS_COLUMN_holder, account.getAccountHolderName());
        values.put(ACCOUNTS_COLUMN_holder, account.getAccountHolderName());
        values.put(ACCOUNTS_COLUMN_balance,account.getBalance());

        // updating row
        db.update(ACCOUNTS_TABLE_NAME, values, ACCOUNTS_COLUMN_ACNUM + " = ?",
                new String[] { account.getAccountNo() });
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TRANSACTION_COLUMN_acno, accountNo); // Contact Name
        values.put("date", (new SimpleDateFormat("yyyy-MM-dd")).format(date)); // Contact Phone Number
        values.put(TRANSACTION_COLUMN_expence_type,expenseType.toString());
        values.put(TRANSACTION_COLUMN_amount, amount);
        // Inserting Row
        db.insert(TRANSACTION_TABLE_NAME, null, values);
        db.close();
    }

    public List<Transaction> getAllTransactionLogs()  {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TRANSACTION_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Transaction> transactions=new ArrayList();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String acno=cursor.getString(1);
                Date date= null;
                try {
                    Log.d("ok","kkkkkkk");
                    date = new SimpleDateFormat("dd-MMM-yyyy HH:mm").parse(cursor.getString(2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String type=cursor.getString(3);
                Double amount=cursor.getDouble(4);
                Transaction tr=new Transaction(date,acno, ExpenseType.valueOf(type),amount);
                transactions.add(tr);
            } while (cursor.moveToNext());
        }
        return transactions;
    }

}
