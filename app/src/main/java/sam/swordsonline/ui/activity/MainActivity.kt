package sam.swordsonline.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ninja.sakib.pultusorm.core.PultusORM
import sam.swordsonline.R
import sam.swordsonline.model.Item
import sam.swordsonline.model.ItemList
import sam.swordsonline.model.Player
import sam.swordsonline.model.hideKeyboard
import sam.swordsonline.ui.fragment.MainFragment

class MainActivity : AppCompatActivity() {

    class DbPlayer {
        var strength: Int = 0
        var dexterity: Int = 0
        var intelligence: Int = 0
        var gold: Int = 0
        var name: String = ""
        var helmet: Int = 0
        var shoulders: Int = 0
        var legs:Int = 0
        var offHand:Int=0
        var mainHand:Int=0
        var i5:Int=0
        var i6:Int=0
        var i7:Int=0
        var i8:Int=0
        var i9:Int=0
        var i10:Int=0
        var i11:Int=0
        var i12:Int=0
        var i13:Int=0
        var i14:Int=0
        var i15:Int=0
        var i16:Int=0
        var i17:Int=0
        var i18:Int=0
        var i19:Int=0
        var i20:Int=0
        var i21:Int=0
        var i22:Int=0
        var i23:Int=0
        var i24:Int=0
    }

    var currentPlayer = Player(gold = 3)
    val ItemList = ItemList()
    val appPath: String by lazy { this.applicationContext.getFilesDir().getAbsolutePath() }
    val pultusORM: PultusORM by lazy { PultusORM("swordonline.db", appPath) }

    val startingItems = mutableListOf<Item>(
            ItemList.allItems[0]?:Item(),
            ItemList.allItems[1]?:Item(),
            ItemList.allItems[2]?:Item(),
            ItemList.allItems[3]?:Item(),
            ItemList.allItems[4]?:Item()
    )

    val startingEquippedItems = mutableMapOf(
            0 to ItemList.allItems[0],
            1 to ItemList.allItems[1],
            2 to ItemList.allItems[2],
            3 to ItemList.allItems[3],
            4 to ItemList.allItems[4]
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        button_main.setOnClickListener {
            if(editText_player_name.text.isNotEmpty() && editText_player_name.text.toString() != "NO_NAME"){
                currentPlayer.name = editText_player_name.text.toString()
            }
            currentPlayer.items = startingItems
            currentPlayer.equipped = startingEquippedItems
            this.hideKeyboard()
            GoToMainFragment()
        }
        button_continueAdventure.setOnClickListener {
            val dbPlayers = pultusORM.find(DbPlayer())
            for(v in dbPlayers){
                val p = v as DbPlayer
                currentPlayer.gold = p.gold
                currentPlayer.strength = p.strength
                currentPlayer.dexterity = p.dexterity
                currentPlayer.intelligence = p.intelligence
                currentPlayer.equipped.put(0, ItemList.allItems[p.helmet]?:Item())
                currentPlayer.equipped.put(1, ItemList.allItems[p.shoulders]?:Item())
                currentPlayer.equipped.put(2, ItemList.allItems[p.legs]?:Item())
                currentPlayer.equipped.put(3, ItemList.allItems[p.offHand]?:Item())
                currentPlayer.equipped.put(4, ItemList.allItems[p.mainHand]?:Item())
                currentPlayer.name = p.name
                currentPlayer.items = startingItems
                if(p.i5 == 1) currentPlayer.items.add(ItemList.allItems[5]?:Item())
                if(p.i6 == 1) currentPlayer.items.add(ItemList.allItems[6]?:Item())
                if(p.i7 == 1) currentPlayer.items.add(ItemList.allItems[7]?:Item())
                if(p.i8 == 1) currentPlayer.items.add(ItemList.allItems[8]?:Item())
                if(p.i9 == 1) currentPlayer.items.add(ItemList.allItems[9]?:Item())
                if(p.i10 == 1) currentPlayer.items.add(ItemList.allItems[10]?:Item())
                if(p.i11 == 1) currentPlayer.items.add(ItemList.allItems[11]?:Item())
                if(p.i12 == 1) currentPlayer.items.add(ItemList.allItems[12]?:Item())
                if(p.i13 == 1) currentPlayer.items.add(ItemList.allItems[13]?:Item())
                if(p.i14 == 1) currentPlayer.items.add(ItemList.allItems[14]?:Item())
                if(p.i15 == 1) currentPlayer.items.add(ItemList.allItems[15]?:Item())
                if(p.i16 == 1) currentPlayer.items.add(ItemList.allItems[16]?:Item())
                if(p.i17 == 1) currentPlayer.items.add(ItemList.allItems[17]?:Item())
                if(p.i18 == 1) currentPlayer.items.add(ItemList.allItems[18]?:Item())
                if(p.i19 == 1) currentPlayer.items.add(ItemList.allItems[19]?:Item())
                if(p.i20 == 1) currentPlayer.items.add(ItemList.allItems[20]?:Item())
                if(p.i21 == 1) currentPlayer.items.add(ItemList.allItems[21]?:Item())
                if(p.i22 == 1) currentPlayer.items.add(ItemList.allItems[22]?:Item())
                if(p.i23 == 1) currentPlayer.items.add(ItemList.allItems[23]?:Item())
                if(p.i24 == 1) currentPlayer.items.add(ItemList.allItems[24]?:Item())
            }
            GoToMainFragment()
        }
    }
    fun GoToMainFragment(){
        fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
        button_main.visibility = View.GONE
        editText_player_name.visibility = View.GONE
        button_continueAdventure.visibility = View.GONE
        imageView.visibility = View.GONE
    }
    fun SavePlayer(){
        pultusORM.delete(DbPlayer())
        pultusORM.drop(DbPlayer())
        val dbp: DbPlayer = DbPlayer()
        dbp.dexterity = currentPlayer.dexterity
        dbp.strength = currentPlayer.strength
        dbp.intelligence = currentPlayer.intelligence
        dbp.gold = currentPlayer.gold
        dbp.name = currentPlayer.name

        dbp.helmet = currentPlayer.equippedKeys[0]?:0
        dbp.shoulders = currentPlayer.equippedKeys[1]?:0
        dbp.legs = currentPlayer.equippedKeys[2]?:0
        dbp.offHand = currentPlayer.equippedKeys[3]?:0
        dbp.mainHand = currentPlayer.equippedKeys[4]?:0

        for(v in currentPlayer.items){
            when(v.name){
                ItemList.allItems[5]?.name -> dbp.i5 = 1
                ItemList.allItems[6]?.name -> dbp.i6 = 1
                ItemList.allItems[7]?.name -> dbp.i7 = 1
                ItemList.allItems[8]?.name -> dbp.i8 = 1
                ItemList.allItems[9]?.name -> dbp.i9 = 1
                ItemList.allItems[10]?.name -> dbp.i10 = 1
                ItemList.allItems[11]?.name -> dbp.i11 = 1
                ItemList.allItems[12]?.name -> dbp.i12 = 1
                ItemList.allItems[13]?.name -> dbp.i13 = 1
                ItemList.allItems[14]?.name -> dbp.i14 = 1
                ItemList.allItems[15]?.name -> dbp.i15 = 1
                ItemList.allItems[16]?.name -> dbp.i16 = 1
                ItemList.allItems[17]?.name -> dbp.i17 = 1
                ItemList.allItems[18]?.name -> dbp.i18 = 1
                ItemList.allItems[19]?.name -> dbp.i19 = 1
                ItemList.allItems[20]?.name -> dbp.i20 = 1
                ItemList.allItems[21]?.name -> dbp.i21 = 1
                ItemList.allItems[22]?.name -> dbp.i22 = 1
                ItemList.allItems[23]?.name -> dbp.i23 = 1
                ItemList.allItems[24]?.name -> dbp.i24 = 1
            }
        }

        pultusORM.save(dbp)
        Toast.makeText(this,"DbPlayer Saved",Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        pultusORM.close()
    }
}
