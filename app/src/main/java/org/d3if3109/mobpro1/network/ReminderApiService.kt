package org.d3if3109.mobpro1.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/" +
        "senoajisr/remind-future-me/static-api/welcome.json"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ReminderApiService {
    @GET("welcome.json")
    suspend fun getReminder(): String
}
object ReminderApi {
    val service: ReminderApiService by lazy {
        retrofit.create(ReminderApiService::class.java)
    }
}