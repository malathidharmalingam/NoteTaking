package plan.notes.app.com.notetaking;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import plan.notes.app.com.notetaking.db.NotesContract;
import plan.notes.app.com.notetaking.db.NotesDbHelper;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(NotesDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(NotesContract.NotesEntry.TABLE_NAME);
        mContext.deleteDatabase(NotesDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new NotesDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
    }


    public void testNoteTable() {
        insertNote();
    }

    public long insertNote() {

        NotesDbHelper dbHelper = new NotesDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createValues();
        long locationRowId;
        locationRowId = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, testValues);

        assertTrue(locationRowId != -1);
        Cursor cursor = db.query(
                NotesContract.NotesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue( "Error: No Records returned from location query", cursor.moveToFirst() );

        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);
        assertFalse( "Error: More than one record returned from location query",
                cursor.moveToNext() );

        cursor.close();
        db.close();
        return locationRowId;
    }

}
