package plan.notes.app.com.notetaking;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class application extends Application {

    List<String> notes = new ArrayList<String>();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public List<String> getList() {
        return notes;
    }

    public void updateList(String header) {
        notes.add(header);
    }
}
