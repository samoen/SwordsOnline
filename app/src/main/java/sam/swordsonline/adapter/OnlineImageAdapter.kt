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

    var lastMiss : Int? = null
    var PlayersBoardPos = mutableMapOf<Int,Int>()
    val option = R.drawable.target_square
    val player = R.drawable.hero_image
    val emptySquare = R.drawable.empty_square
    val hitSquare = R.drawable.dead_goblin_image
    val enemy = R.drawable.green_hero_image
    val miss = R.drawable.miss_square
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
        val absos = CalculatePairsFromRelative(squares,mpNum)
        for (v in absos){
            if(v.first in 1..10 && v.second in 1..10){
                mThumbIds.set(CalculatePositionFromPair(v),option)
            }
        }
    }

    fun RemoveMarkers(squares: List<Pair<Int,Int>>, pNum: Int){
        for (v in squares){
            if(v.first in 1..10 && v.second in 1..10){
                for(m in PlayersBoardPos){
                    if(CalculatePositionFromPair(v) == m.value && CalculatePositionFromPair(v) != PlayersBoardPos[pNum]){
                        mThumbIds.set(CalculatePositionFromPair(v),enemy)
                    }else if(CalculatePositionFromPair(v) != PlayersBoardPos[pNum]){
                        mThumbIds.set(CalculatePositionFromPair(v),emptySquare)
                    }
                }
            }
        }
    }

    fun PutHeroToPosition(position: Int, mpNum:Int){
        if(PlayersBoardPos.containsKey(mpNum)){
            mThumbIds.set(PlayersBoardPos[mpNum]?:0,emptySquare)
        }
        mThumbIds.set(position,player)
        PlayersBoardPos.put(mpNum,position)
    }

    fun PutEnemyToPosition(pos:Int, pNum:Int){
        if (PlayersBoardPos.contains(pNum)){
            mThumbIds.set(PlayersBoardPos[pNum]?:0,emptySquare)
        }
        PlayersBoardPos.put(pNum,pos)
        mThumbIds.set(pos,enemy)
    }

    fun ClearLastMiss(){ if(!(lastMiss == null)){ mThumbIds.set(lastMiss as Int,emptySquare) } }

    fun PutEmptyAtPosition(pos:Int?){ if (pos != null) mThumbIds.set(pos,emptySquare) }

    fun PutMissToPosition(pos:Int){ mThumbIds.set(pos,miss) }

    fun PutHitToPosition(pos:Int){ mThumbIds.set(pos,hitSquare) }

    fun CalculatePairsFromRelative(rel: List<Pair<Int,Int>>,mpNum: Int):List<Pair<Int,Int>> {
        val absolutes = mutableListOf<Pair<Int, Int>>()
        for (v in rel) {
            val p = Pair(CalculatePairFromPosition(PlayersBoardPos[mpNum]?:0).first + v.first, CalculatePairFromPosition(PlayersBoardPos[mpNum]?:0).second+ v.second)
            absolutes.add(p)
        }
        return absolutes
    }


}

