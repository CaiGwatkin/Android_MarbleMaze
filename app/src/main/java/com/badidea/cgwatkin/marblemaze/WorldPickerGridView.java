package com.badidea.cgwatkin.marblemaze;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * World Picker Grid View class.
 *
 * Displays a grid view of images sourced from device folders.
 */
public class WorldPickerGridView extends GridView {
    /**
     * Stores thumbnail bitmaps for display;
     */
    private final ArrayList<Integer> mWorldList = new ArrayList<>();

    /**
     * Constructor from context and attributes.
     *
     * @param context The context in which the view is created.
     * @param attributeSet Attributes of the view.
     */
    public WorldPickerGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setAdapter(new WorldAdapter(getContext(), mWorldList));
    }

    /**
     * Initialises the view.
     *
     * Adds worlds to grid view. Sets up click listener to start new activity when world clicked.
     */
    public void init() {
        getWorlds();
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startWorld(position);
            }
        });
    }

    /**
     * Opens a fullscreen image activity.
     *
     * Starts intent to display a new fullscreen image activity based on the path to the image at position.
     *
     * @param position The position of the image from the adapter.
     */
    private void startWorld(int position) {
        Context c = getContext();
        Intent intent = new Intent(c, MarbleMazeActivity.class);
        intent.putExtra("WORLD_NUMBER", mWorldList.get(position));
        c.startActivity(intent);
    }

    /**
     * Gets a all worlds.
     */
    private void getWorlds() {
        mWorldList.add(R.string.world1);
    }
}
