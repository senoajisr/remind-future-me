package org.d3if3109.mobpro1.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3109.mobpro1.model.Reminder
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/" +
        "senoajisr/remind-future-me/static-api/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ReminderApiService {
    @GET("welcome.json")
    suspend fun getReminder(): Reminder
}
object ReminderApi {
    val service: ReminderApiService by lazy {
        retrofit.create(ReminderApiService::class.java)
    }
}