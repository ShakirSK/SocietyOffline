package test.soc365.society365.maneger;





import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
        import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
        import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
        import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
        import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import test.soc365.society365.BuildConfig;
import test.soc365.society365.R;

import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.Calendar;
import java.util.List;


public class showpdftest_file extends AppCompatActivity implements OnPageChangeListener,OnLoadCompleteListener{
       Uri SAMPLE_FILE;
       String legdername,timeperiod1,timeperiod2,companyname;
    PDFView pdfView;
    Integer pageNumber = 0;
    Uri pdfFileName;



    //pdf share
    private long enqueue;
    private DownloadManager dm;
    BroadcastReceiver receiver;
    String urlstring;
    String date_In_Email_Doc;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showpdftest_file);

        Toolbar toolbar = findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        Intent intent =getIntent();
        urlstring = intent.getStringExtra("urlstring");
        legdername = intent.getStringExtra("sname");
        timeperiod1 = intent.getStringExtra("timeperiod1");
        timeperiod2 = intent.getStringExtra("timeperiod2");
        companyname = intent.getStringExtra("companyname");

        SAMPLE_FILE = Uri.parse(intent.getStringExtra("URL"));

        getSupportActionBar().setTitle("Report of : "+legdername);
        Log.d("urlshowpdf ", String.valueOf(SAMPLE_FILE));
        pdfView= (PDFView)findViewById(R.id.pdfView);
        displayFromAsset(SAMPLE_FILE);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            ImageView view = (ImageView) findViewById(R.id.imageView1);
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            Uri a = Uri.parse(uriString);
                            File d = new File(a.getPath());
                            // copy file from external to internal will esaily avalible on net use google.
                            //     view.setImageURI(a);




                                Intent share = new Intent();
                                // set flag to give temporary permission to external app to use your FileProvider
                                share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri apkURI = FileProvider.getUriForFile(
                                        context,
                                        BuildConfig.APPLICATION_ID + ".myfileprovider", d);
                                share.setDataAndType(apkURI, "application/pdf");
                                share.setType("plain/text");
                                share.putExtra(Intent.EXTRA_STREAM, apkURI);
                                share.putExtra(Intent.EXTRA_EMAIL, " ");
                                share.putExtra(android.content.Intent.EXTRA_SUBJECT, legdername + " legder report for period " + timeperiod1 + " to " + timeperiod2);

                                final Calendar calendartp = Calendar.getInstance();
                                //setting calender to custom date


                                date_In_Email_Doc = calendartp.get(Calendar.DAY_OF_MONTH) + "-" + calendartp.get(Calendar.MONTH) + "-" + calendartp.get(Calendar.YEAR);


                                share.putExtra(android.content.Intent.EXTRA_TEXT, "\n" +
                                        "Hello!\n" +
                                        "\n" +
                                        "Ledger report of " + legdername + " for period " + timeperiod1 + " to " + timeperiod2
                                        + ".Please find the attached PDF for your reference.\n" +
                                        "\n" +
                                        "\n" +
                                        "\n" +
                                        "Regards,\n" + companyname +
                                        "\n" +
                                        "Date :" + date_In_Email_Doc + "\n" +
                                        "\n" +
                                        "Sent through Society365 App\n" +
                                        "http://society365.in/");
                                share.setAction(Intent.ACTION_SEND);

                                startActivity(share);
                            }


                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void displayFromAsset(Uri assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromUri(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("treeee", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.allreportmenu, menu);
        //menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id ==  android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id==R.id.share)
                {
                        Toast.makeText(getApplicationContext(),"Please Wait",Toast.LENGTH_SHORT).show();
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
         DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(urlstring)).setDestinationInExternalPublicDir("/Society365", legdername+" - "+timeperiod2+".pdf");

        request.setDescription(urlstring);   //appears the same in Notification bar while downloading

        enqueue = dm.enqueue(request);
                }


        return super.onOptionsItemSelected(item);
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem sharemenu = menu.findItem(R.id.action_search);
        MenuItem sortgrp = menu.findItem(R.id.group);
        MenuItem calander = menu.findItem(R.id.action_calender);

        calander.setVisible(false);
        sharemenu.setVisible(false);
        sortgrp.setVisible(false);
        return true;
    }

}