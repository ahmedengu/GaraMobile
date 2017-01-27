package com.g_ara.gara.controller;

import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.list.ListModel;
import com.codename1.ui.util.EventDispatcher;

import java.io.IOException;

/**
 * Created by ahmedengu.
 */
class ImageList implements ListModel<Image> {
    private int selection;
    private String[] imageIds;
    private EncodedImage[] images;
    private EventDispatcher listeners = new EventDispatcher();

    public ImageList(String[] images) {
        this.imageIds = images;
        this.images = new EncodedImage[images.length];
    }

    public Image getItemAt(final int index) {
        if (images[index] == null) {
            images[index] = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayHeight() / 4, Display.getInstance().getDisplayHeight() / 4, 0xffffff), false);
            URLImage.createToStorage(images[index], imageIds[index].substring(imageIds[index].lastIndexOf("/") + 1), imageIds[index]);

            Util.downloadUrlToStorageInBackground(imageIds[index], imageIds[index].substring(imageIds[index].lastIndexOf("/") + 1), new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        images[index] = EncodedImage.create(Storage.getInstance().createInputStream(imageIds[index].substring(imageIds[index].lastIndexOf("/") + 1)));
                        listeners.fireDataChangeEvent(index, DataChangedListener.CHANGED);
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                }
            });
        }
        return images[index];
    }

    public int getSize() {
        return imageIds.length;
    }

    public int getSelectedIndex() {
        return selection;
    }

    public void setSelectedIndex(int index) {
        selection = index;
    }

    public void addDataChangedListener(DataChangedListener l) {
        listeners.addListener(l);
    }

    public void removeDataChangedListener(DataChangedListener l) {
        listeners.removeListener(l);
    }

    public void addSelectionListener(SelectionListener l) {
    }

    public void removeSelectionListener(SelectionListener l) {
    }

    public void addItem(Image item) {
    }

    public void removeItem(int index) {
    }
}
