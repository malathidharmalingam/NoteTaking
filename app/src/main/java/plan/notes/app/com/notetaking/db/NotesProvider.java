package plan.notes.app.com.notetaking.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class NotesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sNotesQueryBuilder;
    private NotesDbHelper mNotesHelper;
    static final int NOTES = 100;

    static{
        sNotesQueryBuilder = new SQLiteQueryBuilder();
        sNotesQueryBuilder.setTables(NotesContract.NotesEntry.TABLE_NAME);
    }

    @Override
    public boolean onCreate() {
        mNotesHelper =  new NotesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case NOTES:
                return NotesContract.NotesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case NOTES:
                retCursor = mNotesHelper.getReadableDatabase().query(
                        NotesContract.NotesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mNotesHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long _id;
        switch (match) {
            case NOTES:
                 _id = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = NotesContract.NotesEntry.buildNotesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                _id = db.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = NotesContract.NotesEntry.buildNotesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mNotesHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if ( null == selection ) selection = "1";
        switch (match) {
            case NOTES:
                rowsDeleted = db.delete(
                        NotesContract.NotesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mNotesHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case NOTES:
                rowsUpdated = db.update(NotesContract.NotesEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private Cursor getNotes(
            Uri uri, String[] projection, String sortOrder) {

        return sNotesQueryBuilder.query(mNotesHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    public void shutdown() {
        mNotesHelper.close();
        super.shutdown();
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NotesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, NotesContract.PATH_NOTES, NOTES);
        return matcher;
    }
}
