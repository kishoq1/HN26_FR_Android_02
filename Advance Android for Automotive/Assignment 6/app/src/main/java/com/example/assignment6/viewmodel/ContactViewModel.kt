package com.example.assignment6.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.util.query
import com.example.assignment6.data.model.Contact
import com.example.assignment6.data.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import java.util.concurrent.TimeUnit

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val repository: ContactRepository
) : ViewModel() {
    //Trạm dọn rác, quản lý tất cả kết nối của Rx
    private val compositeDisposable = CompositeDisposable()
    //luồng đóng vai trò lưu trữ từ khóa tìm kiếm
    private val searchQuerySubject : BehaviorSubject<String> = BehaviorSubject.createDefault("")

    //Luồng dữ liệu chính để Jetpack Compose lắng nghe
    val contacts : Observable<List<Contact>> = searchQuerySubject
        .debounce(300, TimeUnit.MILLISECONDS)
        //bỏ qua nếu từ khóa mới giống hệt từ cữ
        .distinctUntilChanged()
        .switchMap { query ->
            //chuyển từ khóa thành luồng dữ liệu danh sách liên hệ từ db
            if(query.isBlank()){
                repository.getAllContacts()
            }
            else repository.searchContacts(query)
        }
        //Lấy dữ liệu dưới background thread
        .subscribeOn(Schedulers.io())
        //Đẩy kq lên UI thread
        .observeOn(AndroidSchedulers.mainThread())

    //Hàm nhận sự kiện gõ phím từ ô tìm kiếm
    fun onSearchQueryChanged(query: String){
        searchQuerySubject.onNext(query)
    }
    // các thao tác chỉnh sửa chèn Log để kiểm tra
    //Thêm liên hệ mới
    fun addContact(name: String, phoneNumber : String){
        if(name.isBlank() || phoneNumber.isBlank()) return

        val newContact = Contact(name = name, phoneNumber = phoneNumber)
        val disposable = repository.insertContact(newContact)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.d("ContactViewModel","Thêm thành công!")},
                {error -> Log.e("ContactViewModel","Lỗi khi thêm ${error.message}")}
            )
        compositeDisposable.add(disposable)
    }

    //Xóa liên hệ
    fun deleteContact(contact: Contact){
        val disposable = repository.deleteContact(contact)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {Log.d("ContactViewModel", "Xóa thành công!")},
                {error -> Log.e("ContactViewModel", "Lỗi khi xóa ${error.message}")}
            )
        compositeDisposable.add(disposable)
    }

    //Sửa liên hệ
    fun updateContact(contact: Contact){
        if(contact.name.isBlank() || contact.phoneNumber.isBlank()) return
        val disposable = repository.updateContact(contact)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {Log.d("ContactViewModel","Cập nhật thành công!")},
                {error -> Log.e("ContactViewModel", "Lỗi khi cập nhật ${error.message}")}
            )
        compositeDisposable.add(disposable)
    }

    //Hủy tất cả luồng Rx khi ViewModel bị hủy để tránh ML
    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}