package com.passerbywhu.study.contentproviderstudy;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.passerbywhu.study.R;

/**
 * Created by wuwenchao3 on 2015/6/8.
 */
public class DictionaryProviderStudy extends Activity {
    String[] mProjection = {
            UserDictionary.Words._ID,
            UserDictionary.Words.WORD,
            UserDictionary.Words.LOCALE
    };

    String mSortOrder = mProjection[0];
    private Cursor mCursor;

    String mSelectionClause = null;
    String[] mSelectionArgs = {""};

    String[] mWordListColumns = {
            UserDictionary.Words.WORD,
            UserDictionary.Words.LOCALE
    };

    int[] mWorldListItems = {
            R.id.dictWord,
            R.id.locale
    };

    private EditText queryWord;
    private Button doQuery;

    private CursorAdapter mCursorAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary_provider_study);
        queryWord = (EditText) findViewById(R.id.queryWord);
        doQuery = (Button) findViewById(R.id.doQuery);
        mListView = (ListView) findViewById(R.id.listView);

        doQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mSearchString = queryWord.getText().toString().trim();
                if (TextUtils.isEmpty(mSearchString)) {
                    mSelectionClause = null;
                    mSelectionArgs[0] = "";
                    mSelectionArgs = null;
                } else {
                    mSelectionClause = UserDictionary.Words.WORD + "= ?";
                    mSelectionArgs[0] = mSearchString;
                }

                mCursor = getContentResolver().query(
                        UserDictionary.Words.CONTENT_URI,
                        mProjection,
                        mSelectionClause,
                        mSelectionArgs,
                        mSortOrder
                );

                mCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.dictionary_provider_list_item, mCursor,
                        mWordListColumns, mWorldListItems, 0);

                mListView.setAdapter(mCursorAdapter);
                mListView.setVisibility(View.VISIBLE);


                if (null == mCursor) {

                } else if (mCursor.getCount() < 1) {
                    //没结果
                } else {
                    while (mCursor.moveToNext()) {
                        int index = mCursor.getColumnIndex(UserDictionary.Words.WORD);
                        String newWord = mCursor.getString(index);
                    }
                }
            }
        });
    }

    public Uri insert() {
        Uri mNewUri;

        ContentValues mNewValues  = new ContentValues();
        mNewValues.put(UserDictionary.Words.APP_ID, "example.user");
        mNewValues.put(UserDictionary.Words.LOCALE, "en_US");
        mNewValues.put(UserDictionary.Words.WORD, "insert");
        mNewValues.put(UserDictionary.Words.FREQUENCY, "100");

        mNewUri = getContentResolver().insert(UserDictionary.Words.CONTENT_URI, mNewValues);
        return mNewUri;
    }

    public int update() {
        ContentValues mUpdateValues = new ContentValues();

        String mSelectionClause = UserDictionary.Words.LOCALE + "LIKE ?";
        String[] mSelectionArgs = {"en_%"};

        int mRowsUpdated = 0;
        mUpdateValues.putNull(UserDictionary.Words.LOCALE);

        mRowsUpdated = getContentResolver().update(UserDictionary.Words.CONTENT_URI, mUpdateValues, mSelectionClause, mSelectionArgs);
        return mRowsUpdated;
    }

    public int delete() {
        String mSelectionClause = UserDictionary.Words.APP_ID + " LIKE ?";
        String[] mSelectionArgs = {"user"};
        int mRowDeleted = 0;
        mRowDeleted = getContentResolver().delete(UserDictionary.Words.CONTENT_URI, mSelectionClause, mSelectionArgs);
        return mRowDeleted;
    }
}

























