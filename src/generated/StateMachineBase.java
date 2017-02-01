/**
 * This class contains generated code from the Codename One Designer, DO NOT MODIFY!
 * This class is designed for subclassing that way the code generator can overwrite it
 * anytime without erasing your changes which should exist in a subclass!
 * For details about this file and how it works please read this blog post:
 * http://codenameone.blogspot.com/2010/10/ui-builder-class-how-to-actually-use.html
*/
package generated;

import com.codename1.ui.*;
import com.codename1.ui.util.*;
import com.codename1.ui.plaf.*;
import java.util.Hashtable;
import com.codename1.ui.events.*;

public abstract class StateMachineBase extends UIBuilder {
    private Container aboutToShowThisContainer;
    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    /**
    * @deprecated use the version that accepts a resource as an argument instead
    
**/
    protected void initVars() {}

    protected void initVars(Resources res) {}

    public StateMachineBase(Resources res, String resPath, boolean loadTheme) {
        startApp(res, resPath, loadTheme);
    }

    public Container startApp(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        UIBuilder.registerCustomComponent("Slider", com.codename1.ui.Slider.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("InfiniteProgress", com.codename1.components.InfiniteProgress.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("CheckBox", com.codename1.ui.CheckBox.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        UIBuilder.registerCustomComponent("MultiList", com.codename1.ui.list.MultiList.class);
        UIBuilder.registerCustomComponent("MultiButton", com.codename1.components.MultiButton.class);
        UIBuilder.registerCustomComponent("ShareButton", com.codename1.components.ShareButton.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    if(resPath.endsWith(".res")) {
                        res = Resources.open(resPath);
                        System.out.println("Warning: you should construct the state machine without the .res extension to allow theme overlays");
                    } else {
                        res = Resources.openLayered(resPath);
                    }
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        if(res != null) {
            setResourceFilePath(resPath);
            setResourceFile(res);
            initVars(res);
            return showForm(getFirstFormName(), null);
        } else {
            Form f = (Form)createContainer(resPath, getFirstFormName());
            initVars(fetchResourceFile());
            beforeShow(f);
            f.show();
            postShow(f);
            return f;
        }
    }

    protected String getFirstFormName() {
        return "Splash";
    }

    public Container createWidget(Resources res, String resPath, boolean loadTheme) {
        initVars();
        UIBuilder.registerCustomComponent("Container", com.codename1.ui.Container.class);
        UIBuilder.registerCustomComponent("Slider", com.codename1.ui.Slider.class);
        UIBuilder.registerCustomComponent("Form", com.codename1.ui.Form.class);
        UIBuilder.registerCustomComponent("InfiniteProgress", com.codename1.components.InfiniteProgress.class);
        UIBuilder.registerCustomComponent("TextArea", com.codename1.ui.TextArea.class);
        UIBuilder.registerCustomComponent("Button", com.codename1.ui.Button.class);
        UIBuilder.registerCustomComponent("CheckBox", com.codename1.ui.CheckBox.class);
        UIBuilder.registerCustomComponent("Label", com.codename1.ui.Label.class);
        UIBuilder.registerCustomComponent("TextField", com.codename1.ui.TextField.class);
        UIBuilder.registerCustomComponent("MultiList", com.codename1.ui.list.MultiList.class);
        UIBuilder.registerCustomComponent("MultiButton", com.codename1.components.MultiButton.class);
        UIBuilder.registerCustomComponent("ShareButton", com.codename1.components.ShareButton.class);
        if(loadTheme) {
            if(res == null) {
                try {
                    res = Resources.openLayered(resPath);
                } catch(java.io.IOException err) { err.printStackTrace(); }
            }
            initTheme(res);
        }
        return createContainer(resPath, "Splash");
    }

    protected void initTheme(Resources res) {
            String[] themes = res.getThemeResourceNames();
            if(themes != null && themes.length > 0) {
                UIManager.getInstance().setThemeProps(res.getTheme(themes[0]));
            }
    }

    public StateMachineBase() {
    }

    public StateMachineBase(String resPath) {
        this(null, resPath, true);
    }

    public StateMachineBase(Resources res) {
        this(res, null, true);
    }

    public StateMachineBase(String resPath, boolean loadTheme) {
        this(null, resPath, loadTheme);
    }

    public StateMachineBase(Resources res, boolean loadTheme) {
        this(res, null, loadTheme);
    }

    public com.codename1.ui.Button findAdd(Component root) {
        return (com.codename1.ui.Button)findByName("Add", root);
    }

    public com.codename1.ui.Button findAdd() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Add", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Add", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findNew(Component root) {
        return (com.codename1.ui.Button)findByName("New", root);
    }

    public com.codename1.ui.Button findNew() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("New", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("New", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findEmail(Component root) {
        return (com.codename1.ui.TextField)findByName("Email", root);
    }

    public com.codename1.ui.TextField findEmail() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Email", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Email", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel(Component root) {
        return (com.codename1.ui.Label)findByName("Label", root);
    }

    public com.codename1.ui.Label findLabel() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer3(Component root) {
        return (com.codename1.ui.Container)findByName("Container3", root);
    }

    public com.codename1.ui.Container findContainer3() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container3", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container3", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findLoadingCnt(Component root) {
        return (com.codename1.ui.Container)findByName("LoadingCnt", root);
    }

    public com.codename1.ui.Container findLoadingCnt() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("LoadingCnt", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("LoadingCnt", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer1(Component root) {
        return (com.codename1.ui.Container)findByName("Container1", root);
    }

    public com.codename1.ui.Container findContainer1() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer2(Component root) {
        return (com.codename1.ui.Container)findByName("Container2", root);
    }

    public com.codename1.ui.Container findContainer2() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container2", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container2", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.list.MultiList findUsers(Component root) {
        return (com.codename1.ui.list.MultiList)findByName("Users", root);
    }

    public com.codename1.ui.list.MultiList findUsers() {
        com.codename1.ui.list.MultiList cmp = (com.codename1.ui.list.MultiList)findByName("Users", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.list.MultiList)findByName("Users", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findName(Component root) {
        return (com.codename1.ui.TextField)findByName("Name", root);
    }

    public com.codename1.ui.TextField findName() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Name", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Name", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findSend(Component root) {
        return (com.codename1.ui.Button)findByName("Send", root);
    }

    public com.codename1.ui.Button findSend() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Send", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Send", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findContainer(Component root) {
        return (com.codename1.ui.Container)findByName("Container", root);
    }

    public com.codename1.ui.Container findContainer() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Container", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Container", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.InfiniteProgress findInfiniteProgress(Component root) {
        return (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", root);
    }

    public com.codename1.components.InfiniteProgress findInfiniteProgress() {
        com.codename1.components.InfiniteProgress cmp = (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.InfiniteProgress)findByName("InfiniteProgress", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findSearch(Component root) {
        return (com.codename1.ui.Button)findByName("Search", root);
    }

    public com.codename1.ui.Button findSearch() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Search", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Search", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.CheckBox findFemale(Component root) {
        return (com.codename1.ui.CheckBox)findByName("female", root);
    }

    public com.codename1.ui.CheckBox findFemale() {
        com.codename1.ui.CheckBox cmp = (com.codename1.ui.CheckBox)findByName("female", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.CheckBox)findByName("female", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findReset(Component root) {
        return (com.codename1.ui.Button)findByName("Reset", root);
    }

    public com.codename1.ui.Button findReset() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Reset", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Reset", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.ShareButton findShareButton(Component root) {
        return (com.codename1.components.ShareButton)findByName("ShareButton", root);
    }

    public com.codename1.components.ShareButton findShareButton() {
        com.codename1.components.ShareButton cmp = (com.codename1.components.ShareButton)findByName("ShareButton", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.ShareButton)findByName("ShareButton", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Component findCars(Component root) {
        return (com.codename1.ui.Component)findByName("Cars", root);
    }

    public com.codename1.ui.Component findCars() {
        com.codename1.ui.Component cmp = (com.codename1.ui.Component)findByName("Cars", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Component)findByName("Cars", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findMessages(Component root) {
        return (com.codename1.ui.Container)findByName("Messages", root);
    }

    public com.codename1.ui.Container findMessages() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Messages", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Messages", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Component findRate(Component root) {
        return (com.codename1.ui.Component)findByName("Rate", root);
    }

    public com.codename1.ui.Component findRate() {
        com.codename1.ui.Component cmp = (com.codename1.ui.Component)findByName("Rate", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Component)findByName("Rate", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Component findLogin(Component root) {
        return (com.codename1.ui.Component)findByName("Login", root);
    }

    public com.codename1.ui.Component findLogin() {
        com.codename1.ui.Component cmp = (com.codename1.ui.Component)findByName("Login", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Component)findByName("Login", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findSave(Component root) {
        return (com.codename1.ui.Button)findByName("Save", root);
    }

    public com.codename1.ui.Button findSave() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Save", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Save", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findMessage(Component root) {
        return (com.codename1.ui.TextArea)findByName("Message", root);
    }

    public com.codename1.ui.TextArea findMessage() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("Message", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("Message", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findCancel(Component root) {
        return (com.codename1.ui.Button)findByName("Cancel", root);
    }

    public com.codename1.ui.Button findCancel() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Cancel", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Cancel", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findWebsite(Component root) {
        return (com.codename1.ui.Button)findByName("Website", root);
    }

    public com.codename1.ui.Button findWebsite() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Website", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Website", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findConfirm(Component root) {
        return (com.codename1.ui.Button)findByName("Confirm", root);
    }

    public com.codename1.ui.Button findConfirm() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Confirm", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Confirm", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.list.MultiList findHistoryList(Component root) {
        return (com.codename1.ui.list.MultiList)findByName("HistoryList", root);
    }

    public com.codename1.ui.list.MultiList findHistoryList() {
        com.codename1.ui.list.MultiList cmp = (com.codename1.ui.list.MultiList)findByName("HistoryList", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.list.MultiList)findByName("HistoryList", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findFeedback(Component root) {
        return (com.codename1.ui.Button)findByName("Feedback", root);
    }

    public com.codename1.ui.Button findFeedback() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Feedback", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Feedback", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Component findGroups(Component root) {
        return (com.codename1.ui.Component)findByName("Groups", root);
    }

    public com.codename1.ui.Component findGroups() {
        com.codename1.ui.Component cmp = (com.codename1.ui.Component)findByName("Groups", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Component)findByName("Groups", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Component findChat(Component root) {
        return (com.codename1.ui.Component)findByName("Chat", root);
    }

    public com.codename1.ui.Component findChat() {
        com.codename1.ui.Component cmp = (com.codename1.ui.Component)findByName("Chat", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Component)findByName("Chat", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findNotes(Component root) {
        return (com.codename1.ui.TextField)findByName("Notes", root);
    }

    public com.codename1.ui.TextField findNotes() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Notes", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Notes", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findPassword(Component root) {
        return (com.codename1.ui.TextField)findByName("Password", root);
    }

    public com.codename1.ui.TextField findPassword() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Password", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Password", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextArea findComment(Component root) {
        return (com.codename1.ui.TextArea)findByName("Comment", root);
    }

    public com.codename1.ui.TextArea findComment() {
        com.codename1.ui.TextArea cmp = (com.codename1.ui.TextArea)findByName("Comment", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextArea)findByName("Comment", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findReport(Component root) {
        return (com.codename1.ui.Button)findByName("Report", root);
    }

    public com.codename1.ui.Button findReport() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Report", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Report", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Label findLabel1(Component root) {
        return (com.codename1.ui.Label)findByName("Label1", root);
    }

    public com.codename1.ui.Label findLabel1() {
        com.codename1.ui.Label cmp = (com.codename1.ui.Label)findByName("Label1", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Label)findByName("Label1", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findPic(Component root) {
        return (com.codename1.ui.Button)findByName("Pic", root);
    }

    public com.codename1.ui.Button findPic() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Pic", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Pic", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findInfo(Component root) {
        return (com.codename1.ui.TextField)findByName("Info", root);
    }

    public com.codename1.ui.TextField findInfo() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Info", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Info", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findPics(Component root) {
        return (com.codename1.ui.Container)findByName("Pics", root);
    }

    public com.codename1.ui.Container findPics() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Pics", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Pics", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findMobile(Component root) {
        return (com.codename1.ui.TextField)findByName("Mobile", root);
    }

    public com.codename1.ui.TextField findMobile() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Mobile", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Mobile", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.components.MultiButton findAnalytics(Component root) {
        return (com.codename1.components.MultiButton)findByName("Analytics", root);
    }

    public com.codename1.components.MultiButton findAnalytics() {
        com.codename1.components.MultiButton cmp = (com.codename1.components.MultiButton)findByName("Analytics", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.components.MultiButton)findByName("Analytics", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findLogo(Component root) {
        return (com.codename1.ui.Button)findByName("Logo", root);
    }

    public com.codename1.ui.Button findLogo() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Logo", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Logo", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findPlate(Component root) {
        return (com.codename1.ui.TextField)findByName("Plate", root);
    }

    public com.codename1.ui.TextField findPlate() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Plate", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Plate", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findSearchField(Component root) {
        return (com.codename1.ui.TextField)findByName("SearchField", root);
    }

    public com.codename1.ui.TextField findSearchField() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("SearchField", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("SearchField", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findYear(Component root) {
        return (com.codename1.ui.TextField)findByName("Year", root);
    }

    public com.codename1.ui.TextField findYear() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Year", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Year", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.TextField findUsername(Component root) {
        return (com.codename1.ui.TextField)findByName("Username", root);
    }

    public com.codename1.ui.TextField findUsername() {
        com.codename1.ui.TextField cmp = (com.codename1.ui.TextField)findByName("Username", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.TextField)findByName("Username", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Component findRegister(Component root) {
        return (com.codename1.ui.Component)findByName("Register", root);
    }

    public com.codename1.ui.Component findRegister() {
        com.codename1.ui.Component cmp = (com.codename1.ui.Component)findByName("Register", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Component)findByName("Register", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Container findSummary(Component root) {
        return (com.codename1.ui.Container)findByName("Summary", root);
    }

    public com.codename1.ui.Container findSummary() {
        com.codename1.ui.Container cmp = (com.codename1.ui.Container)findByName("Summary", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Container)findByName("Summary", aboutToShowThisContainer);
        }
        return cmp;
    }

    public com.codename1.ui.Button findOk(Component root) {
        return (com.codename1.ui.Button)findByName("Ok", root);
    }

    public com.codename1.ui.Button findOk() {
        com.codename1.ui.Button cmp = (com.codename1.ui.Button)findByName("Ok", Display.getInstance().getCurrent());
        if(cmp == null && aboutToShowThisContainer != null) {
            cmp = (com.codename1.ui.Button)findByName("Ok", aboutToShowThisContainer);
        }
        return cmp;
    }

    public static final int COMMAND_LoginRegister = 2;
    public static final int COMMAND_CarSave = 10;
    public static final int COMMAND_DriveSummaryCancel = 21;
    public static final int COMMAND_ProfileSave = 3;
    public static final int COMMAND_LoginLogin = 1;
    public static final int COMMAND_RideMapCancel = 22;
    public static final int COMMAND_RegisterRegister = 4;

    protected boolean onLoginRegister() {
        return false;
    }

    protected boolean onCarSave() {
        return false;
    }

    protected boolean onDriveSummaryCancel() {
        return false;
    }

    protected boolean onProfileSave() {
        return false;
    }

    protected boolean onLoginLogin() {
        return false;
    }

    protected boolean onRideMapCancel() {
        return false;
    }

    protected boolean onRegisterRegister() {
        return false;
    }

    protected void processCommand(ActionEvent ev, Command cmd) {
        switch(cmd.getId()) {
            case COMMAND_LoginRegister:
                if(onLoginRegister()) {
                    ev.consume();
                    return;
                }
                break;

            case COMMAND_CarSave:
                if(onCarSave()) {
                    ev.consume();
                    return;
                }
                break;

            case COMMAND_DriveSummaryCancel:
                if(onDriveSummaryCancel()) {
                    ev.consume();
                    return;
                }
                break;

            case COMMAND_ProfileSave:
                if(onProfileSave()) {
                    ev.consume();
                    return;
                }
                break;

            case COMMAND_LoginLogin:
                if(onLoginLogin()) {
                    ev.consume();
                    return;
                }
                break;

            case COMMAND_RideMapCancel:
                if(onRideMapCancel()) {
                    ev.consume();
                    return;
                }
                break;

            case COMMAND_RegisterRegister:
                if(onRegisterRegister()) {
                    ev.consume();
                    return;
                }
                break;

        }
        if(ev.getComponent() != null) {
            handleComponentAction(ev.getComponent(), ev);
        }
    }

    protected void exitForm(Form f) {
        if("History".equals(f.getName())) {
            exitHistory(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Countdown".equals(f.getName())) {
            exitCountdown(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSettings".equals(f.getName())) {
            exitDriveSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("TripFeedback".equals(f.getName())) {
            exitTripFeedback(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Groups".equals(f.getName())) {
            exitGroups(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Register".equals(f.getName())) {
            exitRegister(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("newGroup".equals(f.getName())) {
            exitNewGroup(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Home".equals(f.getName())) {
            exitHome(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Settings".equals(f.getName())) {
            exitSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Cars".equals(f.getName())) {
            exitCars(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Login".equals(f.getName())) {
            exitLogin(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Profile".equals(f.getName())) {
            exitProfile(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Chat".equals(f.getName())) {
            exitChat(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Splash".equals(f.getName())) {
            exitSplash(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Requests".equals(f.getName())) {
            exitRequests(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Conversion".equals(f.getName())) {
            exitConversion(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("UserSearch".equals(f.getName())) {
            exitUserSearch(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Car".equals(f.getName())) {
            exitCar(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("RideMap".equals(f.getName())) {
            exitRideMap(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSummary".equals(f.getName())) {
            exitDriveSummary(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void exitHistory(Form f) {
    }


    protected void exitCountdown(Form f) {
    }


    protected void exitDriveSettings(Form f) {
    }


    protected void exitTripFeedback(Form f) {
    }


    protected void exitGroups(Form f) {
    }


    protected void exitRegister(Form f) {
    }


    protected void exitNewGroup(Form f) {
    }


    protected void exitHome(Form f) {
    }


    protected void exitSettings(Form f) {
    }


    protected void exitCars(Form f) {
    }


    protected void exitLogin(Form f) {
    }


    protected void exitProfile(Form f) {
    }


    protected void exitChat(Form f) {
    }


    protected void exitSplash(Form f) {
    }


    protected void exitRequests(Form f) {
    }


    protected void exitConversion(Form f) {
    }


    protected void exitUserSearch(Form f) {
    }


    protected void exitCar(Form f) {
    }


    protected void exitRideMap(Form f) {
    }


    protected void exitDriveSummary(Form f) {
    }

    protected void beforeShow(Form f) {
    aboutToShowThisContainer = f;
        if("History".equals(f.getName())) {
            beforeHistory(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Countdown".equals(f.getName())) {
            beforeCountdown(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSettings".equals(f.getName())) {
            beforeDriveSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("TripFeedback".equals(f.getName())) {
            beforeTripFeedback(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Groups".equals(f.getName())) {
            beforeGroups(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Register".equals(f.getName())) {
            beforeRegister(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("newGroup".equals(f.getName())) {
            beforeNewGroup(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Home".equals(f.getName())) {
            beforeHome(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Settings".equals(f.getName())) {
            beforeSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Cars".equals(f.getName())) {
            beforeCars(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Login".equals(f.getName())) {
            beforeLogin(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Profile".equals(f.getName())) {
            beforeProfile(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Chat".equals(f.getName())) {
            beforeChat(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Splash".equals(f.getName())) {
            beforeSplash(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Requests".equals(f.getName())) {
            beforeRequests(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Conversion".equals(f.getName())) {
            beforeConversion(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("UserSearch".equals(f.getName())) {
            beforeUserSearch(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Car".equals(f.getName())) {
            beforeCar(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("RideMap".equals(f.getName())) {
            beforeRideMap(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSummary".equals(f.getName())) {
            beforeDriveSummary(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeHistory(Form f) {
    }


    protected void beforeCountdown(Form f) {
    }


    protected void beforeDriveSettings(Form f) {
    }


    protected void beforeTripFeedback(Form f) {
    }


    protected void beforeGroups(Form f) {
    }


    protected void beforeRegister(Form f) {
    }


    protected void beforeNewGroup(Form f) {
    }


    protected void beforeHome(Form f) {
    }


    protected void beforeSettings(Form f) {
    }


    protected void beforeCars(Form f) {
    }


    protected void beforeLogin(Form f) {
    }


    protected void beforeProfile(Form f) {
    }


    protected void beforeChat(Form f) {
    }


    protected void beforeSplash(Form f) {
    }


    protected void beforeRequests(Form f) {
    }


    protected void beforeConversion(Form f) {
    }


    protected void beforeUserSearch(Form f) {
    }


    protected void beforeCar(Form f) {
    }


    protected void beforeRideMap(Form f) {
    }


    protected void beforeDriveSummary(Form f) {
    }

    protected void beforeShowContainer(Container c) {
        aboutToShowThisContainer = c;
        if("History".equals(c.getName())) {
            beforeContainerHistory(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Countdown".equals(c.getName())) {
            beforeContainerCountdown(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSettings".equals(c.getName())) {
            beforeContainerDriveSettings(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("TripFeedback".equals(c.getName())) {
            beforeContainerTripFeedback(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Groups".equals(c.getName())) {
            beforeContainerGroups(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Register".equals(c.getName())) {
            beforeContainerRegister(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("newGroup".equals(c.getName())) {
            beforeContainerNewGroup(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Home".equals(c.getName())) {
            beforeContainerHome(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Settings".equals(c.getName())) {
            beforeContainerSettings(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Cars".equals(c.getName())) {
            beforeContainerCars(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Login".equals(c.getName())) {
            beforeContainerLogin(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Profile".equals(c.getName())) {
            beforeContainerProfile(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Chat".equals(c.getName())) {
            beforeContainerChat(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Splash".equals(c.getName())) {
            beforeContainerSplash(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Requests".equals(c.getName())) {
            beforeContainerRequests(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Conversion".equals(c.getName())) {
            beforeContainerConversion(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("UserSearch".equals(c.getName())) {
            beforeContainerUserSearch(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Car".equals(c.getName())) {
            beforeContainerCar(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("RideMap".equals(c.getName())) {
            beforeContainerRideMap(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSummary".equals(c.getName())) {
            beforeContainerDriveSummary(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void beforeContainerHistory(Container c) {
    }


    protected void beforeContainerCountdown(Container c) {
    }


    protected void beforeContainerDriveSettings(Container c) {
    }


    protected void beforeContainerTripFeedback(Container c) {
    }


    protected void beforeContainerGroups(Container c) {
    }


    protected void beforeContainerRegister(Container c) {
    }


    protected void beforeContainerNewGroup(Container c) {
    }


    protected void beforeContainerHome(Container c) {
    }


    protected void beforeContainerSettings(Container c) {
    }


    protected void beforeContainerCars(Container c) {
    }


    protected void beforeContainerLogin(Container c) {
    }


    protected void beforeContainerProfile(Container c) {
    }


    protected void beforeContainerChat(Container c) {
    }


    protected void beforeContainerSplash(Container c) {
    }


    protected void beforeContainerRequests(Container c) {
    }


    protected void beforeContainerConversion(Container c) {
    }


    protected void beforeContainerUserSearch(Container c) {
    }


    protected void beforeContainerCar(Container c) {
    }


    protected void beforeContainerRideMap(Container c) {
    }


    protected void beforeContainerDriveSummary(Container c) {
    }

    protected void postShow(Form f) {
        if("History".equals(f.getName())) {
            postHistory(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Countdown".equals(f.getName())) {
            postCountdown(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSettings".equals(f.getName())) {
            postDriveSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("TripFeedback".equals(f.getName())) {
            postTripFeedback(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Groups".equals(f.getName())) {
            postGroups(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Register".equals(f.getName())) {
            postRegister(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("newGroup".equals(f.getName())) {
            postNewGroup(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Home".equals(f.getName())) {
            postHome(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Settings".equals(f.getName())) {
            postSettings(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Cars".equals(f.getName())) {
            postCars(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Login".equals(f.getName())) {
            postLogin(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Profile".equals(f.getName())) {
            postProfile(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Chat".equals(f.getName())) {
            postChat(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Splash".equals(f.getName())) {
            postSplash(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Requests".equals(f.getName())) {
            postRequests(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Conversion".equals(f.getName())) {
            postConversion(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("UserSearch".equals(f.getName())) {
            postUserSearch(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("Car".equals(f.getName())) {
            postCar(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("RideMap".equals(f.getName())) {
            postRideMap(f);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSummary".equals(f.getName())) {
            postDriveSummary(f);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postHistory(Form f) {
    }


    protected void postCountdown(Form f) {
    }


    protected void postDriveSettings(Form f) {
    }


    protected void postTripFeedback(Form f) {
    }


    protected void postGroups(Form f) {
    }


    protected void postRegister(Form f) {
    }


    protected void postNewGroup(Form f) {
    }


    protected void postHome(Form f) {
    }


    protected void postSettings(Form f) {
    }


    protected void postCars(Form f) {
    }


    protected void postLogin(Form f) {
    }


    protected void postProfile(Form f) {
    }


    protected void postChat(Form f) {
    }


    protected void postSplash(Form f) {
    }


    protected void postRequests(Form f) {
    }


    protected void postConversion(Form f) {
    }


    protected void postUserSearch(Form f) {
    }


    protected void postCar(Form f) {
    }


    protected void postRideMap(Form f) {
    }


    protected void postDriveSummary(Form f) {
    }

    protected void postShowContainer(Container c) {
        if("History".equals(c.getName())) {
            postContainerHistory(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Countdown".equals(c.getName())) {
            postContainerCountdown(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSettings".equals(c.getName())) {
            postContainerDriveSettings(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("TripFeedback".equals(c.getName())) {
            postContainerTripFeedback(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Groups".equals(c.getName())) {
            postContainerGroups(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Register".equals(c.getName())) {
            postContainerRegister(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("newGroup".equals(c.getName())) {
            postContainerNewGroup(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Home".equals(c.getName())) {
            postContainerHome(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Settings".equals(c.getName())) {
            postContainerSettings(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Cars".equals(c.getName())) {
            postContainerCars(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Login".equals(c.getName())) {
            postContainerLogin(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Profile".equals(c.getName())) {
            postContainerProfile(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Chat".equals(c.getName())) {
            postContainerChat(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Splash".equals(c.getName())) {
            postContainerSplash(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Requests".equals(c.getName())) {
            postContainerRequests(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Conversion".equals(c.getName())) {
            postContainerConversion(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("UserSearch".equals(c.getName())) {
            postContainerUserSearch(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("Car".equals(c.getName())) {
            postContainerCar(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("RideMap".equals(c.getName())) {
            postContainerRideMap(c);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSummary".equals(c.getName())) {
            postContainerDriveSummary(c);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void postContainerHistory(Container c) {
    }


    protected void postContainerCountdown(Container c) {
    }


    protected void postContainerDriveSettings(Container c) {
    }


    protected void postContainerTripFeedback(Container c) {
    }


    protected void postContainerGroups(Container c) {
    }


    protected void postContainerRegister(Container c) {
    }


    protected void postContainerNewGroup(Container c) {
    }


    protected void postContainerHome(Container c) {
    }


    protected void postContainerSettings(Container c) {
    }


    protected void postContainerCars(Container c) {
    }


    protected void postContainerLogin(Container c) {
    }


    protected void postContainerProfile(Container c) {
    }


    protected void postContainerChat(Container c) {
    }


    protected void postContainerSplash(Container c) {
    }


    protected void postContainerRequests(Container c) {
    }


    protected void postContainerConversion(Container c) {
    }


    protected void postContainerUserSearch(Container c) {
    }


    protected void postContainerCar(Container c) {
    }


    protected void postContainerRideMap(Container c) {
    }


    protected void postContainerDriveSummary(Container c) {
    }

    protected void onCreateRoot(String rootName) {
        if("History".equals(rootName)) {
            onCreateHistory();
            aboutToShowThisContainer = null;
            return;
        }

        if("Countdown".equals(rootName)) {
            onCreateCountdown();
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSettings".equals(rootName)) {
            onCreateDriveSettings();
            aboutToShowThisContainer = null;
            return;
        }

        if("TripFeedback".equals(rootName)) {
            onCreateTripFeedback();
            aboutToShowThisContainer = null;
            return;
        }

        if("Groups".equals(rootName)) {
            onCreateGroups();
            aboutToShowThisContainer = null;
            return;
        }

        if("Register".equals(rootName)) {
            onCreateRegister();
            aboutToShowThisContainer = null;
            return;
        }

        if("newGroup".equals(rootName)) {
            onCreateNewGroup();
            aboutToShowThisContainer = null;
            return;
        }

        if("Home".equals(rootName)) {
            onCreateHome();
            aboutToShowThisContainer = null;
            return;
        }

        if("Settings".equals(rootName)) {
            onCreateSettings();
            aboutToShowThisContainer = null;
            return;
        }

        if("Cars".equals(rootName)) {
            onCreateCars();
            aboutToShowThisContainer = null;
            return;
        }

        if("Login".equals(rootName)) {
            onCreateLogin();
            aboutToShowThisContainer = null;
            return;
        }

        if("Profile".equals(rootName)) {
            onCreateProfile();
            aboutToShowThisContainer = null;
            return;
        }

        if("Chat".equals(rootName)) {
            onCreateChat();
            aboutToShowThisContainer = null;
            return;
        }

        if("Splash".equals(rootName)) {
            onCreateSplash();
            aboutToShowThisContainer = null;
            return;
        }

        if("Requests".equals(rootName)) {
            onCreateRequests();
            aboutToShowThisContainer = null;
            return;
        }

        if("Conversion".equals(rootName)) {
            onCreateConversion();
            aboutToShowThisContainer = null;
            return;
        }

        if("UserSearch".equals(rootName)) {
            onCreateUserSearch();
            aboutToShowThisContainer = null;
            return;
        }

        if("Car".equals(rootName)) {
            onCreateCar();
            aboutToShowThisContainer = null;
            return;
        }

        if("RideMap".equals(rootName)) {
            onCreateRideMap();
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSummary".equals(rootName)) {
            onCreateDriveSummary();
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void onCreateHistory() {
    }


    protected void onCreateCountdown() {
    }


    protected void onCreateDriveSettings() {
    }


    protected void onCreateTripFeedback() {
    }


    protected void onCreateGroups() {
    }


    protected void onCreateRegister() {
    }


    protected void onCreateNewGroup() {
    }


    protected void onCreateHome() {
    }


    protected void onCreateSettings() {
    }


    protected void onCreateCars() {
    }


    protected void onCreateLogin() {
    }


    protected void onCreateProfile() {
    }


    protected void onCreateChat() {
    }


    protected void onCreateSplash() {
    }


    protected void onCreateRequests() {
    }


    protected void onCreateConversion() {
    }


    protected void onCreateUserSearch() {
    }


    protected void onCreateCar() {
    }


    protected void onCreateRideMap() {
    }


    protected void onCreateDriveSummary() {
    }

    protected Hashtable getFormState(Form f) {
        Hashtable h = super.getFormState(f);
        if("History".equals(f.getName())) {
            getStateHistory(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Countdown".equals(f.getName())) {
            getStateCountdown(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("DriveSettings".equals(f.getName())) {
            getStateDriveSettings(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("TripFeedback".equals(f.getName())) {
            getStateTripFeedback(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Groups".equals(f.getName())) {
            getStateGroups(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Register".equals(f.getName())) {
            getStateRegister(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("newGroup".equals(f.getName())) {
            getStateNewGroup(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Home".equals(f.getName())) {
            getStateHome(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Settings".equals(f.getName())) {
            getStateSettings(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Cars".equals(f.getName())) {
            getStateCars(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Login".equals(f.getName())) {
            getStateLogin(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Profile".equals(f.getName())) {
            getStateProfile(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Chat".equals(f.getName())) {
            getStateChat(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Splash".equals(f.getName())) {
            getStateSplash(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Requests".equals(f.getName())) {
            getStateRequests(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Conversion".equals(f.getName())) {
            getStateConversion(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("UserSearch".equals(f.getName())) {
            getStateUserSearch(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("Car".equals(f.getName())) {
            getStateCar(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("RideMap".equals(f.getName())) {
            getStateRideMap(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

        if("DriveSummary".equals(f.getName())) {
            getStateDriveSummary(f, h);
            aboutToShowThisContainer = null;
            return h;
        }

            return h;
    }


    protected void getStateHistory(Form f, Hashtable h) {
    }


    protected void getStateCountdown(Form f, Hashtable h) {
    }


    protected void getStateDriveSettings(Form f, Hashtable h) {
    }


    protected void getStateTripFeedback(Form f, Hashtable h) {
    }


    protected void getStateGroups(Form f, Hashtable h) {
    }


    protected void getStateRegister(Form f, Hashtable h) {
    }


    protected void getStateNewGroup(Form f, Hashtable h) {
    }


    protected void getStateHome(Form f, Hashtable h) {
    }


    protected void getStateSettings(Form f, Hashtable h) {
    }


    protected void getStateCars(Form f, Hashtable h) {
    }


    protected void getStateLogin(Form f, Hashtable h) {
    }


    protected void getStateProfile(Form f, Hashtable h) {
    }


    protected void getStateChat(Form f, Hashtable h) {
    }


    protected void getStateSplash(Form f, Hashtable h) {
    }


    protected void getStateRequests(Form f, Hashtable h) {
    }


    protected void getStateConversion(Form f, Hashtable h) {
    }


    protected void getStateUserSearch(Form f, Hashtable h) {
    }


    protected void getStateCar(Form f, Hashtable h) {
    }


    protected void getStateRideMap(Form f, Hashtable h) {
    }


    protected void getStateDriveSummary(Form f, Hashtable h) {
    }

    protected void setFormState(Form f, Hashtable state) {
        super.setFormState(f, state);
        if("History".equals(f.getName())) {
            setStateHistory(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Countdown".equals(f.getName())) {
            setStateCountdown(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSettings".equals(f.getName())) {
            setStateDriveSettings(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("TripFeedback".equals(f.getName())) {
            setStateTripFeedback(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Groups".equals(f.getName())) {
            setStateGroups(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Register".equals(f.getName())) {
            setStateRegister(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("newGroup".equals(f.getName())) {
            setStateNewGroup(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Home".equals(f.getName())) {
            setStateHome(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Settings".equals(f.getName())) {
            setStateSettings(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Cars".equals(f.getName())) {
            setStateCars(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Login".equals(f.getName())) {
            setStateLogin(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Profile".equals(f.getName())) {
            setStateProfile(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Chat".equals(f.getName())) {
            setStateChat(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Splash".equals(f.getName())) {
            setStateSplash(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Requests".equals(f.getName())) {
            setStateRequests(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Conversion".equals(f.getName())) {
            setStateConversion(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("UserSearch".equals(f.getName())) {
            setStateUserSearch(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("Car".equals(f.getName())) {
            setStateCar(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("RideMap".equals(f.getName())) {
            setStateRideMap(f, state);
            aboutToShowThisContainer = null;
            return;
        }

        if("DriveSummary".equals(f.getName())) {
            setStateDriveSummary(f, state);
            aboutToShowThisContainer = null;
            return;
        }

            return;
    }


    protected void setStateHistory(Form f, Hashtable state) {
    }


    protected void setStateCountdown(Form f, Hashtable state) {
    }


    protected void setStateDriveSettings(Form f, Hashtable state) {
    }


    protected void setStateTripFeedback(Form f, Hashtable state) {
    }


    protected void setStateGroups(Form f, Hashtable state) {
    }


    protected void setStateRegister(Form f, Hashtable state) {
    }


    protected void setStateNewGroup(Form f, Hashtable state) {
    }


    protected void setStateHome(Form f, Hashtable state) {
    }


    protected void setStateSettings(Form f, Hashtable state) {
    }


    protected void setStateCars(Form f, Hashtable state) {
    }


    protected void setStateLogin(Form f, Hashtable state) {
    }


    protected void setStateProfile(Form f, Hashtable state) {
    }


    protected void setStateChat(Form f, Hashtable state) {
    }


    protected void setStateSplash(Form f, Hashtable state) {
    }


    protected void setStateRequests(Form f, Hashtable state) {
    }


    protected void setStateConversion(Form f, Hashtable state) {
    }


    protected void setStateUserSearch(Form f, Hashtable state) {
    }


    protected void setStateCar(Form f, Hashtable state) {
    }


    protected void setStateRideMap(Form f, Hashtable state) {
    }


    protected void setStateDriveSummary(Form f, Hashtable state) {
    }

    protected boolean setListModel(List cmp) {
        String listName = cmp.getName();
        if("Users".equals(listName)) {
            return initListModelUsers(cmp);
        }
        if("HistoryList".equals(listName)) {
            return initListModelHistoryList(cmp);
        }
        return super.setListModel(cmp);
    }

    protected boolean initListModelUsers(List cmp) {
        return false;
    }

    protected boolean initListModelHistoryList(List cmp) {
        return false;
    }

    protected void handleComponentAction(Component c, ActionEvent event) {
        Container rootContainerAncestor = getRootAncestor(c);
        if(rootContainerAncestor == null) return;
        String rootContainerName = rootContainerAncestor.getName();
        Container leadParentContainer = c.getParent().getLeadParent();
        if(leadParentContainer != null && leadParentContainer.getClass() != Container.class) {
            c = c.getParent().getLeadParent();
        }
        if(rootContainerName == null) return;
        if(rootContainerName.equals("History")) {
            if("HistoryList".equals(c.getName())) {
                onHistory_HistoryListAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("TripFeedback")) {
            if("Comment".equals(c.getName())) {
                onTripFeedback_CommentAction(c, event);
                return;
            }
            if("Cancel".equals(c.getName())) {
                onTripFeedback_CancelAction(c, event);
                return;
            }
            if("Ok".equals(c.getName())) {
                onTripFeedback_OkAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Groups")) {
            if("Groups".equals(c.getName())) {
                onGroups_GroupsAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Register")) {
            if("Pic".equals(c.getName())) {
                onRegister_PicAction(c, event);
                return;
            }
            if("Name".equals(c.getName())) {
                onRegister_NameAction(c, event);
                return;
            }
            if("Username".equals(c.getName())) {
                onRegister_UsernameAction(c, event);
                return;
            }
            if("Email".equals(c.getName())) {
                onRegister_EmailAction(c, event);
                return;
            }
            if("Password".equals(c.getName())) {
                onRegister_PasswordAction(c, event);
                return;
            }
            if("Mobile".equals(c.getName())) {
                onRegister_MobileAction(c, event);
                return;
            }
            if("female".equals(c.getName())) {
                onRegister_FemaleAction(c, event);
                return;
            }
            if("Register".equals(c.getName())) {
                onRegister_RegisterAction(c, event);
                return;
            }
            if("Login".equals(c.getName())) {
                onRegister_LoginAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("newGroup")) {
            if("Email".equals(c.getName())) {
                onNewGroup_EmailAction(c, event);
                return;
            }
            if("Info".equals(c.getName())) {
                onNewGroup_InfoAction(c, event);
                return;
            }
            if("New".equals(c.getName())) {
                onNewGroup_NewAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Settings")) {
            if("Analytics".equals(c.getName())) {
                onSettings_AnalyticsAction(c, event);
                return;
            }
            if("Website".equals(c.getName())) {
                onSettings_WebsiteAction(c, event);
                return;
            }
            if("ShareButton".equals(c.getName())) {
                onSettings_ShareButtonAction(c, event);
                return;
            }
            if("Rate".equals(c.getName())) {
                onSettings_RateAction(c, event);
                return;
            }
            if("Feedback".equals(c.getName())) {
                onSettings_FeedbackAction(c, event);
                return;
            }
            if("Report".equals(c.getName())) {
                onSettings_ReportAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Cars")) {
            if("Cars".equals(c.getName())) {
                onCars_CarsAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Login")) {
            if("Logo".equals(c.getName())) {
                onLogin_LogoAction(c, event);
                return;
            }
            if("Username".equals(c.getName())) {
                onLogin_UsernameAction(c, event);
                return;
            }
            if("Password".equals(c.getName())) {
                onLogin_PasswordAction(c, event);
                return;
            }
            if("Login".equals(c.getName())) {
                onLogin_LoginAction(c, event);
                return;
            }
            if("Register".equals(c.getName())) {
                onLogin_RegisterAction(c, event);
                return;
            }
            if("Reset".equals(c.getName())) {
                onLogin_ResetAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Profile")) {
            if("Pic".equals(c.getName())) {
                onProfile_PicAction(c, event);
                return;
            }
            if("Name".equals(c.getName())) {
                onProfile_NameAction(c, event);
                return;
            }
            if("Username".equals(c.getName())) {
                onProfile_UsernameAction(c, event);
                return;
            }
            if("Email".equals(c.getName())) {
                onProfile_EmailAction(c, event);
                return;
            }
            if("Password".equals(c.getName())) {
                onProfile_PasswordAction(c, event);
                return;
            }
            if("Mobile".equals(c.getName())) {
                onProfile_MobileAction(c, event);
                return;
            }
            if("female".equals(c.getName())) {
                onProfile_FemaleAction(c, event);
                return;
            }
            if("Save".equals(c.getName())) {
                onProfile_SaveAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Chat")) {
            if("Chat".equals(c.getName())) {
                onChat_ChatAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Conversion")) {
            if("Message".equals(c.getName())) {
                onConversion_MessageAction(c, event);
                return;
            }
            if("Send".equals(c.getName())) {
                onConversion_SendAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("UserSearch")) {
            if("SearchField".equals(c.getName())) {
                onUserSearch_SearchFieldAction(c, event);
                return;
            }
            if("Search".equals(c.getName())) {
                onUserSearch_SearchAction(c, event);
                return;
            }
            if("Users".equals(c.getName())) {
                onUserSearch_UsersAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("Car")) {
            if("Add".equals(c.getName())) {
                onCar_AddAction(c, event);
                return;
            }
            if("Name".equals(c.getName())) {
                onCar_NameAction(c, event);
                return;
            }
            if("Year".equals(c.getName())) {
                onCar_YearAction(c, event);
                return;
            }
            if("Plate".equals(c.getName())) {
                onCar_PlateAction(c, event);
                return;
            }
            if("Notes".equals(c.getName())) {
                onCar_NotesAction(c, event);
                return;
            }
            if("Save".equals(c.getName())) {
                onCar_SaveAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("RideMap")) {
            if("Cancel".equals(c.getName())) {
                onRideMap_CancelAction(c, event);
                return;
            }
        }
        if(rootContainerName.equals("DriveSummary")) {
            if("Cancel".equals(c.getName())) {
                onDriveSummary_CancelAction(c, event);
                return;
            }
            if("Confirm".equals(c.getName())) {
                onDriveSummary_ConfirmAction(c, event);
                return;
            }
        }
    }

      protected void onHistory_HistoryListAction(Component c, ActionEvent event) {
      }

      protected void onTripFeedback_CommentAction(Component c, ActionEvent event) {
      }

      protected void onTripFeedback_CancelAction(Component c, ActionEvent event) {
      }

      protected void onTripFeedback_OkAction(Component c, ActionEvent event) {
      }

      protected void onGroups_GroupsAction(Component c, ActionEvent event) {
      }

      protected void onRegister_PicAction(Component c, ActionEvent event) {
      }

      protected void onRegister_NameAction(Component c, ActionEvent event) {
      }

      protected void onRegister_UsernameAction(Component c, ActionEvent event) {
      }

      protected void onRegister_EmailAction(Component c, ActionEvent event) {
      }

      protected void onRegister_PasswordAction(Component c, ActionEvent event) {
      }

      protected void onRegister_MobileAction(Component c, ActionEvent event) {
      }

      protected void onRegister_FemaleAction(Component c, ActionEvent event) {
      }

      protected void onRegister_RegisterAction(Component c, ActionEvent event) {
      }

      protected void onRegister_LoginAction(Component c, ActionEvent event) {
      }

      protected void onNewGroup_EmailAction(Component c, ActionEvent event) {
      }

      protected void onNewGroup_InfoAction(Component c, ActionEvent event) {
      }

      protected void onNewGroup_NewAction(Component c, ActionEvent event) {
      }

      protected void onSettings_AnalyticsAction(Component c, ActionEvent event) {
      }

      protected void onSettings_WebsiteAction(Component c, ActionEvent event) {
      }

      protected void onSettings_ShareButtonAction(Component c, ActionEvent event) {
      }

      protected void onSettings_RateAction(Component c, ActionEvent event) {
      }

      protected void onSettings_FeedbackAction(Component c, ActionEvent event) {
      }

      protected void onSettings_ReportAction(Component c, ActionEvent event) {
      }

      protected void onCars_CarsAction(Component c, ActionEvent event) {
      }

      protected void onLogin_LogoAction(Component c, ActionEvent event) {
      }

      protected void onLogin_UsernameAction(Component c, ActionEvent event) {
      }

      protected void onLogin_PasswordAction(Component c, ActionEvent event) {
      }

      protected void onLogin_LoginAction(Component c, ActionEvent event) {
      }

      protected void onLogin_RegisterAction(Component c, ActionEvent event) {
      }

      protected void onLogin_ResetAction(Component c, ActionEvent event) {
      }

      protected void onProfile_PicAction(Component c, ActionEvent event) {
      }

      protected void onProfile_NameAction(Component c, ActionEvent event) {
      }

      protected void onProfile_UsernameAction(Component c, ActionEvent event) {
      }

      protected void onProfile_EmailAction(Component c, ActionEvent event) {
      }

      protected void onProfile_PasswordAction(Component c, ActionEvent event) {
      }

      protected void onProfile_MobileAction(Component c, ActionEvent event) {
      }

      protected void onProfile_FemaleAction(Component c, ActionEvent event) {
      }

      protected void onProfile_SaveAction(Component c, ActionEvent event) {
      }

      protected void onChat_ChatAction(Component c, ActionEvent event) {
      }

      protected void onConversion_MessageAction(Component c, ActionEvent event) {
      }

      protected void onConversion_SendAction(Component c, ActionEvent event) {
      }

      protected void onUserSearch_SearchFieldAction(Component c, ActionEvent event) {
      }

      protected void onUserSearch_SearchAction(Component c, ActionEvent event) {
      }

      protected void onUserSearch_UsersAction(Component c, ActionEvent event) {
      }

      protected void onCar_AddAction(Component c, ActionEvent event) {
      }

      protected void onCar_NameAction(Component c, ActionEvent event) {
      }

      protected void onCar_YearAction(Component c, ActionEvent event) {
      }

      protected void onCar_PlateAction(Component c, ActionEvent event) {
      }

      protected void onCar_NotesAction(Component c, ActionEvent event) {
      }

      protected void onCar_SaveAction(Component c, ActionEvent event) {
      }

      protected void onRideMap_CancelAction(Component c, ActionEvent event) {
      }

      protected void onDriveSummary_CancelAction(Component c, ActionEvent event) {
      }

      protected void onDriveSummary_ConfirmAction(Component c, ActionEvent event) {
      }

}
