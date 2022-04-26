package com.keystone.capmobile.ui.view.keyValueProducts

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import coil.load
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentKeyValueProductDetailBinding
import com.keystone.capmobile.ui.adapters.GenericRecyclerViewAdapter
import com.keystone.capmobile.ui.adapters.GenericSimpleRecyclerBindingInterface
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class KeyValueProductDetailFragment @Inject constructor() : Fragment(), Player.Listener {
    private val args: KeyValueProductDetailFragmentArgs by navArgs()
    private lateinit var prgBar: ProgressBar
    private lateinit var flyerImageView: ImageView
    private lateinit var webV: WebView
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var player: ExoPlayer? = null
    private lateinit var backArrowImageView: ImageView
    private lateinit var videoPlayerView: PlayerView
    private lateinit var backTextView: TextView
    private lateinit var recyclerV: RecyclerView
    private lateinit var binding: FragmentKeyValueProductDetailBinding
    private lateinit var recyclerViewAdapter: GenericRecyclerViewAdapter<String>
    private var bindingInterface =
        object : GenericSimpleRecyclerBindingInterface<String> {
            override fun bindData(item: String, view: View) {
                val itemIcon = view.findViewById<TextView>(R.id.onBoardingStepText)
                itemIcon.text = item
            }
        }
    private lateinit var dataToPassToAdapter: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_key_value_product_detail,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        if (args.keyValueProductItem.hasOnlyFlyer && args.keyValueProductItem.flyer != null) {
            webV.visibility = View.INVISIBLE
            recyclerV.visibility = View.INVISIBLE
            flyerImageView.apply {
                visibility = View.VISIBLE
                flyerImageView.load(args.keyValueProductItem.flyer!!)
            }
        } else {
            dataToPassToAdapter = args.keyValueProductItem.onBoardingSteps as ArrayList<String>

            recyclerViewAdapter = GenericRecyclerViewAdapter(
                R.layout.key_value_products_onboarding_steps,
                bindingInterface
            ) {
                // Do nothing at onClick
            }

            recyclerViewAdapter.updateData(dataToPassToAdapter)
            recyclerV.adapter = recyclerViewAdapter

            webV.loadUrl(args.keyValueProductItem.movieUrl)

            val frameVideo =
                "<html><body><iframe width=\"405\" height=\"300\" src=\"" + args.keyValueProductItem.movieUrl + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>"

            webV.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return false
                }
            }
            val webSettings = webV.settings
            webSettings.javaScriptEnabled = true
            webV.loadData(frameVideo, "text/html", "utf-8")
        }
    }

    //    override fun onStart() {
//        super.onStart()
//        if (Util.SDK_INT >= 24) {
//            initializePlayer()
//        }
//    }
//
    override fun onResume() {
        super.onResume()
        backTextView.setOnClickListener {
            findNavController().popBackStack()
        }
        backArrowImageView.setOnClickListener {
            findNavController().popBackStack()
        }
    }
//
//    override fun onPause() {
//        super.onPause()
//        if (Util.SDK_INT < 24) {
//            releasePlayer()
//        }
//    }


    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
            super.onStop()
        }
    }

    private fun initViews() {
        with(binding) {
            videoPlayerView = videoView
            backArrowImageView = backArrow
            flyerImageView = flyerIV
            backTextView = backText
            recyclerV = rView
            webV = webview
            prgBar = progressBar4
        }
    }

//    private fun releasePlayer() {
//        player?.run {
//            playbackPosition = currentPosition
//            currentWindow = currentWindowIndex
//            playWhenReady = this.playWhenReady
//            release()
//        }
//        player = null
//    }

//    private fun initializePlayer2() {
//
//        setUpExoplayer(videoPlayerView, prgBar , requireContext(), "https://wave.video/embed/5e87987f46e0fb002b7e972c/5e87988046e0fb002b7e972f.mp4")
//    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding.videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

//    private fun buildMediaSource(videoUrl: String): MediaSource? {
//        val dataSourceFactory = DefaultDataSourceFactory(requireContext(), "sample")
//        return ProgressiveMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(Uri.parse(videoUrl))
//    }

    @SuppressLint("StaticFieldLeak")
    private fun initializePlayer(youtubeLink: String) {
        player = ExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = player

        object : YouTubeExtractor(requireContext()) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles != null) {

                    val iTag = 137//tag of video 1080
                    val audioTag = 140 //tag m4a audio
                    // 720, 1080, 480
                    var videoUrl = ""
                    val iTags: List<Int> = listOf(22, 137, 18)
                    for (i in iTags) {
                        val ytFile = ytFiles.get(i)
                        if (ytFile != null) {
                            val downloadUrl = ytFile.url
                            if (downloadUrl != null && downloadUrl.isNotEmpty()) {
                                videoUrl = downloadUrl
                            }
                        }
                    }
                    if (videoUrl == "")
                        videoUrl = ytFiles[iTag].url
                    val audioUrl = ytFiles[audioTag].url
                    val audioSource: MediaSource = ProgressiveMediaSource
                        .Factory(DefaultHttpDataSource.Factory())
                        .createMediaSource(MediaItem.fromUri(audioUrl))
                    val videoSource: MediaSource = ProgressiveMediaSource
                        .Factory(DefaultHttpDataSource.Factory())
                        .createMediaSource(MediaItem.fromUri(videoUrl))
                    player?.setMediaSource(
                        MergingMediaSource(true, videoSource, audioSource), true
                    )
                    player?.prepare()
                    player?.playWhenReady = playWhenReady
                    player?.seekTo(currentWindow, playbackPosition)
                    player?.addListener(this@KeyValueProductDetailFragment)
                }
            }

        }.extract(youtubeLink, true, true)

    }

//    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//        if (playbackState == Player.STATE_READY) {
//            binding.progressBar.visibility = View.INVISIBLE
//        } else {
//            binding.progressBar.visibility = View.VISIBLE
//        }
//    }


    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24 || player == null) {
            initializePlayer(args.keyValueProductItem.movieUrl)
        }
    }

    override fun onPause() {
        if (Util.SDK_INT < 24) releasePlayer()
        super.onPause()
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentMediaItemIndex
            player?.release()
            player = null
        }

    }
}