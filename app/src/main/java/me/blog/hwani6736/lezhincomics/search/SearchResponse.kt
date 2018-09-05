package me.blog.hwani6736.lezhincomics.search

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by NarZa on 2018. 9. 5..
 */
data class SearchResponse(val metaData: Meta, val documents: ArrayList<Document>) {

    companion object {
        fun parseJson(jsonObject: JsonObject): SearchResponse {


            val gson = Gson()
            val metaData: Meta = gson.fromJson<Meta>(jsonObject.getAsJsonObject("meta"), Meta::class.java)

            val listType = object : TypeToken<List<Document>>() {}.type
            val documents: ArrayList<Document> = gson.fromJson<ArrayList<Document>>(jsonObject.getAsJsonArray("documents").toString(), listType)

            return SearchResponse(metaData, documents)
        }
    }

    data class Meta(
            @SerializedName("is_end") var isEnd: Boolean = false,
            @SerializedName("total_count") var totalCount: Int = 0,
            @SerializedName("pageable_count") var pageableCount: Int = 0
    ) : Parcelable {

        private constructor(inParcel: Parcel):this(
                inParcel.readInt() == 1,
                inParcel.readInt(),
                inParcel.readInt()
        )

        override fun describeContents() = 0

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeInt(if (isEnd) 1 else 0)
            parcel.writeInt(totalCount)
            parcel.writeInt(pageableCount)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<Meta> = object : Parcelable.Creator<Meta> {
                override fun createFromParcel(parcel: Parcel): Meta? {
                    return Meta(parcel)
                }

                override fun newArray(i: Int): Array<Meta?> {
                    return arrayOfNulls(i)
                }
            }
        }
    }

    data class Document(
            var collection: String? = null,
            var width: Int = 0,
            var height: Int = 0,
            @SerializedName("thumbnail_url") var thumbnailUrl: String,
            @SerializedName("image_url") var imageUrl: String,
            @SerializedName("display_sitename") var displaySiteName: String,
            @SerializedName("doc_url") var docUrl: String
    ) : Parcelable {

        constructor(inParcel: Parcel):this (
                inParcel.readString(),
                inParcel.readInt(),
                inParcel.readInt(),
                inParcel.readString(),
                inParcel.readString(),
                inParcel.readString(),
                inParcel.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(parcel: Parcel, i: Int) {
            parcel.writeString(collection)
            parcel.writeInt(width)
            parcel.writeInt(height)
            parcel.writeString(thumbnailUrl)
            parcel.writeString(imageUrl)
            parcel.writeString(displaySiteName)
            parcel.writeString(docUrl)
        }

        companion object {

            @JvmField
            val CREATOR: Parcelable.Creator<Document> = object : Parcelable.Creator<Document> {
                override fun createFromParcel(parcel: Parcel): Document? {
                    return Document(parcel)
                }

                override fun newArray(i: Int): Array<Document?> {
                    return arrayOfNulls(i)
                }
            }
        }
    }
}

