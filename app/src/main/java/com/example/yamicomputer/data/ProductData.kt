package com.example.yamicomputer.data

import androidx.annotation.Keep

@Keep
data class ProductData(
    var name: String = "",
    var date: String = "",
    var product: String = "",
    var description: String = "",
    var price: String = "",
    var photo: String = "",
    var status: String = "",
    var productID: String = "",
    var mobileNo: String = "",
    var collectorName: String = "",
    var collectionDate: String = "",
    var dealerName: String = "",
)

//enum class ComplaintStatus {
//    ONGOING,
//    COMPLETE,
//    PENDING,
//    NOTHING,
//    OUTWARDS
//}

enum class ProductStatus {
    BUY,
    SELL,
    NEW_SELL,
    NOTHING
}

//fun String.stringToComplaintStatus(): ComplaintStatus {
//    return when (this) {
//        ComplaintStatus.ONGOING.name -> ComplaintStatus.ONGOING
//        ComplaintStatus.COMPLETE.name -> ComplaintStatus.COMPLETE
//        ComplaintStatus.PENDING.name -> ComplaintStatus.PENDING
//        ComplaintStatus.NOTHING.name -> ComplaintStatus.NOTHING
//        ComplaintStatus.OUTWARDS.name -> ComplaintStatus.OUTWARDS
//        else -> {ComplaintStatus.NOTHING}
//    }
//}

fun String.stringToProductStatus(): ProductStatus {
    return when (this) {
        ProductStatus.BUY.name -> ProductStatus.BUY
        ProductStatus.SELL.name -> ProductStatus.SELL
        ProductStatus.NEW_SELL.name -> ProductStatus.NEW_SELL
        ProductStatus.NOTHING.name -> ProductStatus.NOTHING
        else -> {
            ProductStatus.BUY
        }
    }
}

enum class ProductActions {

    CREATE,
    UPDATE,

}
