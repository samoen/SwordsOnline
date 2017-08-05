package sam.swordsonline.ui.fragment

import android.app.Fragment
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_main.*
import sam.swordsonline.R
import sam.swordsonline.adapter.ItemAdapter
import sam.swordsonline.model.Item
import sam.swordsonline.ui.activity.MainActivity

class MainFragment : Fragment(), ItemAdapter.onViewSelectedListener {

    var listItems = mutableListOf<Item>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_game_room_1.setOnClickListener {
            (activity as MainActivity).fragmentManager.beginTransaction().remove(OnlineFragment()).commitAllowingStateLoss()
            fragmentManager.beginTransaction().replace(R.id.framelayout_main,OnlineFragment()).commit()
        }
        textView_playerInfo.setText("${(activity as MainActivity).currentPlayer.name} The Brave\n" +
                                    "Gold: ${(activity as MainActivity).currentPlayer.gold}\n" +
                                    "Strength: ${(activity as MainActivity).currentPlayer.strength}, Intelligence: ${(activity as MainActivity).currentPlayer.intelligence}, Dexterity: ${(activity as MainActivity).currentPlayer.dexterity}")

        button_save.setOnClickListener { (activity as MainActivity).SavePlayer() }

        button_itemShop.setOnClickListener {
            fragmentManager.beginTransaction().replace(R.id.framelayout_main,ItemShopFragment()).commit()
        }
        button_respec.setOnClickListener {
            fragmentManager.beginTransaction().replace(R.id.framelayout_main,RespecFragment()).commit()
        }
        button_adventure.setOnClickListener {
            fragmentManager.beginTransaction().replace(R.id.framelayout_main,AdventureFragment()).commit()
        }
        recyclerView_playerItems.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
        }

        RefreshTextViews()
        RefreshListItems()

        recyclerView_playerItems.adapter = ItemAdapter(this, listItems)

    }

    override fun onItemSelected(item: Item) {
        val simpleAlert = AlertDialog.Builder(activity).create()
        simpleAlert.setTitle("Equip Item")
        simpleAlert.setMessage(item.name)
        simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if ((activity as MainActivity).currentPlayer.StatsMet(item)){
                    (activity as MainActivity).currentPlayer.equipped.put(item.equipment_slot,item)
                    (activity as MainActivity).currentPlayer.equippedKeys.put(item.equipment_slot,findKey(item.name))
                    Toast.makeText(activity,"Item equipped",Toast.LENGTH_SHORT).show()
                    RefreshTextViews()
                }else{
                    Toast.makeText(activity,"You cannot equip this due to your stats",Toast.LENGTH_SHORT).show()
                }
            }
        })
        simpleAlert.show()
    }
    fun findKey(str:String):Int{
        for(i in (activity as MainActivity).shopItems.allItems.keys){
            if ((activity as MainActivity).shopItems.allItems[i]?.name == str){
                return i
            }
        }
        return 0
    }

    fun RefreshTextViews(){
        textView_helmet.text = (activity as MainActivity).currentPlayer.equipped[0]?.name
        textView_shoulders.text = (activity as MainActivity).currentPlayer.equipped[1]?.name
        textView_legs.text = (activity as MainActivity).currentPlayer.equipped[2]?.name
        textView_offhand.text = (activity as MainActivity).currentPlayer.equipped[3]?.name
        textView_right_hand.text = (activity as MainActivity).currentPlayer.equipped[4]?.name
    }
    fun RefreshListItems(){
        listItems = (activity as MainActivity).currentPlayer.items
    }

}