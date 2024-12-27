package id.ac.polbeng.ardianto.onlineservice.services

import id.ac.polbeng.ardianto.onlineservice.models.DefaultResponse
import id.ac.polbeng.ardianto.onlineservice.models.JasaResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface JasaService {
    @GET("services")
    fun getJasa(): Call<JasaResponse>

    @GET("userServices/{id}")
    fun getJasaUser(
        @Path("id") id: Int
    ): Call<JasaResponse>

    @Multipart
    @POST("services")
    fun addJasa(
        @Part image: MultipartBody.Part,
        @Part("id_user") idUser: RequestBody,
        @Part("nama_jasa") namaJasa: RequestBody,
        @Part("deskripsi_singkat") deskripsiSingkat: RequestBody,
        @Part("uraian_deskripsi") uraianDeskripsi: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("file") gambar: RequestBody
    ) : Call<DefaultResponse>
}