package com.example.laundryhub.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.laundryhub.Model.User

//Beberapa ada yg gw comment dulu ya :)
//import com.example.laundryhub.Model.Transaction
//import com.example.laundryhub.Model.DetailTransaction

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

//    fun getTransaction(userId: Int): ArrayList<Transaction> {
//        val transactionList = ArrayList<Transaction>()
//
//        val db = readableDatabase
//        val query = "select receiptCode, dropDate, pickUpDate from transactions where userId = ?"
//
//        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
//        if (cursor.moveToFirst()) {
//            do {
//                val transaction = Transaction()
//                transaction.receiptCode = cursor.getInt(cursor.getColumnIndexOrThrow("receiptCode"))
//                transaction.dropDate = cursor.getString(cursor.getColumnIndexOrThrow("dropDate"))
//                transaction.pickUpDate = cursor.getString(cursor.getColumnIndexOrThrow("pickUpDate"))
//                transactionList.add(transaction)
//            } while (cursor.moveToNext())
//        }
//
//        cursor.close()
//        db.close()
//        return transactionList
//    }

//    fun getItems(receiptCode: Int): ArrayList<DetailTransaction> {
//        val itemList = ArrayList<DetailTransaction>()
//
//        val db = readableDatabase
//        val query = "select clothing, quantity from transactionItems where receiptCode = ?"
//
//        val cursor = db.rawQuery(query, arrayOf(receiptCode.toString()))
//        if(cursor.moveToFirst()) {
//            do{
//                val items = DetailTransaction()
//                items.clothing = cursor.getString(cursor.getColumnIndexOrThrow("clothing"))
//                items.quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
//                itemList.add(items)
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        db.close()
//        return itemList
//    }

    fun insertUser(user: User): Boolean {
        if (isUsernameExists(user.username)) {
            Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
            return false
        }

        val db = writableDatabase
        val cv = ContentValues()
        cv.put("username", user.username)
        cv.put("password", user.password)
        cv.put("phoneNumber", user.phoneNumber)

        val result = db.insert("users", null, cv)
        db.close()

        return if (result == -1L) {
            Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
            false
        } else {
            Toast.makeText(context, "Registration Success", Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun syncData(): MutableList<User> {
        val userList = mutableListOf<User>()
        val db = readableDatabase
        val cursor = db.rawQuery("select * from users", null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("userId"))
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username")) // Get username
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            val phoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow("phoneNumber"))

            val user = User(id, username, password, phoneNumber) // Pass username to constructor
            userList.add(user)
        }
        cursor.close()
        db.close()
        return userList
    }

    fun getUserByPhoneAndPassword(phone: Int, password: String): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE phoneNumber = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(phone.toString(), password))

        var user: User? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("userId"))
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            val pass = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            val phoneNum = cursor.getInt(cursor.getColumnIndexOrThrow("phoneNumber"))

            user = User(id, username, pass, phoneNum)
        }

        cursor.close()
        db.close()
        return user
    }



    fun isUsernameExists(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun getUserById(userId: Int): User? {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE userId = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        var user: User? = null
        if (cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            val phoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow("phoneNumber"))

            user = User(userId, username, password, phoneNumber)
        }

        cursor.close()
        db.close()
        return user
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists users")
        db?.execSQL("drop table if exists transactions")
        db?.execSQL("drop table if exists transactionItems")

        onCreate(db)
    }

}