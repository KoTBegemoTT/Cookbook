package com.example.cookbook.TTS;

import static android.widget.Toast.makeText;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class SpeechToText implements RecognitionListener {

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String COMMANDS = "commands";

    private static final String KEYPHRASE = "кулинарная книга";
    private static final String KEYPHRASE_OFF = "выключить";

    Activity activity;

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizer recognizer;
    public static Boolean fragmentReady = false;
    public static Boolean recognizerReady = false;

    public interface RecognizerCommandListener {
        void onGetCommand(String data);
    }

    private RecognizerCommandListener recognizerCommandListener;

    public SpeechToText(Activity activity){
        this.recognizerCommandListener = (RecognizerCommandListener)activity;
        this.activity = activity;
    }

    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<SpeechToText> activityReference;
        SetupTask(SpeechToText activity) {
            this.activityReference = new WeakReference<>(activity);
        }
        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get().activity);
                File assetDir = assets.syncAssets();
                activityReference.get().setupRecognizer(assetDir);
            } catch (IOException e) {
                return e;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
//                ((TextView) activityReference.get().activity.findViewById(R.id.caption_text))
//                        .setText("Failed to init recognizer " + result);
            } else {
                recognizerReady = true;
                if (fragmentReady)
                    activityReference.get().switchSearch(KWS_SEARCH);
            }
        }
    }

    public void onStartFragment(){
        fragmentReady = true;
        if (recognizerReady)
            switchSearch(KWS_SEARCH);
    }

    public void onDestroyFragment(){
        fragmentReady = false;
        recognizer.stop();
    }

    public void onRequestPermissionsGranted() {
        new SetupTask(this).execute();
    }

    public void onDestroy() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;
        String text = hypothesis.getHypstr();
        Log.i("onPartialResult", text);
        if (text.equals(KEYPHRASE))
            switchSearch(COMMANDS);
        else if (text.equals(KEYPHRASE_OFF))
            switchSearch(KWS_SEARCH);
    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis == null
                || hypothesis.getHypstr().equals(KEYPHRASE) || hypothesis.getHypstr().equals(KEYPHRASE_OFF)
        )
            return;
        Log.i("T", hypothesis.getHypstr().equals(KEYPHRASE) + "");
        Log.i("T", hypothesis.getHypstr());
        Log.i("T", "----------onResult---------");
        String text = hypothesis.getHypstr();
        recognizerCommandListener.onGetCommand(text);

//        makeText(activity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
//        switchSearch(recognizer.getSearchName());
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        recognizer.startListening(searchName);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "ru4"))
                .setDictionary(new File(assetsDir, "rudict2"))
                .setRawLogDir(assetsDir)
                .setKeywordThreshold(1e-44f)
                .getRecognizer();
        recognizer.addListener(this);

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        File commandsGrammar = new File(assetsDir, "commands.gram");
        recognizer.addGrammarSearch(COMMANDS, commandsGrammar);
    }

    @Override
    public void onError(Exception error) {
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }
}