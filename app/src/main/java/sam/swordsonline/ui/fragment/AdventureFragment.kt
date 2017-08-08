package sam.swordsonline.ui.fragment

import android.app.Fragment
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_adventure.*
import sam.swordsonline.R
import sam.swordsonline.adapter.ImageAdapter
import sam.swordsonline.ui.activity.MainActivity

class AdventureFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_adventure,container,false)
    }

    var activeMarkers: MutableList<Pair<Int,Int>> = mutableListOf()
    var activeAbilityType: String = ""
    var isHeroDead: Boolean = false
    var activeSlot = 5
    var myspd = 0

    var cooldowns = mutableMapOf<Int,Int>(0 to 0, 1 to 0, 2 to 0,3 to 0,4 to 0)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*Handler().postDelayed({folding_tab_bar.expand()},300)
        folding_tab_bar.onFoldingItemClickListener = object : FoldingTabBar.OnFoldingItemSelectedListener {
            override fun onFoldingItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.ftb_menu_head -> {
                        activeSlot =0
                        SelectAbility(activeSlot)
                    }
                    R.id.ftb_menu_shoulders -> {
                        activeSlot =1
                        SelectAbility(activeSlot)
                    }
                    R.id.ftb_menu_legs -> {
                        activeSlot =2
                        SelectAbility(activeSlot)
                    }
                    R.id.ftb_menu_offhand -> {
                        activeSlot =3
                        SelectAbility(activeSlot)
                    }
                    R.id.ftb_menu_mainhand -> {
                        activeSlot =4
                        SelectAbility(activeSlot)
                    }
                    R.id.ftb_menu_wait -> {
                        activeSlot =5
                        SelectAbility(activeSlot)
                    }
                }
                return false
            }
        }*/

        CP().location = Pair(1,1)

        val p = CP().equipped

        textView_head.setText(p[0]?.name)
        textView_shoulders.setText(p[1]?.name)
        textView_legs.setText(p[2]?.name)
        textView_offhand.setText(p[3]?.name)
        textView_mainhand.setText(p[4]?.name)

        textView_head.setOnClickListener {
            activeSlot =0
            SelectAbility(activeSlot)
        }
        textView_shoulders.setOnClickListener {
            activeSlot=1
            SelectAbility(activeSlot)
        }
        textView_legs.setOnClickListener {
            activeSlot=2
            SelectAbility(activeSlot)
        }
        textView_offhand.setOnClickListener {
            activeSlot = 3
            SelectAbility(activeSlot)
        }
        textView_mainhand.setOnClickListener {
            activeSlot = 4
            SelectAbility(activeSlot)
        }
        button_wait.setOnClickListener {
            IA().RemoveMarkers(activeMarkers)
            IA().ActivateMonsters(true,myspd)
            IA().ActivateMonsters(false,myspd)
            IA().GenerateMonster()
            IA().notifyDataSetChanged()
            DecrementAllCooldowns()
            CheckDeath()
        }

        gridView_adventure.adapter = ImageAdapter(context)

        gridView_adventure.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, _view: View?, position: Int, id: Long) {
                if(CheckCooldown(activeSlot)){
                    if(activeMarkers.contains(IA().CalculatePairFromPosition(position))){
                        IA().RemoveMarkers(activeMarkers)
                        IA().ActivateMonsters(true,myspd)
                        IA().notifyDataSetChanged()

                        cooldowns.put(activeSlot,p[activeSlot]?.cooldown?:0)
                        DecrementAllCooldowns()
                        Handler().postDelayed({HeroStage(position)},300)
                        Handler().postDelayed({SlowerStage()},600)
                    }
                }else Toast.makeText(context,"Cooldown ${cooldowns[activeSlot]}",Toast.LENGTH_SHORT).show()
            }
        })

        IA().PutHero(listOf(Pair(5,5)))

        button_backFromAdventure.setOnClickListener {
            val simpleAlert = AlertDialog.Builder(activity).create()
            simpleAlert.setTitle("Leave Adventure")
            simpleAlert.setMessage("Do you really want to leave this adventure?")
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
                }
            })
            simpleAlert.show()
        }
    }
    fun SelectAbility(slot: Int){
        val p = CP().equipped
        myspd = p.get(slot)?.ability?.speed?:0

        IA().RemoveMarkers(activeMarkers)
        activeAbilityType = p.get(slot)?.ability?.type?:""
        IA().PlaceMarkers(p.get(slot)?.ability?.relative_pairs?: listOf())
        IA().notifyDataSetChanged()
        activeMarkers = IA().CalculatePairsFromRelative(p.get(slot)?.ability?.relative_pairs?: listOf()) as MutableList<Pair<Int, Int>>
    }
    fun HeroStage(position:Int){
        CheckDeath()
        if(activeAbilityType == "move"){
            IA().PlaceHeroFromPosition(position)
        }else if (activeAbilityType == "attack"){
            IA().AttackFromPosition(position)
        }
        IA().notifyDataSetChanged()
        activeMarkers.clear()
        if(!isHeroDead){
            CheckDeath()
        }
    }
    fun SlowerStage(){
        if(!isHeroDead){
            IA().ClearLastMiss()
            IA().ActivateMonsters(false,myspd)
            IA().GenerateMonster()
            IA().notifyDataSetChanged()
            CheckDeath()

        }
    }
    fun CheckDeath(){
        if(IA().CheckPlayerDeath()){
            isHeroDead = true
            val simpleAlert = AlertDialog.Builder(activity).create()
            simpleAlert.setTitle("You were struck down")
            simpleAlert.setMessage("You fell in battle, but recovered eventually..")
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {}
            })
            simpleAlert.show()
            fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
        }
    }
    fun CheckCooldown(type: Int):Boolean{
        if(cooldowns[type]==0){
            return true
        }else return false
    }
    fun DecrementAllCooldowns(){
        for(v in cooldowns){
            val old = v.value
            if(old>0){
                cooldowns.put(v.key,old-1)
            }
        }
        val p = CP().equipped
        if(!(cooldowns[0]==0)){
            textView_head.setText("${p[0]?.name} (${cooldowns[0]})")
        }else textView_head.setText(p[0]?.name)
        if(!(cooldowns[1]==0)){
            textView_shoulders.setText("${p[1]?.name} (${cooldowns[1]})")
        }else textView_shoulders.setText(p[1]?.name)
        if(!(cooldowns[2]==0)){
            textView_legs.setText("${p[2]?.name} (${cooldowns[2]})")
        }else textView_legs.setText(p[2]?.name)
        if(!(cooldowns[3]==0)){
            textView_offhand.setText("${p[3]?.name} (${cooldowns[3]})")
        }else textView_offhand.setText(p[3]?.name)
        if(!(cooldowns[4]==0)){
            textView_mainhand.setText("${p[4]?.name} (${cooldowns[4]})")
        }else textView_mainhand.setText(p[4]?.name)
    }
    fun CP() = (activity as MainActivity).currentPlayer
    fun IA() = (gridView_adventure.adapter as ImageAdapter)
}