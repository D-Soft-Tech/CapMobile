package com.keystone.capmobile.util

object ExoplayerManager {

//    @SuppressLint("StaticFieldLeak")
//    private lateinit var playerView: PlayerView
//    @SuppressLint("StaticFieldLeak")
//    private lateinit var simpleExoPlayer: SimpleExoPlayer
//    private lateinit var eventListener: Player.EventListener
//
//
//    fun setUpExoplayer(
//        playerView: PlayerView,
//        progressBar: ProgressBar,
//        context: Context,
//        url: String
//    ) {
//        //Assign variable
//        this.playerView = playerView
//
//        val videoUrl = Uri.parse(url)
//
//        //Initialize load control
//        val loadControl = DefaultLoadControl()
//
//        //Initialize band width meter
//        val bandwidthMeter = DefaultBandwidthMeter()
//
//        //Initialize Track Selector
//        val trackSelector = DefaultTrackSelector(
//            AdaptiveTrackSelection.Factory(bandwidthMeter)
//        )
//
//        //Initialize simple Exoplayer
//        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
//            context, trackSelector, loadControl
//        )
//
//        //Initialize data source factory
//        val factory = DefaultHttpDataSourceFactory(
//            "exoplayer_video"
//        )
//
//        //Initialize extractors factory
//        val extractorsFactory = DefaultExtractorsFactory()
//
//        //Initialize extractors factory
//        val mediaSource = ExtractorMediaSource(videoUrl, factory, extractorsFactory, null, null)
//
//        //Set player
//        playerView.player = simpleExoPlayer
//
//        //Keep screen on
//        playerView.keepScreenOn = true
//
//        //Prepare media
//        simpleExoPlayer.prepare(mediaSource)
//
//        //Play video when ready
//        simpleExoPlayer.playWhenReady = true
//
//
//        eventListener = object : Player.EventListener {
//            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
//
//            }
//
//            override fun onTracksChanged(
//                trackGroups: TrackGroupArray?,
//                trackSelections: TrackSelectionArray?
//            ) {
//
//            }
//
//            override fun onLoadingChanged(isLoading: Boolean) {
//
//            }
//
//            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                //Check Condition
//
//                if (playbackState == Player.STATE_BUFFERING) {
//                    //When buffering
//                    //Show Progress Bar
//                    progressBar.visibility = View.VISIBLE
//                } else if (playbackState == Player.STATE_READY) {
//                    //When ready
//                    //Hide Progress Bar
//                    progressBar.visibility = View.GONE
//                }
//
//            }
//
//            override fun onRepeatModeChanged(repeatMode: Int) {
//
//            }
//
//            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
//
//            }
//
//            override fun onPlayerError(error: ExoPlaybackException?) {
//
//            }
//
//            override fun onPositionDiscontinuity(reason: Int) {
//
//            }
//
//            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
//
//            }
//
//            override fun onSeekProcessed() {
//
//            }
//
//        }
//
//
//        simpleExoPlayer.addListener(eventListener)
//    }
//
//    fun stopPlayer() {
//        simpleExoPlayer.stop()
//
//
////        simpleExoPlayer.removeListener(eventListener)
//    }
//
//
//    fun pausePlayer() {
//
//        simpleExoPlayer.playWhenReady = false
//
//        simpleExoPlayer.playbackState
//    }
//

}