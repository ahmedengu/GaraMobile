/**
 * Your application code goes here<br>
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose
 * of building native mobile applications using Java.
 */


package userclasses;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.list.MultiList;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.parse4cn1.Parse;
import generated.StateMachineBase;

import java.util.HashMap;

import static com.g_ara.gara.controller.CarsController.*;
import static com.g_ara.gara.controller.ChatController.*;
import static com.g_ara.gara.controller.CountdownController.beforeCountdownForm;
import static com.g_ara.gara.controller.DriveSummary.*;
import static com.g_ara.gara.controller.GroupsController.*;
import static com.g_ara.gara.controller.HomeController.beforeHomeForm;
import static com.g_ara.gara.controller.HomeController.postHomeForm;
import static com.g_ara.gara.controller.RequestsController.beforeRequestsForm;
import static com.g_ara.gara.controller.RequestsController.postRequestsForm;
import static com.g_ara.gara.controller.RideMap.beforeRideMapForm;
import static com.g_ara.gara.controller.SettingsController.*;
import static com.g_ara.gara.controller.SplashController.refreshSplash;
import static com.g_ara.gara.controller.TripFeedbackController.*;
import static com.g_ara.gara.controller.UserController.*;
import static com.g_ara.gara.controller.UserSearch.*;

/**
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    public static java.util.Map<String, Object> data = new HashMap<String, Object>();
    public Dialog progressDialog;
    private static StateMachine stateMachine;

    public static StateMachine getStateMachine() {
        return stateMachine;
    }

    public static void showForm(String f) {
        if (!Display.getInstance().getCurrent().getTitle().equals(f))
            stateMachine.showForm(f, null);
    }

    public static Resources getResources() {
        return stateMachine.fetchResourceFile();
    }

    public StateMachine(String resFile) {
        super(resFile);
        stateMachine = this;
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    protected void initVars(Resources res) {
        Parse.initialize("https://gara-app.back4app.io", "GBTIGT2xXUlBHF8ctBXoyEO7nIL18jvwQNyl3gkD", "H3Z3wBo73IOZRvNegnOIvFaxbhyOLnqX3XMA855l");
    }

//    @Override
//    protected String getFirstFormName() {
//        if (onStart()) {
//            return "Home";
//        } else
//            return super.getFirstFormName();
//    }

    @Override
    protected void beforeHome(Form f) {
        beforeHomeForm(f, fetchResourceFile(), this);
    }

    @Override
    protected void onCars_CarsAction(Component c, ActionEvent event) {

    }


    @Override
    protected boolean allowBackTo(String formName) {
        return false;
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
        beforeGroupsForm(f, (MultiList) findGroups(f));
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
        beforeCarsForm(f, (MultiList) findCars(f));
    }

    @Override
    protected void beforeSettings(Form f) {
        beforeSettingsForm(f, findRate(), findFeedback(), findWebsite(), findAnalytics(), this, findReport());
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
        beforeProfileForm(findName(), findUsername(), findPassword(), findMobile(), findPic(), findEmail(), f, this, findSave());
    }

    @Override
    protected void onProfile_PicAction(Component c, ActionEvent event) {
        addPic(findPic());
    }

    @Override
    protected void onLogin_ResetAction(Component c, ActionEvent event) {
        resetPassword();
    }

    @Override
    protected void postLogin(Form f) {
//        onStart(this);
    }


    @Override
    protected void beforeConversion(Form f) {
        beforeConversionForm(findMessages(), f, this, findSend());

    }


    @Override
    protected void beforeChat(Form f) {
        beforeChatForm((MultiList) findChat(f), f);
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
        conversionSendAction(findMessage(), findMessages());
    }

    @Override
    protected void onDriveSummary_ConfirmAction(Component c, ActionEvent event) {
        confirmAction(this);
    }


    @Override
    protected void beforeDriveSummary(Form f) {
        beforeDriveSummaryForm(findSummary(), fetchResourceFile(), f, findCancel(), findConfirm());
    }


    @Override
    protected void beforeRideMap(Form f) {
        beforeRideMapForm(f, fetchResourceFile(), this, findCancel());
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


    @Override
    protected void beforeTripFeedback(Form f) {
        beforeTripFeedbackForm((Slider) findRate(f), findCancel(), findOk());

    }


    @Override
    protected void onTripFeedback_OkAction(Component c, ActionEvent event) {
        okAction(this, (Slider) findRate(), findComment());
    }


    @Override
    protected void onTripFeedback_CancelAction(Component c, ActionEvent event) {
        cancelAction();
    }

    public static void showDelayedToastBar(String msg) {
        showDelayedToastBar(msg, 15000, 1000, FontImage.MATERIAL_ERROR);
    }

    public static void showDelayedToastBar(String msg, int expire, int delay, char icon) {
        ToastBar.Status s = ToastBar.getInstance().createStatus();
        Style stl = UIManager.getInstance().getComponentStyle(s.getMessageUIID());
        s.setMessage(msg);
        s.setIcon(FontImage.createMaterial(icon, stl, 4));
        s.setExpires(expire);
        s.showDelayed(delay);
    }

//    @Override
//    protected void exitRequests(Form f) {
//        try {
//            ParseLiveQuery.close();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void beforeRegister(Form f) {
        FontImage.setMaterialIcon(findPic(), FontImage.MATERIAL_ADD_A_PHOTO);
        FontImage.setMaterialIcon((Button) findLogin(f), FontImage.MATERIAL_ARROW_BACK);
    }

    @Override
    protected void beforeCar(Form f) {
        addUserSideMenu(f);
        FontImage.setMaterialIcon(findAdd(), FontImage.MATERIAL_ADD);
        FontImage.setMaterialIcon(findSave(), FontImage.MATERIAL_SAVE);
    }

    @Override
    protected void beforeNewGroup(Form f) {
        beforeNewGroupForm(f, this, findNew());
    }


    @Override
    protected void beforeUserSearch(Form f) {
        beforeUserSearchForm(f, this, findSearch());
    }

    static Dialog dialogBlocking;

    public static void showBlocking() {
        if (dialogBlocking != null)
            dialogBlocking.dispose();
        dialogBlocking = new InfiniteProgress().showInifiniteBlocking();
        dialogBlocking.show();
    }

    public static void hideBlocking() {
        if (dialogBlocking != null)
            dialogBlocking.dispose();
    }


    @Override
    protected void postSplash(Form f) {
        refreshSplash(f, findLoadingCnt(f), this);
    }

    @Override
    protected void exitConversion(Form f) {
        onConversationExit();
    }

    @Override
    protected void postConversion(Form f) {
        postConversionForm(findMessages(f));
    }

    @Override
    protected void beforeLogin(Form f) {
        FontImage.setMaterialIcon((Button) findRegister(f), FontImage.MATERIAL_ARROW_BACK);
    }

    @Override
    protected void onSettings_ReportAction(Component c, ActionEvent event) {
        reportAction();
    }

    @Override
    protected void postGroups(Form f) {
        postGroupsForm((MultiList) findGroups(f));
    }

    @Override
    protected void postCars(Form f) {
        postCarsForm((MultiList) findCars(f));
    }

    @Override
    protected void postChat(Form f) {
        postChatForm((MultiList) findChat(f));
    }

    @Override
    protected void postRequests(Form f) {
        postRequestsForm(this);
    }

    @Override
    protected void onCreateHome() {
    }

    @Override
    protected void postHome(Form f) {
        postHomeForm(f, fetchResourceFile(), this);
    }

    @Override
    protected void postDriveSummary(Form f) {
        postDriveSummaryForm(findSummary(f), f);
    }
}
