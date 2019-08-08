//package com.example.myapplicatio.db.browse
//
//import android.database.Observable
//import com.bumptech.glide.load.model.Model
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//interface WikiApiService {
//    @GET("api.php")
//    fun hitCountCheck(@Query("action") action: String,
//                      @Query("format") format: String,
//                      @Query("list") list: String,
//                      @Query("srsearch") srsearch: String):
//            Observable<Model.Result>
//
//    companion object {
//        fun create(): WikiApiService {
//
//            val retrofit = Retrofit.Builder()
//                    .addCallAdapterFactory(
//                            RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(
//                            GsonConverterFactory.create())
//                    .baseUrl("https://en.wikipedia.org/w/")
//                    .build()
//
//            return retrofit.create(WikiApiService::class.java)
//        }
//    }
//}