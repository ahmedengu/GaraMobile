package com.g_ara.gara.form;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.util.UIBuilder;

public class HomeForm extends com.codename1.ui.Form {

    public HomeForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }

    public HomeForm(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
//        new Map().initMap(this);
    }

    public void onHomeCommand(ActionEvent ev, Command cmd) {
    }

    private void onCarsCommand(ActionEvent ev, Command command) {
        new UIBuilder().showForm("CarsForm", null);
//        CarsForm carsForm = new CarsForm();
//        carsForm.show();
    }

    private void onWalletCommand(ActionEvent ev, Command command) {

    }

    private void onFeedCommand(ActionEvent ev, Command command) {

    }

    private void onGroupsCommand(ActionEvent ev, Command command) {

    }

    private void onHistoryCommand(ActionEvent ev, Command command) {

    }

    private void onRequestsCommand(ActionEvent ev, Command command) {

    }

    private void onProfileCommand(ActionEvent ev, Command command) {

    }

    private void onSettingsCommand(ActionEvent ev, Command command) {

    }

    private void onChatCommand(ActionEvent ev, Command command) {

    }


    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.BorderLayout());
        setTitle("Home");
        setName("HomeForm");
        com.codename1.ui.Toolbar cn1Toolbar = getToolbar();
        if (cn1Toolbar == null) {
            cn1Toolbar = new com.codename1.ui.Toolbar();
            setToolBar(cn1Toolbar);
        }
        com.codename1.ui.Command cmd_Home = new com.codename1.ui.Command("Home") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onHomeCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Home);
        com.codename1.ui.Command cmd_Chat = new com.codename1.ui.Command("Chat") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onChatCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Chat);
        com.codename1.ui.Command cmd_Settings = new com.codename1.ui.Command("Settings") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onSettingsCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Settings);
        com.codename1.ui.Command cmd_Profile = new com.codename1.ui.Command("Profile") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onProfileCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Profile);
        com.codename1.ui.Command cmd_Requests = new com.codename1.ui.Command("Requests") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onRequestsCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Requests);
        com.codename1.ui.Command cmd_History = new com.codename1.ui.Command("History") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onHistoryCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_History);
        com.codename1.ui.Command cmd_Groups = new com.codename1.ui.Command("Community") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onGroupsCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Groups);
        com.codename1.ui.Command cmd_Feed = new com.codename1.ui.Command("Feed") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onFeedCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Feed);
        com.codename1.ui.Command cmd_Wallet = new com.codename1.ui.Command("Wallet") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onWalletCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Wallet);
        com.codename1.ui.Command cmd_Cars = new com.codename1.ui.Command("Cars") {
            public void actionPerformed(com.codename1.ui.events.ActionEvent ev) {
                onCarsCommand(ev, this);
            }
        };
        cn1Toolbar.addCommandToSideMenu(cmd_Cars);
    }
}
