//package com.example.myapplicatio.db.browse
//
//import retrofit2.CallAdapter
//import retrofit2.Retrofit
//import java.lang.reflect.Type
//
//class RxRetryCallAdapterFactory: CallAdapter.Factory() {
//    companion object {
//        fun create() : CallAdapter.Factory = RxRetryCallAdapterFactory()
//    }
//
//    private val originalFactory = RxJava2CallAdapterFactory.create()
//
//    override fun get(returnType : Type, annotations : Array<Annotation>, retrofit : Retrofit) : CallAdapter<*, *>? {
//        val adapter = originalFactory.get(returnType, annotations, retrofit) ?: return null
//        return RxRetroCallAdapterclass(adapter)
//    }
//}