package plan.notes.app.com.notetaking.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import plan.notes.app.com.notetaking.activity.NoteDetailActivity;
import plan.notes.app.com.notetaking.activity.R;
import plan.notes.app.com.notetaking.activity.NotesEditActivity;
import plan.notes.app.com.notetaking.adapter.NotesAdapter;
import plan.notes.app.com.notetaking.db.NotesContract;


public class NoteList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView notelist;
    private NotesAdapter notesAdapter;
    private FloatingActionButton fab;
    public  static final int COL_NOTE_ID = 0;
    public static final int COL_NOTE_HEADER = 1;
    public static final int COL_NOTE_DETAIL = 2;
    public static final int COL_NOTE_DATE = 3;
    private static final int NOTES_LOADER = 0;
    public NoteList() {
    }

    private static final String[] FORECAST_COLUMNS = {
            NotesContract.NotesEntry.TABLE_NAME + "." + NotesContract.NotesEntry._ID,
            NotesContract.NotesEntry.HEADER_NOTES,
            NotesContract.NotesEntry.DETAIL_NOTES,
            NotesContract.NotesEntry.NOTE_DATE
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.note_items, container, false);
        notelist = (ListView) rootView.findViewById(R.id.notes_list);
        notelist.setEmptyView(rootView.findViewById(R.id.empty_list_item));
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        notesAdapter = new NotesAdapter(getActivity(), null, 0);
        notelist.setAdapter(notesAdapter);
        this.fabClick();
        this.setMultiChoice(notelist);
        this.noteItemClick();
        return rootView;
    }

    private void fabClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notes_edit = new Intent(getActivity(), NotesEditActivity.class);
                startActivity(notes_edit);
            }
        });
    }

    private void noteItemClick() {
        notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                int vint = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry._ID));
                if (cursor != null) {
                    Intent notes_view = new Intent(getActivity(), NoteDetailActivity.class);
                    notes_view.putExtra("NotesId", vint);
                    startActivity(notes_view);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(NOTES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = NotesContract.NotesEntry.NOTE_DATE + " ASC";

        Uri notesURI = NotesContract.NotesEntry.CONTENT_URI;

        return new CursorLoader(getActivity(),
                notesURI,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        notesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notesAdapter.swapCursor(null);
    }

    private void setMultiChoice(final ListView notes) {
        notelist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        notelist.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int notes = 0;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                notesAdapter.clearSelection();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                notes = 0;
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_delete:
                        notes = 0;
                        notesAdapter.deleteSelection();
                        mode.finish();
                }
                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                if (checked) {
                    notes++;
                    notesAdapter.setNewSelection(position, checked);
                } else {
                    notes--;
                    notesAdapter.removeSelection(position);
                }
                mode.setTitle(notes + " selected");

            }
        });

        notelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                notelist.setItemChecked(position, !notesAdapter.isPositionChecked(position));
                return false;
            }
        });
    }
}
