package com.sociality.Notification;

import com.sociality.Notification.MyResponse;
import com.sociality.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


    @Headers(
            {
                    "Content-type:application/json",
                    "Authorization:key=AAAAnq5az4s:APA91bEl-ts6Ff3kM6KA1tqZXcYuS2740-mv4u-Pub8NAkCcQwUNkECwIQgvNGkshIpvvBByj_LgpxpxKqd6ave17d1krBXdAYuDTIDjvetggd0HBEFoHSW9Jh6nFBtgj_DPXBRkJLq4"

            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
