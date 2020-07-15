package com.andresdiaz.finalapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andresdiaz.finalapp.R
import com.andresdiaz.finalapp.inflate
import com.andresdiaz.finalapp.models.Rate
import com.andresdiaz.finalapp.util.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_rates_item.view.*
import java.text.SimpleDateFormat

class RatesAdapter (private val items: List<Rate>): RecyclerView.Adapter<RatesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(parent.inflate(R.layout.fragment_rates_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(rate: Rate) =with (itemView){
            textViewRate.text=rate.text
            textViewStar.text=rate.rate.toString()
            textViewCalendar.text= SimpleDateFormat("dd MMM, yyyy").format(rate.createdAt)
            Picasso.get().load(rate.profileImgURL).resize(100,100)
                .centerCrop().transform(CircleTransform()).into(imageViewProfile)
        }
    }


}