package sam.swordsonline

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

class AdventureFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_adventure,container,false)
    }

    var activeMarkers: MutableList<Pair<Int,Int>> = mutableListOf()
    var activeAbilityType: String = ""
    var isHeroDead: Boolean = false
    var activeSlot = 5

    var cooldowns = mutableMapOf<Int,Int>(0 to 0, 1 to 0, 2 to 0,3 to 0,4 to 0)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CP().location = Pair(1,1)

        val p = CP().equipped

        button_head.setText(p[0]?.name)
        button_shoulders.setText(p[1]?.name)
        button_legs.setText(p[2]?.name)
        button_offHand.setText(p[3]?.name)
        button_mainHand.setText(p[4]?.name)

        button_head.setOnClickListener {
            activeSlot =0
            SelectAbility(activeSlot)
        }
        button_shoulders.setOnClickListener {
            activeSlot=1
            SelectAbility(activeSlot)
        }
        button_legs.setOnClickListener {
            activeSlot=2
            SelectAbility(activeSlot)
        }
        button_offHand.setOnClickListener {
            activeSlot = 3
            SelectAbility(activeSlot)
        }
        button_mainHand.setOnClickListener {
            activeSlot = 4
            SelectAbility(activeSlot)
        }
        button_wait.setOnClickListener {
            IA().RemoveMarkers(activeMarkers)
            IA().ActivateMonsters(true)
            IA().ActivateMonsters(false)
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
                        IA().ActivateMonsters(true)
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
        CP().current_speed=p.get(slot)?.ability?.speed?:0
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
            IA().ActivateMonsters(false)
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
            button_head.setText("${p[0]?.name} (${cooldowns[0]})")
        }else button_head.setText(p[0]?.name)
        if(!(cooldowns[1]==0)){
            button_shoulders.setText("${p[1]?.name} (${cooldowns[1]})")
        }else button_shoulders.setText(p[1]?.name)
        if(!(cooldowns[2]==0)){
            button_legs.setText("${p[2]?.name} (${cooldowns[2]})")
        }else button_legs.setText(p[2]?.name)
        if(!(cooldowns[3]==0)){
            button_offHand.setText("${p[3]?.name} (${cooldowns[3]})")
        }else button_offHand.setText(p[3]?.name)
        if(!(cooldowns[4]==0)){
            button_mainHand.setText("${p[4]?.name} (${cooldowns[4]})")
        }else button_mainHand.setText(p[4]?.name)
    }
    fun CP() = (activity as MainActivity).currentPlayer
    fun IA() = (gridView_adventure.adapter as ImageAdapter)
}