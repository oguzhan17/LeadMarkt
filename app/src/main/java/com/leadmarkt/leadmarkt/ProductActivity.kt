package com.leadmarkt.leadmarkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    var userName : ArrayList<String> = ArrayList()
    var userComment : ArrayList<String> = ArrayList()
    var adapter : ProductAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        //get intent
        val intent2 = intent
        val textviewtext = intent2.getStringExtra("barcodenumbertext")
        if(textviewtext != null) {
            barcodeTextView.text = textviewtext.toString()

        }

        getDataFromFirestore()

        //RecyclerView
        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = ProductAdapter(userName,userComment)
        recyclerView.adapter = adapter


    }

    fun getDataFromFirestore() {

        db.collection("Barcode").addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage?.toString(),Toast.LENGTH_LONG).show()
            }else{

                if (snapshot!=null)
                    if(!snapshot.isEmpty){
                        val documents = snapshot.documents
                     

                        for(document in documents ){



                            val barcodeNo = document.get("barcodeNo") as? String
//                            val name = document.get("name") as? String
//                            val comment = document.get("comment") as? String
                            val title = document.get("product_title") as? String
                            val img= document.get("product_image") as? String



                           if (barcodeNo.toString() == barcodeTextView.text.toString()) {

                               Picasso.get().load(img).into(imageView)
                               titleTextView.text = title.toString()
                               imageTextView.text = img.toString()

                               db.collection("Comment").addSnapshotListener { snapshot, exception ->
                                   if (exception != null){
                                       Toast.makeText(applicationContext,exception.localizedMessage?.toString(),Toast.LENGTH_LONG).show()
                                   }else {
                                       if (snapshot!=null)
                                           if(!snapshot.isEmpty){
                                              userName.clear()
                                              userComment.clear()

                                               val documents = snapshot.documents
                                               for(document in documents ){


                                       val name = document.get("name") as? String
                                       val comment = document.get("comment") as? String
                                       val barcodeNo = document.get("barcodeNo") as? String


                                                   //i = position of name who comment product in fb


                                                   if (barcodeNo.toString() == barcodeTextView.text.toString() ) {



                                                       userName.add(name.toString())
                                                       userComment.add(comment.toString())
                                                       adapter!!.notifyDataSetChanged()

                                                       println(barcodeNo)

                                                       println(userName)
                                                       println(userComment)



                                                   }




                                               }}}}

                               // barcodeTextView.text = textviewtext.toString()

                         }


                        /*
                           if (nameTextView.text=="TextView"){
                               nameTextView.text = "Ürün Bulunamadı"
                               titleTextView.text = ""
                               imageTextView.text = ""
                               Toast.makeText(applicationContext,"Ürün Bulunamadı".toString(),Toast.LENGTH_LONG).show()

                               var intent = Intent(applicationContext,ScanActivity::class.java)
                               startActivity(intent)
                               finish() }
*/
                        else{}

                        }}}}}}