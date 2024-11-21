package com.cmc.alarm

import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlarmActivity : AppCompatActivity() {

    lateinit var btnCancelAlarm: FloatingActionButton
    private var mediaPlayer: MediaPlayer? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activer le mode edge-to-edge (pour les barres de statut et de navigation)
        enableEdgeToEdge()

        // Définir le contenu de l'activité
        setContentView(R.layout.activity_alarm)

        // Appliquer les insets pour gérer les barres système (barre de statut, navigation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.alarmMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val timeTextView: TextView = findViewById(R.id.timeTextView)

        // Récupérer la date et l'heure actuelle
        val currentDateTime = LocalDateTime.now()

        // Créer un formatteur pour la date (jour, mois, année)
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")
        val formattedDate = currentDateTime.format(dateFormatter)

        // Créer un formatteur pour l'heure (heure, minutes, secondes)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = currentDateTime.format(timeFormatter)

        // Afficher la date dans le TextView approprié
        dateTextView.text = formattedDate

        // Afficher l'heure dans le TextView approprié
        timeTextView.text = formattedTime

        var hour = currentDateTime.hour

        var sunMoonImageView = findViewById<ImageView>(R.id.sunMoon)
        if (hour in 6..18) {
            sunMoonImageView.setImageResource(R.drawable.day)
        } else {
            sunMoonImageView.setImageResource(R.drawable.night)

        }


        // Initialiser le bouton d'annulation de l'alarme
        btnCancelAlarm = findViewById(R.id.btnCancelAlarm)

        // Configurer l'action du bouton d'annulation
        btnCancelAlarm.setOnClickListener {
            // Arrêter l'alarme (si elle est en train de jouer)
            stopAlarmSound()

            // Retour à l'écran principal (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Facultatif : Fermer cette activité après le lancement de MainActivity
            finish()  // Facultatif, pour fermer l'activité en cours
        }

        // Jouer l'alarme dès que l'activité démarre
        playAlarmSound()
    }

    // Jouer le son de l'alarme
    private fun playAlarmSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.alarmclockshort)  // Assurez-vous que alarm_sound.mp3 existe dans res/raw
        mediaPlayer?.start()
    }

    // Arrêter le son de l'alarme
    private fun stopAlarmSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()  // Libérer les ressources du MediaPlayer
        mediaPlayer = null  // Nullifier pour éviter d'accéder à un objet MediaPlayer qui n'existe plus
    }

    // Libérer les ressources du MediaPlayer lors de la destruction de l'activité
    override fun onDestroy() {
        super.onDestroy()
        stopAlarmSound()  // S'assurer d'arrêter le son si l'activité est détruite
    }
}
