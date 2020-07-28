package com.example.itunesapp2.model.entity

import com.google.gson.annotations.SerializedName

data class ResultTrackResponse(
    @SerializedName("results") val results: List<TrackResponse>
) {
    data class TrackResponse(
        @SerializedName("wrapperType")
        val wrapperType: String,

        @SerializedName("trackName")
        //Название музыкального трэка
        val trackName: String,

        @SerializedName("previewUrl")
        //Ссылка на демо-версию трэка для прослушивания
        val previewUrl: String,

        @SerializedName("trackPrice")
        //Стоимость трэка
        val trackPrice: String,

        @SerializedName("currency")
        //Валюта стоимости
        val currency: String
    )
}