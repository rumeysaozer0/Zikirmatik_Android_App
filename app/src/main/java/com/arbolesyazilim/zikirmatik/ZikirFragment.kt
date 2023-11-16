package com.arbolesyazilim.zikirmatik
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.arbolesyazilim.zikirmatik.databinding.FragmentZikirBinding

class ZikirFragment : Fragment() {
    private var _binding: FragmentZikirBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private var zikir = 0
    private var zikirKey: String = ""
    private lateinit var databaseHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = ZikirFragmentArgs.fromBundle(requireArguments())
        val zikirName = args.zikirName
        // Her zikir için benzersiz bir anahtar oluştur
        zikirKey = "zikir_${zikirName}"

        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        // Kayıtlı zikir sayısını yükle, yoksa varsayılan değeri kullan
        zikir = sharedPreferences.getInt(zikirKey, 0)
        databaseHelper = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentZikirBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true) // Fragment üzerinde menü oluşturulacaksa bu metodu çağırın
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ZikirFragmentArgs.fromBundle(requireArguments())
        val zikirName = args.zikirName
        val zikirNumber = args.zikirNumber
        binding.zikirName.text = zikirName
        binding.zikirNumber.text = zikirNumber.toString()

        // Kayıtlı zikir sayısını güncelle
        binding.number.text = zikir.toString()
        binding.buton2.visibility = View.INVISIBLE
        binding.buton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Zikir1 butonuna dokunulduğunda
                    zikir++
                    binding.number.text = zikir.toString()
                    binding.buton2.visibility = View.VISIBLE
                    binding.buton.visibility = View.INVISIBLE
                    if (zikir == zikirNumber) {
                        vibrateDevice()
                        showAlertDialog()
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

        binding.reset.setOnClickListener {
            showResetConfirmationDialog()
        }
    }

    override fun onStop() {
        super.onStop()
        // Zikir sayısını SharedPreferences'e kaydet
        with(sharedPreferences.edit()) {
            putInt(zikirKey, zikir)
            apply()
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

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Hedef Zikir Sayısına Ulaşıldı")
        alertDialogBuilder.setMessage("Zikir sayısına ulaştınız.")
        alertDialogBuilder.setPositiveButton("Tamam") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showResetConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Sıfırlamak İstediğinize Emin Misiniz?")
        alertDialogBuilder.setNegativeButton("Hayır") { dialogInterface, _ ->
            // Hayır seçeneği seçildiğinde yapılacak işlemler
            dialogInterface.dismiss()
        }

        alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
            // Evet seçeneği seçildiğinde yapılacak işlemler
            // Örneğin: Zikir sayısını sıfırlama
            zikir = 0
            binding.number.text = zikir.toString()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.zikir_menu, menu) // Menu dosyanızın adını ve yerini ayarlayın
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Veriyi Silmek İstediğinize Emin Misiniz?")
        alertDialogBuilder.setNegativeButton("Hayır") { dialogInterface, _ ->
            // Hayır seçeneği seçildiğinde yapılacak işlemler
            dialogInterface.dismiss()
        }

        alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
            // Evet seçeneği seçildiğinde yapılacak işlemler
            // Örneğin: Veriyi veritabanından silme
            deleteZikirDataFromDatabase()

            // Silme işleminden sonra fragment'i kapatabilirsiniz
            findNavController().popBackStack()
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun deleteZikirDataFromDatabase() {
        val args = ZikirFragmentArgs.fromBundle(requireArguments())
        val zikirName = args.zikirName
        databaseHelper.deleteZikirData(zikirName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
