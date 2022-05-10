package com.adl.tugascomposeitem.repo

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

sealed class ProductResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?):ProductResponse()
data class OnFailure(val exception: FirebaseFirestoreException?):ProductResponse()
