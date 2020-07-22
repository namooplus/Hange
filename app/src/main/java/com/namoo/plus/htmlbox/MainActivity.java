package com.namoo.plus.htmlbox;

import android.app.*;
import android.content.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.model.*;
import com.namoo.plus.htmlbox.*;
import java.io.*;
import java.net.*;
import java.util.*;

import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import com.namoo.plus.htmlbox.R;
import android.view.inputmethod.*;
import org.xml.sax.*;
import com.google.android.gms.ads.*;

public class MainActivity extends ActionBarActivity 
{
	private static final int MSG_TIMER_EXPIRED = 1;
	private static final int BACKKEY_TIMEOUT = 2; 
	private static final int MILLIS_IN_SEC = 1000;
	private boolean mIsBackKeyPressed = false; 
	private long mCurrTimeInMillis = 0; 

	private int page;
	private int a;

	Handler handler;

	Toolbar toolbar;
	Drawer.Result result;

	ProgressDialog pd;

	WebView webView;
	EditText editor;
	TextView wt, wu;
	ImageView wi;
	ProgressBar pb;
	LinearLayout tv, ti;
	ScrollView te;
	RelativeLayout hl;

	String baseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Hange");
		getSupportActionBar().setSubtitle(baseUrl);
		
		AdView adView = (AdView)this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		page = 1;
		baseUrl = "http://www.google.co.kr/";
		
		pd = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
		pd.setTitle("Hange");
		pd.setMessage("Now Hanging...");
		pd.setCancelable(false);

		handler = new Handler();
		
		webView = (WebView)findViewById(R.id.webView);
		editor = (EditText)findViewById(R.id.editor);
		wt = (TextView)findViewById(R.id.web_title);
		wu = (TextView)findViewById(R.id.web_url);
		wi = (ImageView)findViewById(R.id.web_icon);
		pb = (ProgressBar)findViewById(R.id.web_progress);
		tv = (LinearLayout)findViewById(R.id.tab_viewer);
		ti = (LinearLayout)findViewById(R.id.tab_info);
		te = (ScrollView)findViewById(R.id.tab_editor);
		hl = (RelativeLayout)findViewById(R.id.hange_layout);
		
		a = editor.getLineCount();

		result = new Drawer()
			.withActivity(this)
			.withHeader(R.layout.header)
			.withToolbar(toolbar)
			.withActionBarDrawerToggle(true)			
			.addDrawerItems(new PrimaryDrawerItem().withName("Viewer").withIcon(getResources().getDrawable(R.drawable.nd_tab_viewer)),
							new PrimaryDrawerItem().withName("Editor").withIcon(getResources().getDrawable(R.drawable.nd_tab_editor)),
							new PrimaryDrawerItem().withName("Information").withIcon(getResources().getDrawable(R.drawable.nd_tab_info)),
							new SectionDrawerItem().withName(""),
							new SecondaryDrawerItem().withName("Application Info"),
							new SecondaryDrawerItem().withName("Changelog"),
		                    new SecondaryDrawerItem().withName("Developer Info"))
			.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem)
				{
					if (drawerItem instanceof PrimaryDrawerItem)
					{
						switch (position)
						{
							case 1:

								page = 1;
								tv.setVisibility(View.VISIBLE);
								te.setVisibility(View.GONE);
								ti.setVisibility(View.GONE);
								hl.setVisibility(View.VISIBLE);
								getSupportActionBar().setSubtitle(webView.getUrl());

								break;

							case 2:

								page = 2;
								tv.setVisibility(View.GONE);
								te.setVisibility(View.VISIBLE);
								ti.setVisibility(View.GONE);
								hl.setVisibility(View.VISIBLE);
								getSupportActionBar().setSubtitle("Editor");

								break;	

							case 3:

								page = 3;
								tv.setVisibility(View.GONE);
								te.setVisibility(View.GONE);
								ti.setVisibility(View.VISIBLE);
								hl.setVisibility(View.GONE);
								getSupportActionBar().setSubtitle("Information");

								break;							
						}				
					}
					if (drawerItem instanceof SecondaryDrawerItem)
					{
						switch (position)
						{
							case 5:
								AlertDialog.Builder bu = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
								bu.setTitle("Application Info");
								bu.setMessage(getString(R.string.app_info));
								bu.setPositiveButton("Cancel", new AlertDialog.OnClickListener()
									{
										@Override
										public void onClick(DialogInterface p1, int p2)
										{

										}
									});
								bu.show();
								break;

							case 6:
								AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
								ab.setTitle("Changelog");
								ab.setMessage(getString(R.string.change_log));
								ab.setPositiveButton("Cancel", new AlertDialog.OnClickListener()
									{
										@Override
										public void onClick(DialogInterface p1, int p2)
										{

										}
									});
								ab.show();
								break;

							case 7:
								AlertDialog.Builder av = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
								av.setTitle("Developer Info");
								av.setMessage(getString(R.string.dev_info));
								av.setPositiveButton("Cancel", new AlertDialog.OnClickListener()
									{
										@Override
										public void onClick(DialogInterface p1, int p2)
										{

										}
									});
								av.show();
								break;
						}						
					}
				}
			})
			.build();	

		WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
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
		webView.loadUrl(baseUrl);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.pop, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        switch (item.getItemId())
		{
			case R.id.pop_more:

				if (page == 1)
				{
					final String[] items = {"Reload", "Stop Loading", "Go Back", "Go Forward", "Copy Url"};
					AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					builder.setItems(items, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								if (items[which].equals("Reload"))
								{
									webView.reload();
								}
								else if (items[which].equals("Stop Loading"))
								{
									webView.stopLoading();
								}
								else if (items[which].equals("Go Back"))
								{
									webView.goBack();
								}
								else if (items[which].equals("Go Forward"))
								{
									webView.goForward();
								}
								else
								{
									tool_copy();
								}
							}
						});
					builder.show();
				}
				else if (page == 2)
				{
					final String[] items = {"Save", "Load", "Source Clean", "Edit Mode"};
					AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
					builder.setItems(items, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								if (items[which].equals("Save"))
								{
									tool_save();
								}
								else if (items[which].equals("Load"))
								{
									tool_load();
								}
								else if (items[which].equals("Source Clean"))
								{
									tool_clean();
								}
								else
								{
									tool_editmode();
								}
							}
						});
					builder.show();
				}

				break;

			case R.id.pop_search:

				if (page == 1)
				{
					tool_go();
				}
				else if (page == 2)
				{
					tool_search();
				}

				break;
		}
		return true;
    }
    //Web Set
	public class webViewClient extends WebViewClient 
	{
		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);

			if (page == 1)
				getSupportActionBar().setSubtitle(url);

			wt.setText(view.getTitle());
			wu.setText(url);

			Drawable d = new BitmapDrawable(getResources(), view.getFavicon());

			if (d.getMinimumHeight() <= 0 && d.getMinimumWidth() <= 0)
				wi.setImageResource(R.drawable.ic_launcher);

			else
				wi.setImageDrawable(d);
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
	public void hange(View v)
	{
		if (page == 1)
		{
			if (webView.getUrl().equals(""))
			{
				showError();
			}
			else
			{
				page = 2;
				tv.setVisibility(View.GONE);
				te.setVisibility(View.VISIBLE);
				ti.setVisibility(View.GONE);
				hl.setVisibility(View.VISIBLE);
				getSupportActionBar().setSubtitle("Editor");

				tool_html(webView.getUrl());
			}		
		}
		else
		{
			if (editor.getText().toString().equals("") || editor.getText().toString().equals(" "))
			{
				showError();
			}
			else
			{
				tool_web();
			}
		}
	}
	public void tool_go()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		LayoutInflater inflater= getLayoutInflater();
		View layout=inflater.inflate(R.layout.go_dialog, null);

		final EditText urlg = (EditText)layout.findViewById(R.id.go_edit);
		urlg.setHint(webView.getUrl());

		alert.setTitle("Go");
		alert.setCancelable(true);
		alert.setView(layout);
		alert.setPositiveButton("Go", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					if (urlg.getText().toString().contains(" "))
					{
						showError();
					}
					else if (urlg.getText().toString().contains("http://") || urlg.getText().toString().contains("https://"))
					{
						webView.loadUrl(urlg.getText().toString());
					}
					else
					{
						webView.loadUrl("http://" + urlg.getText().toString());
					}
				}
			});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{

				}
			});
		alert.show();
	}
	public void tool_html(final String u)
	{
		pd.show();

		Thread thread = new Thread()
		{
			@Override
			public void run()
			{
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy); 

				try
				{
					final StringBuilder html = new StringBuilder();

					URL url = new URL(u);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();

					if (conn != null)
					{
						conn.setConnectTimeout(10000);
						conn.setUseCaches(false); 

						if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
						{ 
							BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

							for (;;)
							{
								String line = br.readLine(); 

								if (line == null)
									break;

								html.append(line + '\n');
							}
							final String nhtml = html.toString().replaceAll("\n", "").replaceAll("<", "\n<").replaceAll(">", ">\n").replaceAll(";", ";\n").replaceAll("\"\n", "\"").replaceAll("\n\"", "\"").trim();

							handler.post(new Runnable()
								{
									@Override
									public void run()
									{
										editor.setText(nhtml);
										pd.dismiss();
									}
								});
							br.close();
						}					
						conn.disconnect();
					}
				} 
				catch (NetworkOnMainThreadException e)
				{
					Log.d("[Hange] Log :", e.getMessage());
				} 
				catch (IOException e)
				{
					Log.d("[Hange] Log :", e.getMessage());
				} 
			}
		};
		thread.start();
	}
	public void tool_web()
	{
		if (editor.getText().toString().equals(""))
		{
			showError();
		}
		else
		{
			Intent i = new Intent(this, CustomViewer.class);
			i.putExtra("Custom", editor.getText().toString());
			startActivity(i);
		}
	}
	public void tool_copy()
	{
		if (webView.getUrl().equals(""))
		{
			showError();
		}
		else
		{
			ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
			clip.setText(webView.getUrl());

			Toast.makeText(MainActivity.this, "\'" + webView.getUrl() + "\'is copied.", Toast.LENGTH_SHORT).show();		
		}
	}
	public void tool_clean()
	{
		if (editor.getText().toString().equals(""))
		{
			showError();
		}
		else
		{
			String a = editor.getText().toString().replaceAll("\n", "").replaceAll("<", "\n<").replaceAll(">", ">\n").replaceAll(";", ";\n").replaceAll("\"\n", "\"").replaceAll("\n\"", "\"").trim();
			editor.setText(a);
		}
	}
	public void tool_search()
	{
		if (editor.getText().toString().equals(""))
		{
			showError();
		}
		else
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			LayoutInflater inflater= getLayoutInflater();
			View layout=inflater.inflate(R.layout.search_dialog, null);

			final EditText keyword = (EditText)layout.findViewById(R.id.search_edit);
			final EditText replaceword = (EditText)layout.findViewById(R.id.search_replace);

			alert.setTitle("Search");
			alert.setCancelable(true);
			alert.setView(layout);
			alert.setPositiveButton("Search", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						if (keyword.getText().toString().equals("") || keyword.getText().toString().equals(" "))
						{
							showError();
						}
						else
						{
							String before = editor.getText().toString();
							String after = before.replace(keyword.getText().toString(), "★" + keyword.getText().toString() + "★").toString();

							editor.setText(after);

							Toast.makeText(getApplicationContext(), "\'★\'is attached to the both side of the keyword you search.", 3000).show();
						}
					}
				});
			alert.setNeutralButton("Replace", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						if (replaceword.getText().toString().equals("") || keyword.getText().toString().equals("") || replaceword.getText().toString().equals(" ") || keyword.getText().toString().equals(" "))
						{
							showError();
						}
						else
						{
							String v = editor.getText().toString().replaceAll(keyword.getText().toString(), replaceword.getText().toString());
							editor.setText(v);
						}
					}
				});
			alert.setNegativeButton("None", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						String v = editor.getText().toString().replace("★", "").toString();
						editor.setText(v);
					}
				});
			alert.show();
		}
	}
	public void tool_save()
	{
		if (editor.getText().toString().equals(""))
		{
			showError();
		}
		else
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			LayoutInflater inflater= getLayoutInflater();
			View layout=inflater.inflate(R.layout.save_dialog, null);

			final EditText title = (EditText)layout.findViewById(R.id.save_edit);

			alert.setTitle("Save");
			alert.setCancelable(true);
			alert.setView(layout);
			alert.setPositiveButton("Save", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						if (title.getText().toString().equals("") || title.getText().toString().contains(" "))
						{
							showError();
						}
						else
						{
							save(title.getText().toString(), editor.getText().toString());
						}
					}
				});
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{

					}
				});
			alert.show();
		}
	}
	public void save(String name, String text)
	{
		String path = "/sdcard/Hange/"; 
		String fileName = name + ".txt"; 

		makeDirectory(path);

		File file = new File(path + fileName);
		FileOutputStream fos = null;

		if (file.exists())
		{
			showError();
		}
		else
		{
			try 
			{
				fos = new FileOutputStream(file); 
				fos.write((text).getBytes()); 
				fos.close(); 
				Toast.makeText(this, "Successfully saved.", Toast.LENGTH_SHORT).show();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Toast.makeText(this, "Error : " + e, Toast.LENGTH_SHORT).show();
			}
		}
	}
	private File makeDirectory(String dir_path)
	{
		File dir = new File(dir_path);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		else
		{

		}
		return dir;
	}
	public void tool_load()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.load_dialog, null);

		final ListView lv = (ListView)layout.findViewById(R.id.load_lv);

		alert.setTitle("Load");
		alert.setCancelable(true);
		alert.setView(layout);
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{

				}
			});
		alert.show();

		File file = new File("/sdcard/Hange");

		if (file.exists() == false)
		{
			return;
		}

		File[] files = file.listFiles();

		final ArrayList<String> arrayList = new ArrayList<String>();

		for (int i=0; i < files.length; i++)
		{
			if (!files[i].isHidden() && files[i].isFile())
				arrayList.add(files[i].getName());
		}

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
		lv.setAdapter(arrayAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() 
			{
				public void onItemClick(AdapterView<?> parent, View view, final int position, long id) 
				{
					pd.show();

					Thread thread = new Thread()
					{
						@Override
						public void run()
						{
							try 
							{
								File file = new File("/sdcard/Hange/", arrayAdapter.getItem(position));
								FileInputStream fis = new FileInputStream(file);
								InputStreamReader isr = new InputStreamReader(fis);
								BufferedReader br = new BufferedReader(isr);

								String uptxt = ""; 
								String semi_uptxt="";
								while ((semi_uptxt = br.readLine()) != null)
								{
									uptxt += semi_uptxt + "\n";
								}
								final String resulttxt = uptxt;
								br.close();

								handler.post(new Runnable()
									{
										@Override
										public void run()
										{
											Toast.makeText(getApplicationContext(), "Successfully loaded.", 3000).show();
											editor.setText(resulttxt);
											pd.dismiss();
										}
									});

							} 
							catch (Exception e) 
							{
								Log.e("Exception", "Error : " + e);
								pd.dismiss();
							}
						}
					};
					thread.start();
				}
			});
	}
	public void tool_editmode()
	{
		if (editor.isEnabled())
		{
			editor.setEnabled(false);
			Toast.makeText(getApplicationContext(), "Edit enable : false", Toast.LENGTH_SHORT).show();
		}
		else
		{
			editor.setEnabled(true);
			Toast.makeText(getApplicationContext(), "Edit enable : true", Toast.LENGTH_SHORT).show();
		}
	}
	public void onBackPressed() 
	{
		if (page == 1)
		{
			if (webView.canGoBack())
			{
				webView.goBack();
			}
			else if (!webView.canGoBack())
			{
				if (mIsBackKeyPressed == false) 
				{
					mIsBackKeyPressed = true; 
					mCurrTimeInMillis = Calendar.getInstance().getTimeInMillis(); 
					Toast.makeText(this, "Do you really want to finish Hange?", Toast.LENGTH_SHORT).show();
					startTimer();
				} 
				else 
				{ 
					mIsBackKeyPressed = false;
					if (Calendar.getInstance().getTimeInMillis() <= (mCurrTimeInMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC))) 
					{
						finish(); 
					}
				} 
			} 
			else
			{
				showError();
			}
		}
		else
		{
			if (mIsBackKeyPressed == false) 
			{
				mIsBackKeyPressed = true; 
				mCurrTimeInMillis = Calendar.getInstance().getTimeInMillis(); 
				Toast.makeText(this, "Do you really want to finish Hange?", Toast.LENGTH_SHORT).show();
				startTimer();
			} 
			else 
			{ 
				mIsBackKeyPressed = false;
				if (Calendar.getInstance().getTimeInMillis() <= (mCurrTimeInMillis + (BACKKEY_TIMEOUT * MILLIS_IN_SEC))) 
				{
					finish(); 
				}
			} 
		}
	}
	public void showError()
	{
		Toast.makeText(getApplicationContext(), "Error :)", 3000).show();
	}
	private void startTimer()
	{ 
		mTimerHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, BACKKEY_TIMEOUT * MILLIS_IN_SEC);
	} 
	private Handler mTimerHandler = new Handler()
	{ 
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{ 
				case MSG_TIMER_EXPIRED:
					{ 
						mIsBackKeyPressed = false; 
					} 
					break;
			}
		} 
	};
}
