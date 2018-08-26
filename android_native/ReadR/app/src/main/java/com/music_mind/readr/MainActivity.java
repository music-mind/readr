package com.music_mind.readr;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

import com.music_mind.readr.db.ReviewContract;
import com.music_mind.readr.db.ReviewDbHelper;
import com.snapchat.kit.sdk.creative.api.SnapCreativeKitApi;
import com.snapchat.kit.sdk.creative.models.SnapLiveCameraContent;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ReviewDbHelper mHelper;
    private ListView mTaskListView;
    private ArrayAdapter<String> mAdapter;

    private void updateUI() {
        ArrayList<String> ReviewList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(ReviewContract.ReviewEntry.TABLE_NAME,
                new String[]{ReviewContract.ReviewEntry.COLUMN_NAME_ENTRY_ID, ReviewContract.ReviewEntry.COLUMN_NAME_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_NAME_TITLE);
            Log.d(TAG, "Review: " + cursor.getString(idx));
            ReviewList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_review,
                    R.id.review_title,
                    ReviewList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(ReviewList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new ReviewDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.review_list);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_review:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Add a new review! :)");

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView view_title = new TextView(this);
                view_title.setText("Title: ");
                layout.addView(view_title);

                final EditText title = new EditText(this);
                layout.addView(title);

                final TextView view_text = new TextView(this);
                view_text.setText("Thoughts: ");
                layout.addView(view_text);

                final EditText text = new EditText(this);
                layout.addView(text);

                final TextView view_quote = new TextView(this);
                view_quote.setText("Quote: ");
                layout.addView(view_quote);

                final EditText quote = new EditText(this);
                layout.addView(quote);

                final TextView view_rating = new TextView(this);
                view_rating.setText("Rate It!! ");
                layout.addView(view_rating);

                final RatingBar rating = new RatingBar(this);
                rating.setStepSize(1);
                rating.setNumStars(5);
                rating.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.addView(rating);
                dialog.setView(layout);
                dialog.setNeutralButton("Snap", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title_str = title.getText().toString();
                        String text_str = text.getText().toString();
                        String quote_str = quote.getText().toString();
                        int rating_val = Math.round(rating.getRating());

                    }
                });

                        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String entry_id = UUID.randomUUID().toString();
                                String title_str = title.getText().toString();
                                String text_str = text.getText().toString();
                                String quote_str = quote.getText().toString();
                                int rating_val = Math.round(rating.getRating());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(ReviewContract.ReviewEntry.COLUMN_NAME_ENTRY_ID, entry_id);
                                values.put(ReviewContract.ReviewEntry.COLUMN_NAME_TITLE, title_str);
                                values.put(ReviewContract.ReviewEntry.COLUMN_NAME_TEXT, text_str);
                                values.put(ReviewContract.ReviewEntry.COLUMN_NAME_QUOTE, quote_str);
                                values.put(ReviewContract.ReviewEntry.COLUMN_NAME_RATING, rating_val);
                                db.insertWithOnConflict(ReviewContract.ReviewEntry.TABLE_NAME,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteReview(View view) {
        View parent = (View) view.getParent();
        TextView reviewTextView = (TextView) parent.findViewById(R.id.review_title);
        String review = String.valueOf(reviewTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(ReviewContract.ReviewEntry.TABLE_NAME,
                ReviewContract.ReviewEntry.COLUMN_NAME_TITLE + " = ?",
                new String[]{review});
        db.close();
        updateUI();
    }
}
