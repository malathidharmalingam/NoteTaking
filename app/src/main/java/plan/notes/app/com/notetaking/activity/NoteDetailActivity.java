package plan.notes.app.com.notetaking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import plan.notes.app.com.notetaking.fragment.NoteDetail;

public class NoteDetailActivity extends NoteBaseActivity {

    private static int position;
    private static final String NotesId = "NotesId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateFragment(savedInstanceState);
        this.getNotesId();
    }

    public void updateFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            NoteDetail NotesDetailFragment = new NoteDetail();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, NotesDetailFragment).commit();
        }
    }

    public void getNotesId() {
        Intent intent = getIntent();
        Bundle noteposition = intent.getExtras();
        if (noteposition != null) {
            position = noteposition.getInt(NotesId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            Intent notes_view = new Intent(this, NotesEditActivity.class);
            notes_view.putExtra(NotesId, position);
            startActivity(notes_view);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
