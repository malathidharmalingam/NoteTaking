package plan.notes.app.com.notetaking.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import plan.notes.app.com.notetaking.activity.R;
import plan.notes.app.com.notetaking.db.NotesContract;


public class NoteDetail extends android.support.v4.app.Fragment implements View.OnClickListener{

    TextView header;
    TextView detail;
    public static int position;
    public static final String NotesId ="NotesId";

    public NoteDetail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_detail_view, container, false);
        header = (TextView) rootView.findViewById(R.id.note_header_view);
        detail = (TextView) rootView.findViewById(R.id.note_detail_view);
        this.getPositionId();
        return rootView;
    }

    public void getPositionId() {
        Intent intent = getActivity().getIntent();
        Bundle noteposition = intent.getExtras();
        if(noteposition != null) {
            position = noteposition.getInt(NotesId);
            getSQLiteValues();
        }
    }

    public void getSQLiteValues() {
        String [] projection = {NotesContract.NotesEntry.HEADER_NOTES,NotesContract.NotesEntry.DETAIL_NOTES,NotesContract.NotesEntry.NOTE_DATE};
        String mSelectionClause = NotesContract.NotesEntry._ID + " = ?";
        String sortOrder = NotesContract.NotesEntry.NOTE_DATE + " ASC";
        String [] values  = {position+""};
        Cursor cursor = getActivity().getContentResolver().query(NotesContract.NotesEntry.CONTENT_URI, projection, mSelectionClause, values, sortOrder);
        if(cursor != null ) {
            cursor.moveToFirst();
            String headers = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.HEADER_NOTES));
            String details = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.DETAIL_NOTES));
            header.setText(headers);
            detail.setText(details);
        }
    }

    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(header.getText().toString()) && !TextUtils.isEmpty(detail.getText().toString())) {
                insertNotes(getActivity(), header.getText().toString(), detail.getText().toString());
        }
    }

    static long insertNotes(Context context,String header,String detail) {
        ContentValues testValues = createContentValues(header, detail);
        context.getContentResolver().insert(NotesContract.NotesEntry.CONTENT_URI, testValues);
        return 0;
    }

    static ContentValues createContentValues(String header,String detail) {
        ContentValues testValues = new ContentValues();
        testValues.put(NotesContract.NotesEntry.HEADER_NOTES,header);
        testValues.put(NotesContract.NotesEntry.DETAIL_NOTES, detail);
        return testValues;
    }
}
