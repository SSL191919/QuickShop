package com.gmyscl.ecom.firstorder.communicate;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.gmyscl.ecom.firstorder.R;

import static android.graphics.Color.RED;

public class HelpActivity extends AppCompatActivity {

    private WebView webView_help;
    private ProgressBar progressBar_webView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_help );

        webView_help = findViewById( R.id.webView_help );
        progressBar_webView = findViewById( R.id.progressBar_webView );
        progressBar_webView.setDrawingCacheBackgroundColor( RED );
        progressBar_webView.setMax( 100 );

        webView_help.loadUrl( "http://gomyschool.in/Privacy.aspx" );
        webView_help.getSettings().setJavaScriptEnabled( true );
        webView_help.setWebViewClient( new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar_webView.setVisibility( View.VISIBLE );
                super.onPageStarted( view, url, favicon );
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar_webView.setVisibility( View.GONE );
                super.onPageFinished( view, url );
            }
        } );
//        webView_help.setWebChromeClient( new WebChromeClient(){
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle( view, title );
//                if (title != null ){
//                    getSupportActionBar().setTitle( title );
//                }
//            }
//        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate( R.menu.back_forward_menu, menu );
//        return super.onCreateOptionsMenu( menu );
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.back_menu:
//                onBackPressed();
//                break;
//            case R.id.forward_menu:
//                onForwardPressed();
//                break;
//            case R.id.refresh_web_menu:
//                webView_help.reload();
//                break;
//            default:
//                break;
//        }
//
//        return super.onOptionsItemSelected( item );
//    }

    @Override
    public void onBackPressed() {

//        if( webView_help.canGoBack() ){
//            webView_help.goBack();
//            findViewById( R.id.forward_menu ).setEnabled( true );
//        }
//        else
//            finish();
            super.onBackPressed();

    }

    public void onForwardPressed(){
        if(webView_help.canGoForward()){
            webView_help.goForward();
        }
    }

}

