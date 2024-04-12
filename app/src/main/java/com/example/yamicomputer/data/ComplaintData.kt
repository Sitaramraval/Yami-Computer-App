package com.example.yamicomputer.data

import androidx.annotation.Keep

@Keep
data class ComplaintData(
    var name: String = "",
    var date: String = "",
    var item: String = "",
    var problem: String = "",
    var charge: String = "",
    var photo: String = "",
    var status: String = "",
    var complaintId: String = "",
    var mobileNo: String = "",
    var collectorName: String = "",
    var collectionDate: String = "",
    var dealerName: String = "",
)

enum class ComplaintStatus {
    ONGOING,
    COMPLETE,
    PENDING,
    NOTHING,
    OUTWARDS
}

enum class DealerNames {
    SITARAM,
    SUBHASH
}

val listOfItems = mutableListOf(
    "Laptop with Adapter",
    "Laptop with Adapter with Bag",
    "CPU",
    "LCD",
    "Printer",
    "Printer with Printer Cable",
    "Keyboard",
    "Mouse",
    "Hard Disk",
    "External Hard Disk",
)

fun String.stringToComplaintStatus(): ComplaintStatus {
    return when (this) {
        ComplaintStatus.ONGOING.name -> ComplaintStatus.ONGOING
        ComplaintStatus.COMPLETE.name -> ComplaintStatus.COMPLETE
        ComplaintStatus.PENDING.name -> ComplaintStatus.PENDING
        ComplaintStatus.NOTHING.name -> ComplaintStatus.NOTHING
        ComplaintStatus.OUTWARDS.name -> ComplaintStatus.OUTWARDS
        else -> {ComplaintStatus.NOTHING}
    }
}

fun String.stringToDealerName(): DealerNames {
    return when (this) {
        DealerNames.SITARAM.name -> DealerNames.SITARAM
        DealerNames.SUBHASH.name -> DealerNames.SUBHASH
        else -> {DealerNames.SITARAM}
    }
}
