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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import plan.notes.app.com.notetaking.activity.R;
import plan.notes.app.com.notetaking.db.NotesContract;
public class NotesEdit extends android.support.v4.app.Fragment {
    EditText header;
    EditText detail;
    static SimpleDateFormat  dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    static int position;
    boolean update = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        header = (EditText) rootView.findViewById(R.id.note_header);
        detail = (EditText) rootView.findViewById(R.id.note_detail);
        this.getPositionId();
        setHasOptionsMenu(true);
        return rootView;
    }

    public void getPositionId(){
        Intent intent = getActivity().getIntent();
        Bundle noteposition = intent.getExtras();
        if(noteposition != null) {
            position = noteposition.getInt(NoteDetail.NotesId);
            getSQLiteValues();
            update= true;
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                onSave();
                break;
        }
        return true;

    }

    public void onSave() {
        if (!TextUtils.isEmpty(header.getText().toString()) && !TextUtils.isEmpty(detail.getText().toString())) {
            if(update) {
                updateNotes(getActivity(), header.getText().toString(), detail.getText().toString());
            }else {
                insertNotes(getActivity(), header.getText().toString(), detail.getText().toString());
            }
            getActivity().finish();
        }else {
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.save_note),Toast.LENGTH_LONG).show();
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

    static long updateNotes(Context context,String header,String detail) {
        ContentValues testValues = createContentValues(header, detail);
        String mSelectionClause = NotesContract.NotesEntry._ID + " = ?";
        String [] values  = {position+""};
        context.getContentResolver().update(NotesContract.NotesEntry.CONTENT_URI, testValues,mSelectionClause,values);
        return 0;
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
        testValues.put(NotesContract.NotesEntry.NOTE_DATE, "Edited on : " + getEditDate());
        return testValues;
    }

    static String getEditDate() {
        Date date = new Date();
        String strDate = dateFormat.format(date);
        return strDate;
    }
}
