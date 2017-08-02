package sam.swordsonline

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.shop_item.view.*

class ItemAdapter(val viewActions: onViewSelectedListener, shopItems: MutableList<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = shopItems as ArrayList<Item>
    interface onViewSelectedListener {
        fun onItemSelected(item: Item)
    }
    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val h = holder as ItemViewHolder
        h.bind(items.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(parent)
    }

    inner class ItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_item,parent,false)) {
        fun bind(item: Item) = with(itemView) {
            var slotString = ""
            when(item.equipment_slot){
                0->slotString = "Head"
                1->slotString = "Shoulders"
                2->slotString = "Legs"
                3->slotString = "Off Hand"
                4->slotString = "Main Hand"
            }

            textView1.text = item.name
            textView2.text = "Slot: $slotString, Speed: ${item.ability.speed}, Cooldown: ${item.cooldown-1} Price: ${item.price}"
            textView3.text = "Stat Requirments: Str ${item.stat_requirement.strength_range}  Dex ${item.stat_requirement.dexterity_range}  Int ${item.stat_requirement.intelligence_range}"
            textView4.text = "Ability: ${item.ability.type} ${item.ability.relative_pairs}"
            super.itemView.setOnClickListener { viewActions.onItemSelected(item)}
        }
    }
}