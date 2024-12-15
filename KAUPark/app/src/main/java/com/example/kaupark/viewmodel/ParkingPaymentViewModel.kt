package com.example.kaupark.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ParkingPaymentViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _deposit = MutableLiveData<Int>()
    val deposit: LiveData<Int> get() = _deposit

    private val _paymentResult = MutableLiveData<String>()
    val paymentResult: LiveData<String> get() = _paymentResult

    private val userDocumentId = "PfANoIw3kBX8V2BmfgAsDz6Rf423"

    init {
        loadDeposit()
    }

    // 예치금 로드
    private fun loadDeposit() {
        firestore.collection("users")
            .document(userDocumentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.contains("deposit")) {
                    _deposit.value = document.getLong("deposit")?.toInt() ?: 0
                    Log.d("ParkingPayment", "Current deposit: ${_deposit.value}")
                } else {
                    Log.e("ParkingPayment", "No deposit field found.")
                }
            }
    }

    // 결제 처리
    fun processPayment(isRegularSelected: Boolean, isOnSiteSelected: Boolean, cost: Int?) {
        if (isRegularSelected) {
            if (_deposit.value != null && _deposit.value!! >= 60000) {
                val newDeposit = _deposit.value!! - 60000
                updateDeposit(newDeposit)
            } else {
                _paymentResult.value = "잔액이 부족합니다."
            }
        } else if (isOnSiteSelected && cost != null) {
            if (_deposit.value != null && _deposit.value!! >= cost) {
                val newDeposit = _deposit.value!! - cost
                updateDeposit(newDeposit)
            } else {
                _paymentResult.value = "잔액이 부족합니다."
            }
        } else {
            _paymentResult.value = "정기권 또는 현장 결제를 선택하세요."
        }
    }

    // 예치금 업데이트
    private fun updateDeposit(newDeposit: Int) {
        firestore.collection("users")
            .document(userDocumentId)
            .update("deposit", newDeposit)
            .addOnSuccessListener {
                _deposit.value = newDeposit
                _paymentResult.value = "결제가 완료되었습니다."
            }
    }

    // 현장 요금 로드
    fun loadDuration(onDurationLoaded: (Int) -> Unit) {
        firestore.collection("users")
            .document(userDocumentId)
            .collection("parking_records")
            .document("duration")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.contains("duration")) {
                    val duration = document.getLong("duration")?.toInt() ?: 0
                    Log.d("ParkingPayment", "Duration: $duration")
                    onDurationLoaded(duration)
                }
            }
    }
}
