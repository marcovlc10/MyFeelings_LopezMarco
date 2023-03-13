package lopez.marco.myfeelings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import lopez.myfeelings.graph.utilities.CustomBarDrawable
import lopez.myfeelings.graph.utilities.CustomCircleDrawable
import lopez.myfeelings.graph.utilities.JSONFile
import lopez.myfeelings.graph.utilities.emociones
import org.json.JSONArray
import org.json.JSONException
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var jsonFile: JSONFile?=null
    var veryHappy=0.0F
    var happy=0.0F
    var neutral=0.0F
    var sad=0.0F
    var verySad=0.0F
    var data:Boolean=false
    var lista=ArrayList<emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun fetchingData(){
        try{
            var json: String=jsonFile?.getData(this)?:""
            if(json!=""){
                this.data=true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista=parseJson(jsonArray)

                for(i in lista){
                    when(i.nombre){
                        "Muy feliz"-> veryHappy=i.total
                        "Feliz"-> happy=i.total
                        "Neutral"-> neutral=i.total
                        "Triste"->sad=i.total
                        "Muy triste"-> verySad=i.total
                    }
                }
            } else {
                this.data=false
            }
        } catch(exception: JSONException){
            exception.printStackTrace()
        }
    }

    fun iconoMayoria(){
        if (happy > veryHappy && happy > neutral && happy > sad && happy > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }

        if (veryHappy > happy && veryHappy > neutral && veryHappy > sad && veryHappy > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }

        if (neutral > veryHappy && neutral > happy && neutral > sad && neutral > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }

        if (sad > happy && sad > neutral && sad > veryHappy && sad > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }

        if (verySad > happy && verySad > neutral && verySad > sad && veryHappy > verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }
    }

    fun actualizarGrafica(){
        var context: Context? = null
        val total = veryHappy+happy+neutral+verySad+sad

        var pVH:Float=(veryHappy*100/total).toFloat()
        var pH:Float=(happy*100/total).toFloat()
        var pN:Float=(neutral*100/total).toFloat()
        var pS:Float=(sad*100/total).toFloat()
        var pVS:Float=(verySad*100/total).toFloat()

        Log.d("porcentajes","very happy "+pVH)
        Log.d("porcentajes","happy "+pH)
        Log.d("porcentajes","neutral "+pN)
        Log.d("porcentajes","sad "+pS)
        Log.d("porcentajes","very sad "+pVS)

        lista.clear()
        lista.add(emociones("Muy feliz",pVH, R.color.mustard, veryHappy))
        lista.add(emociones("Feliz",pH, R.color.orange, happy))
        lista.add(emociones("Neutral",pN, R.color.greenie, neutral))
        lista.add(emociones("Triste",pS, R.color.blue, sad))
        lista.add(emociones("Muy triste",pVS, R.color.deepBlue, verySad))

        val fondo= CustomCircleDrawable(this, lista)

        var inflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.activity_main, null)
        var graphVeryHappy: View = view.findViewById(R.id.graphVeryHappy)
        var graphHappy: View = view.findViewById(R.id.graphVeryHappy)
        var graphNeutral: View = view.findViewById(R.id.graphVeryHappy)
        var graphSad: View = view.findViewById(R.id.graphVeryHappy)
        var graphVerySad: View = view.findViewById(R.id.graphVeryHappy)

        graphVeryHappy.background= CustomBarDrawable(this, emociones("Muy feliz", pVH,R.color.mustard, veryHappy))
        graphHappy.background= CustomBarDrawable(this, emociones("Feliz", pH,R.color.orange, happy))
        graphNeutral.background= CustomBarDrawable(this, emociones("Neutral", pN,R.color.greenie, neutral))
        graphSad.background= CustomBarDrawable(this, emociones("Triste", pS,R.color.blue, sad))
        graphVerySad.background= CustomBarDrawable(this, emociones("Muy triste", pVS,R.color.deepBlue, verySad))

        view.background=fondo
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<emociones>{
        var lista=ArrayList<emociones>()

        for(i in 0..jsonArray.length()){
            try {
                val nombre=jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje=jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color=jsonArray.getJSONObject(i).getInt("color")
                val total=jsonArray.getJSONObject(i).getDouble("total").toFloat()
                val emocion= emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            } catch(exception: JSONException){
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun guardar(){

    }
}