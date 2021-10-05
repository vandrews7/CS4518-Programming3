package com.example.basketballcounter
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import java.io.File
import java.util.*

private const val TAG = "GameFragment"
private const val REQUEST_CODE = 0
private const val REQUEST_PHOTO = 2
private const val ARG_GAME_ID = "game_id"

class GameFragment: Fragment() {

    private val scoreViewModel: ScoreViewModel by lazy {
        ViewModelProviders.of(this).get(ScoreViewModel::class.java)
    }

    private lateinit var game: Game
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private lateinit var teamAname: EditText
    private lateinit var teamBname: EditText
    private lateinit var teamAscore: TextView
    private lateinit var teamBscore: TextView
    private lateinit var Abtn3: Button
    private lateinit var Abtn2: Button
    private lateinit var AbtnFree: Button
    private lateinit var Bbtn3: Button
    private lateinit var Bbtn2: Button
    private lateinit var BbtnFree: Button
    private lateinit var save: Button
    private lateinit var resetBtn: Button
    private lateinit var display: Button
    private lateinit var winnerBtn: Button
    private lateinit var winnerTxt: TextView
    private lateinit var photoButtonA: ImageButton
    private lateinit var photoButtonB: ImageButton
    private lateinit var photoViewA: ImageView
    private lateinit var photoViewB: ImageView

    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProviders.of(this).get(GameDetailViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        game = Game()
        val gameId: String = arguments?.getSerializable(ARG_GAME_ID) as String
        Log.d(TAG, "arg bundle game ID: $gameId")
        gameDetailViewModel.loadGame(gameId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        teamAname = view.findViewById(R.id.teamA) as EditText
        teamBname = view.findViewById(R.id.teamB) as EditText
        teamAscore = view.findViewById(R.id.scoreA) as TextView
        teamBscore = view.findViewById(R.id.scoreB) as TextView
        winnerTxt = view.findViewById(R.id.winnerDisp) as TextView
        Abtn3 = view.findViewById(R.id.Apoints3) as Button
        Abtn2 = view.findViewById(R.id.Apoints2) as Button
        AbtnFree = view.findViewById(R.id.Afree) as Button
        Bbtn3 = view.findViewById(R.id.Bpoints3) as Button
        Bbtn2 = view.findViewById(R.id.Bpoints2) as Button
        BbtnFree = view.findViewById(R.id.Bfree) as Button
        save = view.findViewById(R.id.saveBtn) as Button
        resetBtn = view.findViewById(R.id.reset) as Button
        display = view.findViewById(R.id.displayBtn) as Button
        winnerBtn = view.findViewById(R.id.winner) as Button
        photoButtonA = view.findViewById(R.id.imageButtonA) as ImageButton
        photoButtonB = view.findViewById(R.id.imageButtonB) as ImageButton
        photoViewA = view.findViewById(R.id.teamAIcon) as ImageView
        photoViewB = view.findViewById(R.id.teamBIcon) as ImageView

        if(!activity!!.isFinishing && (scoreViewModel.getScoreA() > 0 || scoreViewModel.getScoreB() > 0)) {
            Log.i(TAG, "Persisting score across screen rotation")
            teamAscore.text = scoreViewModel.getScoreA().toString()
            teamBscore.text = scoreViewModel.getScoreB().toString()
            if(scoreViewModel.getWinPressed() > 0) {
                print(scoreViewModel.getWinPressed())
                winnerTxt.text = scoreViewModel.getScore()
            }
        }

        Abtn3.setOnClickListener {
            teamAscore.text = scoreViewModel.addScoreA(3)
        }

        Abtn2.setOnClickListener {
            teamAscore.text = scoreViewModel.addScoreA(2)
        }

        AbtnFree.setOnClickListener {
            teamAscore.text = scoreViewModel.addScoreA(1)
        }

        Bbtn3.setOnClickListener {
            teamBscore.text = scoreViewModel.addScoreB(3)
        }

        Bbtn2.setOnClickListener {
            teamBscore.text = scoreViewModel.addScoreB(2)
        }

        BbtnFree.setOnClickListener {
            teamBscore.text = scoreViewModel.addScoreB(1)
        }

        winnerBtn.setOnClickListener {
            winnerTxt.text = scoreViewModel.getScore()
        }

        resetBtn.setOnClickListener {
            teamAscore.text = scoreViewModel.resetScoreA()
            teamBscore.text = scoreViewModel.resetScoreB()
            scoreViewModel.resetWinPressed()
            winnerTxt.text = ""
        }

        save.setOnClickListener {
            Log.i(TAG, "save button clicked, switching to SaveActivity")
            val intent = SaveActivity.newIntent(activity!!, scoreViewModel.getScoreA(), scoreViewModel.getScoreB())
            startActivityForResult(intent, REQUEST_CODE)

//            Toast.makeText(
//                activity!!,
//                R.string.save_toast,
//                Toast.LENGTH_SHORT)
//                .show()
        }

        display.setOnClickListener {

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailViewModel.gameLiveData.observe(
            viewLifecycleOwner,
            Observer { game ->
                game?.let {
                    this.game = game
                    photoFile = gameDetailViewModel.getPhotoFile(game)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                        "com.example.basketballcounter.FileProvider",
                        photoFile)
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        photoButtonA.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(captureImage,
                PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null){
                isEnabled = false
            }

            setOnClickListener{
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)

                for(cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }

                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }

        photoButtonB.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null){
                isEnabled = false
            }

            setOnClickListener{
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)

                for(cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }

                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }

        val teamAWatcher = object: TextWatcher{
            override fun beforeTextChanged(sequence: CharSequence?,
                                           start: Int,
                                           count: Int,
                                           after: Int
            ) {
            }

            override fun onTextChanged(sequence: CharSequence?,
                                       start: Int,
                                       count: Int,
                                       after: Int
            ) {
                game.teamAName = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        teamAname.addTextChangedListener(teamAWatcher)

        val teamBWatcher = object: TextWatcher{
            override fun beforeTextChanged(sequence: CharSequence?,
                                           start: Int,
                                           count: Int,
                                           after: Int
            ) {
            }

            override fun onTextChanged(sequence: CharSequence?,
                                       start: Int,
                                       count: Int,
                                       after: Int
            ) {
                game.teamBName = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        teamBname.addTextChangedListener(teamBWatcher)
    }

    override fun onStop() {
        super.onStop()
        gameDetailViewModel.saveGame(game)
    }

    private fun updateUI(){
        teamAname.setText(game.teamAName)
        teamBname.setText(game.teamBName)
        teamAscore.setText(game.teamAScore)
        teamBscore.setText(game.teamBScore)

    }

    companion object{
        fun newInstance(gameId: String): GameFragment{
            val args = Bundle().apply {
                putSerializable(ARG_GAME_ID, gameId)
            }
            return GameFragment().apply{
                arguments = args
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "called onActivityResult()")
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "resultCode = $resultCode")
        if(resultCode != Activity.RESULT_OK) {
            return
        }

        val isCoolClick = data?.getBooleanExtra(EXTRA_COOL_CLICK, false)
        Log.d(TAG, "requestCode = $requestCode")
        Log.d(TAG, "extra_cool_click = $isCoolClick")

        if(requestCode == REQUEST_CODE) {
            scoreViewModel.savePressed = isCoolClick ?: false
        }
        Log.d(TAG, "savePressed = ${scoreViewModel.savePressed}")

        if(scoreViewModel.savePressed) {
            Toast.makeText(
                activity!!,
                R.string.save_toast,
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }


}