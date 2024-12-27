package id.ac.polbeng.ardianto.onlineservice.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.ac.polbeng.ardianto.onlineservice.activities.AddJasaActivity
import id.ac.polbeng.ardianto.onlineservice.adapters.JasaAdapter
import id.ac.polbeng.ardianto.onlineservice.databinding.FragmentServiceBinding
import id.ac.polbeng.ardianto.onlineservice.helpers.SessionHandler
import id.ac.polbeng.ardianto.onlineservice.models.Jasa
import id.ac.polbeng.ardianto.onlineservice.models.JasaResponse
import id.ac.polbeng.ardianto.onlineservice.services.JasaService
import id.ac.polbeng.ardianto.onlineservice.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceFragment : Fragment() {
    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var session: SessionHandler
    private lateinit var jasaAdapter: JasaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        session = SessionHandler(requireContext())
        jasaAdapter = JasaAdapter()
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadService()

        // Listener untuk tombol Floating Action Button
        binding.fabAddJasa.setOnClickListener {
            val intent = Intent(context, AddJasaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.rvData.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = jasaAdapter
        }
    }

    private fun loadService() {
        val userId = session.getUserId()
        if (userId == null) {
            showError("User ID tidak ditemukan.")
            return
        }

        Log.d("ServiceFragment", "User ID: $userId")
        val jasaService: JasaService = ServiceBuilder.buildService(JasaService::class.java)
        val requestCall: Call<JasaResponse> = jasaService.getJasaUser(userId)

        showLoading(true)

        requestCall.enqueue(object : Callback<JasaResponse> {
            override fun onResponse(call: Call<JasaResponse>, response: Response<JasaResponse>) {
                showLoading(false)
                if (response.isSuccessful && response.body() != null) {
                    val jasaResponse = response.body()!!
                    if (!jasaResponse.error) {
                        val daftarJasa = jasaResponse.data
                        Log.d("ServiceFragment", "Received data: $daftarJasa")
                        jasaAdapter.setData(daftarJasa)
                        setupItemClickCallback()
                    } else {
                        showError("Gagal menampilkan data jasa: ${jasaResponse.message}")
                    }
                } else {
                    showError("Response error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JasaResponse>, t: Throwable) {
                showLoading(false)
                showError("Terjadi kesalahan: ${t.message}")
            }
        })
    }

    private fun setupItemClickCallback() {
        jasaAdapter.setOnItemClickCallback(object : JasaAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Jasa) {
                Toast.makeText(context, "Service clicked: ${data.namaJasa}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        Log.e("ServiceFragment", message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
