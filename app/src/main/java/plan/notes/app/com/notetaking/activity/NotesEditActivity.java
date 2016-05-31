package plan.notes.app.com.notetaking.activity;

import android.os.Bundle;
import plan.notes.app.com.notetaking.fragment.NotesEdit;
import plan.notes.app.com.notetaking.activity.R;

public class NotesEditActivity extends NoteBaseActivity {

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
            NotesEdit NotesEditFragment = new NotesEdit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, NotesEditFragment).commit();
        }
    }
}
