package com.g_ara.gara.controller;

import com.codename1.capture.Capture;
import com.codename1.ui.*;
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

/**
 * Created by ahmedengu.
 */
public class CarsController {
    public static void refreshCars(MultiList cars) {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Car");
//            query.include("pics");
            query.whereEqualTo("user", ParseUser.getCurrent());
            java.util.List<ParseObject> results = query.find();

            if (results.size() > 0) {
                ArrayList<Map<String, Object>> data = new ArrayList<>();

                for (int i = 0; i < results.size(); i++) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("Line1", results.get(i).getString("name"));
                    entry.put("Line2", results.get(i).getString("year"));
                    data.add(entry);

                }

                cars.setModel(new DefaultListModel<>(data));
            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    public static void addCarPic(Container pics) {
        String filePath = Capture.capturePhoto();
        if (filePath != null) {
            try {
                Image img = Image.createImage(filePath);
                Button button = new Button();
                button.setIcon(img.scaledWidth(50));
                pics.add(button);
                pics.revalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void addCar(TextField name, TextField year, Container pics, StateMachine stateMachine) {
        try {
            ParseObject car = ParseObject.create("Car");
            car.put("name", name.getText());
            car.put("year", year.getText());
            car.put("user", ParseUser.getCurrent());
            car.save();
            int count = pics.getComponentCount();
            if (count > 1) {
                java.util.List<ParseFile> files = new ArrayList<>();
                for (int i = 1; i < count; i++) {
                    ImageIO imgIO = ImageIO.getImageIO();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    imgIO.save(((Button) pics.getComponentAt(i)).getIcon(), out, ImageIO.FORMAT_JPEG, 1);
                    byte[] ba = out.toByteArray();
                    ParseFile file = new ParseFile(name.getText() + i + ".jpg", ba, "image/jpeg");
                    file.save();
                    files.add(file);
                }

                car.put("pics", files);
                car.save();
            }
            stateMachine.back();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void beforeCarsForm(Form f, MultiList cars) {
        refreshCars(cars);
        f.addPullToRefresh(() -> refreshCars(cars));
    }
}
