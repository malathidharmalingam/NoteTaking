package plan.notes.app.com.notetaking.activity;
import android.os.Bundle;
import plan.notes.app.com.notetaking.activity.R;
import plan.notes.app.com.notetaking.fragment.NoteList;


public class NotesListActivity extends NoteBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateFragment(savedInstanceState);
    }

    public void updateFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            NoteList NoteListFragment = new NoteList();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, NoteListFragment).commit();
        }
    }
}
