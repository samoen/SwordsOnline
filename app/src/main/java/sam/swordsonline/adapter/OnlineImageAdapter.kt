package sam.swordsonline.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import sam.swordsonline.R
import sam.swordsonline.model.CalculatePairFromPosition
import sam.swordsonline.model.CalculatePositionFromPair

class OnlineImageAdapter(private val mContext: Context) : BaseAdapter() {

    var lootBags: MutableMap<Int,Int> = mutableMapOf()
    var activeMarkers: MutableList<Pair<Int,Int>> = mutableListOf()
    var underMarkerImagePos: MutableMap<Int,Int> = mutableMapOf()
    var lastMiss : Pair<Int,Int>? = null
    var lastHit : Int? = null
    var PlayersBoardPos = mutableMapOf<Int,Int>()
    val option = R.drawable.target_square
    val player = R.drawable.hero_image
    val emptySquare = R.drawable.empty_square
    val hitSquare = R.drawable.dead_goblin_image
    val enemy = R.drawable.green_hero_image
    val miss = R.drawable.miss_square
    val lootbag = R.drawable.loot_image
    private val mThumbIds = arrayOf<Int>(
            emptySquare, emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,
            emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare,emptySquare
    )

    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(mContext)
            imageView.setLayoutParams(ViewGroup.LayoutParams(104,105))
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
            imageView.setPadding(2, 2, 2, 2)
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(mThumbIds[position])
        return imageView
    }

    fun PlaceMarkers(squares: List<Pair<Int,Int>>,mpNum: Int){
        activeMarkers = CalculatePairsFromRelative(squares,mpNum) as MutableList<Pair<Int, Int>>
        val absos = CalculatePairsFromRelative(squares,mpNum)
        for (v in absos){
            if(v.first in 1..10 && v.second in 1..10){
                val pos = CalculatePositionFromPair(v)
                underMarkerImagePos.set(pos,mThumbIds[pos])
                mThumbIds.set(pos,option)
            }
        }
        notifyDataSetChanged()
    }

    fun RemoveMarkers(pNum: Int){
        for (v in activeMarkers){
            if(v.first in 1..10 && v.second in 1..10){
                val pos = CalculatePositionFromPair(v)
                mThumbIds.set(pos,underMarkerImagePos[pos]?:0)
            }
        }
        activeMarkers.clear()
        underMarkerImagePos.clear()
        notifyDataSetChanged()
    }

    fun PutHeroToPosition(position: Int, mpNum:Int){
        if(PlayersBoardPos.containsKey(mpNum)){
            mThumbIds.set(PlayersBoardPos[mpNum]?:0,emptySquare)
        }
        mThumbIds.set(position,player)
        PlayersBoardPos.put(mpNum,position)
        notifyDataSetChanged()
    }

    fun PutEnemyToPosition(pos:Int, pNum:Int){
        if (PlayersBoardPos.contains(pNum)){
            mThumbIds.set(PlayersBoardPos[pNum]?:0,emptySquare)
        }
        PlayersBoardPos.put(pNum,pos)
        mThumbIds.set(pos,enemy)
        notifyDataSetChanged()
    }

    fun RemoveEnemy(pNum: Int){
        if (PlayersBoardPos.containsKey(pNum)){
            mThumbIds.set(PlayersBoardPos[pNum]?:0,emptySquare)
            PlayersBoardPos.remove(pNum)
            notifyDataSetChanged()
        }
    }
    fun KillEnemy(pNum: Int){
        if (PlayersBoardPos.containsKey(pNum)){
            mThumbIds.set(PlayersBoardPos[pNum]?:0,lootbag)
            PlayersBoardPos.remove(pNum)
            notifyDataSetChanged()
        }
    }

    fun PutMissToPosition(pos:Int){
        lastMiss = Pair(pos,mThumbIds.get(pos))
        mThumbIds.set(pos,miss)
    }
    fun ClearLastMiss(){
        if(!(lastMiss == null)){
            mThumbIds.set(lastMiss?.first?:0,lastMiss?.second?:0)
            lastMiss = null
            notifyDataSetChanged()
        }
    }
    fun PutHitToPosition(pos:Int){
        mThumbIds.set(pos,hitSquare)
        lastHit = pos
    }
    fun ClearLastHit(){
        if(!(lastHit == null)){
            mThumbIds.set(lastHit as Int,emptySquare)
            lastHit = null
        }
    }
    fun PutLootBag(pos:Int,id:Int){
        mThumbIds.set(pos,lootbag)
        lootBags.put(pos,id)
    }
    fun RemoveLootBag(pos:Int){
        lootBags.remove(pos)
    }

    fun CalculatePairsFromRelative(rel: List<Pair<Int,Int>>,mpNum: Int):List<Pair<Int,Int>> {
        val absolutes = mutableListOf<Pair<Int, Int>>()
        for (v in rel) {
            val p = Pair(CalculatePairFromPosition(PlayersBoardPos[mpNum]?:0).first + v.first, CalculatePairFromPosition(PlayersBoardPos[mpNum]?:0).second+ v.second)
            absolutes.add(p)
        }
        return absolutes
    }

}

