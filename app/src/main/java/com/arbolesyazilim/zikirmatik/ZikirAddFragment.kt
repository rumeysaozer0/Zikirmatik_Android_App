package com.arbolesyazilim.zikirmatik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arbolesyazilim.zikirmatik.databinding.FragmentZikirAddBinding


class ZikirAddFragment : Fragment() {
    private var _binding: FragmentZikirAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentZikirAddBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = DatabaseHelper(requireContext())

        binding.save.setOnClickListener {
            val nameValue = binding.name.text.toString()
            val numberValue = binding.number.text.toString().toIntOrNull()

            if (nameValue.isNotBlank() && numberValue != null) {
                databaseHelper.insertData(nameValue, numberValue)
                val action = ZikirAddFragmentDirections.actionZikirAddFragmentToMainFragment()
                findNavController().navigate(action)
            }

        }
    }

    override fun onDestroy() {
        databaseHelper.close()
        super.onDestroy()
    }
}
