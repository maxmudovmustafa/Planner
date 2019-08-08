//package com.example.myapplicatio.db.browse
//
//import android.content.Intent
//import android.support.v4.content.LocalBroadcastManager
//import com.example.myapplicatio.aralash.BaseActivity
//import io.reactivex.BackpressureStrategy
//import io.reactivex.Completable
//import io.reactivex.Single
//import io.reactivex.schedulers.Schedulers
//import retrofit2.Call
//import retrofit2.CallAdapter
//import java.lang.reflect.Type
//
//class RxRetroCallAdapterclass <R>(private val originalAdapter : CallAdapter<R, *>) : CallAdapter<R, Any> {
//    override fun adapt(call : Call<R>) : Any {
//        val adaptedValue = originalAdapter.adapt(call)
//        return when (adaptedValue) {
//            is Completable -> {
//                adaptedValue.doOnError(this::sendBroadcast)
//                        .retryWhen {
//                            AppProvider.provideRetrySubject().toFlowable(BackpressureStrategy.LATEST)
//                                    .observeOn(Schedulers.io())
//                        }
//            }
//            is Single<*> -> {
//                adaptedValue.doOnError(this::sendBroadcast)
//                        .retryWhen {
//                            AppProvider.provideRetrySubject().toFlowable(BackpressureStrategy.LATEST)
//                                    .observeOn(Schedulers.io())
//                        }
//            }
//            //same for Maybe, Observable, Flowable
//            else -> {
//                adaptedValue
//            }
//        }
//    }
//
//    override fun responseType() : Type = originalAdapter.responseType()
//
//    private fun sendBroadcast(throwable : Throwable) {
//        Timber.e(throwable)
//        LocalBroadcastManager.getInstance(AppProvider.appInstance).sendBroadcast(Intent(BaseActivity.ERROR_ACTION))
//    }
//}