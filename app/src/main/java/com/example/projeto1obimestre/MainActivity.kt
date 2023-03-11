package com.example.projeto1obimestre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getProductWEB()
    }

    fun getProductWEB(){

        val queue = Volley.newRequestQueue(this)
        val url = "http://helioesperidiao.com/api.php"
        val requestBody = "id=1" + "&msg=test_msg"
        val line: LinearLayout = findViewById(R.id.linear)
        val stringReq : StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->

                var resposta = response.toString()
                    val array = JSONArray(resposta)
                    val tamanho =array.length()
                    for (i in 0 until tamanho ) {
                        val item: JSONObject = array.getJSONObject(i) // recupera o objeto na posição dentro do array
                        var idProduto = item.get("id").toString()
                        var type = item.get("type").toString();
                        var desc = item.get("desc").toString();
                        var preco = item.get("preco").toString();
                        var estoque = item.get("estoque").toString();
                        var qtd = item.get("qtd").toString();
                        var img = item.get("img").toString();
                        var novoTextView = TextView(this)
                        novoTextView.text =  idProduto + " - " + type
                        novoTextView.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)

                        line.addView(novoTextView)

                    }
                },
                Response.ErrorListener { error ->
                    Log.d("API", "error => $error")
                }
            ){
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)

    }

}