package com.g_ara.gara;


import com.codename1.analytics.AnalyticsService;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.g_ara.gara.form.LoginForm;

public class Main {

    private Form current;
    private static Resources theme;

    public void init(Object context) {
        theme = UIManager.initFirstTheme("/theme");

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        // Pro only feature, uncomment if you have a pro subscription
        // Log.bindCrashProtection(true);
    }
    
    public void start() {
        AnalyticsService.setAppsMode(true);
        AnalyticsService.init("UA-80287405-1", "g-ara.com");

        if(current != null){
            current.show();
            return;
        }
        current = new LoginForm();
        current.show();

    }

    public void stop() {
        current = Display.getInstance().getCurrent();
        if(current instanceof Dialog) {
            ((Dialog)current).dispose();
            current = Display.getInstance().getCurrent();
        }
    }
    
    public void destroy() {
    }

    public static Resources fetchResourceFile() {
        return theme;
    }
}
