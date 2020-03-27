package com.leadmarkt.leadmarkt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(private val userNameArray: ArrayList<String>, private val userCommentArray: ArrayList<String>):
    RecyclerView.Adapter<ProductAdapter.UserCommentHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCommentHolder {
       val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_view_row,parent,false)
        return  UserCommentHolder(view)
    }

    override fun getItemCount(): Int {
        return userNameArray.size
    }

    override fun onBindViewHolder(holder: UserCommentHolder, position: Int) {
        holder.nameTextT?.text = userNameArray[position]
        holder.commentTextT?.text = userCommentArray[position]
    }

    class UserCommentHolder(view: View) : RecyclerView.ViewHolder(view){
    var nameTextT : TextView? = null
    var commentTextT : TextView? = null

    init {
        nameTextT = view.findViewById(R.id.recyclerName)
        commentTextT = view.findViewById(R.id.recyclerComment)
    }

}}