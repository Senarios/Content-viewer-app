package com.senarios.coneqtliveviewer.Views;


import com.google.firebase.database.core.view.CancelEvent;
import com.senarios.coneqtliveviewer.Model.ChangeSetting;
import com.senarios.coneqtliveviewer.Model.CreateEventHistoryData;
import com.senarios.coneqtliveviewer.Model.CreatorFilter;
import com.senarios.coneqtliveviewer.Model.EventCompletedModel;
import com.senarios.coneqtliveviewer.Model.ForgetPassword;
import com.senarios.coneqtliveviewer.Model.GetEventTypeRes;
import com.senarios.coneqtliveviewer.Model.GetSecretRes;
import com.senarios.coneqtliveviewer.Model.GuestEventResponse;
import com.senarios.coneqtliveviewer.Model.GuestJoinVerify;
import com.senarios.coneqtliveviewer.Model.GuestPay;
import com.senarios.coneqtliveviewer.Model.GuestStartEvent;
import com.senarios.coneqtliveviewer.Model.GuestUser;
import com.senarios.coneqtliveviewer.Model.JoinGuestModel;
import com.senarios.coneqtliveviewer.Model.Login;
import com.senarios.coneqtliveviewer.Model.Logout;
import com.senarios.coneqtliveviewer.Model.NotificationCountModel;
import com.senarios.coneqtliveviewer.Model.NotificationList;
import com.senarios.coneqtliveviewer.Model.OverViewFilter;
import com.senarios.coneqtliveviewer.Model.OverViewViewer;
import com.senarios.coneqtliveviewer.Model.RatingModel;
import com.senarios.coneqtliveviewer.Model.RefundModel;
import com.senarios.coneqtliveviewer.Model.Register;
import com.senarios.coneqtliveviewer.Model.ResetChangedPassword;
import com.senarios.coneqtliveviewer.Model.SocialLogin;
import com.senarios.coneqtliveviewer.Model.StartEventModel;
import com.senarios.coneqtliveviewer.Model.StreamReported;
import com.senarios.coneqtliveviewer.Model.UpdateProfile;
import com.senarios.coneqtliveviewer.Model.VerificationCode;
import com.senarios.coneqtliveviewer.Model.VerifyPasswordReset;
import com.senarios.coneqtliveviewer.stripe.Model.AccountLinksRes;
import com.senarios.coneqtliveviewer.stripe.Model.AccountsInfo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiServices {

    @FormUrlEncoded
    @POST("accounts")
    Call<AccountsInfo> createUser (@Header("Authorization") String authHeader,
                                   @Field("type") String type,
                                   @Field("country") String country,
                                   @Field("email") String email,
                                   @Field("capabilities[card_payments][requested]") boolean capabilities1,
                                   @Field("capabilities[transfers][requested]") boolean capabilities2);

    @FormUrlEncoded
    @POST("account_links")
    Call<AccountLinksRes> accountLink (@Header("Authorization") String authHeader,
                                       @Field("account") String account,
                                       @Field("type") String type,
                                       @Field("return_url") String returnUrl,
                                       @Field("refresh_url") String refreshUrl);

    @FormUrlEncoded
    @POST("signup")
    Call<Register> registerUser (@Field("email") String email,
                                 @Field("password") String password,
                                 @Field("phone") String number,
                                 @Field("first_name") String firstname,
                                 @Field("last_name") String last_name,
                                 @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("login")
    Call<Login> LoginUser (@Field("email") String email,
                              @Field("password") String password,
                              @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("intent")
    Call<GetSecretRes> getSecret (@Header("Authorization") String authHeader,
                                  @Field("amount") String amount,
                                  @Field("customer_id") String customer_id);

    @FormUrlEncoded
    @POST("create")
    Call<GuestUser> getGuestUser (@Field("first_name") String email,
                                 @Field("last_name") String password,
                                 @Field("email") String number,
                                 @Field("event_id") String firstname);

    @FormUrlEncoded
    @POST("pay")
    Call<GuestPay> getGuestPay (@Field("email") String email,
                                @Field("event_id") String eventId,
                                @Field("payment_intent") String paymentIntent);

    @GET("overview")
    Call<OverViewViewer> CreateOverViewRes (@Header("Authorization") String authHeader,
                                            @Query("offset") Integer offset);

    @GET("overview")
    Call<OverViewViewer> GuestCreateOverView(@Query("offset") Integer offset);

    @FormUrlEncoded
    @POST("get_events")
    Call<CreateEventHistoryData> getEventHistory (@Header("Authorization") String authHeader,
                                                  @Field("amount") Double amount,
                                                  @Field("event_id") Integer event_id,
                                                  @Field("description") String description,
                                                  @Field("time") String time,
                                                  @Field("time_duration") String timeDuration,
                                                  @Field("ticket_price") Integer ticketPrice,
                                                  @Field("stream_interaction") String streamInteraction);

    @FormUrlEncoded
    @POST("start_event")
    Call<StartEventModel> getStartEvent (@Header("Authorization") String authHeader,
                                         @Field("event_id") Integer event_id);

    @Multipart
    @POST("settings")
    Call<UpdateProfile> getUpdate(@Header("Authorization") String authorization,
                                         @Part("first_name") RequestBody name,
                                         @Part("last_name") RequestBody type,
                                         @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("change_password_settings")
    Call<ChangeSetting> changeSettings(@Header("Authorization") String authHeader,
                                       @Field("new_password") String newPassword,
                                       @Field("old_password") String oldPassword);

    @FormUrlEncoded
    @POST("App")
    Call<JoinGuestModel> joinEvents(@Field("event_id") String event_id,
                                    @Field("otp") String otp);

    @FormUrlEncoded
    @POST("social_signup_login")
    Call<SocialLogin> getSocialLogin (@Field("first_name") String firstname,
                                      @Field("last_name") String last_name,
                                      @Field("id") String Id,
                                      @Field("email") String email,
                                      @Field("auth_type") String authType,
                                      @Field("image_url") String image,
                                      @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("user")
    Call<VerificationCode> getVerifyCode (@Field("userId") int userId,
                                          @Field("otp") int otp);

    @POST("notification")
    Call<NotificationList> getNotification (@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ForgetPassword> getForgetPassword (@Field("email") String email);

    @FormUrlEncoded
    @POST("verify_password_reset_code")
    Call<VerifyPasswordReset> getVerifiedPassword (@Field("email") String email,
                                                   @Field("verification_code") String verify);

    @FormUrlEncoded
    @POST("change_password")
    Call<ResetChangedPassword> getChangedPassword (@Field("email") String email,
                                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("filter")
    Call<OverViewFilter> getGuestOverViewFilter (@Field("sort") Integer sort,
                                            @Field("type") String type,
                                            @Field("offset") Integer offset);

    @FormUrlEncoded
    @POST("filter")
    Call<OverViewFilter> getOverViewFilter (@Header("Authorization") String authHeader,
                                                 @Field("sort") Integer sort,
                                                 @Field("type") String type,
                                                 @Field("offset") Integer offset);

    @FormUrlEncoded
    @POST("refund")
    Call<RefundModel> getCancelEvent (@Header("Authorization") String authHeader,
                                      @Field("payment_intent") String event_id);

    @FormUrlEncoded
    @POST("otp")
    Call<GuestJoinVerify> getGuestJoinVerify (@Field("event_id") String event_id,
                                        @Field("otp") String otp);

    @FormUrlEncoded
    @POST("guest")
    Call<GuestStartEvent> getGuestStart (@Field("event_id") Integer event_id,
                                         @Field("user") Integer user);

    @FormUrlEncoded
    @POST("filter")
    Call<CreatorFilter> getFilterList (@Header("Authorization") String authHeader,
                                       @Field("stat") Integer stat);
    @FormUrlEncoded
    @POST("url")
    Call<GuestEventResponse> getGuestEvent (@Field("name") String name,
                                            @Field("username") String username);

    @GET("event/type")
    Call<GetEventTypeRes> getEventType ();

    @FormUrlEncoded
    @POST("report_stream")
    Call<StreamReported> getStreamReported (@Header("Authorization") String authHeader,
                                            @Field("creator_id") Integer creatorId,
                                            @Field("event_id") Integer eventId,
                                            @Field("viewer_id") Integer viewerId);

    @FormUrlEncoded
    @POST("event_status")
    Call<EventCompletedModel> getCompletedEvent(@Field("event_id") Integer eventId);

    @FormUrlEncoded
    @POST("event_rating")
    Call<RatingModel> getRating(@Field("rate") double rate,
                                @Field("viewer_id") Integer viewerId ,
                                @Field("event_id") Integer eventId);

    @GET("count")
    Call<NotificationCountModel> getNotificationCount (@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("logout")
    Call<Logout> getLogout (@Header("Authorization") String authHeader,
                            @Field("type") String type);

}
