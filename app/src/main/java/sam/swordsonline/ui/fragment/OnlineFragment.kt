package sam.swordsonline.ui.fragment

import android.app.Fragment
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Chronometer
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_online.*
import sam.swordsonline.R
import sam.swordsonline.adapter.OnlineImageAdapter
import sam.swordsonline.model.CalculatePairFromPosition
import sam.swordsonline.model.FoldingTabBar
import sam.swordsonline.model.Item
import sam.swordsonline.ui.activity.MainActivity

class OnlineFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_online,container,false)
    }

    val MAX_PLAYERS_MINUS_ONE = 2
    val ANIMATION_TIME = 300
    var playerNumber = 0
    var playerReadies : MutableMap<Int,String> = mutableMapOf()
    var playerNames : MutableMap<Int,String> = mutableMapOf()
    var playerTypes : MutableMap<Int,String> = mutableMapOf()
    var playerSpeeds : MutableMap<Int,String> = mutableMapOf()
    var playerPositions : MutableMap<Int,String> = mutableMapOf()
    var playerLocations : MutableMap<Int,String> = mutableMapOf()
    var playerLoots : MutableMap<Int,String> = mutableMapOf()
    var activeAbilityType: String = ""
    var firstDatabaseRead = true
    var isHeroDead: MutableMap<Int,Boolean> = mutableMapOf()
    var activeSlot = 0
    var cooldowns = mutableMapOf<Int,Int>(0 to 0, 1 to 0, 2 to 0,3 to 0,4 to 0)
    var mDatabase: DatabaseReference? = null
    var mProgressDialog: ProgressDialog? = null
    var myspd = 0
    var roomFull = false
    var exited = false
    var Loot: MutableMap<Int,String> = mutableMapOf()
    var myLootSlot = 0
    var lockPlayers = false

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressDialog()
        StartFB(CP().room)

        chronometer_time.base = SystemClock.elapsedRealtime()
        chronometer_time.onChronometerTickListener = object: Chronometer.OnChronometerTickListener {
            override fun onChronometerTick(chronometer: Chronometer?) {
                if(chronometer?.text == "00:10"){
                    Wait()
                }
            }
        }

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
                        Wait()
                    }
                }
                return false
            }
        }





        gridView_online.adapter = OnlineImageAdapter(activity)

        gridView_online.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, _view: View?, position: Int, id: Long) {
                if(IA().activeMarkers.contains(CalculatePairFromPosition(position))){
                    if(cooldowns[activeSlot]==0){
                        IA().RemoveMarkers()
                        FB("Player${playerNumber}Type",activeAbilityType.toUpperCase())
                        FB("Player${playerNumber}Position",position.toString())
                        FB("Player${playerNumber}Speed",myspd.toString())
                        FB("Player${playerNumber}Ready","READY")
                        ClickableButtons(false)
                    }else  Toast.makeText(context,"Cooldown ${cooldowns[activeSlot]}",Toast.LENGTH_SHORT).show()
                }
            }
        })

        button_backFromAdventure.setOnClickListener {
            val simpleAlert = AlertDialog.Builder(activity).create()
            simpleAlert.setTitle(getString(R.string.leave_battle))
            simpleAlert.setMessage(getString(R.string.want_to_leave_battle))
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.leave_battle), object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    LeaveFB()
                    fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commitAllowingStateLoss()
                }
            })
            simpleAlert.show()
        }
    }

    fun ActivateHero(pNum:Int,pos:Int?,type:String?){
        if(isHeroDead[pNum] != true){
            IA().ClearLastMiss()
            IA().notifyDataSetChanged()
            var illegalMovement = false
            for (i in 0..MAX_PLAYERS_MINUS_ONE){
                if (i != pNum){
                    if (IA().PlayersBoardPos[i] == pos && type == "MOVE"){
                        illegalMovement = true
                        Toast.makeText(context,getString(R.string.move_blocked),Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (!illegalMovement){
                if (pNum != playerNumber){
                    if (type=="ATTACK"){
                        if(pos == IA().PlayersBoardPos[playerNumber]){
                            LeaveFB()
                            isHeroDead[playerNumber] = true
                            val simpleAlert = AlertDialog.Builder(activity).create()
                            simpleAlert.setTitle(getString(R.string.struck_down))
                            simpleAlert.setMessage(getString(R.string.you_survive))
                            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), object : DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {}
                            })
                            simpleAlert.show()
                            CP().items.removeAll { it.id == Loot[playerNumber]?.toInt() }
                            CP().equipped.remove(myLootSlot)
                            CP().equipped.set(myLootSlot,IL()[myLootSlot])
                            fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
                        }else if(IA().PlayersBoardPos.containsValue(pos)){
                            val enemyKey = IA().PlayersBoardPos.filter{ it.value == pos }.keys.firstOrNull()
                            IA().PutLootBag(pos?:0,Loot[enemyKey]?.toInt()?:0)
                        }else if(!IA().PlayersBoardPos.containsValue(pos)){
                            IA().PutMissToPosition(pos?:0)
                        }
                    } else if (type == "MOVE" && IA().PlayersBoardPos.containsKey(pNum)){
                        IA().PutEnemyToPosition(pos?:0,pNum)
                    }
                }else if (pNum == playerNumber){
                    val enemyKey = IA().PlayersBoardPos.filter{ it.value == pos }.keys.firstOrNull()
                    if (type == "ATTACK"){
                        if(IA().PlayersBoardPos.containsValue(pos)){
                            isHeroDead[enemyKey?:0] = true
                            IA().PutHitToPosition(pos?:0)
                            IA().KillEnemy(enemyKey?:0)
                            IA().PutLootBag(pos?:0,Loot[enemyKey]?.toInt()?:0)
                        }else if(!IA().PlayersBoardPos.containsValue(pos)){
                            IA().PutMissToPosition(pos?:0)
                        }
                    }else if(type == "MOVE"){
                        IA().PutHeroToPosition(pos?:0,pNum)
                        FB("Player${pNum}Location", IA().PlayersBoardPos[pNum].toString())
                        if (IA().lootBags.containsKey(pos)){
                            Toast.makeText(context,"Picked up a ${IL()[IA().lootBags[pos]]}",Toast.LENGTH_LONG).show()
                            CP().items.add(IL()[IA().lootBags[pos]]?: Item())
                            IA().RemoveLootBag(pos?:0)
                        }
                        if (pos == IA().cavePos){
                            CP().room = "GameRoom2"
                            LeaveFB()
                            fragmentManager.beginTransaction().replace(R.id.framelayout_main, OnlineFragment()).commitAllowingStateLoss()
                        }
                    }
                }
                IA().notifyDataSetChanged()
            }
        }
    }


    fun StartFB(room: String){
        mDatabase =  FirebaseDatabase.getInstance().getReference(room)
        mDatabase?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(this@OnlineFragment.activity != null){
                    for(i in 0..MAX_PLAYERS_MINUS_ONE){
                        playerReadies.put(i, dataSnapshot.child("Player${i}Ready").getValue(String::class.java).toString())
                        playerNames.put(i, dataSnapshot.child("Player${i}Name").getValue(String::class.java).toString())
                        playerPositions.put(i, dataSnapshot.child("Player${i}Position").getValue(String::class.java).toString())
                        playerSpeeds.put(i, dataSnapshot.child("Player${i}Speed").getValue(String::class.java).toString())
                        playerTypes.put(i, dataSnapshot.child("Player${i}Type").getValue(String::class.java).toString())
                        playerLocations.put(i, dataSnapshot.child("Player${i}Location").getValue(String::class.java).toString())
                        playerLoots.put(i, dataSnapshot.child("Player${i}Loot").getValue(String::class.java).toString())
                    }
                    if(firstDatabaseRead){
                        for(i in 0..MAX_PLAYERS_MINUS_ONE){
                            if (playerNames[i]=="NO_NAME"){
                                val startpos = 99-((i+1)*3)
                                playerNumber = i
                                FB("Player${i}Location", startpos.toString())
                                FB("Player${i}Ready","NOT_READY")
                                FB("Player${i}Loot",CalculateMyLoot().toString())
                                FB("Player${i}Name",CP().name)
                                IA().PutHeroToPosition(startpos,i)
                                break
                            }else if (i == MAX_PLAYERS_MINUS_ONE){
                                roomFull = true
                                Toast.makeText(context,"Game Room 1 Full",Toast.LENGTH_SHORT).show()
                                fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
                            }
                        }
                        for (i in playerLocations){
                            if (i.key != playerNumber && playerNames[i.key]!="NO_NAME"){
                                IA().PutEnemyToPosition(i.value.toInt(),i.key)
                            }
                        }
                        firstDatabaseRead = false
                        ClickableButtons(true)
                        hideProgressDialog()

                    }else if(!firstDatabaseRead){

                        val activePlayerNames = playerNames.filter { it.value != "NO_NAME" }
                        val activePlayerSpeeds = playerSpeeds.filter { activePlayerNames.containsKey(it.key) }
                        val speedSortedPlayerNumbers = activePlayerSpeeds.toList().sortedByDescending { (_, v) -> v }.toMap()

                        val mpos = playerPositions
                        val mtyp = playerTypes
                        Loot = playerLoots

                        if(playerReadies[playerNumber]=="READY"){
                            var otherPlayersReadyOrWaiting = true
                            for(v in 0..MAX_PLAYERS_MINUS_ONE){
                                if(v != playerNumber && playerReadies[v] != "WAITING" && playerReadies[v] != "READY" && playerNames[v]!="NO_NAME"){
                                    otherPlayersReadyOrWaiting = false
                                }
                            }
                            if(otherPlayersReadyOrWaiting){
                                if (activeSlot != 5){
                                    cooldowns.put(activeSlot,CP().equipped[activeSlot]?.cooldown?:0)
                                }
                                DecrementAllCooldowns()
                                var inc = 0
                                lockPlayers = true
                                for (v in speedSortedPlayerNumbers){
                                    Handler().postDelayed( {
                                        if(this@OnlineFragment.activity != null){
                                            ActivateHero(v.key,mpos[v.key]?.toInt(),mtyp[v.key])
                                        }
                                    },ANIMATION_TIME*inc.toLong() )
                                    inc++
                                }
                                Handler().postDelayed({
                                    if(this@OnlineFragment.activity != null){
                                        FB("Player${playerNumber}Ready","WAITING")
                                    }
                                },ANIMATION_TIME*activePlayerNames.size.toLong())
                            }

                        }else if (playerReadies[playerNumber]=="WAITING"){
                            var result = true
                            for(v in 0..MAX_PLAYERS_MINUS_ONE){
                                if(v != playerNumber && playerReadies[v] != "WAITING" && playerReadies[v]!="NOT_READY" && playerNames[v]!="NO_NAME"){
                                    result = false
                                }
                            }
                            if (result){
                                FB("Player${playerNumber}Ready","NOT_READY")
                                ClickableButtons(true)
                                IA().ClearLastMiss()
                                lockPlayers = false
                            }

                        }else if (playerReadies[playerNumber]=="NOT_READY"){
                            for (i in 0..MAX_PLAYERS_MINUS_ONE){
                                if(i != playerNumber && !lockPlayers){
                                    if(playerNames[i] == "NO_NAME" && IA().PlayersBoardPos.containsKey(i)){
                                        Toast.makeText(context,getString(R.string.left),Toast.LENGTH_SHORT).show()
                                        IA().RemoveEnemy(i)
                                    }
                                    if (playerNames[i] != "NO_NAME" && !IA().PlayersBoardPos.containsKey(i)){
                                        Toast.makeText(context,"${playerNames[i]} ${getString(R.string.joined)}",Toast.LENGTH_SHORT).show()
                                        IA().PutEnemyToPosition( playerLocations[i]?.toInt()?:0,i)
                                        Loot.put(i,playerLoots[i].toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
    fun LeaveFB(){
        FB("Player${playerNumber}Name","NO_NAME")
        FB("Player${playerNumber}Location","0")
        FB("Player${playerNumber}Type","NO_TYPE")
        FB("Player${playerNumber}Position","0")
        FB("Player${playerNumber}Speed","0")
        FB("Player${playerNumber}Ready","NO_READY")
        FB("Player${playerNumber}Loot","0")

        CP().room = "GameRoom1"

    }

    fun FB(key: String, value: String ){
        mDatabase?.child(key)?.setValue(value)
    }
    fun SelectAbility(slot: Int){
        IA().RemoveMarkers()
        myspd=CP().equipped.get(slot)?.ability?.speed?:0
        activeAbilityType = CP().equipped.get(slot)?.ability?.type?:""
        IA().PlaceMarkers(CP().equipped.get(slot)?.ability?.relative_pairs?: listOf(),playerNumber)
    }
    fun Wait(){
        activeSlot = 5
        IA().RemoveMarkers()
        FB("Player${playerNumber}Type","WAIT")
        FB("Player${playerNumber}Ready","READY")
        ClickableButtons(false)
    }
    fun DecrementAllCooldowns(){
        for(v in cooldowns){
            val old = v.value
            if(old>0) cooldowns.put(v.key,old-1)
        }
    }
    fun showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(context)
            mProgressDialog!!.setMessage(getString(R.string.list_loading))
            mProgressDialog!!.isIndeterminate = true
        }
        mProgressDialog!!.show()
    }

    fun hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }
    fun ClickableButtons(clickable:Boolean){
        if(clickable){
            ShowCooldownTextViews(false)
            folding_tab_bar.expand()
            chronometer_time.base = SystemClock.elapsedRealtime()
            chronometer_time.start()
        }else if(!clickable){
            ShowCooldownTextViews(true)
            folding_tab_bar.rollUp()
            chronometer_time.stop()
        }
    }
    fun ShowCooldownTextViews(showBlank:Boolean){
        if (showBlank){
            textView_offhand.setText("")
            textView_head.setText("")
            textView_legs.setText("")
            textView_mainhand.setText("")
            textView_shoulders.setText("")
        }else if (!showBlank){
            if(cooldowns[0]!=0){ textView_head.setText("${cooldowns[0]}") }else textView_head.setText("")
            if(cooldowns[1]!=0){ textView_shoulders.setText("${cooldowns[1]}") }else textView_shoulders.setText("")
            if(cooldowns[2]!=0){ textView_legs.setText("${cooldowns[2]}") }else textView_legs.setText("")
            if(cooldowns[3]!=0){ textView_offhand.setText("${cooldowns[3]}") }else textView_offhand.setText("")
            if(cooldowns[4]!=0){ textView_mainhand.setText("${cooldowns[4]}") }else textView_mainhand.setText("")
        }
    }

    fun CalculateMyLoot():Int{
        var mostExpensiveId = CP().equipped[0]?.id
        for (k in CP().equipped){
            val mEquip = k.value?.price?:0
            val mCompare = IL()[mostExpensiveId]?.price?:0
            if (mEquip >  mCompare){
                mostExpensiveId = k.value?.id
                myLootSlot = k.key
            }
        }
        return mostExpensiveId?:0
    }
    override fun onStop() {
        super.onStop()
        if (!roomFull){
            LeaveFB()
        }
        exited = true
    }

    override fun onResume() {
        super.onResume()
        if (exited){
            fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
        }
    }


    fun CP() = (activity as MainActivity).currentPlayer
    fun IA() = (gridView_online.adapter as OnlineImageAdapter)
    fun IL() = (activity as MainActivity).ItemList.allItems
}