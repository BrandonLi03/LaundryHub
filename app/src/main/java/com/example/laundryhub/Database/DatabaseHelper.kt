package com.example.laundryhub.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.laundryhub.Model.Transaction
import com.example.laundryhub.Model.DetailTransaction

class DatabaseHelper(var context: Context): SQLiteOpenHelper(context, "LaundryHub", null, 1) {
    fun createUser(db: SQLiteDatabase?){
        val createUserTable = "create table if not exists users(userId integer primary key autoincrement," +
                "username text, " +
                "password text, " +
                "phoneNumber integer)"
        db?.execSQL(createUserTable)
    }

    fun createTransaction(db: SQLiteDatabase?){
        val createTransactionTable = "create table if not exists transactions(receiptCode integer primary key autoincrement," +
                "userId integer, " +
                "dropDate text," +
                "pickUpDate text, " +
                "foreign key(userId) references users(userId))"
        db?.execSQL(createTransactionTable)
    }

    fun createTransactionItems(db: SQLiteDatabase?){
        val createTransactionItemsTable = "create table if not exists transactionItems(itemId integer primary key autoincrement," +
                "receiptCode integer," +
                "clothing text," +
                "quantity integer," +
                "foreign key(receiptCode) references transactions(receiptCode))"
        db?.execSQL(createTransactionItemsTable)
    }

    fun insertTransactionDummy(db: SQLiteDatabase?){
        val insertTransaction = "insert into transactions(receiptCode, userId, dropDate, pickUpDate) values(null, 1, '2025-10-10', '2025-12-10'), " +
                "(null, 1, '2025-20-10', '2025-25-10')"
        db?.execSQL(insertTransaction)
    }

    fun insertItemsDummy(db: SQLiteDatabase?){
        val insertItems = "insert into transactionItems(itemId, receiptCode, clothing, quantity) values(null, 1, 'Shirt', 2), " +
                "(null, 1, 'Pants', 4)"
        db?.execSQL(insertItems)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        createUser(db)
        createTransaction(db)
        createTransactionItems(db)

        insertTransactionDummy(db)
        insertItemsDummy(db)
    }

    fun getTransaction(userId: Int): ArrayList<Transaction> {
        val transactionList = ArrayList<Transaction>()

        val db = readableDatabase
        val query = "select receiptCode, dropDate, pickUpDate from transactions where userId = ?"

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val transaction = Transaction()
                transaction.receiptCode = cursor.getInt(cursor.getColumnIndexOrThrow("receiptCode"))
                transaction.dropDate = cursor.getString(cursor.getColumnIndexOrThrow("dropDate"))
                transaction.pickUpDate = cursor.getString(cursor.getColumnIndexOrThrow("pickUpDate"))
                transactionList.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return transactionList
    }

    fun getItems(receiptCode: Int): ArrayList<DetailTransaction> {
        val itemList = ArrayList<DetailTransaction>()

        val db = readableDatabase
        val query = "select clothing, quantity from transactionItems where receiptCode = ?"

        val cursor = db.rawQuery(query, arrayOf(receiptCode.toString()))
        if(cursor.moveToFirst()) {
            do{
                val items = DetailTransaction()
                items.clothing = cursor.getString(cursor.getColumnIndexOrThrow("clothing"))
                items.quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
                itemList.add(items)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists users")
        db?.execSQL("drop table if exists transactions")
        db?.execSQL("drop table if exists transactionItems")

        onCreate(db)
    }

}