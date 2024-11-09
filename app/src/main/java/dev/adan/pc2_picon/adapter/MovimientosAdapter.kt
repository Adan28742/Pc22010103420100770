package dev.adan.pc2_picon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.adan.pc2_picon.R
import dev.adan.pc2_picon.modell.MovimientoModel

class MovimientosAdapter (private var lstMovimientos: List<MovimientoModel>):
    RecyclerView.Adapter<MovimientosAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)
        val tvMonto = itemView.findViewById<TextView>(R.id.tvMonto)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_movimiento, parent, false))
    }

    override fun getItemCount(): Int {
        return  lstMovimientos.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemProduct = lstMovimientos[position]
        holder.tvFecha.text = itemProduct.fecha
        holder.tvMonto.text = itemProduct.monto.toString()

    }
}
