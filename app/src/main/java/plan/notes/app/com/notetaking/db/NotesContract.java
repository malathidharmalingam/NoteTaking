package plan.notes.app.com.notetaking.db;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class NotesContract {

    public static final String CONTENT_AUTHORITY = "plan.notes.app.com.notetaking";

    public static final String PATH_NOTES = "notes";

    public static final class NotesEntry implements BaseColumns {

        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

        public static final String TABLE_NAME = "notes";

        public static Uri buildNotesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static final String HEADER_NOTES = "header_notes";
        public static final String DETAIL_NOTES = "detail_notes";
        public static final String NOTE_DATE = "date";
    }

}
