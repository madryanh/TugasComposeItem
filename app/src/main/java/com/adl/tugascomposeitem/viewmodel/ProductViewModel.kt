package com.adl.tugascomposeitem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adl.tugascomposeitem.repo.ProductRepo
import com.adl.tugascomposeitem.repo.ProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductViewModel(val productRepo: ProductRepo):ViewModel() {

    val productStateFlow = MutableStateFlow<ProductResponse?>(null)

    init{
        viewModelScope.launch {
            productRepo.getProductDetails().collect(){
                productStateFlow.value = it
            }
        }
    }

    fun getBookInfo() = productRepo.getProductDetails()

}