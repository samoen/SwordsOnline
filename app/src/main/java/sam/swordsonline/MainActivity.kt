package sam.swordsonline

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ninja.sakib.pultusorm.core.PultusORM

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
    }

    val startingItems = mutableListOf<Item>(
            ShopItemList.allItems[0]?:Item(),
            ShopItemList.allItems[1]?:Item(),
            ShopItemList.allItems[2]?:Item(),
            ShopItemList.allItems[3]?:Item(),
            ShopItemList.allItems[4]?:Item()
    )

    val startingEquippedItems = mutableMapOf(
            0 to ShopItemList.allItems[0],
            1 to ShopItemList.allItems[1],
            2 to ShopItemList.allItems[2],
            3 to ShopItemList.allItems[3],
            4 to ShopItemList.allItems[4]
    )

    var currentPlayer = Player(gold = 3)
    val appPath: String by lazy { this.applicationContext.getFilesDir().getAbsolutePath() }
    val pultusORM: PultusORM by lazy { PultusORM("swordonline.db", appPath) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_main.setOnClickListener {
            if(editText_player_name.text.isNotEmpty()){
                currentPlayer.name = editText_player_name.text.toString()
            }
            currentPlayer.items = startingItems
            currentPlayer.equipped = startingEquippedItems
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
                currentPlayer.equipped.put(0,ShopItemList.allItems[p.helmet]?:Item())
                currentPlayer.equipped.put(1,ShopItemList.allItems[p.shoulders]?:Item())
                currentPlayer.equipped.put(2,ShopItemList.allItems[p.legs]?:Item())
                currentPlayer.equipped.put(3,ShopItemList.allItems[p.offHand]?:Item())
                currentPlayer.equipped.put(4,ShopItemList.allItems[p.mainHand]?:Item())
                currentPlayer.name = p.name
                currentPlayer.items = startingItems
                if(p.i5 == 1) currentPlayer.items.add(ShopItemList.allItems[5]?:Item())
                if(p.i6 == 1) currentPlayer.items.add(ShopItemList.allItems[6]?:Item())
                if(p.i7 == 1) currentPlayer.items.add(ShopItemList.allItems[7]?:Item())
                if(p.i8 == 1) currentPlayer.items.add(ShopItemList.allItems[8]?:Item())
                if(p.i9 == 1) currentPlayer.items.add(ShopItemList.allItems[9]?:Item())
                if(p.i10 == 1) currentPlayer.items.add(ShopItemList.allItems[10]?:Item())
                if(p.i11 == 1) currentPlayer.items.add(ShopItemList.allItems[11]?:Item())
                if(p.i12 == 1) currentPlayer.items.add(ShopItemList.allItems[12]?:Item())
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
                ShopItemList.allItems[5]?.name -> dbp.i5 = 1
                ShopItemList.allItems[6]?.name -> dbp.i6 = 1
                ShopItemList.allItems[7]?.name -> dbp.i7 = 1
                ShopItemList.allItems[8]?.name -> dbp.i8 = 1
                ShopItemList.allItems[9]?.name -> dbp.i9 = 1
                ShopItemList.allItems[10]?.name -> dbp.i10 = 1
                ShopItemList.allItems[11]?.name -> dbp.i11 = 1
                ShopItemList.allItems[12]?.name -> dbp.i12 = 1
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
