package com.kaaneneskpc.shopr.ui.feature.payment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kaaneneskpc.shopr.model.PaymentModel
import com.kaaneneskpc.shopr.model.PaymentResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentViewModel : ViewModel() {
    private val _paymentInfo = MutableStateFlow(PaymentModel())
    val paymentInfo = _paymentInfo.asStateFlow()
    
    private val _verificationCode = MutableStateFlow("")
    val verificationCode = _verificationCode.asStateFlow()
    
    val _paymentResult = MutableStateFlow<PaymentResult?>(null)
    val paymentResult = _paymentResult.asStateFlow()
    
    private val _userEnteredCode = MutableStateFlow("")
    val userEnteredCode = _userEnteredCode.asStateFlow()

    fun setPaymentInfo(paymentModel: PaymentModel) {
        _paymentInfo.value = paymentModel
    }
    
    fun updateVerificationCode(code: String) {
        _verificationCode.value = code
        Log.d("PaymentViewModel", "Generated verification code set: $code")
    }
    
    fun updateUserEnteredCode(code: String) {
        _userEnteredCode.value = code
        Log.d("PaymentViewModel", "User entered code: $code")
    }
    
    fun setPaymentResult(result: PaymentResult) {
        _paymentResult.value = result
        Log.d("PaymentViewModel", "Payment result set: ${result.isSuccess}, Message: ${result.message}")
    }
    
    fun processPayment(): PaymentResult {
        Log.d("PaymentViewModel", "Processing payment. Generated code: ${_verificationCode.value}, User entered: ${_userEnteredCode.value}")
        
        val result = PaymentResult(
            isSuccess = true,
            message = "Payment processed successfully!",
            transactionId = generateTransactionId()
        )
        
        _paymentResult.value = result
        return result
    }
    
    private fun generateTransactionId(): String {
        return "TX-" + (100000..999999).random().toString()
    }
} 