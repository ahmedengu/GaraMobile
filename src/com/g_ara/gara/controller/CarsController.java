package com.g_ara.gara.controller;

import com.codename1.capture.Capture;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.list.DefaultListModel;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.ImageIO;
import com.parse4cn1.*;
import userclasses.StateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.g_ara.gara.controller.UserController.getUserEmptyObject;
import static userclasses.StateMachine.*;

/**
 * Created by ahmedengu.
 */
public class CarsController {
    static java.util.List<String> picList = new ArrayList();

    public static void refreshCars(MultiList cars) {
        try {
            HashMap<String, Object>[] data = getCarsArr();

            cars.setModel(new DefaultListModel<>(data));
//            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    public static HashMap<String, Object>[] getCarsArr() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Car");
//            query.include("pics");
        query.whereEqualTo("user", ParseUser.getCurrent());
        query.whereEqualTo("archived", false);
        java.util.List<ParseObject> results = query.find();

//            if (results.size() > 0) {
        HashMap<String, Object>[] data = new HashMap[results.size()];

        for (int i = 0; i < results.size(); i++) {
            HashMap<String, Object> entry = new HashMap<>();
            entry.put("Line1", results.get(i).getString("name"));
            entry.put("Line2", results.get(i).getString("year"));
            java.util.List<ParseFile> pics = results.get(i).getList("pics");
            if (pics != null && pics.size() > 0) {
                EncodedImage placeholder = EncodedImage.createFromImage(Image.createImage(Display.getInstance().getDisplayHeight() / 8, Display.getInstance().getDisplayHeight() / 8, 0xffffff), false);
                String url = pics.get(0).getUrl();
                entry.put("icon", URLImage.createToStorage(placeholder, url.substring(url.lastIndexOf("/") + 1), url));
            }

            entry.put("object", (ParseObject) results.get(i));
            data[i] = entry;
        }
        return data;
    }

    public static void addCarPic(Container pics) {
        if (Dialog.show("Add Image", "Choose the image location", "Camera", "Gallery")) {
            String filePath = Capture.capturePhoto();
            if (filePath != null) {
                try {
                    Image img = Image.createImage(filePath);
                    Button button = new Button();
                    button.setIcon(img.scaledWidth(50));
                    button.addActionListener(evt -> {
                        button.remove();
                        picList.remove(filePath);
                        pics.revalidate();
                    });
                    pics.add(button);
                    picList.add(filePath);
                    pics.repaint();
                    pics.revalidate();
                    pics.getParent().revalidate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Display.getInstance().openGallery(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (ev != null && ev.getSource() != null) {
                        String filePath = (String) ev.getSource();
                        if (filePath != null) {
                            Display.getInstance().callSerially(() -> {
                                try {
                                    Image img = Image.createImage(filePath);
                                    Button button = new Button();
                                    button.setIcon(img.scaledWidth(50));
                                    button.addActionListener(evt -> {
                                        button.remove();
                                        picList.remove(filePath);
                                        pics.revalidate();
                                    });
                                    pics.add(button);
                                    picList.add(filePath);
                                    pics.repaint();
                                    pics.revalidate();
                                    pics.getParent().revalidate();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                }
            }, Display.GALLERY_IMAGE);
        }
    }

    public static void addCar(TextField name, TextField year, Container pics, StateMachine stateMachine, TextField notes) {
        if (pics.getComponentCount() == 1) {
            ToastBar.showErrorMessage("Please add pictures of the car");
            return;
        }
        try {
            ParseObject car = ParseObject.create("Car");
            car.put("name", name.getText());
            car.put("year", year.getText());
            car.put("user", getUserEmptyObject());
            car.put("notes", notes.getText());
            car.put("archived", false);

            showBlocking();
            car.save();
            int count = pics.getComponentCount();
            if (count > 1) {
                java.util.List<ParseFile> files = new ArrayList<>();
                for (int i = 1; i < count; i++) {
                    Image img = Image.createImage(picList.get(i - 1));
                    ImageIO imgIO = ImageIO.getImageIO();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    imgIO.save(img, out, ImageIO.FORMAT_JPEG, 1);
                    byte[] ba = out.toByteArray();
                    ParseFile file = new ParseFile(name.getText() + i + ".jpg", ba, "image/jpeg");
                    file.save();
                    files.add(file);
                }

                car.addAllToArrayField("pics", files);
                car.save();
            }
            hideBlocking();
            showForm("Cars");
        } catch (ParseException e) {
            e.printStackTrace();
            hideBlocking();
            ToastBar.showErrorMessage(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            hideBlocking();
        }
    }

    public static void beforeCarsForm(Form f, MultiList cars) {
        UserController.addUserSideMenu(f);
        cars.addPullToRefresh(() -> refreshCars(cars));

        FloatingActionButton floatingActionButton = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
        floatingActionButton.addActionListener(evt -> showForm("Car"));
        floatingActionButton.bindFabToContainer(f.getContentPane());
    }

    public static void postCarsForm(MultiList cars) {
        showBlocking();
        refreshCars(cars);
        hideBlocking();
    }

    public static void archiveCarOnClick(ActionEvent event, MultiList cars) {
        if (Dialog.show("Archive", "Do you really want to archive this car?", "Yes", "No")) {
            ParseObject item = (ParseObject) ((Map<String, Object>) ((MultiList) event.getSource()).getSelectedItem()).get("object");
            item.put("archived", true);
            try {
                showBlocking();
                item.save();
                hideBlocking();
            } catch (ParseException e) {
                e.printStackTrace();
                hideBlocking();
                ToastBar.showErrorMessage(e.getMessage());
            }
            refreshCars(cars);
        }
    }

}
