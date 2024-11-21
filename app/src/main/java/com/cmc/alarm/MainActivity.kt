package com.cmc.alarm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cmc.alarm.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var alarmSwitch: SwitchCompat
    lateinit var btnAddAlarm: FloatingActionButton
    lateinit var timeTextView: TextView
    lateinit var sunMoonImageView: ImageView

    private var alarmHour = 0
    private var alarmMinute = 0
    private val handler = Handler()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // Lier les vues avec ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyWindowInsets()

        initializeViews()

        // Configurer l'action du bouton d'ajout d'alarme
        configureAddAlarmButton()

        // Gérer les données passées par Intent et mettre à jour l'interface
        handleIntentData()

        val dateTextView: TextView = findViewById(R.id.dateTextView)

        // Récupérer la date et l'heure actuelle
        val currentDateTime = LocalDateTime.now()

        // Créer un formatteur pour la date (jour, mois, année)
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
        val formattedDate = currentDateTime.format(dateFormatter)


        // Afficher la date dans le TextView approprié
        dateTextView.text = formattedDate
    }

    private fun initializeViews() {
        btnAddAlarm = binding.btnAddAlarm
        timeTextView = binding.time
        sunMoonImageView = binding.sunMoon
        alarmSwitch = binding.alarmSwitch
    }

    private fun applyWindowInsets() {
        val mainView = findViewById<View>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun configureAddAlarmButton() {
        btnAddAlarm.setOnClickListener {
            val intent = Intent(this, AddAlarm::class.java)
            val bundle = Bundle().apply {
                putString("cle", "valeur") // Exemple de données passées à l'activité
            }
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun handleIntentData() {
        val selectedTime = intent.getStringExtra("selectedTime")
        val selectedImg = intent.getStringExtra("selectedImg")
        val Hour = intent.getStringExtra("formattedHour")
        val Minute = intent.getStringExtra("formattedMinute")

        // Afficher l'heure et les minutes
        if (Hour != null && Minute != null) {
            alarmHour = Hour.toInt()
            alarmMinute = Minute.toInt()

            timeTextView.text = "$Hour:$Minute"
        }

        // Si une heure est passée dans l'intent, l'afficher
        selectedTime?.let {
            timeTextView.text = it
            sunMoonImageView.visibility = View.VISIBLE
            alarmSwitch.visibility = View.VISIBLE
            alarmSwitch.isChecked = true
            checkAlarm()
        }

        // Afficher l'image du jour ou de la nuit
        when (selectedImg) {
            "day" -> sunMoonImageView.setImageResource(R.drawable.day)
            "night" -> sunMoonImageView.setImageResource(R.drawable.night)
        }

        alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Alarme réglée pour $alarmHour:$alarmMinute", Toast.LENGTH_SHORT).show()
                checkAlarm()
            } else {
                // Si l'alarme est désactivée, vous pouvez arrêter la vérification ou réinitialiser l'alarme
                Toast.makeText(this, "Alarme désactivée", Toast.LENGTH_SHORT).show()
                // Vous pouvez ajouter une logique pour annuler l'alarme si nécessaire.
            }
        }
    }

    private fun checkAlarm() {
        val currentTime = Calendar.getInstance()
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentTime.get(Calendar.MINUTE)

        // Vérifier si l'heure actuelle est égale à l'heure de l'alarme
        if (currentHour == alarmHour && currentMinute == alarmMinute) {
            openAlarmActivity()
        }

        handler.postDelayed({ checkAlarm() }, 60000)
    }

    private fun openAlarmActivity() {
        // Démarrer l'activité AlarmActivity
        val intent = Intent(this, AlarmActivity::class.java)
        startActivity(intent)
    }
}
