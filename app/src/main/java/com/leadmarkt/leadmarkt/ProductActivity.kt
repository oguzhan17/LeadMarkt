package com.leadmarkt.leadmarkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        val currentUser = auth.currentUser


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


    //COMMENTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
    fun addComment (view: View){
     //   val comment = commentEditText.text.toString().trim()
        val usermail = auth.currentUser?.email.toString()

        db.collection("Users").addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage?.toString(),Toast.LENGTH_LONG).show()
            }else{

                if (snapshot!=null){
                    if(!snapshot.isEmpty){
                        val documentsUser = snapshot.documents
                        val currentMail = auth.currentUser?.email.toString()

                        for(documentUser in documentsUser ){


                            val emailFB = documentUser.get("email") as? String
                         //  println("emailFB   "+emailFB)

                            if (emailFB == currentMail) {


                                val commentMap = hashMapOf<String, Any>()
                               val name = documentUser.get("name") as? String
                               val surname = documentUser.get("surname") as? String
                                val comment = commentEditText.text.toString().trim()
                                val namesurname = name + " " + surname
                                if (name != null) {
                                    commentMap.put("name",namesurname)
                                }
                                if (surname != null) {
                                    commentMap.put("surname",surname)
                                }
                                commentMap.put("comment",comment)
                                commentMap.put("barcodeNo",barcodeTextView.text.toString())

                                println(commentMap)

                                db.collection("Comment").add(commentMap).addOnCompleteListener { task ->
                                    if (task.isComplete && task.isSuccessful) {
                                        //  println("auth current user $auth.currentUser!!.email")
                                        getDataFromFirestore()
                                    }
                                }.addOnFailureListener{exception ->
                                    Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
                                }
                            }}}}}}}



    //COMMENTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT




    fun getDataFromFirestore() {

        db.collection("Barcode").addSnapshotListener { snapshot, exception ->
            if (exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage?.toString(),Toast.LENGTH_LONG).show()
            }else{

                if (snapshot!=null){
                    if(!snapshot.isEmpty){
                        val documents = snapshot.documents
                     

                        for(document in documents ){

                            val barcodeNo = document.get("barcodeNo") as? String
                            val name = document.get("name") as? String
                            val comment = document.get("comment") as? String
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


                                                       if (name != null) {
                                                           userName.add(name)
                                                       }
                                                       if (comment != null) {
                                                           userComment.add(comment)
                                                       }
                                                       adapter!!.notifyDataSetChanged()

                                                   }
                                                   else{}

                                               }}}}

                               // barcodeTextView.text = textviewtext.toString()

                         }
else{}

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
                            //       else{}

                        }}}}}}}