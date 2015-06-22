package com.comp380.csun.comp380;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gdfairclough on 2/15/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    //database details
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "expenseTracker.db";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_VENDORS = "vendors";
    private static final String TABLE_PASSWORD = "password";
    private static final String TABLE_INCOME = "income";

    //column names
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_VENDOR = "vendor";
    private static final String COLUMN_COST = "cost";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PASSWORD = "passwords";

    //category table column names
    private static final String COLUMN_CATID = "category_id";
    private static final String COLUMN_CATDESC = "category_desc";
    private static final String COLUMN_BUDGET = "budget";

    //vendors table column names
    private static final String COLUMN_VENDID = "vendor_id";
    private static final String COLUMN_VENDDESC = "vendor_desc";

    //income table column names
    private static final String COLUMN_INCID = "income_id";
    private static final String COLUMN_INCSOURCE = "income_source";
    private static final String COLUMN_INCAMOUNT = "income_amount";
    private static final String COLUMN_INCDATE = "income_date";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    //called when database is first created
    public void onCreate(SQLiteDatabase db) {



        //----------initialize database tables ---------------

        //strings to initialize database tables
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " +
                TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATEGORY
                + " Integer," + COLUMN_VENDOR + " INTEGER," + COLUMN_COST +
                " REAL," + COLUMN_DATE + " DATE DEFAULT CURRENT_DATE NOT NULL,"
                + "FOREIGN KEY(" + COLUMN_CATEGORY + ") REFERENCES " + TABLE_CATEGORIES +
                "("+ COLUMN_CATID + "), " +
                "FOREIGN KEY(" + COLUMN_VENDOR + ") REFERENCES " + TABLE_VENDORS +
                "("+ COLUMN_VENDID + ") )";

        //initialize categories table
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                TABLE_CATEGORIES + "("
                + COLUMN_CATID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATDESC
                + " TEXT UNIQUE," + COLUMN_BUDGET + " REAL DEFAULT 10)";

        //password table
        String CREATE_PASSWORD_TABLE  = "CREATE TABLE " +
                TABLE_PASSWORD + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PASSWORD
                + " VARCHAR(255)" + ")";

        //vendors table
        String CREATE_VENDORS_TABLE = "CREATE TABLE " +
                TABLE_VENDORS + "(" + COLUMN_VENDID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_VENDDESC
                + " VARCHAR(255) UNIQUE" + ")";

        //income table
        String CREATE_INCOME_TABLE = "CREATE TABLE " + TABLE_INCOME + "(" +
                COLUMN_INCID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_INCSOURCE
                + " VARCHAR(255)," + COLUMN_INCAMOUNT +  " REAL," + COLUMN_INCDATE +
                " DATE DEFAULT CURRENT_DATE NOT NULL" + ")";
        // ---------------------------------------------------

        //execute the above sql statements

        db.execSQL(CREATE_EXPENSES_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_VENDORS_TABLE);
        db.execSQL(CREATE_PASSWORD_TABLE);
        db.execSQL(CREATE_INCOME_TABLE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {

        //ensure that foreign keys are activated
        String FOREIGN_KEYS_ON = "PRAGMA foreign_keys = ON";
        db.execSQL(FOREIGN_KEYS_ON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        onCreate(db);

    }

    //add an expense to the database
    public void addExpense(Expense expense){

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, getCategoryID(expense.getCategory()));

        values.put(COLUMN_COST, expense.getCost());
        values.put(COLUMN_VENDOR, getVendorID(expense.getVendor()));
        if (expense.getDate() != null){

            values.put(COLUMN_DATE, expense.getDate());
        }

        //insert data into the writable database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_EXPENSES, null,values);
        db.close();

    }
/*    public void testCategoryValues(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(5);
        values.put(COLUMN_CATDESC, "Transportation");
        db.insert(TABLE_CATEGORIES,null,values);
        values.put(COLUMN_CATDESC, "Travel");
        db.insert(TABLE_CATEGORIES,null,values);
        values.put(COLUMN_CATDESC, "Gas");
        db.insert(TABLE_CATEGORIES,null,values);
        values.put(COLUMN_CATDESC, "Food");
        db.insert(TABLE_CATEGORIES,null,values);
        values.put(COLUMN_CATDESC, "Healthcare");
        db.insert(TABLE_CATEGORIES,null,values);
        db.close();
    } */

/*    public void testExpenseValues(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //insert test expenses
        values.put(COLUMN_CATEGORY, "4");
        values.put(COLUMN_COST, "10");
        values.put(COLUMN_VENDOR, "1");
        values.put(COLUMN_DATE, "2015-03-03");
        db.insert(TABLE_EXPENSES, null,values);

        values.put(COLUMN_CATEGORY, "2");
        values.put(COLUMN_COST, "400");
        values.put(COLUMN_VENDOR, "2");
        values.put(COLUMN_DATE, "2015-03-03");
        db.insert(TABLE_EXPENSES, null,values);

        values.put(COLUMN_CATEGORY, "3");
        values.put(COLUMN_COST, "28");
        values.put(COLUMN_VENDOR, "3");
        values.put(COLUMN_DATE, "2015-03-07");
        db.insert(TABLE_EXPENSES, null,values);
        db.close();
    }*/

/*    public void testVendors(){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VENDDESC, "Five Guys");
        db.insert(TABLE_VENDORS,null,values);
        values.put(COLUMN_VENDDESC, "United");
        db.insert(TABLE_VENDORS,null,values);
        values.put(COLUMN_VENDDESC, "Shell");
        db.insert(TABLE_VENDORS,null,values);
        db.close();
    }*/

    public void addIncome(Income income) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_INCSOURCE, income.getSource());
        values.put(COLUMN_INCAMOUNT, income.getAmount());
        if (income.getDate() != null){

            values.put(COLUMN_INCDATE, income.getDate());
        }

        //insert data into the writable database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_INCOME, null, values);
        db.close();
    }

    public String[] getCategoriesStrings(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CATDESC + " FROM " + TABLE_CATEGORIES, null);
        if (cursor != null){
            //store category text to a string
            int i = 0;
            String[] categories = new String[cursor.getCount()];
            while(cursor.moveToNext()){
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATDESC));
                categories[i] = category;
                i++;
            }
            cursor.close();
            return categories;
        }
        System.out.print("Error reading categories to string");
        db.close();
        return null;

    }

    public String[] getIncomeMonths() {
        SQLiteDatabase db = this.getWritableDatabase();
        String MONTH_QUERY_SQL = "SELECT DISTINCT strftime('%m-%Y', " + COLUMN_INCDATE + ") FROM " + TABLE_INCOME
                + " ORDER BY " + COLUMN_INCDATE;
        Cursor cursor = db.rawQuery(MONTH_QUERY_SQL, null);
        if (cursor != null) {
            int i = 0;
            String[] months = new String[cursor.getCount()];
            while (cursor.moveToNext()) {
                String month = cursor.getString(0);
                months[i] = month;
                i++;
            }
            cursor.close();
            return months;
        }
        System.out.print("Error reading months to string");
        db.close();
        return null;
    }

    //lookup to see if a category exists
    public int getCategoryID(String category){
        SQLiteDatabase db = this.getReadableDatabase();

        String categoryQuery = "SELECT * FROM " + TABLE_CATEGORIES
                + " WHERE " + COLUMN_CATDESC + " = \"" + category +"\"";
        //query the categories table for the specified value
        Cursor cursor = db.rawQuery(categoryQuery,null);
        //category does not exist, so add it to the table
        if(!cursor.moveToFirst())
        {
            ContentValues catValue = new ContentValues();
            catValue.put(COLUMN_CATDESC,category);
            db.insert(TABLE_CATEGORIES, null, catValue);
        }
        //query the categories table for the specified category
        cursor = db.rawQuery(categoryQuery,null);
        //check for null cursor, indicating an error
        if(cursor.moveToFirst()){
            //return the integer specified at that location
            db.close();
            return (cursor.getInt(cursor.getColumnIndex(COLUMN_CATID)));

        }else{

            //return a -1 to indicate an error
            db.close();
            return -1;

        }


    }

    public boolean isCategory(String category){
        SQLiteDatabase db = this.getReadableDatabase();

        String categoryQuery = "SELECT * FROM " + TABLE_CATEGORIES
                + " WHERE " + COLUMN_CATDESC + " = \"" + category +"\"";
        //query the categories table for the specified value
        Cursor cursor = db.rawQuery(categoryQuery,null);
        //category does not exist, so add it to the table
        if(cursor.moveToFirst())
        {
            return true;
        }else{

            return false;
        }

    }

    public String[] getVendorStrings(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_VENDDESC+ " FROM " + TABLE_VENDORS,null);
        if (cursor != null){
            //store vendor text to a string
            int i = 0;
            String[] vendors = new String[cursor.getCount()];
            while(cursor.moveToNext()){
                String vendor = cursor.getString(cursor.getColumnIndex(COLUMN_VENDDESC));
                vendors[i] = vendor;
                i++;
            }
            db.close();
            return vendors;
        }
        Log.d("Vendor String Error","Error reading vendors to string");
        db.close();
        return null;

    }

    public String[] getIncomeStrings(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_INCSOURCE+ " FROM " + TABLE_INCOME,null);
        if (cursor != null){
            //store vendor text to a string
            int i = 0;
            String[] sources = new String[cursor.getCount()];
            while(cursor.moveToNext()){
                String vendor = cursor.getString(cursor.getColumnIndex(COLUMN_INCSOURCE));
                sources[i] = vendor;
                i++;
            }
            db.close();
            return sources;
        }
        Log.d("Source String Error","Error reading sources to string");
        db.close();
        return null;

    }

    //lookup to see if a category exists
    public int getVendorID(String vendor){
        SQLiteDatabase db = this.getReadableDatabase();

        String vendorQuery = "SELECT * FROM " + TABLE_VENDORS
                + " WHERE " + COLUMN_VENDDESC + " = \"" + vendor +"\"";
        //query the vendors table for the specified value
        Cursor cursor = db.rawQuery(vendorQuery,null);
        //vendor does not exist, so add it to the table
        if(!cursor.moveToFirst())
        {
            ContentValues vendorValue = new ContentValues();
            vendorValue.put(COLUMN_VENDDESC,vendor);
            db.insert(TABLE_VENDORS, null, vendorValue);
        }
        //query the vendors table for the specified vendor
        cursor = db.rawQuery(vendorQuery,null);
        //check for null cursor, indicating an error
        if(cursor != null){
            cursor.moveToFirst();
            //return the integer specified at that location
            db.close();
            return (cursor.getInt(cursor.getColumnIndex(COLUMN_VENDID)));

        }else{

            //return a -1 to indicate an error
            db.close();
            return -1;

        }

    }

    public void addCategory(String category, int budget){

        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_CATEGORY_SQL = "INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATDESC
                + "," + COLUMN_BUDGET + ") VALUES ('" + category + "'," + budget + ")";

        db.execSQL(CREATE_CATEGORY_SQL);

        db.close();

    }

    //Gets all the rows of the expense table on one cursor for ListView
    public Cursor getAllRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        //join categories and expenses tables
        String JOIN_TABLES_SQL = "SELECT " + COLUMN_ID +","+ COLUMN_CATDESC +","+COLUMN_VENDDESC
                +","+COLUMN_COST+","+COLUMN_DATE+ " FROM " + TABLE_EXPENSES +","+TABLE_CATEGORIES
                + "," + TABLE_VENDORS + " WHERE "+ TABLE_EXPENSES +"."+COLUMN_CATEGORY+"="
                +TABLE_CATEGORIES+"."+COLUMN_CATID +" AND " + TABLE_EXPENSES + "." + COLUMN_VENDOR
                + "=" + TABLE_VENDORS+"."+COLUMN_VENDID +" ORDER BY " + COLUMN_DATE + " DESC,"
                + COLUMN_ID +" DESC";

        Cursor cursor = db.rawQuery(JOIN_TABLES_SQL,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }

    public Cursor getAllRowsForCategory(int categoryId){

        SQLiteDatabase db = this.getReadableDatabase();

        //join categories, vendor and expense tables for a specific category

        String CATEGORY_SELECT_SQL = "SELECT " + COLUMN_ID +","+ COLUMN_CATDESC +","+COLUMN_VENDDESC
                +","+COLUMN_COST+","+COLUMN_DATE+ " FROM " + TABLE_EXPENSES +","+TABLE_CATEGORIES
                + "," + TABLE_VENDORS + " WHERE "+ TABLE_EXPENSES +"."+COLUMN_CATEGORY+"="
                +TABLE_CATEGORIES+"."+COLUMN_CATID +" AND " + TABLE_EXPENSES + "." + COLUMN_VENDOR
                + "=" + TABLE_VENDORS+"."+COLUMN_VENDID +" AND "+ COLUMN_CATID +"="+categoryId
                + " ORDER BY " + COLUMN_DATE + " DESC,"+COLUMN_ID +" DESC";

        Cursor cursor = db.rawQuery(CATEGORY_SELECT_SQL,null);


        if (cursor != null){
            cursor.moveToFirst();
            db.close();
            return cursor;
        }else{
            Log.d("Cursor Error", "Problem creating the cursor");
            db.close();
            return null;
        }

    }

    public Cursor getAllRowsForMonth(String month) {
        SQLiteDatabase db = this.getReadableDatabase();

        if (month.equals("Current Month")) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            month = "0" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "-" +
                    Integer.toString(cal.get(Calendar.YEAR));
        }

        String MONTH_SELECT_SQL = "SELECT " + COLUMN_INCID +"," + COLUMN_INCSOURCE +","
                + COLUMN_INCAMOUNT +"," +COLUMN_INCDATE + " FROM " + TABLE_INCOME +  " WHERE "
                + "strftime('%m-%Y', " + COLUMN_INCDATE + ") IN('" +month + "') ORDER BY " +COLUMN_INCDATE;

        Cursor cursor = db.rawQuery(MONTH_SELECT_SQL, null);

        if (cursor != null) {
            cursor.moveToFirst();
            db.close();
            return cursor;
        }
        else {
            Log.d("Month Cursor Error:", " Issue creating the cursor for month picker");
            db.close();
            return null;
        }
    }

    //Gets all the rows of the income table on one cursor for ListView
    public Cursor getAllIncomeRows() {
        SQLiteDatabase db = this.getReadableDatabase();

        //join categories and expenses tables
        String INCOME_SELECT_SQL = "SELECT " + COLUMN_INCID +", "+ COLUMN_INCSOURCE +", "+COLUMN_INCAMOUNT
                +", " +COLUMN_INCDATE + " FROM " + TABLE_INCOME +" ORDER BY " + COLUMN_INCDATE + " DESC,"
                + COLUMN_INCID +" DESC";

        Cursor cursor = db.rawQuery(INCOME_SELECT_SQL, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }

    // Gets the row that links from the viewlist item that was clicked.
    public void updateIncomeRow(int id, Income income) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_INCSOURCE, income.getSource());
        contentValues.put(COLUMN_INCAMOUNT, income.getAmount());
        if (income.getDate() != null){

            contentValues.put(COLUMN_INCDATE, income.getDate());
        }

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_INCOME, contentValues, COLUMN_INCID + "=" + id, null);
        db.close();
    }

    //Gets all String names of table
    public String[] tableNames() {
        String[] names = new String[] {COLUMN_CATDESC, COLUMN_VENDDESC, COLUMN_COST,
                COLUMN_DATE,COLUMN_ID};
        return names;
    }

    //Gets all String names of Income Table
    public String[] incomeColumnNames() {
        String[] columnNames = new String[] {COLUMN_INCSOURCE, COLUMN_INCAMOUNT, COLUMN_INCDATE,
                COLUMN_INCID};
        return columnNames;
    }

    //find an expense using this method which generates a SELECT statement
    public Expense findExpense(String category){
        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE "+ COLUMN_CATEGORY
                + " = \"" + category + "\" ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Expense expense  = new Expense();

        //move the cursor to the first record returned
        if (cursor.moveToFirst()){
            //why call again?
            //cursor.moveToFirst();
            expense.setID(Integer.parseInt(cursor.getString(0)));
            expense.setCategory(cursor.getString(1));
            expense.setVendor(cursor.getString(2));
            expense.setCost(Float.parseFloat(cursor.getString(3)));
            expense.setDate(cursor.getString(4));

        }else {
            expense = null;
            Log.d("Not Found","expense not found");
        }
        db.close();
        return expense;
    }

    /**
     * Get a vendor String from the vendor table based on the provided Id
     * @param vendorId
     * @return
     */
    public String vendorIdToString(int vendorId){

        SQLiteDatabase db = this.getWritableDatabase();

        String VENDOR_ID_SQL = "SELECT " + COLUMN_VENDDESC + " FROM " + TABLE_VENDORS + " WHERE "
                + COLUMN_VENDID + "=" + vendorId;

        Cursor cursor = db.rawQuery(VENDOR_ID_SQL,null);


        if(cursor.moveToFirst()){

            //return the vendor name as a string
            db.close();
            return cursor.getString(0);

        }else{


            //return null when no vendors match the provided Id
            db.close();
            return null;
        }

    }

    /**
     * Function for retrieving a category name based on a provided id
     * @param categoryId
     * @return
     */
    public String categoryToString(int categoryId){

        SQLiteDatabase db = this.getReadableDatabase();

        String CATEGORY_ID_SQL = "SELECT " + COLUMN_CATDESC + " FROM " + TABLE_CATEGORIES
                + " WHERE " + COLUMN_CATID + "=" + categoryId;

        Cursor cursor = db.rawQuery(CATEGORY_ID_SQL,null);

        if(cursor.moveToFirst()){

            //return the category name as a string
            db.close();
            return cursor.getString(0);

        }else{

            //no category matches the id passed in, so return null
            db.close();
            return null;
        }
    }


    /**
     * Method for retrieving the top categories in terms of expenditures from ALL HISTORICAL DATA
     * @param category
     * @return cursor
     */

    public Cursor vendorByCost(String category){

        int categoryId = getCategoryID(category);

        if(categoryId > 0){

            //selects vendor column (which is saved as an id) and "Total" column from expenses table
            String VENDOR_COST_SQL = "SELECT " + COLUMN_VENDOR + ",SUM(" + COLUMN_COST + ") AS Total From "
                    + TABLE_EXPENSES + " WHERE " + COLUMN_CATEGORY + " = " + categoryId
                    + " GROUP BY " + COLUMN_VENDOR + " ORDER BY " + COLUMN_COST + " DESC";

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(VENDOR_COST_SQL,null);

            if(cursor.moveToFirst()){

                return cursor;
            }else{

                //nothing in the table, so return null
                db.close();
                return null;

            }

        }else{

            //nonexistent category string was entered
            return null;
        }

    }

    /**
     * Method for retrieving the categories and the amount spent in each category
     * currently not sorted by date and will return all historical data
     * @return cursor
     */
    public Cursor categoryByCost(){


        //this select statement will give you a "category" (which is saved as an id
        // and "Total" column from the expenses table
        String COST_SORT_SQL =  "SELECT " + COLUMN_CATEGORY + ",SUM(" + COLUMN_COST + ") AS Total FROM "
                + TABLE_EXPENSES + " GROUP BY " + COLUMN_CATEGORY + " ORDER BY " + COLUMN_COST
                + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor  = db.rawQuery(COST_SORT_SQL, null);

        if (cursor.moveToFirst()){

            db.close();
            return cursor;
        }

        db.close();
        return null;

    }

    /**
     * Function for accessing the budget for a supplied category
     * @param category
     * @return
     */
    //TODO: return should be changed to a double, budgets should be stored as doubles
    public int getBudget(String category){

        int categoryId = getCategoryID(category);

        //get the budget max for a supplied category
        SQLiteDatabase db = this.getReadableDatabase();

        String BUDGET_SQL = "SELECT " + COLUMN_BUDGET + " FROM " + TABLE_CATEGORIES+ " WHERE "
                + COLUMN_CATID + "=" + categoryId;

        Cursor cursor = db.rawQuery(BUDGET_SQL, null);

        int budgetAmount;

        if(cursor.moveToFirst()){

            //assign the budget amount as an integer
            budgetAmount = Integer.parseInt(cursor.getString(0));

        }else{

            //return -1 if their is an error reading the cursor
            budgetAmount= -1;
        }
        cursor.close();
        db.close();

        return budgetAmount;


    }

    public double getTotalOfBudgets(){


        SQLiteDatabase db = this.getReadableDatabase();

        String BUDGET_TOTAL_SQL = "SELECT SUM(" + COLUMN_BUDGET + ") FROM " + TABLE_CATEGORIES;
        Cursor cursor = db.rawQuery(BUDGET_TOTAL_SQL,null);

        double budgetTotal;
        if(cursor.moveToFirst()){

            budgetTotal = Double.parseDouble(cursor.getString(0));
        }else{
            budgetTotal = 0;

        }

        cursor.close();
        db.close();

        return budgetTotal;
    }

    public double getTotalOfIncome(String sort) {
        SQLiteDatabase db = this.getReadableDatabase();
        String INCOME_TOTAL_SQL;
        if (sort.equals("All Months")) {
            INCOME_TOTAL_SQL = "SELECT SUM(" + COLUMN_INCAMOUNT + ") FROM " + TABLE_INCOME;
        }
        else if (sort.equals("Current Month")) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String month = "0" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "-" +
                    Integer.toString(cal.get(Calendar.YEAR));

            INCOME_TOTAL_SQL = "SELECT SUM(" + COLUMN_INCAMOUNT + ") FROM " + TABLE_INCOME
                    + " WHERE strftime('%m-%Y', " + COLUMN_INCDATE + ") IN('" + month +"')";
        }
        else {
            INCOME_TOTAL_SQL = "SELECT SUM(" + COLUMN_INCAMOUNT + ") FROM " + TABLE_INCOME
                    + " WHERE strftime('%m-%Y', " + COLUMN_INCDATE + ") IN('" + sort +"')";
        }
        Cursor cursor = db.rawQuery(INCOME_TOTAL_SQL, null);

        double incomeTotal;
        if (cursor.moveToFirst() && cursor.getString(0) != null) {
            incomeTotal = Double.parseDouble(cursor.getString(0));
        }
        else {
            incomeTotal = 0;
        }

        cursor.close();
        db.close();

        return incomeTotal;
    }

    public double getExpensesForMonth(Date prevDate, Date curDate){

        Calendar cal = Calendar.getInstance();
        cal.setTime(prevDate);
        int prevMonth = cal.get(Calendar.MONTH) + 1 ;
        int prevYear = cal.get(Calendar.YEAR) ;

        String stringPrevMonth ;

        //prepend a zero to months less that 10 for sorting purposes
        if (prevMonth < 10){

             stringPrevMonth = "0" + String.valueOf(prevMonth);
        }else{
            stringPrevMonth = String.valueOf(prevMonth);
        }

        cal.setTime(curDate);
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curYear = cal.get(Calendar.YEAR);

        String stringCurMonth;

        if (curMonth < 10){
            stringCurMonth = "0" + String.valueOf(curMonth);
        }
        else{
            stringCurMonth = String.valueOf(curMonth);
        }

        String prevDateWithMonth = String.valueOf(prevYear) + "-" + stringPrevMonth;
        String curDateWithMonth = String.valueOf(curYear) + "-" + stringCurMonth;

        //sql to get expenses where date is greater than prevDateWithMonth and less than curDateWithMonth
        String MONTH_EXPENSES_SQL = "SELECT SUM(" + COLUMN_COST + ") FROM " + TABLE_EXPENSES +
                " WHERE " + COLUMN_DATE + " > '" + prevDateWithMonth + "' AND " +
                COLUMN_DATE + " < '" + curDateWithMonth+ "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(MONTH_EXPENSES_SQL,null);

        double expenseTotal;
        if (cursor.moveToFirst()){

            expenseTotal = Double.parseDouble(cursor.getString(0));

        }else{

            expenseTotal = 0;
        }
        cursor.close();
        db.close();

        return expenseTotal;
    }

    //delete an expense
    public void updateExpense(Expense expense){

        int category = getCategoryID(expense.getCategory());
        int vendor = getVendorID(expense.getVendor());


        String update = "UPDATE " + TABLE_EXPENSES + " SET " + COLUMN_CATEGORY + " = "
                + category + "," + COLUMN_VENDOR + "=" + vendor + "," + COLUMN_COST + "="
                + expense.getCost() +","+ COLUMN_DATE + "='" + expense.getDate() + "' WHERE "
                + COLUMN_ID + "=" + expense.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL(update);
        }catch(android.database.SQLException e){
            Log.d("Exception",e.getLocalizedMessage());
        }
        db.close();

    }

    //delete a category
    public void deleteExpense(int expenseId){

        String delete = "DELETE FROM " + TABLE_EXPENSES+  " WHERE " + COLUMN_ID
                + " = " + expenseId + "";

        SQLiteDatabase db = this.getWritableDatabase();

        //try to delete the category, and throw an error if something goes wrong (printed to log)
        try{
            db.execSQL(delete);
        }catch(android.database.SQLException e){
            Log.d("Exception",e.getLocalizedMessage());
        }
        db.close();

    }

    // Delete row from INCOME TABLE by id
    public boolean deleteIncomeRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_INCOME, COLUMN_INCID + "=" + id, null) > 0;
    }

    //----------------------PASSWORD METHODS--------------------------------------

    //Password
    public void addPassword(String password) {
        System.out.println("Password " + password);
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, password);

        //insert into password table
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PASSWORD, null, values);
        db.close();
    }

    // Checks to find if there is a password in the DB - TABLE_PASSWORD
    public boolean hasPassword() {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_PASSWORD};
        Cursor cursor = db.query(TABLE_PASSWORD, columns, null, null, null,
                null, null);
        cursor.getCount();
        if (cursor.getCount() > 0){
            db.close();
            return true;
        }
        else{
            db.close();
            return false;
        }
    }

    public boolean checkPassword(String pw) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_PASSWORD};
        Cursor cursor = db.query(TABLE_PASSWORD, columns, null, null, null,
                null, null);
        cursor.moveToFirst();
        if (cursor.getString(1).equals(pw)) {
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }
}