package dev.adan.pc2_picon.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.adan.pc2_picon.databinding.FragmentHomeBinding
import dev.adan.pc2_picon.model.MovModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.btSave.setOnClickListener {
            saveMovimiento()
        }

        return root
    }

    private fun saveMovimiento() {
        val descripcion = binding.etDescripcion.text.toString()
        val montoText = binding.etMonto.text.toString()
        val fechaText = binding.etFecha.text.toString()

        if (descripcion.isEmpty() || montoText.isEmpty() || fechaText.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val monto = montoText.toDoubleOrNull()
        if (monto == null) {
            Toast.makeText(requireContext(), "Monto debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fecha: Date? = dateFormat.parse(fechaText)
        if (fecha == null) {
            Toast.makeText(requireContext(), "Fecha debe estar en formato yyyy-MM-dd", Toast.LENGTH_SHORT).show()
            return
        }

        val movimiento = MovModel(
            descripcion = descripcion,
            fecha = fecha,
            monto = monto
        )

        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId).collection("Movimientos")
            .add(movimiento)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Movimiento guardado exitosamente", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        binding.etDescripcion.text.clear()
        binding.etMonto.text.clear()
        binding.etFecha.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
