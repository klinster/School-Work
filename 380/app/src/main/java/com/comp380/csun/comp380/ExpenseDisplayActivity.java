package com.comp380.csun.comp380;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gdfairclough on 3/7/15.
 */
public class ExpenseDisplayActivity extends ActionBarActivity implements View.OnClickListener, BudgetPickerFragment.BudgetPickerListener {


    //the projection of the column names
    String[] mColumns;
    DatabaseHandler dbHandler;
    TextView noExpenses;
    Toolbar toolbar;
    public CustomAdapter adapter;
    public String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_display);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        noExpenses = (TextView)findViewById(R.id.noExpenses);
        final TextView categoryText = (TextView)findViewById(R.id.budgetEdit);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String sortType = intent.getStringExtra("key");

        dbHandler = new DatabaseHandler(this, null, null, 1);

        //set up the database cursor based on sort type
        Cursor cursor;

        //TODO should be handled in a function
        if (dbHandler.isCategory(sortType)) {

            cursor = dbHandler.getAllRowsForCategory(dbHandler.getCategoryID(sortType));

            if(!cursor.moveToFirst()){

                noExpenses.setVisibility(View.VISIBLE);
            }

            categoryText.setText(sortType);
        }

        else {
            categoryText.setText("All Categories");
            cursor = dbHandler.getAllRows();
        }


        mColumns = dbHandler.tableNames();

        //set up the adapter for the listView
        adapter = new CustomAdapter(this,cursor);

        final ListView listview = (ListView) findViewById(R.id.listViewTasks);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              TextView categoryClick =   (TextView) view.findViewById(R.id.categoryViewer);
              TextView vendorClick = (TextView) view.findViewById(R.id.vendorViewer);
              TextView costClick = (TextView) view.findViewById(R.id.costViewer);
              TextView dateClick = (TextView) view.findViewById(R.id.date_separator);
              TextView idClick = (TextView) view.findViewById(R.id.idViewer);

                String costNum = costClick.getText().toString().substring(1);

              Intent intent = new Intent(parent.getContext(), EditExpenseActivity.class);
                //place all of the strings for the current expense into the intent
                intent.putExtra("id", idClick.getText().toString());
                intent.putExtra("category", categoryClick.getText().toString());
                intent.putExtra("vendor", vendorClick.getText().toString());
                intent.putExtra("cost", costNum);

                date = convertToNumMDY(dateClick.getText().toString());

                intent.putExtra("date", date);


              //start the expense edit activity
              startActivity(intent);
                //finish the activity so the intent button takes the user back to the main screen
                finish();
            }
        });

    }

    private String convertToNumMDY(String dateString) {

        try {

            //convert Date to a new String
            DateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            Date date = df.parse(dateString);

            //convert to Date
            DateFormat convert = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

            return convert.format(date);

        } catch (ParseException pe) {

            pe.printStackTrace();

        }

        return null;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.budgetEdit:
                showBudgetPicker();
                break;
            default:
                Log.d("Button Error", "Button doesn't match any known buttons");

        }

    }

    public void showBudgetPicker(){

        BudgetPickerFragment budgetPicker = new BudgetPickerFragment();

        //start the budget picker fragment

        budgetPicker.show(getFragmentManager(), "Budget Picker");

    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String category) {

        //display categories in the list view based on the chosen category

        TextView categoryText = (TextView)findViewById(R.id.budgetEdit);
        categoryText.setText(category);
        Cursor cursor;


        //TODO should be handled in a function
        if(dbHandler.isCategory(category))
        {
            noExpenses.setVisibility(View.GONE);
            cursor = dbHandler.getAllRowsForCategory(dbHandler.getCategoryID(category));
            if(!cursor.moveToFirst()){

                noExpenses.setVisibility(View.VISIBLE);
            }
        }else{
            noExpenses.setVisibility(View.GONE);
            cursor = dbHandler.getAllRows();
        }


        //set up the adapter for the listView
        CustomAdapter adapter = new CustomAdapter(this,cursor);

        ListView listview = (ListView) findViewById(R.id.listViewTasks);
        listview.setAdapter(adapter);
    }

    /**
     * Class for extending the adapter, allowing for dynamic headers in the list view
     */
    public class CustomAdapter extends BaseAdapter{

        private Context mContext;
        private Cursor mCursor;

        //row state when needs to show separator
        private static final int SEPARATOR_STATE= 1;

        //row state when does not need to show separator
        private static final int REGULAR_STATE = 2;

        //cache row states based on position
        private int[] mRowStates;

        public CustomAdapter(Context context, Cursor cursor){
            mContext = context;
            mCursor = cursor;
            mRowStates = new int[getCount()];
        }

        @Override
        public int getCount(){
            return mCursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            boolean showSeparator = false;

            mCursor.moveToPosition(position);

            if(convertView == null) {

                LayoutInflater inflater =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.expense_item,null);
            }
            else {
                view = convertView;
            }

            //set expense category, vendor, cost and date
            TextView categoryView = (TextView) view.findViewById(R.id.categoryViewer);
            TextView vendorView = (TextView) view.findViewById(R.id.vendorViewer);
            TextView costView = (TextView) view.findViewById(R.id.costViewer);
            TextView dateView = (TextView) view.findViewById(R.id.date_separator);
            TextView idView = (TextView) view.findViewById(R.id.idViewer);

            String category = mCursor.getString(mCursor.getColumnIndex(mColumns[0]));
            String vendor = mCursor.getString(mCursor.getColumnIndex(mColumns[1]));

            String id =  mCursor.getString(mCursor.getColumnIndex("_id"));

            Double costNum = Double.parseDouble(mCursor.getString(mCursor.getColumnIndex(mColumns[2])));
            String costString = String.format("%.2f", costNum);
            String cost = "$" + costString;
            String date = mCursor.getString(mCursor.getColumnIndex(mColumns[3]));

            String convertedDate = convertDateToMDY(date);

            idView.setText(id);
            categoryView.setText(category);
            vendorView.setText(vendor);
            costView.setText(cost);
            dateView.setText(convertedDate);

            //show separator
            switch (mRowStates[position]){

                case SEPARATOR_STATE:
                    showSeparator=true;
                    break;
                case REGULAR_STATE:
                    showSeparator=false;
                    break;
                default:
                    //first run through, position will be zero
                    if(position == 0){
                        showSeparator = true;
                    }
                    else{
                        //get the previous position of the cursor
                        mCursor.moveToPosition(position-1);

                        String previousDate = mCursor.getString(mCursor.getColumnIndex(mColumns[3]));
                        if(!previousDate.equals(date)){
                            //dates are different, so show the seperator
                            showSeparator = true;
                        }

                        //reset the cursor to its correct position
                        mCursor.moveToPosition(position);


                    }

                    // Cache it with this ternary operator
                    mRowStates[position] = showSeparator ? SEPARATOR_STATE : REGULAR_STATE;

                    break;
            }


            if(showSeparator){
                //show the date of the record at the current cursor position

                dateView.setVisibility(View.VISIBLE);

            }else{
                dateView.setVisibility(View.GONE);
            }

            return view;

        }
    }

    public String convertDateToMDY(String dateString) {

        try{
            //convert to Date
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date result = df.parse(dateString);

            //convert Date to a new String
            DateFormat convert = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            dateString = convert.format(result);

            return dateString;

        }catch(ParseException pe){

            pe.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }

        // Switch to addExpenseActivity if the plus button is pushed
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddExpenseActivity.class));
            finish();
        }

        // Switch to IncomeActivity if money bag is pressed
        if (id == R.id.action_add_income) {
            Intent intent = new Intent(this, IncomeActivity.class);
            intent.putExtra("key", "All Months");
            startActivity(intent);
        }

        // Switch to GoalsActivity if the goals button is pushed
        if (id == R.id.goals) {
            startActivity(new Intent(this, GoalsActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
