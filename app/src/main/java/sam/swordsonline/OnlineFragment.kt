package sam.swordsonline

import android.app.Fragment
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_online.*

class OnlineFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_online,container,false)
    }

    val MAX_PLAYERS_MINUS_ONE = 2
    val PLAYER_KILL_REWARD = 5
    var playerNumber = 0
    var playerReadies : MutableMap<Int,String> = mutableMapOf()
    var playerNames : MutableMap<Int,String> = mutableMapOf()
    var playerTypes : MutableMap<Int,String> = mutableMapOf()
    var playerSpeeds : MutableMap<Int,String> = mutableMapOf()
    var playerPositions : MutableMap<Int,String> = mutableMapOf()
    var playerLocations : MutableMap<Int,String> = mutableMapOf()
    var activeMarkers: MutableList<Pair<Int,Int>> = mutableListOf()
    var activeAbilityType: String = ""
    var firstDatabaseRead = true
    var isHeroDead: MutableMap<Int,Boolean> = mutableMapOf()
    var activeSlot = 0
    var cooldowns = mutableMapOf<Int,Int>(0 to 0, 1 to 0, 2 to 0,3 to 0,4 to 0)
    var mDatabase: DatabaseReference? = null
    var mProgressDialog: ProgressDialog? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressDialog()

        ClickableButtons(false)

        gridView_adventure.adapter = OnlineImageAdapter(activity)

        button_head.setText(CP().equipped[0]?.name)
        button_shoulders.setText(CP().equipped[1]?.name)
        button_legs.setText(CP().equipped[2]?.name)
        button_offHand.setText(CP().equipped[3]?.name)
        button_mainHand.setText(CP().equipped[4]?.name)

        mDatabase =  FirebaseDatabase.getInstance().getReference("GameRoom1")
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
                    }
                    if(firstDatabaseRead){
                        for(i in 0..MAX_PLAYERS_MINUS_ONE){
                            if (playerNames[i]=="NO_NAME"){
                                val startpos = 99-((i+1)*3)
                                playerNumber = i
                                FB("Player${i}Location", startpos.toString())
                                FB("Player${i}Ready","NOT_READY")
                                FB("Player${i}Name",CP().name)
                                IA().PutHeroToPosition(startpos,i)
                                break
                            }else if (i == MAX_PLAYERS_MINUS_ONE){
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
                        IA().notifyDataSetChanged()

                    }else if(!firstDatabaseRead){

                        val activePlayerNames = playerNames.filter { it.value != "NO_NAME" }
                        val activePlayerSpeeds = playerSpeeds.filter { activePlayerNames.containsKey(it.key) }
                        val speedSortedPlayerNumbers = activePlayerSpeeds.toList().sortedByDescending { (_, v) -> v }.toMap()

                        val mpos = playerPositions
                        val mtyp = playerTypes

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
                                for (v in speedSortedPlayerNumbers){
                                    Handler().postDelayed( {ActivateHero(v.key,mpos[v.key]?.toInt(),mtyp[v.key])},300*inc.toLong() )
                                    inc++
                                }
                                Handler().postDelayed({ClickableButtons(true)},300*activePlayerNames.size.toLong())
                                FB("Player${playerNumber}Ready","WAITING")
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
                            }

                        }else if (playerReadies[playerNumber]=="NOT_READY"){
                            for (i in 0..MAX_PLAYERS_MINUS_ONE){
                                if(i != playerNumber){
                                    if(playerNames[i] == "NO_NAME" && IA().PlayersBoardPos.containsKey(i)){
                                        Toast.makeText(context,getString(R.string.left),Toast.LENGTH_SHORT).show()
                                        IA().PutEmptyAtPosition( IA().PlayersBoardPos[i] )
                                        IA().PlayersBoardPos.remove(i)
                                        IA().notifyDataSetChanged()
                                    }
                                    if (playerNames[i] != "NO_NAME" && !IA().PlayersBoardPos.containsKey(i)){
                                        Toast.makeText(context,"${playerNames[i]} ${getString(R.string.joined)}",Toast.LENGTH_SHORT).show()
                                        IA().PutEnemyToPosition( playerLocations[i]?.toInt()?:0,i)
                                        IA().PlayersBoardPos.put(i,playerLocations[i]?.toInt()?:0)
                                        IA().notifyDataSetChanged()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })

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
            activeSlot = 5
            IA().RemoveMarkers(activeMarkers,playerNumber)
            FB("Player${playerNumber}Type","MOVE")
            FB("Player${playerNumber}Position",IA().PlayersBoardPos[playerNumber].toString())
            FB("Player${playerNumber}Speed","0")
            FB("Player${playerNumber}Ready","READY")
            ClickableButtons(false)

        }

        gridView_adventure.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, _view: View?, position: Int, id: Long) {
                if(activeMarkers.contains(CalculatePairFromPosition(position))){
                    if(cooldowns[activeSlot]==0){
                        IA().RemoveMarkers(activeMarkers,playerNumber)
                        FB("Player${playerNumber}Type",activeAbilityType.toUpperCase())
                        FB("Player${playerNumber}Position",position.toString())
                        FB("Player${playerNumber}Speed",CP().current_speed.toString())
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
                    FB("Player${playerNumber}Name","NO_NAME")
                    fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commitAllowingStateLoss()
                }
            })
            simpleAlert.show()
        }
    }

    fun ActivateHero(pNum:Int,pos:Int?,type:String?){
        if(this@OnlineFragment.activity != null && isHeroDead[pNum] != true){
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

            if (!IA().PlayersBoardPos.containsValue(pos) && type == "ATTACK"){
                IA().PutMissToPosition(pos?:0)
                IA().lastMiss = pos
            } else if (!illegalMovement){
                if (pNum != playerNumber){
                    if (type=="ATTACK"){
                        if(pos == IA().PlayersBoardPos[playerNumber]){
                            FB("Player${playerNumber}Name","NO_NAME")
                            isHeroDead[playerNumber] = true
                            val simpleAlert = AlertDialog.Builder(activity).create()
                            simpleAlert.setTitle(getString(R.string.struck_down))
                            simpleAlert.setMessage(getString(R.string.you_survive))
                            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), object : DialogInterface.OnClickListener{
                                override fun onClick(dialog: DialogInterface?, which: Int) {}
                            })
                            simpleAlert.show()
                            CP().gold = CP().gold - PLAYER_KILL_REWARD
                            fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
                        }
                    } else if (IA().PlayersBoardPos.containsKey(pNum)){
                        IA().PutEnemyToPosition(pos?:0,pNum)
                    }
                }else if (pNum == playerNumber){
                    if (type == "ATTACK" && IA().PlayersBoardPos.containsValue(pos)){
                        isHeroDead[IA().PlayersBoardPos.filter{ it.value == pos }.keys.first()] = true
                        IA().PutHitToPosition(pos?:0)
                        CP().gold = CP().gold + PLAYER_KILL_REWARD
                    }else if(type == "MOVE"){
                        IA().PutHeroToPosition(pos?:0,pNum)
                        FB("Player${pNum}Location", IA().PlayersBoardPos[pNum].toString())
                    }
                }
                IA().notifyDataSetChanged()
            }
        }
    }

    fun SelectAbility(slot: Int){
        IA().RemoveMarkers(activeMarkers,playerNumber)
        CP().current_speed=CP().equipped.get(slot)?.ability?.speed?:0
        activeAbilityType = CP().equipped.get(slot)?.ability?.type?:""
        IA().PlaceMarkers(CP().equipped.get(slot)?.ability?.relative_pairs?: listOf(),playerNumber)
        IA().notifyDataSetChanged()
        activeMarkers = IA().CalculatePairsFromRelative(CP().equipped.get(slot)?.ability?.relative_pairs?: listOf(),playerNumber) as MutableList<Pair<Int, Int>>
    }
    fun DecrementAllCooldowns(){
        for(v in cooldowns){
            val old = v.value
            if(old>0) cooldowns.put(v.key,old-1)
        }
        if(!(cooldowns[0]==0)){ button_head.setText("${CP().equipped[0]?.name} (${cooldowns[0]})") }else button_head.setText(CP().equipped[0]?.name)
        if(!(cooldowns[1]==0)){ button_shoulders.setText("${CP().equipped[1]?.name} (${cooldowns[1]})") }else button_shoulders.setText(CP().equipped[1]?.name)
        if(!(cooldowns[2]==0)){ button_legs.setText("${CP().equipped[2]?.name} (${cooldowns[2]})") }else button_legs.setText(CP().equipped[2]?.name)
        if(!(cooldowns[3]==0)){ button_offHand.setText("${CP().equipped[3]?.name} (${cooldowns[3]})") }else button_offHand.setText(CP().equipped[3]?.name)
        if(!(cooldowns[4]==0)){ button_mainHand.setText("${CP().equipped[4]?.name} (${cooldowns[4]})") }else button_mainHand.setText(CP().equipped[4]?.name)
    }
    fun FB(key: String, value: String ){
        mDatabase?.child(key)?.setValue(value)
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
        if(this@OnlineFragment.activity != null){
            button_head.isEnabled = clickable
            button_shoulders.isEnabled = clickable
            button_legs.isEnabled = clickable
            button_mainHand.isEnabled = clickable
            button_offHand.isEnabled = clickable
            button_wait.isEnabled = clickable
            button_backFromAdventure.isEnabled = clickable
        }
    }
    override fun onStop() {
        super.onStop()
        FB("Player${playerNumber}Name","NO_NAME")
        FB("Player${playerNumber}Location","0")
        FB("Player${playerNumber}Type","MOVE")
        FB("Player${playerNumber}Position","0")
        FB("Player${playerNumber}Speed","0")
        FB("Player${playerNumber}Ready","NOT_READY")
    }
    fun CP() = (activity as MainActivity).currentPlayer
    fun IA() = (gridView_adventure.adapter as OnlineImageAdapter)
}