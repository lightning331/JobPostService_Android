package com.kelvin.jacksgogo.Utils.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kelvin.jacksgogo.Utils.Models.Jobs_Services_Events.JGGAppointmentModel;

/**
 * Created by PUMA on 3/19/2018.
 */

public class JGGGetAppResponse extends JGGBaseResponse {

    @SerializedName("Value")
    @Expose
    private JGGAppointmentModel Value;

    public JGGAppointmentModel getValue() {
        return Value;
    }

    public void setValue(JGGAppointmentModel value) {
        Value = value;
    }
}
