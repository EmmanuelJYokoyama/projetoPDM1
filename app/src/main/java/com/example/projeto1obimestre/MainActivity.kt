package com.example.projeto1obimestre

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    lateinit var btn_finalizar: Button
    lateinit var cb_P: CheckBox
    lateinit var cb_R: CheckBox
    lateinit var cb_S: CheckBox

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        getProductWEB(cb_P.isChecked, cb_R.isChecked, cb_S.isChecked)


        cb_P.setOnCheckedChangeListener{buttonView, isChecked ->
            getProductWEB(cb_P.isChecked, cb_R.isChecked, cb_S.isChecked)

        }

        cb_R.setOnCheckedChangeListener{buttonView, isChecked ->
            getProductWEB(cb_P.isChecked, cb_R.isChecked, cb_S.isChecked)

        }

        cb_S.setOnCheckedChangeListener{buttonView, isChecked ->
            getProductWEB(cb_P.isChecked, cb_R.isChecked, cb_S.isChecked)

        }

    }

    fun init(){
        btn_finalizar = findViewById(R.id.btn)
        cb_P = findViewById(R.id.check_Pizza)
        cb_R = findViewById(R.id.check_Refri)
        cb_S= findViewById(R.id.check_Sobre)
    }

    fun getProductWEB(cbx_R: Boolean, cbx_P: Boolean, cbx_S: Boolean){

        val line: LinearLayout = findViewById(R.id.linear)
        val queue = Volley.newRequestQueue(this)
        val url = "http://helioesperidiao.com/api.php"
        val requestBody = "id=1" + "&msg=test_msg"

        val stringReq : StringRequest =
            object : StringRequest(Method.POST, url,
                Response.Listener { response ->

                    var resposta = response.toString()
                    val array = JSONArray(resposta)
                    val tamanho =array.length()
                    for (i in 0 until tamanho ) {
                        val item: JSONObject = array.getJSONObject(i) // recupera o objeto na posição dentro do array
                        var idProduto = item.get("id").toString()
                        var nome = item.get("nome").toString()
                        var type = item.get("type").toString();
                        var desc = item.get("desc").toString();
                        var preco = item.get("preco").toString();
                        var estoque = item.get("estoque").toString();
                        var qtd = item.get("qtd").toString();
                        var img = item.get("img").toString();
                        var novoTextView = TextView(this)

                        if(cbx_R || cbx_P || cbx_S){
                            if((cbx_R && (type == " refrigerante")) || (cbx_P && (type == "pizza")) || (cbx_S && (type == "sobremesa")))
                                views_dinamicas(idProduto, type, nome, desc, qtd, preco, img)

                        }else{
                            views_dinamicas(idProduto, type, nome, desc, qtd, preco, img)
                        }

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

    fun views_dinamicas(idProduto: String, type: String, nome:String, desc:String, qtd:String, preco:String, img:String){
        val linearProdutos: LinearLayout = findViewById(R.id.linear)
        linearProdutos.scrollY
        var bloco = LinearLayout(this)

        //bloco linear layout de produto
        bloco.orientation = LinearLayout.VERTICAL


        bloco.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        //criar imagem
        var imagev = ImageView(this)
        imagev.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        //carregar img
        DownloadImageFromInternet(imagev).execute(img)

        //texto do produto
        var novoTextView = TextView(this)

        novoTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        novoTextView.text = "${type} -> ${nome} R$ ${preco} \n "//${desc}

        //botao para adicionar produto em pedido
        val adicionar = Button(this)
        adicionar.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        adicionar.text = "Adicionar ao pedido"
        adicionar.setOnClickListener {
            adicionarPedido()
        }

        bloco.addView(imagev)
        bloco.addView(novoTextView)
        bloco.addView(adicionar)

        linearProdutos.addView(bloco)
        var espaco = TextView(this)

        espaco.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        espaco.text = "   "
        espaco.height = 100
        linearProdutos.addView(espaco)

    }

    fun adicionarPedido(){


        var linearPedido: LinearLayout = findViewById(R.id.linear)

    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        init {
            Toast.makeText(applicationContext, "Carregando imagem", Toast.LENGTH_SHORT).show()
        }
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }

}