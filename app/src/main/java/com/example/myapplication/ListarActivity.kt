package com.example.myapplication

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar)

        val db = Firebase.firestore

        val edtPesq: EditText = findViewById(R.id.edtPesq)
        val btnBusca: Button = findViewById(R.id.btnBuscar)
        val txtResultNome: TextView = findViewById(R.id.txtResultNome)
        val txtResultId: TextView = findViewById(R.id.txtResultId)
        val txtResultEnd: TextView = findViewById(R.id.txtResultEnd)
        val txtResultBairro: TextView = findViewById(R.id.txtResultBairro)
        val txtResultCep: TextView = findViewById(R.id.txtResultCep)
        val btnExcluir: Button = findViewById(R.id.btnExcluir)
        val btnEditar: Button = findViewById(R.id.btnEditar)

        db.collection("cadastro")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }

        btnBusca.setOnClickListener {
            if (edtPesq.text.isNotEmpty()){
                val docRef = db.collection("cadastro").document(edtPesq.text.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                            txtResultId.setText("${document.id}")
                            txtResultNome.setText(" ${document.get("nome")}")
                            txtResultEnd.setText(" ${document.get("endereco")}")
                            txtResultBairro.setText(" ${document.get("bairro")}")
                            txtResultCep.setText("${document.get("cep")}")
                        } else {
                            Log.d(ContentValues.TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "get failed with ", exception)
                    }
            }else{
                Toast.makeText(this,"Insira um Id para completar a busca!", Toast.LENGTH_LONG).show()
            }
        }
        btnExcluir.setOnClickListener{

            db.collection("cadastro").document(edtPesq.text.toString())
                .delete()
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!")
                    txtResultId.setText(null)
                    txtResultNome.setText(null)
                    txtResultEnd.setText(null)
                    txtResultBairro.setText(null)
                    txtResultCep.setText(null)}
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }
            Toast.makeText(this,"Deletado com Sucesso!", Toast.LENGTH_SHORT).show()
        }

        btnEditar.setOnClickListener{
            val id = db.collection("cadastro").document(edtPesq.text.toString())

            // Set the "isCapital" field of the city 'DC'
            id
                .update(mapOf(
                    "nome" to txtResultNome.text.toString(),
                    "endereco" to txtResultEnd.text.toString(),
                    "bairro" to txtResultBairro.text.toString(),
                    "cep" to txtResultCep.text.toString()
                ))
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!")}
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error updating document", e) }
            Toast.makeText(this,"Atualizado com Sucesso!", Toast.LENGTH_SHORT).show()
        }
    }
}