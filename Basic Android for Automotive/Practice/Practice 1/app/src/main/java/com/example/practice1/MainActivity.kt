package com.example.practice1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etGameTag : EditText
    private lateinit var cbVal : CheckBox
    private lateinit var cbLOL : CheckBox
    private lateinit var cbTFT : CheckBox
    private lateinit var cbFF : CheckBox
    private lateinit var btnCreate : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etGameTag = findViewById<EditText>(R.id.etGameTag)
        cbVal = findViewById<CheckBox>(R.id.cbVLR)
        cbLOL = findViewById<CheckBox>(R.id.cbLOL)
        cbTFT = findViewById<CheckBox>(R.id.cbTFT)
        cbFF = findViewById<CheckBox>(R.id.cbFF)
        btnCreate = findViewById<Button>(R.id.btnCreate)

        etGameTag.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cbVal.setOnCheckedChangeListener { _, _ -> validateForm() }
                cbLOL.setOnCheckedChangeListener { _, _ -> validateForm() }
                cbTFT.setOnCheckedChangeListener { _, _ -> validateForm() }
                cbFF.setOnCheckedChangeListener { _, _ -> validateForm() }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnCreate.setOnClickListener {
            val selectedGames = ArrayList<String>()

            if (cbVal.isChecked) selectedGames.add("Valorant")
            if (cbLOL.isChecked) selectedGames.add("League of Legends")
            if (cbTFT.isChecked) selectedGames.add("Teamfight Tactics")
            if (cbFF.isChecked) selectedGames.add("Free Fire")

            val gamerTag = etGameTag.text.toString()
            val intent = Intent(this, ProfileActivity::class.java)

            intent.putExtra("GAMER_TAG", gamerTag)
            intent.putStringArrayListExtra("SELECTED_GAMES", selectedGames)

            startActivity(intent)
        }
    }

    private fun validateForm(){
        var isTagValid = etGameTag.text.isNotBlank()
        var isGameSelected = cbFF.isChecked || cbTFT.isChecked || cbLOL.isChecked || cbVal.isChecked
        btnCreate.isEnabled = isTagValid && isGameSelected
    }
}