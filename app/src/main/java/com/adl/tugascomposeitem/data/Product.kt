package com.adl.tugascomposeitem.data

data class Product(val productName:String, val category:String, val description:String, val price:String, val fullDescription:String, val gambar:String, val button:String){
    constructor():this("","","","","","","")
}
