package com.swenggco.contactapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

abstract class BaseViewModel : ViewModel() {



    private val unAuthorizedUser: MutableLiveData<Boolean> = MutableLiveData()

    private var schedulersTransformer: SingleTransformer<Objects, Objects> = SingleTransformer { observable ->
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> applySchedulers(): SingleTransformer<T, T> {
        return schedulersTransformer as SingleTransformer<T, T>
    }

    /**
     * Indicate currentPage
     */
    protected var currentPage: Int = 0
    /**
     * hold items total account. Need to update this when get response from api
     */
    private var totalCount: Int = 1
    /**
     * Page content size
     */
    protected var pageSize = 10


    val compositeDisposable = CompositeDisposable()

    /**
     * child class has to call this
     */
    fun setItemTotal(total: Int) {
        totalCount = total
    }

    fun resetItemCount() {
        currentPage = 0
        totalCount = 1
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onHandleError(error: Throwable): String {

        when (error) {
            //is HttpException -> return ApiErrorResponse(error).message
            is SocketTimeoutException -> return "Problem to connect server!"
            is IOException -> return "Problem to internet connection!"
            //is InvalidAuthException -> return error.message?:"Invalid credentials"
        }

        return error.message ?: "unknown error."
    }

    fun shouldLoadMore(): Boolean {

        return if (currentPage * pageSize < totalCount) {
            currentPage++
            true
        } else {
            false
        }

    }

    fun subscribeUnAuthorizedUser(): LiveData<Boolean> {
        return unAuthorizedUser
    }
}
