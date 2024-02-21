package com.example.tfgtxurdinaga

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addnota_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class addnota_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var hora: EditText
    private lateinit var fecha: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_addnota_fragment, container, false)

        val titulo = view.findViewById<EditText>(R.id.titulo)
        val descripcion = view.findViewById<EditText>(R.id.descripcion)
        val link = view.findViewById<EditText>(R.id.link)
        val email = view.findViewById<EditText>(R.id.email)
        val telefono = view.findViewById<EditText>(R.id.telefono)

        hora = view.findViewById<EditText>(R.id.horas)
        fecha = view.findViewById<EditText>(R.id.fecha)


        val buttoncerrar = view.findViewById<ImageButton>(R.id.imageButtonX2)
        val buttoncheck = view.findViewById<ImageButton>(R.id.imageButtoCheckn2)

        buttoncerrar.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }



        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addnota_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                addnota_fragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
    fun mostrarCalendario(view: View) {
        val calendario = Calendar.getInstance()
        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val día = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Aquí puedes manejar la fecha seleccionada
                val fechaSeleccionada = "$dayOfMonth/${monthOfYear + 1}/$year"
                fecha.setText(fechaSeleccionada)
            },
            año,
            mes,
            día
        )

        datePickerDialog.show()
    }
    private fun mostrarTimePickerDialog() {
        val calendario = Calendar.getInstance()
        val horaActual = calendario.get(Calendar.HOUR_OF_DAY)
        val minutoActual = calendario.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, horaSeleccionada, minutoSeleccionado ->
                // Formatear la hora seleccionada a "HH:mm"
                val horaFormateada = String.format("%02d:%02d", horaSeleccionada, minutoSeleccionado)
                // Establecer el texto en el EditText
                hora.setText(horaFormateada)
            },
            horaActual,
            minutoActual,
            true
        )

        timePickerDialog.show()
    }
}
