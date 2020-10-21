package com.cdek.courier.utils

//TODO rename all names
val SCAN_PVZ_PREF_NAME = "ScanPvzApp"


// Shared preferences keys
val SCAN_PVZ_PREF_KEY_SERVER = "ScanPvzServer"
val SCAN_PVZ_PREF_KEY_TOKEN = "ScanPvzToken"
val SCAN_PVZ_PREF_KEY_USER_LOGIN = "ScanPvzUserLogin"
val SCAN_PVZ_PREF_KEY_LANG = "ScanPvzLang"
val SCAN_PVZ_PREF_KEY_USERID = "ScanPvzUserId"
val SCAN_PVZ_PREF_KEY_FCM_TOKEN = "ScanPvzFCMToken"


// servers
val URL_PARALELS_SERV_QA2 = "https://xxx"
val SCAN_PVZ_SERVER_BASE = "https://xxx"
val SCAN_PVZ_SERVER_PRED_PROD = "https://xxx"
val SCAN_PVZ_SERVER_QA = "https://xxx"
val SCAN_PVZ_SERVER_QA2 = "https://xxx"
val SCAN_PVZ_SERVER_DEV = "https://xxx"
val SCAN_PVZ_SERVER_DEV2 = "http://xxx"
val SCAN_PVZ_SERVER_DEV_LOCAL = "http://xxx"

// server endpoints
val SERVER_ENDPOINT_GET_CATALOG_REASON = "android/get-catalog"
val SERVER_ENDPOINT_TOKEN = "auth/token"
val SERVER_ENDPOINT_NOTIFICATION = "androidV3/get-notification"
val SERVER_ENDPOINT_AUTOCOMPLETE = "android/autocomplete"
val SERVER_ENDPOINT_AUTH = "android/loginUser"
val SERVER_ENDPOINT_TASK = "androidV3/getTasks"

// REQUEST
val PERMISSIONS_REQUEST_CODE = 1234
val REQUEST_TAKE_SIGN = 104
val REQUEST_TAKE_PHOTO = 101

// Reasons reject
val CATALOG_TYPE_ORDER_DELIVERY_REJECT_TYPE = "ORDER_DELIVERY_REJECT_TYPE"

//detail delivery
val HANDED = 4
val NOT_HANDED = 5
val IN_PROGRESS = 0
val DEFAULT_STATE = 0

//for bundle and intent
val SEND_STRING_INTENT = "sendStringIntent"
val STATE_ERROR = "stateError"


val CREATE_DATE_TEMPLATE = "dd.MM.yyyy HH:mm:ss"
val CREATE_DATE_TEMPLATE_SHORT = "ddMMyyyy_HHmmss"
val STATE_READED_NOTIFICATION = 1

// Text param
val LOCALE_LANG = "RU"
val TASK_NUMBER = "taskNumber"
val BOOLEAN_TRUE = 1
val BOOLEAN_FALSE = 0
val MESSAGE_ATTACH_SCAN_OK = "Фото отправится автоматически в течение 15 минут"

//FCM
val FCM_KEY_GET_TASK = "updateTask"
val FCM_KEY_UPDATE_COURIER = "updateCourier"

// photo options
val INDEX_QUALITY = 85
val INDEX_SIZE = 0.8
val SIZE_IN_B = 1048576

//increment\decrement for partial delivery
val DECREMENT = -1
val INCREMENT = 1
val INCREASE = 5
val REDUCE = -5
val OPERATION_DECREMENT = "decrement"
val OPERATION_INCREMENT = "increment"
val OPERATION_INCREASE = "increase"
val OPERATION_REDUCE = "reduce"
val PARTIAL_ZERO_DELIVERY =
    "При частичной доставке нельзя передать пустое кол-во доставленных товаров"
val PARTIAL_ALL_DELIVERY = "Кол-во доставленных товаров не изменено"



