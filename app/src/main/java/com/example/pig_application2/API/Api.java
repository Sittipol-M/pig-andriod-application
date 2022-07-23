package com.example.pig_application2.API;

import com.example.pig_application2.API.ApiModel.BreedsApiModel;
import com.example.pig_application2.API.ApiModel.CompanyUserApiModel;
import com.example.pig_application2.API.ApiModel.FarmPermissionsApiModel;
import com.example.pig_application2.API.ApiModel.PigApiModel;
import com.example.pig_application2.API.ApiModel.ResponseModel;
import com.example.pig_application2.API.ApiModel.SpermsApiModel;
import com.example.pig_application2.API.ApiModel.UserApiModel;
import com.example.pig_application2.API.ApiModel.VaccinationApiModel;
import com.example.pig_application2.API.ApiModel.VaccinationsApiModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {
    String PIG_API_URL = "https://pig-applicaion-api.herokuapp.com/api/";


    @POST("users/login")
    Call<UserApiModel>login ( @Body JsonObject loginPost );

    @GET("companies/{companyName}/companyUsers/{userId}/farmPermissions")
    Call<FarmPermissionsApiModel>getFarmPermissions(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("userId") String userId
    );

    @POST("companies/{companyName}/farms/{farmId}/pigs")//use
    Call<PigApiModel> newPigWithRfid(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Body JsonObject newPigPost
//            @Field("rfid_code") String rfid_code,
//            @Field("pig_code") String pig_code,
//            @Field("sex") String sex
    );


    @PATCH("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}")//use
    Call<PigApiModel> UpdatePigWithRfid(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Body JsonObject updateUnitAndBlock
    );


    @GET("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}")//use
    Call<PigApiModel> getPig(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode
    );

    @DELETE("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}")
    Call<PigApiModel> deletePig(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode
    );


    @POST("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/vaccinations")
    Call<ResponseModel> newVaccinationEach(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Body JsonObject newVaccination
    );

    @POST("companies/{companyName}/farms/{farmId}/units/{unitCode}/pigs/vaccinations")
    Call<ResponseModel> newVaccinationUnit(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("unitCode") String unitCode,
            @Body JsonObject newVaccinationUnit
    );

    @GET("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/vaccinations")
    Call<VaccinationsApiModel> getVaccinations(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode
    );


    @GET("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/vaccinations/{vaccinationId}")
    Call<VaccinationApiModel> getVaccination(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Path("vaccinationId") String vaccinationId
    );

    @DELETE("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/vaccinations/{vaccinationId}")
    Call<ResponseModel> deleteVaccination(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Path("vaccinationId") String vaccinationId
    );

    @GET("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/breeds")//use
    Call<BreedsApiModel> getBreeds(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode
    );

    @GET("companies/{companyName}/companyUsers/{companyUserId}")
    Call<CompanyUserApiModel> getCompanyUser(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("companyUserId") String companyUserId
    );

    @POST("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/breeds")
    Call<ResponseModel> addBreed(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Body JsonObject newBreed
    );

    @DELETE("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/breeds/{breedId}")
    Call<ResponseModel> deleteBreed(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Path("breedId") String breedId
    );

    @PATCH("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/breeds/{breedId}/newBorn")
    Call<ResponseModel> editNewBorn(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Path("breedId") String breedId,
            @Body JsonObject editNewBorn
    );

    @GET("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/sperms")
    Call<SpermsApiModel> getSperms(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode
    );

    @DELETE("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/sperms/{spermId}")
    Call<ResponseModel> deleteSperm(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Path("spermId") String spermId
    );

    @POST("companies/{companyName}/farms/{farmId}/pigs/{rfidCode}/sperms")
    Call<ResponseModel> addSperm(
            @Header("authorization") String authorization,
            @Path("companyName") String companyName,
            @Path("farmId") String farmId,
            @Path("rfidCode") String rfidCode,
            @Body JsonObject newSperm
    );
}
