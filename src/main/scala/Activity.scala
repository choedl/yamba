package at.yamba

import _root_.android.app.Activity
import android.view.View.OnClickListener
import android.view.View
import android.util.Log
import android.os.{AsyncTask, Bundle}
import winterwell.jtwitter.{TwitterException, Twitter}
import android.widget.{TextView, Toast, Button, EditText}
import android.graphics.Color
import android.text.{Editable, TextWatcher}

class StatusActivity extends Activity with OnClickListener with TextWatcher {

  val Tag = "StatusActivity"
  val MaxTextCount = 140

  var editText: EditText = null
  var updateButton: Button = null
  var twitter: Twitter = null
  var textCount: TextView = null

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

    twitter = new Twitter("student", "password")
    twitter.setAPIRootUrl("http://yamba.marakana.com/api")
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
      val twitterStatus = twitter.updateStatus(editText.getText.toString)
      twitterStatus.text
    } catch {
      case e: Exception =>
        Log.e(Tag, e.toString)
        "Failed to post"
    }
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
}
