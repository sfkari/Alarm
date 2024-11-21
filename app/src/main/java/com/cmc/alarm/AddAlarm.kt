package com.cmc.alarm

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddAlarm : AppCompatActivity() {

    // Déclaration des variables des vues
    private lateinit var time: TextView
    private lateinit var simpleTimePicker: TimePicker
    private lateinit var sunMoonImageView: ImageView
    private lateinit var btnAnnuler: Button
    private lateinit var btnEnregistrer: Button
    private lateinit var formattedHour : String
    private lateinit var formattedMinute : String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activer le mode Edge-to-Edge si votre appareil supporte cette fonctionnalité
        enableEdgeToEdge()

        setContentView(R.layout.activity_add_alarm)

        // Initialiser les vues
        initializeViews()

        // Vérification et application des WindowInsets pour gérer les barres système
        applyWindowInsets()

        // Configuration du TimePicker et de l'affichage de l'heure
        configureTimePicker()

        // Configurer les boutons Annuler et Enregistrer
        configureButtons()

        // Configurer les jours de la semaine et les CheckBoxes associés
        setupDayCheckboxes()

        val dateTextView: TextView = findViewById(R.id.dateTextView)

        // Récupérer la date et l'heure actuelle
        val currentDateTime = LocalDateTime.now()

        // Créer un formatteur pour la date (jour, mois, année)
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
        val formattedDate = currentDateTime.format(dateFormatter)


        // Afficher la date dans le TextView approprié
        dateTextView.text = formattedDate
    }

    // Initialisation des vues
    private fun initializeViews() {
        time = findViewById(R.id.time)
        simpleTimePicker = findViewById(R.id.simpleTimePicker)
        sunMoonImageView = findViewById(R.id.sunMoon)
        btnAnnuler = findViewById(R.id.btnAnnuler)
        btnEnregistrer = findViewById(R.id.btnEnregistrer)
    }

    // Appliquer les WindowInsets pour la gestion des barres système
    private fun applyWindowInsets() {
        val mainView = findViewById<View>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun configureTimePicker() {
        simpleTimePicker.setIs24HourView(false) // Utiliser le format AM/PM
        var dayOrNight = ""

        // Lier l'événement de changement d'heure
        simpleTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            // Déterminer si c'est le jour ou la nuit et mettre à jour l'image
            if (hourOfDay in 6..18) {
                sunMoonImageView.setImageResource(R.drawable.day)
                dayOrNight = "day"
            } else {
                sunMoonImageView.setImageResource(R.drawable.night)
                dayOrNight = "night"
            }

            // Formater et afficher l'heure
            formattedHour = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
            formattedMinute = if (minute < 10) "0$minute" else "$minute"
            time.text = "$formattedHour : $formattedMinute"
        }
    }

    private fun configureButtons() {
        btnEnregistrer.setOnClickListener {
            val selectedTime = time.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selectedTime", selectedTime)
            intent.putExtra("formattedHour", formattedHour)
            intent.putExtra("formattedMinute", formattedMinute)
            intent.putExtra("selectedImg", if (sunMoonImageView.drawable.constantState == resources.getDrawable(R.drawable.day).constantState) "day" else "night")
            startActivity(intent)
        }

        btnAnnuler.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Configurer les jours de la semaine et leurs CheckBoxes associés
    private fun setupDayCheckboxes() {
        setupDayClick(R.id.lundi, R.id.checkLundi)
        setupDayClick(R.id.mardi, R.id.checkMardi)
        setupDayClick(R.id.mercredi, R.id.checkMercredi)
        setupDayClick(R.id.jeudi, R.id.checkJeudi)
        setupDayClick(R.id.vendredi, R.id.checkVendredi)
        setupDayClick(R.id.samedi, R.id.checkSamedi)
        setupDayClick(R.id.dimanche, R.id.checkDimanche)
    }

    // Méthode pour configurer le comportement d'un jour (TextView et CheckBox)
    private fun setupDayClick(dayTextViewId: Int, checkBoxId: Int) {
        val dayTextView: TextView = findViewById(dayTextViewId)
        val dayCheckBox: CheckBox = findViewById(checkBoxId)

        dayTextView.setOnClickListener {
            // Alterner l'état du CheckBox et mettre à jour l'arrière-plan
            dayCheckBox.isChecked = !dayCheckBox.isChecked
            if (dayCheckBox.isChecked) {
                dayTextView.setBackgroundResource(R.drawable.circle)
                dayTextView.setTextColor(ContextCompat.getColor(this, R.color.Darkviolet))
            } else {
                dayTextView.setBackgroundColor(Color.TRANSPARENT)
                dayTextView.setTextColor(ContextCompat.getColor(this, R.color.rose))
            }
        }
    }
}
