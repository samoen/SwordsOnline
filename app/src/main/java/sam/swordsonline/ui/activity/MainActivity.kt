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
    val shopItems = ItemList()
    val appPath: String by lazy { this.applicationContext.getFilesDir().getAbsolutePath() }
    val pultusORM: PultusORM by lazy { PultusORM("swordonline.db", appPath) }

    val startingItems = mutableListOf<Item>(
            shopItems.allItems[0]?:Item(),
            shopItems.allItems[1]?:Item(),
            shopItems.allItems[2]?:Item(),
            shopItems.allItems[3]?:Item(),
            shopItems.allItems[4]?:Item()
    )

    val startingEquippedItems = mutableMapOf(
            0 to shopItems.allItems[0],
            1 to shopItems.allItems[1],
            2 to shopItems.allItems[2],
            3 to shopItems.allItems[3],
            4 to shopItems.allItems[4]
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
                currentPlayer.equipped.put(0,shopItems.allItems[p.helmet]?:Item())
                currentPlayer.equipped.put(1,shopItems.allItems[p.shoulders]?:Item())
                currentPlayer.equipped.put(2,shopItems.allItems[p.legs]?:Item())
                currentPlayer.equipped.put(3,shopItems.allItems[p.offHand]?:Item())
                currentPlayer.equipped.put(4,shopItems.allItems[p.mainHand]?:Item())
                currentPlayer.name = p.name
                currentPlayer.items = startingItems
                if(p.i5 == 1) currentPlayer.items.add(shopItems.allItems[5]?:Item())
                if(p.i6 == 1) currentPlayer.items.add(shopItems.allItems[6]?:Item())
                if(p.i7 == 1) currentPlayer.items.add(shopItems.allItems[7]?:Item())
                if(p.i8 == 1) currentPlayer.items.add(shopItems.allItems[8]?:Item())
                if(p.i9 == 1) currentPlayer.items.add(shopItems.allItems[9]?:Item())
                if(p.i10 == 1) currentPlayer.items.add(shopItems.allItems[10]?:Item())
                if(p.i11 == 1) currentPlayer.items.add(shopItems.allItems[11]?:Item())
                if(p.i12 == 1) currentPlayer.items.add(shopItems.allItems[12]?:Item())
                if(p.i13 == 1) currentPlayer.items.add(shopItems.allItems[13]?:Item())
                if(p.i14 == 1) currentPlayer.items.add(shopItems.allItems[14]?:Item())
                if(p.i15 == 1) currentPlayer.items.add(shopItems.allItems[15]?:Item())
                if(p.i16 == 1) currentPlayer.items.add(shopItems.allItems[16]?:Item())
                if(p.i17 == 1) currentPlayer.items.add(shopItems.allItems[17]?:Item())
                if(p.i18 == 1) currentPlayer.items.add(shopItems.allItems[18]?:Item())
                if(p.i19 == 1) currentPlayer.items.add(shopItems.allItems[19]?:Item())
                if(p.i20 == 1) currentPlayer.items.add(shopItems.allItems[20]?:Item())
                if(p.i21 == 1) currentPlayer.items.add(shopItems.allItems[21]?:Item())
                if(p.i22 == 1) currentPlayer.items.add(shopItems.allItems[22]?:Item())
                if(p.i23 == 1) currentPlayer.items.add(shopItems.allItems[23]?:Item())
                if(p.i24 == 1) currentPlayer.items.add(shopItems.allItems[24]?:Item())
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
                shopItems.allItems[5]?.name -> dbp.i5 = 1
                shopItems.allItems[6]?.name -> dbp.i6 = 1
                shopItems.allItems[7]?.name -> dbp.i7 = 1
                shopItems.allItems[8]?.name -> dbp.i8 = 1
                shopItems.allItems[9]?.name -> dbp.i9 = 1
                shopItems.allItems[10]?.name -> dbp.i10 = 1
                shopItems.allItems[11]?.name -> dbp.i11 = 1
                shopItems.allItems[12]?.name -> dbp.i12 = 1
                shopItems.allItems[13]?.name -> dbp.i13 = 1
                shopItems.allItems[14]?.name -> dbp.i14 = 1
                shopItems.allItems[15]?.name -> dbp.i15 = 1
                shopItems.allItems[16]?.name -> dbp.i16 = 1
                shopItems.allItems[17]?.name -> dbp.i17 = 1
                shopItems.allItems[18]?.name -> dbp.i18 = 1
                shopItems.allItems[19]?.name -> dbp.i19 = 1
                shopItems.allItems[20]?.name -> dbp.i20 = 1
                shopItems.allItems[21]?.name -> dbp.i21 = 1
                shopItems.allItems[22]?.name -> dbp.i22 = 1
                shopItems.allItems[23]?.name -> dbp.i23 = 1
                shopItems.allItems[24]?.name -> dbp.i24 = 1
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
