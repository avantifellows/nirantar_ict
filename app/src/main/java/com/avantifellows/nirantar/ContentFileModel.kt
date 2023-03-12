package com.avantifellows.nirantar

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


data class ContentFile(

    @PropertyName("Grade")
    @SerializedName("Grade")
    var grade: Int = 0,

    @SerializedName("Description")
    @PropertyName("Description")
    var description: String = "",

    @SerializedName("Title")
    @PropertyName("Title")
    var title: String = "",

    @SerializedName("Subtitle")
    @PropertyName("Subtitle")
    var subtitle: String = "",

    @SerializedName("Link")
    @PropertyName("Link")
    var link: String = ""
)

const val BASE_URL = APPS_SCRIPT_URL

interface APIService {
    @GET(APPS_SCRIPT_PATH)
    suspend fun getFileList(): List<ContentFile>

    companion object {
        var apiService: APIService? = null
        fun getInstance(): APIService {
            if (apiService == null) {

                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(APIService::class.java)
            }
            return apiService!!
        }
    }
}

suspend fun getFirestoreFileList(): List<ContentFile> {
    val firestore = FirebaseFirestore.getInstance()
    val contentFileList : MutableList<ContentFile> = mutableListOf()
    val querySnapshot = firestore.collection("NirantarICTLessonData").get().await()

    for (document in querySnapshot) {
        val contentFile = document.toObject(ContentFile::class.java)
        Log.d(TAG, document.id + " => ${contentFile.title}")
        contentFileList.add(contentFile)

    }

    return contentFileList
}

