package com.avantifellows.nirantar

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


data class ContentFile(
    @SerializedName("Grade")
    var grade: Int,

    @SerializedName("Description")
    var description: String,

    @SerializedName("Title")
    var title: String,

    @SerializedName("Subtitle")
    var subtitle: String,

    @SerializedName("Link")
    var link: String
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
