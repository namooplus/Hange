package com.namoo.plus.htmlbox;

import android.app.*;
import android.os.*;
import android.webkit.*;
import android.view.*;
import android.content.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.widget.ProgressBar;
import com.google.android.gms.ads.*;

public class CustomViewer extends ActionBarActivity
{
	Toolbar toolbar;
	WebView webView;
	ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_viewer);
		
		toolbar = (Toolbar) findViewById(R.id.custom_toolbar_actionbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Custom Viewer");
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		
		webView = (WebView)findViewById(R.id.custom_webView);
		pb = (ProgressBar)findViewById(R.id.custom_web_progress);
		
		Intent i = getIntent();
		String a = i.getStringExtra("Custom");
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setLightTouchEnabled(true); 
		webView.getSettings().setSavePassword(true); 
		webView.getSettings().setSaveFormData(true); 
		webView.getSettings().setUseWideViewPort(true);
		webView.setWebViewClient(new webViewClient());
		webView.setWebChromeClient(new webViewChrome());
		webView.loadData(a, "text/html; charset=utf-8", "utf-8");
	}
	public boolean onOptionsItemSelected(android.view.MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case android.R.id.home:
				
				finish();
				
				return true;
		}
		return super.onOptionsItemSelected(item);
	}; 
	public class webViewClient extends WebViewClient 
	{
		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView mView, String url)
		{
			mView.loadUrl(url);

			return super.shouldOverrideUrlLoading(mView, url);
		}
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}
	public class webViewChrome extends WebChromeClient 
	{
		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			super.onProgressChanged(view, newProgress);

			if (newProgress < 100)
			{
				pb.setVisibility(View.VISIBLE);
				pb.setProgress(newProgress);
			}
			else
			{
				pb.setVisibility(View.GONE);
			}
		}
	}
}
