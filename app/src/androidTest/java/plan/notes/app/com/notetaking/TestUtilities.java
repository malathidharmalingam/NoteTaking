package plan.notes.app.com.notetaking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import plan.notes.app.com.notetaking.db.NotesContract;
import plan.notes.app.com.notetaking.db.NotesDbHelper;

public class TestUtilities  extends AndroidTestCase {

    static ContentValues createValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(NotesContract.NotesEntry.HEADER_NOTES, "Bangalore");
        testValues.put(NotesContract.NotesEntry.DETAIL_NOTES, "Place");
        testValues.put(NotesContract.NotesEntry.NOTE_DATE, "31-05-2016");
        return testValues;
    }


    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static long insertValues(Context context) {
        NotesDbHelper dbHelper = new NotesDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createValues();

        long locationRowId;
        locationRowId = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, testValues);
        return locationRowId;
    }

}
