package com.example.tfgtxurdinaga

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class adapter(context: Context, resource: Int, objects: List<entity>) :
    ArrayAdapter<entity>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.nota_layout, parent, false)

        val notas = getItem(position)

        val textViewTitulo: TextView = itemView.findViewById(R.id.titleTextView)
        val textViewDescripcion: TextView = itemView.findViewById(R.id.descriptionTextView)
        val textViewFecha: TextView = itemView.findViewById(R.id.fechaTextView)
        val textViewHora: TextView = itemView.findViewById(R.id.horaTextView)

        notas?.let {
            textViewTitulo.text = it.titulo
            textViewDescripcion.text = it.descripcion
            textViewFecha.text = "Fecha: ${it.fecha}"
            textViewHora.text = "Hora: ${it.hora}"
        }

        return itemView
    }
}