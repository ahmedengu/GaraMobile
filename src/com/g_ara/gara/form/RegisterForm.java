package com.g_ara.gara.form;

import com.codename1.ui.events.ActionEvent;

public class RegisterForm extends com.codename1.ui.Form {
    public RegisterForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }

    public RegisterForm(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
    }

    private void onRegisterActionEvent(ActionEvent ev) {

    }

    //-- DON'T EDIT BELOW THIS LINE!!!
    private com.codename1.ui.Container gui_Container_1 = new com.codename1.ui.Container(new com.codename1.ui.layouts.BoxLayout(com.codename1.ui.layouts.BoxLayout.Y_AXIS));
    private com.codename1.ui.TextField gui_Name = new com.codename1.ui.TextField();
    private com.codename1.ui.TextField gui_Username = new com.codename1.ui.TextField();
    private com.codename1.ui.TextField gui_Email = new com.codename1.ui.TextField();
    private com.codename1.ui.TextField gui_Password = new com.codename1.ui.TextField();
    private com.codename1.ui.Button gui_Register = new com.codename1.ui.Button();


// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void guiBuilderBindComponentListeners() {
        EventCallbackClass callback = new EventCallbackClass();
        gui_Register.addActionListener(callback);
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

            if(sourceComponent == gui_Register) {
                onRegisterActionEvent(ev);
            }
        }

        public void dataChanged(int type, int index) {
        }
    }
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        guiBuilderBindComponentListeners();
        setLayout(new com.codename1.ui.layouts.FlowLayout());
        setTitle("Register");
        setName("RegisterForm");
        ((com.codename1.ui.layouts.FlowLayout)getLayout()).setAlign(com.codename1.ui.Component.CENTER);
        ((com.codename1.ui.layouts.FlowLayout)getLayout()).setValign(com.codename1.ui.Component.CENTER);
        addComponent(gui_Container_1);
        gui_Container_1.setName("Container_1");
        gui_Container_1.addComponent(gui_Name);
        gui_Container_1.addComponent(gui_Username);
        gui_Container_1.addComponent(gui_Email);
        gui_Container_1.addComponent(gui_Password);
        gui_Container_1.addComponent(gui_Register);
        gui_Name.setHint("Name");
        gui_Name.setName("Name");
        gui_Username.setHint("Username");
        gui_Username.setName("Username");
        gui_Email.setHint("Email");
        gui_Email.setName("Email");
        gui_Password.setHint("Password");
        gui_Password.setName("Password");
        gui_Register.setText("Register");
        gui_Register.setName("Register");
        gui_Register.setPropertyValue("maskName", "");
        gui_Container_1.setName("Container_1");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
