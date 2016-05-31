package plan.notes.app.com.notetaking;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import plan.notes.app.com.notetaking.db.NotesContract;
import plan.notes.app.com.notetaking.db.NotesDbHelper;

public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }
    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                NotesContract.NotesEntry.CONTENT_URI,
                null,
                null
        );
    }

    public void testNotesQuery() {
        // insert our test records into the database
        NotesDbHelper dbHelper = new NotesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createValues();
        long locationRowId = TestUtilities.insertValues(mContext);
        db.close();

        // Test the basic content provider query
        Cursor weatherCursor = mContext.getContentResolver().query(
                NotesContract.NotesEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

    }
}
