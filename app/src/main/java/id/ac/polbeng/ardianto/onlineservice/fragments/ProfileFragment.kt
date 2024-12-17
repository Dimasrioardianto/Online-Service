package id.ac.polbeng.ardianto.onlineservice.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.ac.polbeng.ardianto.onlineservice.R
import id.ac.polbeng.ardianto.onlineservice.activities.EditProfileActivity
import id.ac.polbeng.ardianto.onlineservice.activities.LoginActivity
import id.ac.polbeng.ardianto.onlineservice.databinding.FragmentProfileBinding
import id.ac.polbeng.ardianto.onlineservice.helpers.Config
import id.ac.polbeng.ardianto.onlineservice.helpers.SessionHandler
import id.ac.polbeng.ardianto.onlineservice.models.DefaultResponse
import id.ac.polbeng.ardianto.onlineservice.models.User
import id.ac.polbeng.ardianto.onlineservice.services.ServiceBuilder
import id.ac.polbeng.ardianto.onlineservice.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var session: SessionHandler
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        session = SessionHandler(requireContext())
        user = session.getUser()

        setupProfileData()
        setupListeners()

        return binding.root
    }

    private fun setupProfileData() {
        user?.let {
            val url = Config.PROFILE_IMAGE_URL + it.gambar
            Glide.with(requireContext())
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                )
                .into(binding.imgLogo)

            val titikDua = ": "
            binding.tvNama.text = titikDua + it.nama
            binding.tvTanggalLahir.text = titikDua + it.tanggalLahir
            binding.tvJenisKelamin.text = titikDua + it.jenisKelamin
            binding.tvNomorHP.text = titikDua + it.nomorHP
            binding.tvAlamat.text = titikDua + it.alamat
            binding.tvEmail.text = titikDua + it.email
            binding.tvWaktuSesi.text = titikDua + session.getExpiredTime()
        } ?: run {
            Toast.makeText(context, "Data user tidak ditemukan.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        binding.btnEditProfil.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }

        binding.btnHapusUser.setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Akun")
            .setMessage("Apakah anda yakin menghapus akun? Anda tidak akan bisa lagi login ke akun ini.")
            .setIcon(R.drawable.baseline_delete_forever_24)
            .setPositiveButton("Ya") { dialog, _ ->
                user?.id?.let { userId ->
                    deleteUser(userId)
                } ?: run {
                    Toast.makeText(context, "ID user tidak ditemukan.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun deleteUser(userId: Int) {
        val userService = ServiceBuilder.buildService(UserService::class.java)
        val requestCall = userService.deleteUser(userId)

        showLoading(true)
        requestCall.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                showLoading(false)
                if (response.isSuccessful && response.body()?.error == false) {
                    response.body()?.let {
                        session.removeUser()
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        navigateToLogin()
                    }
                } else {
                    Toast.makeText(context, "Gagal menghapus user: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(context, "Error terjadi ketika menghapus user: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun navigateToLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
