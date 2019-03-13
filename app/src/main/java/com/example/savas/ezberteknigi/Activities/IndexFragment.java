package com.example.savas.ezberteknigi.Activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.savas.ezberteknigi.BLL.InternetConnectivitySocket;
import com.example.savas.ezberteknigi.Models.ReadingText;
import com.example.savas.ezberteknigi.Models.Word;
import com.example.savas.ezberteknigi.R;
import com.example.savas.ezberteknigi.Repositories.ReadingTextRepository;
import com.example.savas.ezberteknigi.Repositories.WordRepository;
import com.example.savas.ezberteknigi.Services.WordAndTextService;
import com.example.savas.ezberteknigi.Services.WordRevisedService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.example.savas.ezberteknigi.AppStarter.CHANNEL_WORD_REVISION;
import static com.example.savas.ezberteknigi.WordRevisionScheduler.EXTRA_WORD_TO_SAVE_REVISED;

public class IndexFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_index, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Index");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Button btn = getActivity().findViewById(R.id.button_index_deneme);
//        btn.setVisibility(View.GONE);
//        btn.setText("DENEME");
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        Button btnSearchBooks = getActivity().findViewById(R.id.button_search_books);
        btnSearchBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new InternetConnectivitySocket().isConnectedToInternet()){
                    Intent i = new Intent(getContext(), SearchBooksActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getContext(), "İnternet bağlantısı mevcut değil", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnDeneme = getActivity().findViewById(R.id.button_index_deneme);
        btnDeneme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity().getApplicationContext());

                List<Word> wordsToReviseMock = new ArrayList<>();
                wordsToReviseMock.add(new Word("deneme", "trial", 0, "example sentence 1"));
                wordsToReviseMock.add(new Word("deneme 2", "trial 2", 0, "example sentence 2"));
                wordsToReviseMock.add(new Word("deneme 3", "trial 3", 0, "example sentence 3"));
                wordsToReviseMock.add(new Word("deneme 4", "trial 4", 0, "example sentence 4"));
                wordsToReviseMock.add(new Word("deneme 5", "trial 5", 0, "example sentence 5"));
                wordsToReviseMock.add(new Word("deneme 6", "trial 6", 0, "example sentence 6"));
                wordsToReviseMock.add(new Word("deneme 7", "trial 7", 0, "example sentence 7"));
                wordsToReviseMock.add(new Word("deneme 8", "trial 8", 0, "example sentence 8"));

                for (int i = 0; i < wordsToReviseMock.size(); i++) {

                    Word word = wordsToReviseMock.get(i);
                    Log.d(TAG, "showMultipleNotificationsInGroup: word: " + word.toString());
                    Intent wordRevisedIntent = new Intent(getActivity(), WordRevisedService.class);
                    wordRevisedIntent.putExtra(EXTRA_WORD_TO_SAVE_REVISED, word.getWordId());
                    PendingIntent deletePendingIntent = PendingIntent.getService(getActivity().getApplicationContext(), word.getWordId(), wordRevisedIntent, 0);

                    Notification notification = new NotificationCompat.Builder(getActivity().getApplicationContext(), CHANNEL_WORD_REVISION)
                            .setSmallIcon(R.drawable.button_revision)
                            .setContentTitle(word.getWord())
                            .setContentText(String.format("%s :\n %s", word.getTranslation(), word.getExampleSentence()))
                            .setGroup("words")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setOnlyAlertOnce(true)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setDeleteIntent(deletePendingIntent)
                            .build();

                    notificationManager.notify(i, notification);
                }

                if(Build.VERSION.SDK_INT >= 24){

                } else {

                    Locale l = new Locale("tr");
                    Notification summaryNotification = new NotificationCompat.Builder(getActivity(), CHANNEL_WORD_REVISION)
                            .setSmallIcon(R.drawable.button_revision)

                            .setContentTitle("Tekrar Edilecek Kelimeler")
                            .setContentText(getWordListInString(wordsToReviseMock))

                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .setBigContentTitle("Tekrar Edilecek Kelimeler")
                                    .setSummaryText(String.format(l, "%d Tekrar Edilecek Kelime", wordsToReviseMock.size()))
                                    .bigText(getWordListInString(wordsToReviseMock)))
                            .setPriority(NotificationCompat.PRIORITY_LOW)
                            .setGroup("words")
                            .setGroupSummary(true)
                            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                            .build();

                    notificationManager.notify(-1, summaryNotification);

                }


            }
        });
    }

    @NonNull
    private String getWordListInString(List<Word> wordsToRevise) {
        StringBuilder wordsToDisplay = new StringBuilder();
        for (Word w : wordsToRevise) {
            wordsToDisplay.append(w.getWord()).append(", ");
        }
        return wordsToDisplay.toString().substring(0, wordsToDisplay.length() - 2);
    }

    private void startShareTextService() {
        Log.d(TAG, "onClick: button index deneme clicked");

        Intent intent = new Intent(getActivity(), WordAndTextService.class);
        intent.putExtra(Intent.EXTRA_TEXT, "deneme sharedText");
        intent.setType("text/plain");
        intent.setAction(Intent.ACTION_SEND);
        getActivity().startService(intent);
    }

    private void sendDataToFirebase() {
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");

        myRef.setValue("Hello, World!");
    }

    private void addSampleNews() {
        ReadingTextRepository repository = new ReadingTextRepository(getActivity().getApplication());
        String newsContent = "this is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news pagethis is the content of a BBC news page";
        repository.insert(new ReadingText("en", "BBC", ReadingText.DOCUMENT_TYPE_PLAIN, newsContent));
    }

    private void addSampleWord(){
        WordRepository repo = new WordRepository(getActivity().getApplication());
        repo.insert(new Word("word_sample", "translation_sample", 0, "sample_example_sentence"));
    }
}
