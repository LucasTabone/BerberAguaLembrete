package com.example.berberagua_lembreteapp

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.*
import com.example.berberagua_lembreteapp.model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var ic_redefinir_dados: ImageView
    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_resultado_ml: TextView
    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minutos: TextView

    private lateinit var calcularIngestaoDiaria : CalcularIngestaoDiaria
    private var resultadoMl = 0.0

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var Calendario: Calendar
    var horaAtual = 0
    var minutusAtuais = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()
        iniciarComponentes()
        calcularIngestaoDiaria = CalcularIngestaoDiaria()

        bt_calcular.setOnClickListener {
            if (edit_peso.text.toString().isEmpty()){
                Toast.makeText( this,R.string.toast_informe_peso, Toast.LENGTH_SHORT).show()
            }
            else if (edit_idade.text.toString().isEmpty()){
                Toast.makeText( this,R.string.toast_informe_idade, Toast.LENGTH_SHORT).show()
            }
            else {
                val peso = edit_peso.text.toString().toDouble()
                val idade = edit_idade.text.toString().toInt()
                calcularIngestaoDiaria.CalcularTotalML(peso,idade)
                resultadoMl = calcularIngestaoDiaria.ResultadoML()
                val formatar = NumberFormat.getNumberInstance(Locale("pt","br"))
                formatar.isGroupingUsed = false
                txt_resultado_ml.text = formatar.format(resultadoMl) + "" + "ml"
            }
        }

        ic_redefinir_dados.setOnClickListener {

            val alertDialog = AlertDialog.Builder (this)
            alertDialog.setTitle(R.string.dialog_titulo)
                . setMessage(R.string.dialog_desc)
                .setPositiveButton("ok" , {dialogInterface , i ->
                    edit_peso.setText("")
                    edit_idade.setText("")
                    txt_resultado_ml.setText("")
                })
           alertDialog.setNegativeButton("canclear",{dialogInterface, i ->
           })
            val dialog = alertDialog.create()
            dialog.show()
        }

        bt_lembrete.setOnClickListener {
            Calendario = Calendar.getInstance()
            horaAtual = Calendario.get(Calendar.HOUR_OF_DAY)
            minutusAtuais = Calendario.get(Calendar.MINUTE)
            timePickerDialog = TimePickerDialog(this,{ TimePicker : TimePicker, hourOfDay: Int, minutes: Int ->
                txt_hora.text = String.format("%02d",hourOfDay)
                txt_minutos.text = String.format("%02d",minutes)
            }, horaAtual,minutusAtuais,true)
            timePickerDialog.show()
        }

        bt_alarme.setOnClickListener {
            if (!txt_hora.text.toString().isEmpty() && !txt_minutos.text.toString().isEmpty()){
              val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, txt_hora.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES, txt_minutos.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.Alarme_msg))

                if (intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
            }
        }

    }
    private fun  iniciarComponentes(){
        ic_redefinir_dados = findViewById(R.id.ic_redefinir)
        edit_peso = findViewById(R.id.edit_peso)
        edit_idade = findViewById(R.id.edit_idade)
        bt_calcular = findViewById(R.id.bt_calcular)
        txt_resultado_ml = findViewById(R.id.txt_resultado_ml)
        bt_lembrete = findViewById(R.id.bt_definir_lembrete)
        bt_alarme = findViewById(R.id.bt_alarme)
        txt_hora = findViewById(R.id.txt_hora)
        txt_minutos = findViewById(R.id.txt_minutos)

    }
}