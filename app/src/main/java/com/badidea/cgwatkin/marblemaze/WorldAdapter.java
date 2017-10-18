package com.badidea.cgwatkin.marblemaze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * World Adapter class.
 *
 * Allows views to be dynamically created/updated from array of strings.
 */
class WorldAdapter extends BaseAdapter {

    /**
     * Inflater used to inflate new views.
     */
    private LayoutInflater layoutInflater;

    /**
     * List of world name string resources.
     */
    private ArrayList<Integer> worldList;

    /**
     * Constructs object using context and world list.
     *
     * @param context The current context of the world adapter.
     * @param worldList An array list of world names.
     */
    WorldAdapter(Context context, ArrayList<Integer> worldList) {

        this.layoutInflater = LayoutInflater.from(context);
        this.worldList = worldList;
    }

    /**
     * How many items are in the world list.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {

        return this.worldList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position The position of the string within the world list.
     * @return The string at the specified position from the world list.
     */
    @Override
    public Object getItem(int position) {

        return this.worldList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the string within the world list.
     * @return The id of the item at the specified position; same as position.
     */
    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     * Get a View that displays the text at the specified position in the world list.
     *
     * @param position    The position of the string within the world list.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.world_picker_cell, null);
            textView = (TextView) convertView.findViewById(R.id.world);
            textView.setText(this.worldList.get(position));
        }
        else {
            textView = (TextView) convertView.findViewById(R.id.world);
            textView.setText(this.worldList.get(position));
        }
        return convertView;
    }
}
