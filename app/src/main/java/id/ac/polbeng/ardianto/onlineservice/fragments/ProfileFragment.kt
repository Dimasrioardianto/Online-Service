package id.ac.polbeng.ardianto.onlineservice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import id.ac.polbeng.ardianto.onlineservice.databinding.FragmentProfileBinding
import id.ac.polbeng.ardianto.onlineservice.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentProfileBinding.inflate(inflater, container,
            false)
        val root: View = binding.root

        profileViewModel.text.observe(viewLifecycleOwner) {
            binding.textProfile.text = it
        }
        binding.textProfile.text = "Ini Profile Fragment"
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}