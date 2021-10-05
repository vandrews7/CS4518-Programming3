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
import androidx.lifecycle.Observer
import java.util.*

private const val TAG = "GameListFragment"

class GameListFragment : Fragment() {

    interface Callbacks{
        fun onGameSelected(gameId: String)
    }

    private var callbacks: Callbacks? = null

    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = GameAdapter(emptyList())

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)

        gameRecyclerView = view.findViewById(R.id.game_recycler_view) as RecyclerView

        gameRecyclerView.layoutManager = LinearLayoutManager(context)
        gameRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameListViewModel.gameListLiveData.observe(
            viewLifecycleOwner,
            Observer { games->
                games?.let {
                    Log.i(TAG, "Got games") //should have ${games.size} but it didn't like .size
                    updateUI(games)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(games: List<Game>) {
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
            gameNum.text = this.game.id.toString()
            teamNames.text = getString(R.string.item_list_format, this.game.teamAName, this.game.teamBName)
            teamScores.text =
                getString(R.string.item_list_format, this.game.teamAScore.toString(), this.game.teamBScore.toString())
            gameDate.text = this.game.date.toString()
            teamAicon.visibility = if (this.game.teamAScore > this.game.teamBScore) {
                View.VISIBLE
            } else {
                View.GONE
            }
            teamBicon.visibility = if (this.game.teamAScore > this.game.teamBScore) {
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