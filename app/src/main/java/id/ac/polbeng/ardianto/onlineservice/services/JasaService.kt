package id.ac.polbeng.ardianto.onlineservice.services

import id.ac.polbeng.ardianto.onlineservice.models.JasaResponse

import retrofit2.Call
import retrofit2.http.GET
interface JasaService {
    @GET("services")
    fun getJasa() : Call<JasaResponse>
}