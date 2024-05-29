package com.teamapp.lcs

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.teamapp.lcs.databinding.ActivityLoginBinding
import com.teamapp.lcs.databinding.ErrorDialogBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // check if the user exists in the realtime database
                val db = Firebase.database
                val query = db.getReference("employees").orderByChild("email").equalTo(email)

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            firebaseAuth.signInWithEmailAndPassword(email, pass)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                        finish()
                                    } else {
                                        showErrorDialog("Email sau parola gresita")
                                    }
                                }
                        } else {
                            showErrorDialog("Utilizatorul nu exista!")
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        showErrorDialog("Eroare la conectare!")
                    }
                })
            } else {
                showErrorDialog("Campurile nu pot fi goale")
            }
        }
    }

    private fun showErrorDialog(message: String) {
        Dialog(this).apply {
            val dialogBinding =
                ErrorDialogBinding.inflate(LayoutInflater.from(this@LoginActivity))
            setContentView(dialogBinding.root)
            dialogBinding.textView.text = message
            dialogBinding.dialogTitle.text = "Eroare"
            dialogBinding.dialogPositiveButton.text = "OK"

            dialogBinding.dialogPositiveButton.setOnClickListener {
                dismiss()
            }
            setCancelable(false)
            show()
        }
    }
}
