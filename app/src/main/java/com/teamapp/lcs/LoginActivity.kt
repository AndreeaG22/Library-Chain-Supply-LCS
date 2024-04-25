package com.teamapp.lcs



import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
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

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        showErrorDialog("E-mail sau parola incorecte!")
                    }
                }
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