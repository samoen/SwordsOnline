package sam.swordsonline

import android.app.Fragment
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_item_shop.*

class ItemShopFragment:Fragment(), ItemAdapter.onViewSelectedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_item_shop,container,false)
    }
    override fun onItemSelected(item: Item) {

        if ((activity as MainActivity).currentPlayer.items.contains(item)){
            Toast.makeText(context,"You already own that item",Toast.LENGTH_SHORT).show()
        }else{
            val simpleAlert = AlertDialog.Builder(activity).create()
            simpleAlert.setTitle("Buy Item")
            simpleAlert.setMessage("${item.name} for ${item.price} gold")
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    if((activity as MainActivity).currentPlayer.CanAfford(item)){
                        (activity as MainActivity).currentPlayer.items.add(item)
                        (activity as MainActivity).currentPlayer.gold = (activity as MainActivity).currentPlayer.gold - item.price
                        Toast.makeText(context,"Item Purchased!",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"You can't afford that",Toast.LENGTH_SHORT).show()
                    }
                }
            })
            simpleAlert.show()
        }
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_goToStart.setOnClickListener {
            fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
        }
        recyclerView_shopItems.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
        }
        recyclerView_shopItems.adapter = ItemAdapter(this,ShopItemList.allItems.values.toMutableList())
    }
}

