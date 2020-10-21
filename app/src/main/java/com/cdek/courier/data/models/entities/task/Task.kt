package com.cdek.courier.data.models.entities.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity(tableName = "task")
@TypeConverters(TaskConverter::class)
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @field:SerializedName("date") var date: String?,
    @field:SerializedName("courierId") var courierId: String?,
    @field:SerializedName("courierCardUUID") var courierCardUUID: String?,
    @field:SerializedName("basisUUID") var basisUUID: String?,
    @field:SerializedName("basisNumber") var basisNumber: String?,
    @field:SerializedName("isDeleted") var isDeleted: Boolean?,
    @field:SerializedName("timestamp") var timestamp: Long?,
    @field:SerializedName("taskType") var taskType: String?,
    @field:SerializedName("taskState") var taskState: String?,
    @field:SerializedName("docNumber") var docNumber: String?,
    @field:SerializedName("numberIm") var numberIm: String?,
    @field:SerializedName("reverseFirstId") var reverseFirstId: String?,
    @field:SerializedName("notes") var notes: List<Note>? = null,
    @field:SerializedName("senderName") var senderName: String?,
    @field:SerializedName("senderPhones") var senderPhones: List<Phone>?,
    @field:SerializedName("senderContactFace") var senderContactFace: String?,
    @field:SerializedName("senderAddress") var senderAddress: TaskAddress?,
    @field:SerializedName("senderCityCode") var senderCityCode: String?,
    @field:SerializedName("timeMin") var timeMin: String?,
    @field:SerializedName("timeMax") var timeMax: String?,
    @field:SerializedName("previousTimeMin") var previousTimeMin: String?,
    @field:SerializedName("previousTimeMax") var previousTimeMax: String?,
    @field:SerializedName("lunchTimeFrom") var lunchTimeFrom: String?,
    @field:SerializedName("lunchTimeTo") var lunchTimeTo: String?,
    @field:SerializedName("receiverPhones") var receiverPhones: List<Phone>?,
    @field:SerializedName("receiverContactFace") var receiverContactFace: String?,
    @field:SerializedName("receiver") var receiver: String?,
    @field:SerializedName("receiverAddress") var receiverAddress: TaskAddress?,
    @field:SerializedName("receiverCityCode") var receiverCityCode: String?,
    @field:SerializedName("cityReceiver") var cityReceiver: String?,
    @field:SerializedName("targetAddress") var targetAddress: String?,
    @field:SerializedName("tariffName") var tariffName: String?,
    @field:SerializedName("sumToPay") var sumToPay: String?,
    @field:SerializedName("deliveryCost") var deliveryCost: String?,
    @field:SerializedName("additionalServices") var additionalServices: List<AdditionalService>?,
    @field:SerializedName("needAttorney") var needAttorney: Boolean?,
    @field:SerializedName("isPayDone") var isPayDone: Boolean?,
    @field:SerializedName("mayCardPay") var mayCardPay: Boolean?,
    @field:SerializedName("mayCashPay") var mayCashPay: Boolean?,

    @field:SerializedName("urgency") var urgency: Boolean?,
    @field:SerializedName("isRequireCheckPassport") var isRequireCheckPassport: Boolean?,
    @field:SerializedName("enableElectronicSignature") var enableElectronicSignature: Boolean?,
    @field:SerializedName("canPartDelivery") var canPartDelivery: Boolean?,

    @field:SerializedName("returnTimeWarehouse") var returnTimeWarehouse: String?,
    @field:SerializedName("ciPickUpTime") var ciPickUpTime: String?,

//    @field:SerializedName("cargo") var cargo: Cargo? = null
    @field:SerializedName("places") var places: List<Place>?,
    var notification: Int?
) : Serializable