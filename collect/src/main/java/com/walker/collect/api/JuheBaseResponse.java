package com.walker.collect.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JuheBaseResponse {
    @SerializedName("error_code")
    @Expose
    public Integer errorCode;
    @SerializedName("reason")
    @Expose
    public String reason;
}
