package com.example.tfgtxurdinaga

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class EventosAdapter(private val context: Context, private val eventos: List<Evento>) : BaseAdapter() {

    override fun getCount(): Int {
        return eventos.size
    }

    override fun getItem(position: Int): Any {
        return eventos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val evento = getItem(position) as Evento

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tarjetaEvento = inflater.inflate(R.layout.tarjeta_evento, null)

        // Personaliza la tarjeta con la información del evento
        val textViewTitulo = tarjetaEvento.findViewById<TextView>(R.id.textViewTitulo)
        textViewTitulo.text = evento.titulo

        // Puedes personalizar otros elementos de la tarjeta según sea necesario

        return tarjetaEvento
    }
}
