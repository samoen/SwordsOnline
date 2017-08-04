package sam.swordsonline

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_respec.*

class RespecFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_respec,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RefreshTextViews()
        button_dexterity_down.setOnClickListener {
            if((activity as MainActivity).currentPlayer.CanChangeStat("decrease","dexterity")){
                (activity as MainActivity).currentPlayer.dexterity--
                RefreshTextViews()
            }else Toast.makeText(context,"Your equipped items prevent you from changing that stat",Toast.LENGTH_SHORT).show()
        }
        button_dexterity_up.setOnClickListener {
            if((activity as MainActivity).currentPlayer.CanChangeStat("increase","dexterity")) {
                (activity as MainActivity).currentPlayer.dexterity++
                RefreshTextViews()
            }else Toast.makeText(context,"Your equipped items prevent you from changing that stat",Toast.LENGTH_SHORT).show()
        }
        button_strength_down.setOnClickListener {
            if((activity as MainActivity).currentPlayer.CanChangeStat("decrease","strength")) {
                (activity as MainActivity).currentPlayer.strength--
                RefreshTextViews()
            }else Toast.makeText(context,"Your equipped items prevent you from changing that stat",Toast.LENGTH_SHORT).show()
        }
        button_strength_up.setOnClickListener {
            if((activity as MainActivity).currentPlayer.CanChangeStat("increase","strength")) {
                (activity as MainActivity).currentPlayer.strength++
                RefreshTextViews()
            }else Toast.makeText(context,"Your equipped items prevent you from changing that stat",Toast.LENGTH_SHORT).show()
        }
        button_intelligence_down.setOnClickListener {
            if((activity as MainActivity).currentPlayer.CanChangeStat("decrease","intelligence")) {
                (activity as MainActivity).currentPlayer.intelligence--
                RefreshTextViews()
            }else Toast.makeText(context,"Your equipped items prevent you from changing that stat",Toast.LENGTH_SHORT).show()
        }
        button_intelligence_up.setOnClickListener {
            if ((activity as MainActivity).currentPlayer.CanChangeStat("increase", "intelligence")) {
                (activity as MainActivity).currentPlayer.intelligence++
                RefreshTextViews()
            } else Toast.makeText(context, "Your equipped items prevent you from changing that stat", Toast.LENGTH_SHORT).show()
        }

        button_backToMain.setOnClickListener {
            fragmentManager.beginTransaction().replace(R.id.framelayout_main, MainFragment()).commit()
        }
    }
    fun RefreshTextViews(){
        textView_strength.setText((activity as MainActivity).currentPlayer.strength.toString())
        textView_dexterity.setText((activity as MainActivity).currentPlayer.dexterity.toString())
        textView_intelligence.setText((activity as MainActivity).currentPlayer.intelligence.toString())
    }
}