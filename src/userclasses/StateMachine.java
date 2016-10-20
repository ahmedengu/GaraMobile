/**
 * Your application code goes here<br>
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose
 * of building native mobile applications using Java.
 */


package userclasses;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.Preferences;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.util.Resources;
import com.parse4cn1.Parse;
import generated.StateMachineBase;

import java.util.HashMap;

import static com.g_ara.gara.controller.CarsController.*;
import static com.g_ara.gara.controller.ChatController.*;
import static com.g_ara.gara.controller.CountdownController.beforeCountdownForm;
import static com.g_ara.gara.controller.DriveSummary.beforeDriveSummaryForm;
import static com.g_ara.gara.controller.DriveSummary.confirmAction;
import static com.g_ara.gara.controller.GroupsController.beforeGroupsForm;
import static com.g_ara.gara.controller.GroupsController.newGroup;
import static com.g_ara.gara.controller.HomeController.*;
import static com.g_ara.gara.controller.RequestsController.beforeRequestsForm;
import static com.g_ara.gara.controller.RideMap.beforeRideMapForm;
import static com.g_ara.gara.controller.SettingsController.*;
import static com.g_ara.gara.controller.UserController.*;
import static com.g_ara.gara.controller.UserSearch.searchAction;
import static com.g_ara.gara.controller.UserSearch.usersAction;

/**
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    public static java.util.Map<String, Object> data = new HashMap<>();
    public Dialog progressDialog;

    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    protected void initVars(Resources res) {
        Parse.initialize("http://localhost:1337/parse", "myAppId", "master");
    }

    @Override
    protected String getFirstFormName() {
        if (onStart()) {
            return "Home";
        } else
            return super.getFirstFormName();
    }

    @Override
    protected void beforeHome(Form f) {
        beforeHomeForm(f, fetchResourceFile(), findDrive(f), findRide(f));
    }

    @Override
    protected void onCars_CarsAction(Component c, ActionEvent event) {
        showForm("Car", null);
    }


    @Override
    protected void onGroups_GroupsAction(Component c, ActionEvent event) {
//        showForm("Group", null);
    }

    @Override
    protected void onHome_RideAction(Component c, ActionEvent event) {
        rideAction(this);
    }


    @Override
    protected void onHome_DriveAction(Component c, ActionEvent event) {
        driveAction(this);

    }


    @Override
    protected boolean allowBackTo(String formName) {
        if (formName.indexOf("Login") != -1 || formName.indexOf("Register") != -1)
            return false;
        return super.allowBackTo(formName);
    }

    @Override
    protected void onLogin_LoginAction(Component c, ActionEvent event) {
        login(findUsername(), findPassword(), this);
    }

    @Override
    protected void onRegister_PicAction(Component c, ActionEvent event) {
        addPic(findPic());

    }

    @Override
    protected void onRegister_RegisterAction(Component c, ActionEvent event) {
        register(findUsername(), findPassword(), findName(), findEmail(), findMobile(), findPic(), this);
    }


    @Override
    protected void beforeGroups(Form f) {
        beforeGroupsForm(f, (MultiList) findGroups());
    }

    @Override
    protected void onNewGroup_NewAction(Component c, ActionEvent event) {
        newGroup(findEmail(), this);
    }


    @Override
    protected void onCar_SaveAction(Component c, ActionEvent event) {
        addCar(findName(), findYear(), ((Container) findPics()), this);
    }


    @Override
    protected void onCar_AddAction(Component c, ActionEvent event) {
        addCarPic(((Container) findPics()));
    }

    @Override
    protected void beforeCars(Form f) {
        beforeCarsForm(f, (MultiList) findCars());
    }

    @Override
    protected void beforeSettings(Form f) {
        analyticsState(Preferences.get("Analytics", true), findAnalytics());
    }

    @Override
    protected void onSettings_AnalyticsAction(Component c, ActionEvent event) {
        analyticsAciton(findAnalytics());
    }

    @Override
    protected void onSettings_WebsiteAction(Component c, ActionEvent event) {
        websiteAction();
    }

    @Override
    protected void onSettings_RateAction(Component c, ActionEvent event) {
        rateAction();
    }

    @Override
    protected void onSettings_FeedbackAction(Component c, ActionEvent event) {
        feedbackAction();
    }


    @Override
    protected void onProfile_SaveAction(Component c, ActionEvent event) {
        saveUser(findUsername(), findPassword(), findMobile(), findPic(), this);

    }


    @Override
    protected void beforeProfile(Form f) {
        beforeProffileForm(findName(), findUsername(), findPassword(), findMobile(), findPic(), findEmail());
    }


    @Override
    protected void onProfile_PicAction(Component c, ActionEvent event) {
        addPic(findPic());
    }

    @Override
    protected void onLogin_ResetAction(Component c, ActionEvent event) {
        resetPassword(findUsername());
    }

    @Override
    protected boolean onHomeLogout() {
        logout(this);
        return true;
    }


    @Override
    protected void postLogin(Form f) {
//        onStart(this);
    }


    @Override
    protected void beforeConversion(Form f) {
        beforeConversionForm(findMessages());

    }


    @Override
    protected void beforeChat(Form f) {
        beforeChatForm((MultiList) findChat(), fetchResourceFile());
    }


    @Override
    protected void onUserSearch_SearchAction(Component c, ActionEvent event) {
        searchAction(findSearchField(), findUsers(), fetchResourceFile());
    }


    @Override
    protected void onUserSearch_UsersAction(Component c, ActionEvent event) {
        usersAction(this, findUsers());

    }


    @Override
    protected void onChat_ChatAction(Component c, ActionEvent event) {
        chatAction(this, (MultiList) findChat());
    }


    @Override
    protected void onConversion_SendAction(Component c, ActionEvent event) {
        conversionSendActcion(findMessage(), findMessages());
    }

    @Override
    protected void onDriveSummary_ConfirmAction(Component c, ActionEvent event) {
        confirmAction(this);
    }


    @Override
    protected void beforeDriveSummary(Form f) {
        beforeDriveSummaryForm(findSummary(), fetchResourceFile());
    }


    @Override
    protected void beforeRideMap(Form f) {
        beforeRideMapForm(f, fetchResourceFile(), this);
    }


    @Override
    protected void beforeRequests(Form f) {
        beforeRequestsForm(f, fetchResourceFile(), this);
    }

    @Override
    protected void beforeCountdown(Form f) {
        beforeCountdownForm(findContainer(), this);
    }

    public void showDialog() {
        if (progressDialog == null)
            progressDialog = new InfiniteProgress().showInifiniteBlocking();
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog != null)
            progressDialog.dispose();
    }



    // No need for social loin .. unnecessary headache
//    @Override
//    protected void onLogin_GoogleAction(Component c, ActionEvent event) {
//
//        Login gc = GoogleConnect.getInstance();
//        gc.setClientId(Constants.GPLUS_CLIENT);
//        gc.setRedirectURI("https://www.codenameone.com/oauth2callback");
//        gc.setClientSecret(Constants.GPLUS_SECRET);
//        doLogin(gc, new GoogleData(), false);
//
//    }
//
//    class GoogleData extends ConnectionRequest {
//        private Runnable callback;
//        private Map<String, Object> parsedData;
//
//        public String getName() {
//            return (String) parsedData.get("displayName");
//        }
//
//        public String getId() {
//            return parsedData.get("id").toString();
//        }
//
//        public String getImage() {
//            Map<String, Object> imageMeta = ((Map<String, Object>) parsedData.get("image"));
//            return (String) imageMeta.get("url");
//        }
//
//        public void fetchData(String token, Runnable callback) {
//            this.callback = callback;
//            addRequestHeader("Authorization", "Bearer " + token);
//            setUrl("https://www.googleapis.com/plus/v1/people/me");
//            setPost(false);
//            NetworkManager.getInstance().addToQueue(this);
//        }
//
//        protected void handleErrorResponseCode(int code, String message) {
//            //access token not valid anymore
//            if (code >= 400 && code <= 410) {
//                doLogin(GoogleConnect.getInstance(), this, true);
//                return;
//            }
//            super.handleErrorResponseCode(code, message);
//        }
//
//        protected void readResponse(InputStream input) throws IOException {
//            JSONParser parser = new JSONParser();
//            parsedData = parser.parseJSON(new InputStreamReader(input, "UTF-8"));
//        }
//
//        protected void postResponse() {
//            callback.run();
//        }
//    }
//
//    private String fullName;
//    private String uniqueId;
//    private String imageURL;
//
//    void doLogin(Login lg, GoogleData data, boolean forceLogin) {
//        String t = Preferences.get("googletoken", (String) null);
//        if (t != null) {
//            // we check the expiration of the token which we previously stored as System time
//            long tokenExpires = Preferences.get("tokenExpires", (long) -1);
//            if (tokenExpires < 0 || tokenExpires > System.currentTimeMillis()) {
//                // we are still logged in
//                return;
//            }
//        }
//
//
//        lg.setCallback(new LoginCallback() {
//            @Override
//            public void loginFailed(String errorMessage) {
//                Dialog.show("Error Logging In", "There was an error logging in: " + errorMessage, "OK", null);
//            }
//
//            @Override
//            public void loginSuccessful() {
//                // when login is successful we fetch the full data
//                data.fetchData(lg.getAccessToken().getToken(), () -> {
//                    // we store the values of result into local variables
//                    uniqueId = data.getId();
//                    fullName = data.getName();
//                    imageURL = data.getImage();
//
//                    // we then store the data into local cached storage so they will be around when we run the app next time
//                    Preferences.set("fullName", fullName);
//                    Preferences.set("uniqueId", uniqueId);
//                    Preferences.set("imageURL", imageURL);
//                    Preferences.set("googletoken", lg.getAccessToken().getToken());
//
//                    // token expiration is in seconds from the current time, we convert it to a System.currentTimeMillis value so we can
//                    // reference it in the future to check expiration
//                    Preferences.set("tokenExpires", tokenExpirationInMillis(lg.getAccessToken()));
//                });
//            }
//        });
//        lg.doLogin();
//
//    }
//
//    long tokenExpirationInMillis(AccessToken token) {
//        String expires = token.getExpires();
//        if (expires != null && expires.length() > 0) {
//            try {
//                // when it will expire in seconds
//                long l = (long) (Float.parseFloat(expires) * 1000);
//                return System.currentTimeMillis() + l;
//            } catch (NumberFormatException err) {
//                // ignore invalid input
//            }
//        }
//        return -1;
//    }

}
