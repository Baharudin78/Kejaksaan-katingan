package com.baharudin.pengaduanapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baharudin.pengaduanapp.domain.common.BaseResult
import com.baharudin.pengaduanapp.domain.usecase.ReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportUseCase : ReportUseCase
) : ViewModel(){

    private val _state = MutableStateFlow<PostReportViewState>(PostReportViewState.Init)
    val state : StateFlow<PostReportViewState> get() = _state

    private fun setLoading(){
        _state.value = PostReportViewState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = PostReportViewState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = PostReportViewState.ShowToast(message)
    }

    private fun successCreate(){
        _state.value = PostReportViewState.SuccessCreate
    }

    fun uploadReport(
        param : HashMap<String, @JvmSuppressWildcards RequestBody>,
        partFile : MultipartBody.Part?,
    ){
        viewModelScope.launch {
            reportUseCase.invoke(param, partFile)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect{ result ->
                    when(result) {
                        is BaseResult.Success -> {
                            hideLoading()
                            successCreate()
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }

}
sealed class PostReportViewState{
    object Init : PostReportViewState()
    object SuccessCreate : PostReportViewState()
    data class IsLoading(val isLoading : Boolean) : PostReportViewState()
    data class ShowToast(val message : String) : PostReportViewState()
}