package com.arbolesyazilim.zikirmatik

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.arbolesyazilim.zikirmatik.databinding.FragmentDailyZikirBinding
import com.arbolesyazilim.zikirmatik.databinding.FragmentMainBinding
import com.arbolesyazilim.zikirmatik.databinding.FragmentZikirBinding


class DailyZikirFragment : Fragment() {
    private var _binding: FragmentDailyZikirBinding? = null
    private val binding get() = _binding!!

    private var zikir = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyZikirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.buton2.visibility = View.INVISIBLE
        binding.buton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Zikir1 butonuna dokunulduğunda

                    zikir++
                    binding.textView.text = zikir.toString()
                    binding.buton2.visibility = View.VISIBLE
                    binding.buton.visibility = View.INVISIBLE
                    if (zikir == 1){
                        binding.textView2.text = "Sübhanallah"
                    }

                    else if (zikir == 33) {
                        vibrateDevice()
                        binding.textView2.text = "Elhamdülillah"
                    }
                    else if(zikir == 66){
                        vibrateDevice()
                        binding.textView2.text = "Allahü Ekber"
                    }
                    else if(zikir == 99){
                        vibrateDevice()
                        binding.textView2.text = "Lâ ilâhe illâllahu vahdehû lâ şerike leh. Lehu`l-mulku ve lehu`l-hamdu ve huve alâ kulli şey`in kadîr. "
                    }
                    else if(zikir == 100){
                        zikir = 0
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // El çekildiğinde
                    binding.buton.visibility = View.VISIBLE
                    binding.buton2.visibility = View.INVISIBLE
                }
            }
            true
        }

    }
    private fun vibrateDevice() {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_zikir -> {
                val action  = DailyZikirFragmentDirections.actionDailyZikirFragmentToZikirAddFragment()
                findNavController().navigate(action)
                true
            }
            R.id.zikirler -> {
                val action = DailyZikirFragmentDirections.actionDailyZikirFragmentToMainFragment()
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}