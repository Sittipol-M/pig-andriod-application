package com.example.pig_application2.API.ApiModel;

import com.example.pig_application2.API.DataModel.FarmPermissionModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FarmPermissionsApiModel {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("error_type")
    private String errorType;
    @SerializedName("data")
    private ArrayList<FarmPermissionModel> farmPermissions;
    public FarmPermissionsApiModel(Boolean success, String message, String errorType, ArrayList<FarmPermissionModel> farmPermissions) {
        this.success = success;
        this.message = message;
        this.errorType = errorType;
        this.farmPermissions = farmPermissions;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorType() {
        return errorType;
    }

    public ArrayList<FarmPermissionModel> getFarmPermissions() {
        return farmPermissions;
    }
}
