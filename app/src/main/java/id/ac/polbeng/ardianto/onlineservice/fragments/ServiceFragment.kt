package id.ac.polbeng.ardianto.onlineservice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import id.ac.polbeng.ardianto.onlineservice.databinding.FragmentServiceBinding
import id.ac.polbeng.ardianto.onlineservice.viewmodel.UserServiceViewModel

class ServiceFragment : Fragment() {
    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val serviceViewModel =
            ViewModelProvider(this).get(UserServiceViewModel::class.java)
        _binding = FragmentServiceBinding.inflate(inflater, container,
            false)
        val root: View = binding.root
        serviceViewModel.text.observe(viewLifecycleOwner) {
            binding.textService.text = it
        }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}