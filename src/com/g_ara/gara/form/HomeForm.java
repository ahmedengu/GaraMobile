package com.g_ara.gara.form;

import com.g_ara.gara.controller.Map;

public class HomeForm extends com.codename1.ui.Form {

    public HomeForm() {
        this(com.codename1.ui.util.Resources.getGlobalResources());
    }

    public HomeForm(com.codename1.ui.util.Resources resourceObjectInstance) {
        initGuiBuilderComponents(resourceObjectInstance);
        Map.initMap(this);
    }


//-- DON'T EDIT BELOW THIS LINE!!!


    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initGuiBuilderComponents(com.codename1.ui.util.Resources resourceObjectInstance) {
        setLayout(new com.codename1.ui.layouts.BorderLayout());
        setTitle("Home");
        setName("HomeForm");
    }// </editor-fold>

//-- DON'T EDIT ABOVE THIS LINE!!!
}
