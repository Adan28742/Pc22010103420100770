package dev.adan.pc2_picon.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import dev.adan.pc2_picon.R
import dev.adan.pc2_picon.adapter.MovimientosAdapter
import dev.adan.pc2_picon.databinding.FragmentGalleryBinding
import dev.adan.pc2_picon.modell.MovimientoModel

class GalleryFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_gallery, container, false)
        val db = FirebaseFirestore.getInstance()
        val rvProduct: RecyclerView = view.findViewById(R.id.rvMovimientos)

        db.collection("Movimientos")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return@addSnapshotListener
                }

                // Crear un mapa para almacenar la suma de montos por fecha
                val montosPorFecha = mutableMapOf<String, Double>()

                // Procesar los documentos obtenidos de Firestore
                value?.documents?.forEach { document ->
                    val fecha = document["fecha"].toString()
                    val monto = document["monto"].toString().toDoubleOrNull() ?: 0.0

                    // Acumular el monto en la fecha correspondiente
                    montosPorFecha[fecha] = montosPorFecha.getOrDefault(fecha, 0.0) + monto
                }

                // Convertir el mapa en una lista de MovimientoModel para el adaptador
                val lstMovimientos = montosPorFecha.map { (fecha, monto) ->
                    MovimientoModel(fecha = fecha, monto = monto)
                }

                // Configurar el adaptador con la lista agrupada por fecha
                rvProduct.adapter = MovimientosAdapter(lstMovimientos)
                rvProduct.layoutManager = LinearLayoutManager(requireContext())
            }

        return view
    }
}