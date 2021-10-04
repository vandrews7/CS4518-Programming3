package com.example.basketballcounter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "GameListFragment"

class GameListFragment : Fragment() {

    interface Callbacks{
        fun onGameSelected(id: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = null

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "Total Games: ${gameListViewModel.games.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)

        gameRecyclerView = view.findViewById(R.id.game_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI() {
        val games = gameListViewModel.games
        adapter = GameAdapter(games)
        gameRecyclerView.adapter = adapter
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var game: Game

        private val gameNum: TextView = itemView.findViewById(R.id.gameNum)
        private val teamNames: TextView = itemView.findViewById(R.id.teamNames)
        private val teamScores: TextView = itemView.findViewById(R.id.teamScores)
        private val gameDate: TextView = itemView.findViewById(R.id.gameDate)
        private val teamAicon: ImageView =  itemView.findViewById(R.id.iconA)
        private val teamBicon: ImageView = itemView.findViewById(R.id.iconB)

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(game: Game) {
            this.game = game
            gameNum.text = this.game.index
            teamNames.text = getString(R.string.item_list_format, this.game.teamAname, this.game.teamBname)
            teamScores.text =
                getString(R.string.item_list_format, this.game.scoreA.toString(), this.game.scoreB.toString())
            gameDate.text = this.game.date.toString()
            teamAicon.visibility = if (this.game.scoreA > this.game.scoreB) {
                View.VISIBLE
            } else {
                View.GONE
            }
            teamBicon.visibility = if (this.game.scoreB > this.game.scoreA) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View?) {
            callbacks?.onGameSelected(game.id)
        }
    }

    private inner class GameAdapter(var games: List<Game>) : RecyclerView.Adapter<GameHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
            val view= layoutInflater.inflate(R.layout.list_item_game, parent, false)
            return GameHolder(view)
        }

        override fun getItemCount() = games.size

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = games[position]
            holder.bind(game)
        }
    }

    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }
}