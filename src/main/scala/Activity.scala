package at.yamba

import _root_.android.app.Activity
import android.view.View.OnClickListener
import android.util.Log
import android.os.{AsyncTask, Bundle}
import winterwell.jtwitter.{TwitterException, Twitter}
import android.widget.{TextView, Toast, Button, EditText}
import android.graphics.Color
import android.text.{Editable, TextWatcher}
import android.view.{MenuItem, MenuInflater, Menu, View}
import android.content.{SharedPreferences, Intent}
import android.preference.PreferenceManager
import android.content.SharedPreferences.OnSharedPreferenceChangeListener

class StatusActivity extends Activity with OnClickListener with TextWatcher with OnSharedPreferenceChangeListener {

  val Tag = "StatusActivity"
  val MaxTextCount = 140

  var editText: EditText = null
  var updateButton: Button = null
  var twitter: Option[Twitter] = None
  var textCount: TextView = null

  var prefs: SharedPreferences = null

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.status)

    // Find views
    editText = findViewById(R.id.editText).asInstanceOf[EditText]
    updateButton = findViewById(R.id.buttonUpdate).asInstanceOf[Button]
    updateButton.setOnClickListener(this)

    textCount = findViewById(R.id.textCount).asInstanceOf[TextView]
    textCount.setText(MaxTextCount.toString)
    textCount.setTextColor(Color.GREEN)
    editText.addTextChangedListener(this)

    prefs = PreferenceManager.getDefaultSharedPreferences(this)
    prefs.registerOnSharedPreferenceChangeListener(this)

  }

  override def onClick(v: View) {
    new PostToTwitter(sendTwitterStatusUpdate).doInBackground
    Log.d(Tag, "onClicked")
  }

  class PostToTwitter(f: () => String) {

    def doInBackground {
      new AbstractAsyncTask[() => String, Int, String] {

        protected def doInBackground(f: () => String) = {
          f()
        }

        override def onProgressUpdate(values: Int*) = {
          super.onProgressUpdate(values:_*)
        }

        override def onPostExecute(result: String) = {
          Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show
        }

      }.execute(f)
    }
  }

  private def sendTwitterStatusUpdate(): String = {
    try {
      getTwitter match {
        case Some(twitter: Twitter) =>
          val twitterStatus = twitter.updateStatus(editText.getText.toString)
          twitterStatus.text
        case None =>
          Log.e(Tag, "Twitter object is None")
          "Twitter object is None"
      }
    } catch {
      case e: Exception =>
        Log.e(Tag, e.toString)
        "Failed to post"
    }
  }

  private def getTwitter: Option[Twitter] = {
    twitter match {
      case None =>
        val username = prefs.getString("username", "student")
        val password = prefs.getString("password", "password")
        val apiRootUrl = prefs.getString("apiRoot", "http://yamba.marakana.com/api")

        twitter = Some(new Twitter(username, password))
        twitter.get.setAPIRootUrl(apiRootUrl)
      case Some(t: Twitter) =>
        Log.i(Tag, "twitter not changed")
    }
    twitter
  }

  def afterTextChanged(statusText: Editable) {
    val count = MaxTextCount - statusText.length
    textCount.setText(count.toString)

    textCount.setTextColor(Color.GREEN)

    if (count < 10) textCount.setTextColor(Color.YELLOW)
    if (count < 0)  textCount.setTextColor(Color.RED)
  }

  def beforeTextChanged(p1: CharSequence, p2: Int, p3: Int, p4: Int) {}
  def onTextChanged(p1: CharSequence, p2: Int, p3: Int, p4: Int) {}

  override def onCreateOptionsMenu(menu: Menu) = {
    val inflater: MenuInflater = getMenuInflater
    inflater.inflate(R.menu.menu, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem) = {
    item.getItemId match {
      case R.id.itemPrefs =>
        startActivity(new Intent(this, classOf[PrefsActivity]))
      case i =>
        Log.i(Tag, "unknown item selected: " + i)
    }

    true
  }

  def onSharedPreferenceChanged(prefs: SharedPreferences, key: String) {
    twitter = None
  }
}
