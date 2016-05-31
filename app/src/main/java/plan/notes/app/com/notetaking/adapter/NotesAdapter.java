package plan.notes.app.com.notetaking.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import plan.notes.app.com.notetaking.activity.R;
import plan.notes.app.com.notetaking.db.NotesContract;
import plan.notes.app.com.notetaking.fragment.NoteList;

public class NotesAdapter extends CursorAdapter {

    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    private Context mContext;
    public NotesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_note_item, parent, false);
        NotesViewHolder viewHolder = new NotesViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        NotesViewHolder viewHolder = (NotesViewHolder) view.getTag();
        String header = cursor.getString(NoteList.COL_NOTE_HEADER);
        viewHolder.getHeader().setText(header);
        String detail = cursor.getString(NoteList.COL_NOTE_DETAIL);
        viewHolder.getDetail().setText(detail);
        String date = cursor.getString(NoteList.COL_NOTE_DATE);
        viewHolder.getDate().setText(date);
    }


    public static class NotesViewHolder {
        protected TextView detailText;
        protected TextView headerText;
        protected TextView dateText;
        public NotesViewHolder(View view) {
            this.detailText = (TextView) view.findViewById(R.id.note_detail);
            this.headerText = (TextView) view.findViewById(R.id.note_header);
            this.dateText = (TextView) view.findViewById(R.id.note_date);
        }

        public TextView getHeader() {
            return headerText;
        }
        public TextView getDetail() {
            return detailText;
        }
        public TextView getDate() {
            return dateText;
        }
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    public void deleteSelection() {
        Iterator it = mSelection.entrySet().iterator();
        int count=0;
        while (it.hasNext()) {
            Map.Entry getId = (Map.Entry)it.next();
            Cursor cursor = (Cursor) this.getItem(Integer.parseInt(getId.getKey()+""));
            int id = cursor.getInt(NoteList.COL_NOTE_ID);
            deleteNotes(mContext, id);count++;
            it.remove();
        }
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }


    static long deleteNotes(Context context, int id) {
        String mSelectionClause = NotesContract.NotesEntry._ID + " = ?";
        String [] values  = {id+""};
        context.getContentResolver().delete(NotesContract.NotesEntry.CONTENT_URI, mSelectionClause, values);
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        v.setBackgroundColor(mContext.getResources().getColor(android.R.color.background_light));

        if (mSelection.get(position) != null) {
            v.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
        }
        return v;
    }
}
