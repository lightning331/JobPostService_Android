package com.kelvin.jacksgogo.Utils.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kelvin.jacksgogo.Utils.Models.GoClub_Event.JGGGoClubModel;

public class JGGGetGoClubResponse extends JGGBaseResponse {

    @SerializedName("Value")
    @Expose
    private JGGGoClubModel Value;

    public JGGGoClubModel getValue() {
        return Value;
    }

    public void setValue(JGGGoClubModel value) {
        Value = value;
    }
}
