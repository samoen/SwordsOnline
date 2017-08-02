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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_online.*

class OnlineFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_online,container,false)
    }

    val MAX_PLAYERS_MINUS_ONE = 2
    var playerNumber = 0
    var playerReadies : MutableMap<Int,String> = mutableMapOf()
    var playerNames : MutableMap<Int,String> = mutableMapOf()
    var playerTypes : MutableMap<Int,String> = mutableMapOf()
    var playerSpeeds : MutableMap<Int,String> = mutableMapOf()
    var playerPositions : MutableMap<Int,String> = mutableMapOf()
    var playerLocations : MutableMap<Int,String> = mutableMapOf()


    lateinit var mDatabase: DatabaseReference

    var firstDatabaseRead = true
    var secondDatabaseRead = false
    var activeMarkers: MutableList<Pair<Int,Int>> = mutableListOf()
    var activeAbilityType: String = ""
    var isHeroDead: Boolean = false
    var activeSlot = 0
    var cooldowns = mutableMapOf<Int,Int>(0 to 0, 1 to 0, 2 to 0,3 to 0,4 to 0)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridView_online.adapter = OnlineImageAdapter(context)

        val p = (activity as MainActivity).currentPlayer.equipped
        button_head.setText(p[0]?.name)
        button_shoulders.setText(p[1]?.name)
        button_legs.setText(p[2]?.name)
        button_offHand.setText(p[3]?.name)
        button_mainHand.setText(p[4]?.name)

        mDatabase =  FirebaseDatabase.getInstance().getReference("GameRoom1")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
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
                            playerNumber = i
                            SetFireBaseEntry("Player${i}Name",(activity as MainActivity).currentPlayer.name)
                            break
                        }else if (i == MAX_PLAYERS_MINUS_ONE){
                            Toast.makeText(context,"Game Room 1 Full",Toast.LENGTH_SHORT).show()
                            fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
                        }
                    }
                    (gridView_online.adapter as OnlineImageAdapter).PutHeroToPosition(playerNumber+3,playerNumber)
                    (gridView_online.adapter as OnlineImageAdapter).notifyDataSetChanged()

                    for (i in playerLocations){
                        if (i.key != playerNumber && playerNames[i.key]!="NO_NAME"){
                            (gridView_online.adapter as OnlineImageAdapter).PutEnemyToPosition(i.value.toInt(),i.key)
                        }
                    }

                    SetFireBaseEntry("Player${playerNumber}Ready","NOT_READY")
                    SetFireBaseEntry("Player${playerNumber}Location", (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos[playerNumber].toString())
                    firstDatabaseRead = false
                    secondDatabaseRead = true
                }else if(this@OnlineFragment.activity != null) {
                    for (i in 0..MAX_PLAYERS_MINUS_ONE){
                        if(i != playerNumber){
                            if(playerNames[i] == "NO_NAME" && (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos.containsKey(i)){
                                (gridView_online.adapter as OnlineImageAdapter).PutEmptyAtPosition( (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos[i] )
                                (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos.remove(i)
                            }
                            if (playerNames[i] != "NO_NAME" && !(gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos.containsKey(i)){
                                (gridView_online.adapter as OnlineImageAdapter). PutEnemyToPosition( playerLocations[i]?.toInt()?:0,i)
                                (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos.put(i,playerLocations[i]?.toInt()?:0)
                            }
                        }
                    }
                    if(playerReadies[playerNumber]=="READY" && CheckOtherPlayersReadyOrWaitingOrNoName()){
                        (gridView_online.adapter as OnlineImageAdapter).RemoveMarkers(activeMarkers)
                        cooldowns.put(activeSlot,(activity as MainActivity).currentPlayer.equipped[activeSlot]?.cooldown?:0)
                        DecrementAllCooldowns()
                        val speedorder = playerSpeeds.values.sortedDescending()
                        var i = 1
                        for (v in speedorder){
                            Handler().postDelayed( {ActivateHero(v.toInt())},300*i.toLong() )
                            i++
                        }
                        SetFireBaseEntry("Player${playerNumber}Ready","WAITING")

                    }else if (playerReadies[playerNumber]=="WAITING" && CheckOtherPlayersWaitingOrNotReadyOrNoName()){
                        SetFireBaseEntry("Player${playerNumber}Ready","NOT_READY")
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
            (gridView_online.adapter as OnlineImageAdapter).RemoveMarkers(activeMarkers)
            SetFireBaseEntry("Player${playerNumber}Type","MOVE")
            SetFireBaseEntry("Player${playerNumber}Position",(gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos[playerNumber].toString())
            SetFireBaseEntry("Player${playerNumber}Speed","0")
            SetFireBaseEntry("Player${playerNumber}Ready","READY")
        }

        gridView_online.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, _view: View?, position: Int, id: Long) {
                if(cooldowns[activeSlot]==0){
                    if(activeMarkers.contains(CalculatePairFromPosition(position))){
                        SetFireBaseEntry("Player${playerNumber}Type",activeAbilityType.toUpperCase())
                        SetFireBaseEntry("Player${playerNumber}Position",position.toString())
                        SetFireBaseEntry("Player${playerNumber}Speed",(activity as MainActivity).currentPlayer.current_speed.toString())
                        SetFireBaseEntry("Player${playerNumber}Ready","READY")
                    }
                }else Toast.makeText(context,"Cooldown ${cooldowns[activeSlot]}",Toast.LENGTH_SHORT).show()
            }
        })

        button_backFromAdventure.setOnClickListener {
            val simpleAlert = AlertDialog.Builder(activity).create()
            simpleAlert.setTitle("Leave Adventure")
            simpleAlert.setMessage("Do you really want to leave this adventure?")
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    SetFireBaseEntry("Player${playerNumber}Name","NO_NAME")
                    fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
                }
            })
            simpleAlert.show()
        }
    }

    fun ActivateHero(pNum:Int){
        (gridView_online.adapter as OnlineImageAdapter).ClearLastMiss()
        (gridView_online.adapter as OnlineImageAdapter).notifyDataSetChanged()
        if (pNum != playerNumber){
            if (playerTypes[pNum] == "MOVE"){
                var illegalMovement = false
                for (i in 0..MAX_PLAYERS_MINUS_ONE){
                    if (i != pNum){ if ((gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos[i] == playerPositions[pNum]?.toInt()) illegalMovement = true }
                }
                if (!illegalMovement) (gridView_online.adapter as OnlineImageAdapter).PutEnemyToPosition(playerPositions[pNum]!!.toInt(),pNum)
            }else if (playerTypes[pNum]=="ATTACK" && pNum != playerNumber){
                if(playerPositions[pNum]!!.toInt() == (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos[playerNumber]){
                    SetFireBaseEntry("Player${playerNumber}Name","NO_NAME")
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
            (gridView_online.adapter as OnlineImageAdapter).notifyDataSetChanged()
        }else{
            val myPosition = playerPositions[playerNumber]!!.toInt()
            val otherPlayerLocations = (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos.filter { it.key != playerNumber }
            if (!(otherPlayerLocations.containsValue(playerPositions[pNum]?.toInt())&& playerTypes[playerNumber]=="MOVE")){
                if(playerTypes[playerNumber] == "MOVE"){
                    (gridView_online.adapter as OnlineImageAdapter).PutHeroToPosition(myPosition,playerNumber)
                    SetFireBaseEntry("Player${playerNumber}Location", (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos[playerNumber].toString())
                }else if (playerTypes[playerNumber] == "ATTACK"){
                    if((gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos.containsValue(myPosition) ){
                        (gridView_online.adapter as OnlineImageAdapter).PutHitToPosition(myPosition)
                        (activity as MainActivity).currentPlayer.gold++
                        (gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos.remove((gridView_online.adapter as OnlineImageAdapter).PlayersBoardPos.filter { it.value == myPosition }.keys.first())
                    }else{
                        (gridView_online.adapter as OnlineImageAdapter).PutMissToPosition(myPosition)
                        (gridView_online.adapter as OnlineImageAdapter).lastMiss = myPosition
                    }
                }
                (gridView_online.adapter as OnlineImageAdapter).notifyDataSetChanged()
                activeMarkers.clear()
            }
        }
    }
    fun CheckOtherPlayersReadyOrWaitingOrNoName():Boolean{
        var result = true
        for(v in 0..MAX_PLAYERS_MINUS_ONE){
            if(v == playerNumber){
                continue
            }else if (!(playerReadies[v] == "WAITING" || playerReadies[v]=="READY" || playerNames[v]=="NO_NAME")){
                result = false
            }
        }
        return result
    }
    fun CheckOtherPlayersWaitingOrNotReadyOrNoName():Boolean{
        var result = true
        for(v in 0..MAX_PLAYERS_MINUS_ONE){
            if(v == playerNumber) {
                continue
            }else if(playerNames[v]=="NO_NAME"){
                continue
            } else if (!(playerReadies[v] == "WAITING" || playerReadies[v]=="NOT_READY"|| playerNames[v]=="NO_NAME")){
                result = false
            }
        }
        return result
    }
    fun SelectAbility(slot: Int){
        val p = (activity as MainActivity).currentPlayer.equipped
        (activity as MainActivity).currentPlayer.current_speed=p.get(slot)?.ability?.speed?:0
        (gridView_online.adapter as OnlineImageAdapter).RemoveMarkers(activeMarkers)
        activeAbilityType = p.get(slot)?.ability?.type?:""
        (gridView_online.adapter as OnlineImageAdapter).PlaceMarkers(p.get(slot)?.ability?.relative_pairs?: listOf(),playerNumber)
        (gridView_online.adapter as OnlineImageAdapter).notifyDataSetChanged()
        activeMarkers = (gridView_online.adapter as OnlineImageAdapter).CalculatePairsFromRelative(p.get(slot)?.ability?.relative_pairs?: listOf(),playerNumber) as MutableList<Pair<Int, Int>>
    }
    fun DecrementAllCooldowns(){
        for(v in cooldowns){
            val old = v.value
            if(old>0) cooldowns.put(v.key,old-1)
        }
        val p = (activity as MainActivity).currentPlayer.equipped
        if(!(cooldowns[0]==0)){ button_head.setText("${p[0]?.name} (${cooldowns[0]})") }else button_head.setText(p[0]?.name)
        if(!(cooldowns[1]==0)){ button_shoulders.setText("${p[1]?.name} (${cooldowns[1]})") }else button_shoulders.setText(p[1]?.name)
        if(!(cooldowns[2]==0)){ button_legs.setText("${p[2]?.name} (${cooldowns[2]})") }else button_legs.setText(p[2]?.name)
        if(!(cooldowns[3]==0)){ button_offHand.setText("${p[3]?.name} (${cooldowns[3]})") }else button_offHand.setText(p[3]?.name)
        if(!(cooldowns[4]==0)){ button_mainHand.setText("${p[4]?.name} (${cooldowns[4]})") }else button_mainHand.setText(p[4]?.name)
    }
    fun SetFireBaseEntry(key: String, value: String ){
        mDatabase.child(key).setValue(value)
    }
    override fun onStop() {
        super.onStop()
        SetFireBaseEntry("Player${playerNumber}Name","NO_NAME")
    }
}