package hfad.com.studytimeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import hfad.com.studytimeapp.R
import kotlinx.android.synthetic.main.year_list_item.view.*


class SessionMonthSelectorAdapter( ) : RecyclerView.Adapter<SessionMonthSelectorAdapter.SessionMonthViewHolder>() {


    /*
        Manually call setData from the Activity because we need to update the recycler view when we have data.
        When there is a change to the years list we set this adapter's data with set data
     */

    fun setData(years: List<Int>){
        yearList = years
        notifyDataSetChanged()
    }

    var yearList = emptyList<Int>()

    class SessionMonthViewHolder(val cardView: CardView): RecyclerView.ViewHolder(cardView){

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SessionMonthViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.year_list_item, parent, false) as CardView
        return SessionMonthViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: SessionMonthViewHolder, position: Int) {
        holder.cardView.yearTV.text = yearList[position].toString()
    }

    override fun getItemCount() = yearList.size


}
