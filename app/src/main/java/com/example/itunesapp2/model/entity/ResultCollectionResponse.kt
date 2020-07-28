package com.example.itunesapp2.model.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ResultCollectionResponse(
    @SerializedName("results")  val results: List<CollectionResponse>
) {
    data class CollectionResponse (
        @SerializedName("collectionId")
        //Id музыкального альбома
        val collectionId: String,

        @SerializedName("collectionName")
        //Название музыкального альбома
        val collectionName: String,

        @SerializedName("artistName")
        //Имя исполнителя
        val artistName: String,

        @SerializedName("releaseDate")
        //Дата релиза альбома
        val releaseDate: String,

        @SerializedName("artworkUrl100")
        //Ссылка на картинку альбома
        val artworkUrl100: String,

        @SerializedName("primaryGenreName")
        //Жанр музыки
        val primaryGenreName: String,

        @SerializedName("trackCount")
        //Количество музыкальных трэков
        val trackCount: String,

        @SerializedName("collectionPrice")
        //Стоимость музыкального альбома
        val collectionPrice: String,

        @SerializedName("currency")
        //Валюта стоимости
        val currency: String
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: ""
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(collectionId)
            parcel.writeString(collectionName)
            parcel.writeString(artistName)
            parcel.writeString(releaseDate)
            parcel.writeString(artworkUrl100)
            parcel.writeString(primaryGenreName)
            parcel.writeString(trackCount)
            parcel.writeString(collectionPrice)
            parcel.writeString(currency)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<CollectionResponse> {
            override fun createFromParcel(parcel: Parcel): CollectionResponse {
                return CollectionResponse(parcel)
            }

            override fun newArray(size: Int): Array<CollectionResponse?> {
                return arrayOfNulls(size)
            }
        }

        fun getCollectionYear(): String {
            val cuttingIndex: Int = releaseDate.indexOf('-')

            return if(cuttingIndex != -1) releaseDate.substring(0, cuttingIndex)
            else releaseDate
        }
    }
}