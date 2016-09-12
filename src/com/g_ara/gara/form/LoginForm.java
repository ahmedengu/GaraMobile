package com.g_ara.gara.form;

import com.codename1.ui.events.ActionEvent;

public class LoginForm extends com.codename1.ui.Form {
    public LoginForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }

    public LoginForm(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
    }

    private void onGoogleActionEvent(ActionEvent ev) {

    }

    private void onFacebookActionEvent(ActionEvent ev) {

    }

    private void onRegisterActionEvent(ActionEvent ev) {
        new RegisterForm().show();

    }

    private void onLoginActionEvent(ActionEvent ev) {
        new HomeForm().show();

    }

    //-- DON'T EDIT BELOW THIS LINE!!!
    private com.codename1.ui.Container gui_Container_1 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BoxLayout(com.codename1.ui.layouts.BoxLayout.Y_AXIS));
    private com.codename1.ui.TextField gui_Username = new com.codename1.ui.TextField();
    private com.codename1.ui.TextField gui_Password = new com.codename1.ui.TextField();
    private com.codename1.ui.Button gui_Login = new com.codename1.ui.Button();
    private com.codename1.ui.Button gui_Register = new com.codename1.ui.Button();
    private com.codename1.ui.Button gui_Facebook = new com.codename1.ui.Button();
    private com.codename1.ui.Button gui_Google = new com.codename1.ui.Button();


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void guiBuilderBindComponentListeners() {
        EventCallbackClass callback = new EventCallbackClass();
        gui_Login.addActionListener(callback);
        gui_Register.addActionListener(callback);
        gui_Facebook.addActionListener(callback);
        gui_Google.addActionListener(callback);
    }

    class EventCallbackClass implements com.codename1.ui.events.ActionListener, com.codename1.ui.events.DataChangedListener {
        private com.codename1.ui.Component cmp;
        public EventCallbackClass(com.codename1.ui.Component cmp) {
            this.cmp = cmp;
        }

        public EventCallbackClass() {
        }

        public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
            com.codename1.ui.Component sourceComponent = ev.getComponent();
            if(sourceComponent.getParent().getLeadParent() != null) {
                sourceComponent = sourceComponent.getParent().getLeadParent();
            }

            if(sourceComponent == gui_Login) {
                onLoginActionEvent(ev);
            }
            if(sourceComponent == gui_Register) {
                onRegisterActionEvent(ev);
            }
            if(sourceComponent == gui_Facebook) {
                onFacebookActionEvent(ev);
            }
            if(sourceComponent == gui_Google) {
                onGoogleActionEvent(ev);
            }
        }

        public void dataChanged(int type, int index) {
        }
    }
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        guiBuilderBindComponentListeners();
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setTitle("Login");
        setName("LoginForm");
        ((com.codename1.ui.layouts.FlowLayout)getLayout()).setAlign(com.codename1.ui.Component.CENTER);
        ((com.codename1.ui.layouts.FlowLayout)getLayout()).setValign(com.codename1.ui.Component.CENTER);
        addComponent(gui_Container_1);
        gui_Container_1.setName("Container_1");
        gui_Container_1.addComponent(gui_Username);
        gui_Container_1.addComponent(gui_Password);
        gui_Container_1.addComponent(gui_Login);
        gui_Container_1.addComponent(gui_Register);
        gui_Container_1.addComponent(gui_Facebook);
        gui_Container_1.addComponent(gui_Google);
        gui_Username.setHint("Username");
        gui_Username.setName("Username");
        gui_Password.setHint("Password");
        gui_Password.setName("Password");
        gui_Login.setText("Login");
        gui_Login.setName("Login");
        gui_Register.setText("Register");
        gui_Register.setName("Register");
        gui_Facebook.setText("Facebook");
        gui_Facebook.setName("Facebook");
        gui_Google.setText("Google");
        gui_Google.setName("Google");
        gui_Container_1.setName("Container_1");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
