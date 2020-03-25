package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.MultipleChoiceQuestion

// https://medium.com/@droidbyme/android-recyclerview-with-single-and-multiple-selection-5d50c0c4c739
// Code was taken from
class MyAdapter(private val myDataset: ArrayList<MultipleChoiceQuestion>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    var selected: Int = 0

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_text_view, parent, false) as TextView
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(myDataset[position])
    }

    fun getActiveQuestion(): MultipleChoiceQuestion{
        return myDataset[selected]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    inner class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView){

        fun bind(question: MultipleChoiceQuestion){
            if (selected == -1){
                textView.setBackgroundColor(Color.parseColor("white"))
            }
            else {
                if (selected == adapterPosition) {
                    val currentColor = textView.background as ColorDrawable
                    if (currentColor.color != Color.parseColor("teal")){
                        textView.setBackgroundColor(Color.parseColor("teal"))
                    }
                    else {
                        textView.setBackgroundColor(Color.parseColor("white"))
                    }
                } else {
                    textView.setBackgroundColor(Color.parseColor("white"))
                }
            }
            textView.text = question.toString()
            textView.setOnClickListener{
                textView.setBackgroundColor(Color.parseColor("teal"))
                if (selected != adapterPosition) {
                    this@MyAdapter.notifyItemChanged(selected)
                    selected = adapterPosition
                }
                println(textView.text)
            }
        }
    }
}