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
     * The adapter for this grid view.
     */
    private WorldAdapter mAdapter;

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
        mAdapter = (WorldAdapter) getAdapter();
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
//        intent.putExtra("path", mImagePaths.get(position));
        c.startActivity(intent);
    }

    /**
     * Gets a all worlds.
     */
    private void getWorlds() {
        mWorldList.add(R.string.world1);
//        final String[] columns = new String[]{ MediaStore.Images.Media.DATA };
//        final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
//        Thread thread = new Thread(new Runnable() {
//            /**
//             * Attempts to load images.
//             */
//            @Override
//            public void run() {
//                try {
//                    loadImages();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            /**
//             * Loads images from device folders.
//             *
//             * Stores image paths. Creates and stores thumbnails.
//             */
//            private void loadImages() {
//                Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                        columns, null, null, orderBy);
//                int length = cursor.getCount();
//                for (int i = 0; i < length; i++) {
//                    cursor.moveToPosition(i);
//                    int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                    String path = cursor.getString(dataColumnIndex);
//                    mImagePaths.add(i, path);
//                    mWorldList.add();
//                    mAdapter.notifyDataSetChanged();
//                }
//                cursor.close();
//            }
//        });
//        thread.start();
//        try {
//            thread.join();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
