package br.com.ffscompany.awesomeapp.ui.movieDetails;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import br.com.ffscompany.awesomeapp.databinding.FragmentMovieDetailsBinding;
import br.com.ffscompany.awesomeapp.service.VideoTmdbService;

public class MovieDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private FragmentMovieDetailsBinding binding;

    private MovieDetailsViewModel movieDetailsViewModel;

    private PlayerView playerView;

    private SimpleExoPlayer player;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        movieDetailsViewModel = new ViewModelProvider(this, new ViewModelFactory(bundle.getString("title"), bundle.getString("overview"))).get(MovieDetailsViewModel.class);

        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false);
        playerView = binding.playerView;
        View root = binding.getRoot();

        final TextView movieTitle = binding.movieTitle;
        movieDetailsViewModel.getTitle().observe(getViewLifecycleOwner(), movieTitle::setText);
        final TextView movieSynopsis = binding.movieSynopsis;
        movieDetailsViewModel.getOverview().observe(getViewLifecycleOwner(), movieSynopsis::setText);

        LoaderManager.getInstance(this).initLoader(bundle.getInt("id"), null, this).forceLoad();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new VideoTmdbService(requireContext(), id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        initPlayer(data);

//        videoView.getSettings().setJavaScriptEnabled(true);
//        videoView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        videoView.loadUrl(data);
//        videoView.setWebChromeClient(new WebChromeClient());
    }

    private void initPlayer(String uri) {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);
        playYoutubeVideo(uri);
    }

    private void playYoutubeVideo(String uri) {
        Log.d("LINK", uri);

        new YouTubeExtractor(getContext()) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    int itag = 22;
                    String downloadUrl = ytFiles.get(itag).getUrl();
                }
            }
        }.extract(uri, false, true);
        new YouTubeExtractor(getContext()) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles != null) {
                    int videoTag = 137;
                    int audioTag = 140;
                    MediaSource audio = new ProgressiveMediaSource.Factory(
                            new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "AwesomeApp"))
                    ).createMediaSource(MediaItem.fromUri(ytFiles.get(audioTag).getUrl()));
                    MediaSource video = new ProgressiveMediaSource.Factory(
                            new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "AwesomeApp"))
                    ).createMediaSource(MediaItem.fromUri(ytFiles.get(videoTag).getUrl()));
                    player.setMediaSource(new MergingMediaSource(true, video, audio), true);
                    player.prepare();
                }
            }
        }.extract(uri, false, false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        loader.reset();
    }
}
