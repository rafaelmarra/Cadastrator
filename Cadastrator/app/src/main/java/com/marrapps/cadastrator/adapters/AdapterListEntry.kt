package com.marrapps.cadastrator.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.marrapps.cadastrator.R
import com.marrapps.cadastrator.interfaces.ListClickListener
import com.marrapps.cadastrator.model.Entry
import kotlinx.android.synthetic.main.content_list_item.view.*

class AdapterListEntry(var entryList: List<Entry>, private val listener: ListClickListener) :
    RecyclerView.Adapter<AdapterListEntry.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.content_list_item, p0, false)

        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(entryList[p1])
        setAnimation(p0.itemView)
    }

    private fun setAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.slide_in_left)
        view.startAnimation(animation)
    }

    override fun getItemCount(): Int = entryList.size

    fun update(records: List<Entry>) {

        this.entryList = records

        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val listener: ListClickListener) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        fun bind(entry: Entry) {

            val txtNome = itemView.txtItemName
            txtNome.text = entry.name

            val txtCpf = itemView.txtItemCpf
            txtCpf.text = entry.cpf

            val txtBirth = itemView.txtItemBirth
            txtBirth.text = entry.birth

            val txtAddress = itemView.txtItemAddress
            txtAddress.text = StringBuilder()
                .append(entry.address).append(", ")
                .append(entry.number).append(", ")
                .append(entry.compl)
                .toString()

            val txtCity = itemView.txtItemCity
            txtCity.text = StringBuilder()
                .append(entry.city).append(" - ")
                .append(entry.state).toString()

            val txtLetter = itemView.txtItemLetter
            txtLetter.text = entry.name[0].toString()

            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View) {
            listener.onItemClick(v, adapterPosition)
        }
    }
}